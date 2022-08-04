package com.example.demotailorshop.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demotailorshop.R;
import com.example.demotailorshop.adapter.UserDressTypeAdapter;
import com.example.demotailorshop.databinding.FragmentUserDressTypeBinding;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.example.demotailorshop.viewmodel.UserDressTypeViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserDressTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDressTypeFragment extends Fragment {
    private static final String TAG = "UserDressTypeFragment";

    public UserDressTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserDressTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDressTypeFragment newInstance() {
        UserDressTypeFragment fragment = new UserDressTypeFragment();
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
        FragmentUserDressTypeBinding binding = FragmentUserDressTypeBinding.inflate(getLayoutInflater());
        addListenerForNavigation(binding);
        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        UserDressTypeViewModel userDressTypeViewModel = new ViewModelProvider(requireActivity())
                .get(UserDressTypeViewModel.class);
        if (user != null) {
            List<DressType> dressTypeList = DtsSharedPreferenceUtil.getInstance().getDressTypesFromSharedPref(requireActivity(), DtsSharedPreferenceUtil.KEY_DRESS_TYPES);

            UserDressTypeAdapter userDressTypeAdapter = new UserDressTypeAdapter();
            userDressTypeAdapter.setDressTypeClickListener(new UserDressTypeAdapter.OnDressTypeClickListener() {
                @Override
                public void onDressTypeClick(DressType dressType) {
                    AddUpdateUserDressTypeFragment addUpdateUserDressTypeFragment = new AddUpdateUserDressTypeFragment();
                    addUpdateUserDressTypeFragment.setDressType(dressType);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fcvContainer, addUpdateUserDressTypeFragment, "AddUpdateUserDressTypeFragment")
                            .addToBackStack("AddUpdateUserDressTypeFragment")
                            .commit();
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            if (DtsUtils.isNullOrEmpty(dressTypeList)) {
                Log.v(TAG, "Dress type list not present");
                userDressTypeViewModel.setUser(user);
                userDressTypeViewModel.getDressTypeMutableLiveData().observe(requireActivity(), new Observer<List<DressType>>() {
                    @Override
                    public void onChanged(List<DressType> dressTypeList) {
                        DtsSharedPreferenceUtil.getInstance().saveUserToSharedPreference(requireActivity(), dressTypeList, DtsSharedPreferenceUtil.KEY_DRESS_TYPES);
                        userDressTypeAdapter.setDressTypeList(dressTypeList);
                        userDressTypeAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                Log.v(TAG, "Dress type list present");
                userDressTypeAdapter.setDressTypeList(dressTypeList);
                userDressTypeAdapter.notifyDataSetChanged();
            }
            binding.rvUserDressType.setLayoutManager(layoutManager);
            binding.rvUserDressType.setAdapter(userDressTypeAdapter);

        }


        return binding.getRoot();
    }

    private void addListenerForNavigation(FragmentUserDressTypeBinding binding) {
        binding.mtbToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }


}