package com.example.demotailorshop.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.api.UserDressTypesApi;
import com.example.demotailorshop.databinding.FragmentAddUpdateUserDressTypeBinding;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.listener.CustomDialogListener;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.example.demotailorshop.view.custom.CustomDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUpdateUserDressTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUpdateUserDressTypeFragment extends Fragment {
    private static final String TAG = "UserDressTypeFragment";
    private DressType dressType;


    public AddUpdateUserDressTypeFragment() {
        // Required empty public constructor
    }

    public void setDressType(DressType dressType) {
        this.dressType = dressType;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddUpdateUserDressTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddUpdateUserDressTypeFragment newInstance() {
        AddUpdateUserDressTypeFragment fragment = new AddUpdateUserDressTypeFragment();
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
        FragmentAddUpdateUserDressTypeBinding binding = FragmentAddUpdateUserDressTypeBinding.inflate(getLayoutInflater());
        addListenerForNavigation(binding);

        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (dressType != null) {
            binding.tilName.getEditText().setText(dressType.getTypeName());
            binding.tilDescription.getEditText().setText(dressType.getTypeDescription());
            binding.tilComment.getEditText().setText(dressType.getComment());
            binding.tilPrice.getEditText().setText(String.valueOf(dressType.getPrice()));
        }
        binding.btnUpdateDressType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDressTypeDialog(binding, user);
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        binding.tilPrice.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!DtsUtils.isNullOrEmpty(s.toString())) {
                    double price = Double.parseDouble(s.toString());
                    dressType.setPrice(price);
                }
            }
        });
        return binding.getRoot();
    }

    private void addListenerForNavigation(FragmentAddUpdateUserDressTypeBinding binding) {
        binding.mtbToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void showUpdateDressTypeDialog(FragmentAddUpdateUserDressTypeBinding binding, User user) {
        UserDressTypesApi userDressTypesApi = DtsApiFactory.getRetrofitInstance().create(UserDressTypesApi.class);
        CustomDialogListener listener = new CustomDialogListener() {
            @Override
            public void onPositiveClick() {
                Log.v(TAG, "Updating user dress type");
                binding.btnUpdateDressType.setEnabled(false);
                Map<String, String> headersMap = new HashMap<>();
                headersMap.put(DtsSharedPreferenceUtil.KEY_AUTHORIZATION, user.getAuthenticationToken());
                userDressTypesApi.updateDressType(headersMap, dressType).enqueue(new Callback<ApiResponse<DressType>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<DressType>> call, @NonNull Response<ApiResponse<DressType>> response) {
                        DtsSharedPreferenceUtil.getInstance().remove(requireActivity(), DtsSharedPreferenceUtil.KEY_DRESS_TYPES);
                        Snackbar.make(binding.getRoot(), "Dress type details updated successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
                        binding.btnUpdateDressType.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<DressType>> call, @NonNull Throwable t) {
                        Snackbar.make(binding.getRoot(), "Something went wrong", BaseTransientBottomBar.LENGTH_SHORT).show();
                        binding.btnUpdateDressType.setEnabled(true);

                    }
                });
            }

            @Override
            public void onNegativeClick() {
                binding.btnUpdateDressType.setEnabled(true);
            }
        };
        AlertDialog alertDialog = CustomDialog.getUpdateDialog(requireActivity(), "Update dress type price", "Are you sure you update price?", "Yes", "Cancel", listener);
        alertDialog.show();
    }
}