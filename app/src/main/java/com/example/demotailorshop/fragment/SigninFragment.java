package com.example.demotailorshop.fragment;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demotailorshop.R;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.api.UserApi;
import com.example.demotailorshop.databinding.FragmentSigninBinding;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SigninFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SigninFragment extends Fragment {
    private FragmentSigninBinding binding;
    private static final String TAG = "SigninFragment";


    public SigninFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SigninFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SigninFragment newInstance() {
        return new SigninFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final UserApi userApi = DtsApiFactory.getRetrofitInstance().create(UserApi.class);

        binding = FragmentSigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        String forgotPassword = "Forgot password please click here.";
        SpannableString forgotPasswordSpan = new SpannableString(forgotPassword);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvContainer, ForgotPasswordFragment.class, null, "ForgotPasswordFragment")
                        .addToBackStack("ForgotPasswordFragment")
                        .commit();
            }
        };
        forgotPasswordSpan.setSpan(new UnderlineSpan(), 16, forgotPassword.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        forgotPasswordSpan.setSpan(clickableSpan, 16, forgotPassword.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        binding.tvForgotPassword.setText(forgotPasswordSpan);
        binding.tvForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnSignin.setEnabled(false);
                binding.tilUserName.setError(null);
                binding.tilPassword.setError(null);
                String userName = binding.tilUserName.getEditText().getText().toString();
                String password = binding.tilPassword.getEditText().getText().toString();
                if (DtsUtils.isNullOrEmpty(userName)) {
                    binding.tilUserName.setError("Please enter valid user name");
                    binding.btnSignin.setEnabled(true);
                } else if (DtsUtils.isNullOrEmpty(password)) {
                    binding.tilPassword.setError("Please enter valid password");
                    binding.btnSignin.setEnabled(true);
                } else {
                    binding.tilUserName.setError(null);
                    binding.tilPassword.setError(null);
                    userApi.signin(userName, password).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                            binding.btnSignin.setEnabled(true);
                            int httpStatus = response.code();
                            if (httpStatus == ApiUtils.SUCCESS) {
                                Log.i(TAG, "Signin successfully");
                                boolean isSavedToPref = DtsSharedPreferenceUtil.getInstance().saveUserToSharedPreference(requireActivity(), response.body(), DtsSharedPreferenceUtil.KEY_USER);
                                if (isSavedToPref) {
                                    requireActivity().getSupportFragmentManager().beginTransaction()
                                            .setReorderingAllowed(true)
                                            .replace(R.id.fcvContainer, HomeFragment.class, null, "HomeFragment")
                                            .commit();
                                } else {
                                    Snackbar.make(binding.getRoot(), DtsSharedPreferenceUtil.VALUE_AUTHENTICATION_MESSAGE, BaseTransientBottomBar.LENGTH_SHORT).show();
                                }
//

                            } else if (httpStatus == ApiUtils.FORBIDDEN) {
                                Log.i(TAG, "Username / Password incorrect");
                                ApiError apiError = ApiUtils.getApiErrorResponse(response);
                                Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                            } else {
                                ApiError apiError = ApiUtils.getDefaultErrorResponse(response.code(), "Something went wrong! Please retry");
                                Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                            Snackbar.make(binding.getRoot(), "Something went wrong! Please retry", BaseTransientBottomBar.LENGTH_SHORT).show();
                            Log.e(TAG, "Something went wrong! " + t);
                            binding.btnSignin.setEnabled(true);
                        }
                    });
                }
            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvContainer, SignupFragment.class, null, "SignupFragment")
                        .addToBackStack("SignupFragment")
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

}