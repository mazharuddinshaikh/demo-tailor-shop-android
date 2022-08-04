package com.example.demotailorshop.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demotailorshop.MainActivity;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.api.UserApi;
import com.example.demotailorshop.databinding.FragmentUpdatePasswordBinding;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.listener.CustomDialogListener;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.example.demotailorshop.view.custom.CustomDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdatePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdatePasswordFragment extends Fragment {
    private static final String TAG = "UpdatePasswordFragment";
    private FragmentUpdatePasswordBinding binding;


    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UpdatePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdatePasswordFragment newInstance() {
        UpdatePasswordFragment fragment = new UpdatePasswordFragment();
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
        binding = FragmentUpdatePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        addListenerForNavigation();

        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (user == null) {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.putExtra(DtsSharedPreferenceUtil.KEY_AUTHENTICATION_MESSAGE, DtsSharedPreferenceUtil.VALUE_AUTHENTICATION_MESSAGE);
            startActivity(intent);
        } else {
            binding.tilUserName.getEditText().setText(user.getUserName());
        }
        binding.btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnUpdatePassword.setEnabled(false);
                binding.tilUserName.setError(null);
                binding.tilOldPassword.setError(null);
                binding.tilNewPassword.setError(null);
                binding.tilConfirmPassword.setError(null);
                boolean isReadyToUpdatePassword = false;
                String userName = binding.tilUserName.getEditText().getText().toString();
                String oldPassword = binding.tilOldPassword.getEditText().getText().toString();
                String newPassword = binding.tilNewPassword.getEditText().getText().toString();
                String confirmPassword = binding.tilConfirmPassword.getEditText().getText().toString();
                if (DtsUtils.isNullOrEmpty(userName) || DtsUtils.isNullOrEmpty(oldPassword)
                        || DtsUtils.isNullOrEmpty(newPassword) || DtsUtils.isNullOrEmpty(confirmPassword)
                        || !newPassword.equals(confirmPassword)) {
                    binding.btnUpdatePassword.setEnabled(true);
                    if (DtsUtils.isNullOrEmpty(userName)) {
                        binding.tilUserName.setError("Please enter valid username");
                    }

                    if (DtsUtils.isNullOrEmpty(oldPassword)) {
                        binding.tilOldPassword.setError("Please enter old password");
                    }

                    if (DtsUtils.isNullOrEmpty(newPassword)) {
                        binding.tilNewPassword.setError("Please enter new password");
                    }
                    if (DtsUtils.isNullOrEmpty(confirmPassword)) {
                        binding.tilConfirmPassword.setError("Please enter confirm password");
                    }

                    if (!newPassword.equals(confirmPassword)) {
                        binding.tilConfirmPassword.setError("New password and confirm Password must be same");
                    }
                } else {
                    isReadyToUpdatePassword = true;
                }
                if (isReadyToUpdatePassword) {
                    showUpdatePasswordDialog(userName, oldPassword, newPassword);
                }


            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void showUpdatePasswordDialog(String userName, String oldPassword, String newPassword) {
        final UserApi userApi = DtsApiFactory.getRetrofitInstance().create(UserApi.class);
        CustomDialogListener listener = new CustomDialogListener() {
            @Override
            public void onPositiveClick() {
                userApi.updatePassword(userName, oldPassword, newPassword).enqueue(new Callback<ApiResponse<String>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<String>> call, @NonNull Response<ApiResponse<String>> response) {
                        binding.btnUpdatePassword.setEnabled(true);
                        int httpStatus = response.code();
                        if (httpStatus == ApiUtils.SUCCESS) {
                            Log.i(TAG, "Signup successful");
                            ApiResponse<String> apiResponse = response.body();
                            Snackbar.make(binding.getRoot(), apiResponse.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
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
                    public void onFailure(@NonNull Call<ApiResponse<String>> call, @NonNull Throwable t) {
                        binding.btnUpdatePassword.setEnabled(true);
                        Snackbar.make(binding.getRoot(), "Something went wrong! Please retry", BaseTransientBottomBar.LENGTH_SHORT).show();
                        Log.e(TAG, "Something went wrong! " + t);
                    }
                });
            }

            @Override
            public void onNegativeClick() {
                binding.btnUpdatePassword.setEnabled(true);
            }
        };
        AlertDialog alertDialog = CustomDialog.getUpdateDialog(requireActivity(), "Update password", "Are you sure you want to update password", "Yes", "Cancel", listener);
        alertDialog.show();
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