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
import com.example.demotailorshop.databinding.FragmentUpdateShopBinding;
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
 * Use the {@link UpdateShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateShopFragment extends Fragment {
    private static final String TAG = "UpdateShopFragment";
    private FragmentUpdateShopBinding binding;


    public UpdateShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UpdateShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateShopFragment newInstance() {
        UpdateShopFragment fragment = new UpdateShopFragment();
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
        binding = FragmentUpdateShopBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        addListenerForNavigation();
        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (user == null) {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.putExtra(DtsSharedPreferenceUtil.KEY_AUTHENTICATION_MESSAGE, DtsSharedPreferenceUtil.VALUE_AUTHENTICATION_MESSAGE);
            startActivity(intent);
        } else {
            binding.tilShopName.getEditText().setText(user.getShopName());
            binding.tilShopAddress.getEditText().setText(user.getAddress());
            binding.tilShopMobile.getEditText().setText(user.getAlternateMobileNo());
        }
        binding.btnUpdateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnUpdateShop.setEnabled(false);
                binding.tilShopName.setError(null);
                binding.tilShopMobile.setError(null);
                binding.tilShopAddress.setError(null);
                String userName = user.getUserName();
                String shopName = binding.tilShopName.getEditText().getText().toString();
                String mobileNo = binding.tilShopMobile.getEditText().getText().toString();
                String shopAddress = binding.tilShopAddress.getEditText().getText().toString();
                boolean isReadyToUpdate = false;
                if (DtsUtils.isNullOrEmpty(shopName)) {
                    binding.tilShopName.setError("Please enter valid name");
                    binding.btnUpdateShop.setEnabled(true);
                } else {
                    isReadyToUpdate = true;
                    if (!DtsUtils.isNullOrEmpty(mobileNo)) {
                        boolean isValidMobileNo = Patterns.PHONE.matcher(mobileNo).matches();
                        if (!isValidMobileNo) {
                            binding.tilShopMobile.setError("Please enter valid mobile number");
                            isReadyToUpdate = false;
                        }
                    }
                }

                if (isReadyToUpdate) {
                    User updateUser = new User();
                    updateUser.setUserName(userName);
                    updateUser.setShopName(shopName);
                    if (!DtsUtils.isNullOrEmpty(shopAddress)) {
                        updateUser.setAddress(shopAddress);
                    }
                    if (!DtsUtils.isNullOrEmpty(mobileNo)) {
                        updateUser.setAlternateMobileNo(mobileNo);
                    }
                    showUpdateShopDialog(updateUser);
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

    private void showUpdateShopDialog(User updateUser) {
        final UserApi userApi = DtsApiFactory.getRetrofitInstance().create(UserApi.class);
        CustomDialogListener listener = new CustomDialogListener() {
            @Override
            public void onPositiveClick() {
                userApi.updateShop(updateUser).enqueue(new Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<User>> call, @NonNull Response<ApiResponse<User>> response) {
                        binding.btnUpdateShop.setEnabled(true);
                        int httpStatus = response.code();
                        if (httpStatus == ApiUtils.SUCCESS) {
                            Log.i(TAG, "Signin successfully");
                            DtsSharedPreferenceUtil.getInstance().saveUserToSharedPreference(requireActivity(), response.body().getResult(), DtsSharedPreferenceUtil.KEY_USER);
                            Snackbar.make(binding.getRoot(), response.body().getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        } else if (httpStatus == ApiUtils.BAD_REQUEST) {
                            Log.i(TAG, "Username / Password incorrect");
                            ApiError apiError = ApiUtils.getApiErrorResponse(response);
                            Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                        } else {
                            ApiError apiError = ApiUtils.getDefaultErrorResponse(response.code(), "Something went wrong! Please retry");
                            Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<User>> call, @NonNull Throwable t) {
                        binding.btnUpdateShop.setEnabled(true);
                        Snackbar.make(binding.getRoot(), "Something went wrong! Please retry", BaseTransientBottomBar.LENGTH_SHORT).show();
                        Log.e(TAG, "Something went wrong! " + t);
                    }
                });
            }

            @Override
            public void onNegativeClick() {
                binding.btnUpdateShop.setEnabled(true);
            }
        };
        AlertDialog alertDialog = CustomDialog.getUpdateDialog(requireActivity(), "Update shop detials", "Are you sure you want to update shop details ?", "Yes", "Cancel", listener);
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