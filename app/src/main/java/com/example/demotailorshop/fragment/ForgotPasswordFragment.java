package com.example.demotailorshop.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.api.UserApi;
import com.example.demotailorshop.databinding.FragmentForgotPasswordBinding;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {
    private static final String TAG = "ForgotPasswordFragment";
    private FragmentForgotPasswordBinding binding;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForgotPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final UserApi userApi = DtsApiFactory.getRetrofitInstance().create(UserApi.class);
        addListenerForNavigation();
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        binding.btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnForgotPassword.setEnabled(false);
                binding.tilUserName.setError(null);
                binding.tilEmail.setError(null);
                String userName = binding.tilUserName.getEditText().getText().toString();
                String email = binding.tilEmail.getEditText().getText().toString();
                if (DtsUtils.isNullOrEmpty(userName) && DtsUtils.isNullOrEmpty(email)) {
                    binding.tilUserName.setError("Please enter valid user name");
                    binding.tilEmail.setError("Please enter valid email id");
                    binding.btnForgotPassword.setEnabled(true);
                } else {
                    Log.v(TAG, "Forgot password calling api");
                    userApi.forgotPassword(userName, email).enqueue(new Callback<ApiResponse<String>>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse<String>> call, @NonNull Response<ApiResponse<String>> response) {
                            binding.btnForgotPassword.setEnabled(true);
                            int httpStatus = response.code();
                            if (httpStatus == ApiUtils.SUCCESS) {
                                Log.v(TAG, "Success");
                                Snackbar.make(binding.getRoot(), response.body().getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                            } else if (httpStatus == ApiUtils.BAD_REQUEST) {
                                ApiError apiError = ApiUtils.getApiErrorResponse(response);
                                Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                                Log.i(TAG, apiError.getMessage());
                            } else {
                                ApiError apiError = ApiUtils.getDefaultErrorResponse(response.code(), "Something went wrong! Please retry");
                                Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResponse<String>> call, @NonNull Throwable t) {
                            binding.btnForgotPassword.setEnabled(true);
                            Log.v(TAG, "Error");
                            Snackbar.make(binding.getRoot(), "Something went wrong! Please retry", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addListenerForNavigation() {
        binding.mtbToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}