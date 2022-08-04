package com.example.demotailorshop.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demotailorshop.MainActivity;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.api.UserApi;
import com.example.demotailorshop.databinding.FragmentUpdateUserBinding;
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
 * Use the {@link UpdateUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateUserFragment extends Fragment {
    private static final String TAG = "UpdateUserFragment";
    private FragmentUpdateUserBinding binding;


    public UpdateUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UpdateUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateUserFragment newInstance() {
        UpdateUserFragment fragment = new UpdateUserFragment();
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
        binding = FragmentUpdateUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        addListenerForNavigation();
        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (user == null) {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.putExtra(DtsSharedPreferenceUtil.KEY_AUTHENTICATION_MESSAGE, DtsSharedPreferenceUtil.VALUE_AUTHENTICATION_MESSAGE);
            startActivity(intent);
        } else {
            String firstName = user.getFirstName();
            String middleName = user.getMiddleName();
            String lastName = user.getLastName();
            String name = null;
            if (!DtsUtils.isNullOrEmpty(firstName)) {
                name = firstName;
            }
            if (!DtsUtils.isNullOrEmpty(middleName)) {
                name += " " + middleName;
            }
            if (!DtsUtils.isNullOrEmpty(lastName)) {
                name += " " + lastName;
            }
            binding.tilName.getEditText().setText(name);
            binding.tilEmail.getEditText().setText(user.getEmail());
            binding.tilMobileNo.getEditText().setText(user.getMobileNo());
        }
        binding.btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnUpdateUser.setEnabled(false);
                binding.tilName.setError(null);
                binding.tilEmail.setError(null);
                binding.tilMobileNo.setError(null);
                User updateUser = new User();
                String userName = user.getUserName();
                String name = binding.tilName.getEditText().getText().toString();
                String mobileNo = binding.tilMobileNo.getEditText().getText().toString();
                String emailAddress = binding.tilEmail.getEditText().getText().toString();
                boolean isReadyToUpdate = false;
                if (DtsUtils.isNullOrEmpty(name) || DtsUtils.isNullOrEmpty(emailAddress)
                        || DtsUtils.isNullOrEmpty(mobileNo)) {
                    binding.btnUpdateUser.setEnabled(true);
                    if (DtsUtils.isNullOrEmpty(name)) {
                        binding.tilName.setError("Please enter valid name");
                    }
                    if (DtsUtils.isNullOrEmpty(emailAddress)) {
                        binding.tilEmail.setError("Please enter valid email address");
                    }
                    if (DtsUtils.isNullOrEmpty(mobileNo)) {
                        binding.tilMobileNo.setError("Please enter valid mobileNo");
                    }
                } else {
                    if (!DtsUtils.isNullOrEmpty(name)) {
                        String[] names = name.split(" ", 3);
                        if (names.length == 3) {
                            updateUser.setFirstName(names[0]);
                            updateUser.setMiddleName(names[1]);
                            updateUser.setLastName(names[2]);
                        }
                        if (names.length == 2) {
                            updateUser.setFirstName(names[0]);
                            updateUser.setMiddleName(names[1]);
                        }
                        if (names.length == 1) {
                            updateUser.setFirstName(names[0]);
                        }
                    }
                    boolean isValidMobileNo = Patterns.PHONE.matcher(mobileNo).matches();
                    if (!isValidMobileNo) {
                        binding.tilMobileNo.setError("Please enter valid mobile number");
                        binding.btnUpdateUser.setEnabled(true);
                    }
                    boolean isValidEmail = Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
                    if (!isValidEmail) {
                        binding.tilEmail.setError("Please enter valid email address");
                        binding.btnUpdateUser.setEnabled(true);
                    }
                    if (isValidMobileNo && isValidEmail) {
                        isReadyToUpdate = true;
                    }
                }

                if (isReadyToUpdate) {
                    updateUser.setUserName(userName);
                    updateUser.setMobileNo(mobileNo);
                    updateUser.setEmail(emailAddress);
                    showUpdateUserDialog(userApi, updateUser);
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

    private void showUpdateUserDialog(UserApi userApi, User updateUser) {
        CustomDialogListener listener = new CustomDialogListener() {
            @Override
            public void onPositiveClick() {
                userApi.updateUser(updateUser).enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<User>> call, @NonNull Response<ApiResponse<User>> response) {
                        binding.btnUpdateUser.setEnabled(true);
                        int httpStatus = response.code();
                        if (httpStatus == ApiUtils.SUCCESS) {
                            Log.i(TAG, "User update successfully");
                            DtsSharedPreferenceUtil.getInstance().saveUserToSharedPreference(requireActivity(), response.body().getResult(), DtsSharedPreferenceUtil.KEY_USER);
                            Snackbar.make(binding.getRoot(), response.body().getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        } else if (httpStatus == ApiUtils.BAD_REQUEST) {
                            Log.i(TAG, "Bad request");
                            ApiError apiError = ApiUtils.getApiErrorResponse(response);
                            Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                        } else {
                            ApiError apiError = ApiUtils.getDefaultErrorResponse(response.code(), "Something went wrong! Please retry");
                            Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<User>> call, @NonNull Throwable t) {
                        binding.btnUpdateUser.setEnabled(true);
                        Snackbar.make(binding.getRoot(), "Something went wrong! Please retry", BaseTransientBottomBar.LENGTH_SHORT).show();
                        Log.e(TAG, "Something went wrong! " + t);
                    }
                });
            }

            @Override
            public void onNegativeClick() {
                binding.btnUpdateUser.setEnabled(true);
            }
        };
        AlertDialog dialog = CustomDialog.getUpdateDialog(requireActivity(), "Update User", "Are you sure want to update?", "Yes", "Cancel", listener);

        dialog.show();
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