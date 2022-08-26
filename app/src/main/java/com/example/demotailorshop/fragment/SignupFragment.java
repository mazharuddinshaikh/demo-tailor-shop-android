package com.example.demotailorshop.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demotailorshop.R;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.api.UserApi;
import com.example.demotailorshop.databinding.FragmentSignupBinding;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    FragmentSignupBinding binding;
    private static final String TAG = "SignupFragment";

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
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
        final UserApi userApi = DtsApiFactory.getRetrofitInstance().create(UserApi.class);
        binding = FragmentSignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnSignup.setEnabled(false);
                binding.tilName.setError(null);
                binding.tilEmail.setError(null);
                binding.tilMobileNo.setError(null);
                binding.tilUserName.setError(null);
                binding.tilPassword.setError(null);
                binding.tilConfirmPassword.setError(null);
                binding.tilShopName.setError(null);
                binding.tilShopMobile.setError(null);
                binding.tilShopAddress.setError(null);
                String name = binding.tilName.getEditText().getText().toString();
                String emailAddress = binding.tilEmail.getEditText().getText().toString();
                String mobileNo = binding.tilMobileNo.getEditText().getText().toString();
                String userName = binding.tilUserName.getEditText().getText().toString();
                String password = binding.tilPassword.getEditText().getText().toString();
                String confirmPassword = binding.tilConfirmPassword.getEditText().getText().toString();
                String shopName = binding.tilShopName.getEditText().getText().toString();
                String shopMobile = binding.tilShopMobile.getEditText().getText().toString();
                String shopAddress = binding.tilShopAddress.getEditText().getText().toString();
                boolean isReadyToSignup = false;
                if (DtsUtils.isNullOrEmpty(name)) {
                    binding.tilName.setError("Please enter valid name of user");
                    isReadyToSignup = false;
                }

                if (!DtsUtils.isNullOrEmpty(emailAddress)) {
                    boolean isValidEmail = Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
                    if (!isValidEmail) {
                        binding.tilEmail.setError("Please enter valid email address");
                    }
                    isReadyToSignup = isValidEmail;
                } else {
                    binding.tilEmail.setError("Please enter valid email address");
                }
                if (!DtsUtils.isNullOrEmpty(mobileNo)) {
                    boolean isValidMobile = Patterns.PHONE.matcher(mobileNo).matches();
                    if (!isValidMobile) {
                        binding.tilMobileNo.setError("Please enter valid mobile number");
                    }
                    isReadyToSignup = isReadyToSignup ? isValidMobile : isReadyToSignup;
                } else {
                    binding.tilMobileNo.setError("Please enter mobile number");
                    isReadyToSignup = false;
                }
                if (DtsUtils.isNullOrEmpty(userName)) {
                    binding.tilUserName.setError("Please enter valid user name");
                    isReadyToSignup = false;
                }

                if (DtsUtils.isNullOrEmpty(password) || DtsUtils.isNullOrEmpty(confirmPassword) || !password.equals(confirmPassword)) {
                    isReadyToSignup = false;
                    if (DtsUtils.isNullOrEmpty(password)) {
                        binding.tilPassword.setError("Please enter valid password");
                    }
                    if (DtsUtils.isNullOrEmpty(confirmPassword)) {
                        binding.tilConfirmPassword.setError("Please enter valid password");
                    }
                    if (!password.equals(confirmPassword)) {
                        binding.tilConfirmPassword.setError("Password and confirm Password must be same");
                    }
                }

                if (DtsUtils.isNullOrEmpty(shopName)) {
                    binding.tilShopName.setError("Please enter valid name of shop");
                    isReadyToSignup = false;
                }

                if (!DtsUtils.isNullOrEmpty(shopMobile)) {
                    boolean isValidShopMobile = Patterns.PHONE.matcher(shopMobile).matches();
                    if (!isValidShopMobile) {
                        binding.tilShopMobile.setError("Please enter valid mobile number");
                        isReadyToSignup = false;
                    }
//                    isReadyToSignup = isReadyToSignup ? isValidShopMobile : isReadyToSignup;
                }

                if (isReadyToSignup) {
                    User user = new User();
                    if (!DtsUtils.isNullOrEmpty(name)) {
                        String[] names = name.split(" ", 3);
                        if (names.length == 3) {
                            user.setFirstName(names[0]);
                            user.setMiddleName(names[1]);
                            user.setLastName(names[2]);
                        }
                        if (names.length == 2) {
                            user.setFirstName(names[0]);
                            user.setMiddleName(names[1]);
                        }
                        if (names.length == 1) {
                            user.setFirstName(names[0]);
                        }
                    }
                    user.setEmail(emailAddress);
                    user.setMobileNo(mobileNo);
                    user.setUserName(userName);
                    user.setPassword(password);
                    user.setShopName(shopName);
                    user.setAlternateMobileNo(shopMobile);
                    user.setAddress(shopAddress);
                    userApi.signup(user).enqueue(new Callback<ApiResponse<User>>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse<User>> call, @NonNull Response<ApiResponse<User>> response) {
                            binding.btnSignup.setEnabled(true);
                            int httpStatus = response.code();
                            if (httpStatus == ApiUtils.SUCCESS) {
                                Log.i(TAG, "Signup successful");
                                ApiResponse<User> apiResponse = response.body();
                                Snackbar.make(binding.getRoot(), apiResponse.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
//                                remove back stack entries on successful successfully signup
                                int backStackEntryCount = requireActivity().getSupportFragmentManager().getBackStackEntryCount();
                                if (backStackEntryCount > 0) {
                                    while (backStackEntryCount > 0) {
                                        requireActivity().getSupportFragmentManager().popBackStack();
                                        backStackEntryCount--;
                                    }
                                }
//                redirect to home page
                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.fcvContainer, UserHelpFragment.class, null, "UserHelpFragment")
                                        .commit();
                            } else if (httpStatus == ApiUtils.BAD_REQUEST) {
                                Log.i(TAG, "Validation missed");
                                ApiError apiError = ApiUtils.getApiErrorResponse(response);
                                Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                            } else {
                                ApiError apiError = ApiUtils.getDefaultErrorResponse(response.code(), "Something went wrong! Please retry");
                                Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResponse<User>> call, @NonNull Throwable t) {
                            binding.btnSignup.setEnabled(true);
                            Snackbar.make(binding.getRoot(), "Something went wrong! Please retry", BaseTransientBottomBar.LENGTH_SHORT).show();
                            Log.e(TAG, "Something went wrong! " + t);
                        }
                    });


                    //api call validate username like is this user name is available
                    //api call for signup after validating all fields
//
                } else {
                    binding.btnSignup.setEnabled(true);
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
}