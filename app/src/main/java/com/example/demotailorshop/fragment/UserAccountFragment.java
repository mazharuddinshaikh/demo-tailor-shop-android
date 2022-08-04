package com.example.demotailorshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demotailorshop.MainActivity;
import com.example.demotailorshop.R;
import com.example.demotailorshop.adapter.UserAccountAdapter;
import com.example.demotailorshop.databinding.FragmentUserAccountBinding;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserAccountFragment extends Fragment {


    public UserAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserAccountFragment newInstance() {
        UserAccountFragment fragment = new UserAccountFragment();
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
        FragmentUserAccountBinding binding = FragmentUserAccountBinding.inflate(getLayoutInflater());
        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (user == null) {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.putExtra(DtsSharedPreferenceUtil.KEY_AUTHENTICATION_MESSAGE, DtsSharedPreferenceUtil.VALUE_AUTHENTICATION_MESSAGE);
            startActivity(intent);
        }
        View view = binding.getRoot();
        final List<String> optionsList = Arrays.asList("Update User", "Update Shop Details", "Update Password", "Add / Update Dress Types", "Logout");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        UserAccountAdapter adapter = new UserAccountAdapter();
        adapter.setOptionList(optionsList);
        adapter.setUserAccountSelectionListener(this::openFragment);
        binding.rvUserAccount.setLayoutManager(layoutManager);
        binding.rvUserAccount.setAdapter(adapter);

        return view;
    }

    private void openFragment(int position) {
        switch (position) {
            case 0:
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvContainer, UpdateUserFragment.class, null, "UpdateUserFragment")
                        .addToBackStack("UpdateUserFragment")
                        .commit();
                break;
            case 1:
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvContainer, UpdateShopFragment.class, null, "UpdateShopFragment")
                        .addToBackStack("UpdateShopFragment")
                        .commit();
                break;
            case 2:
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvContainer, UpdatePasswordFragment.class, null, "UpdatePasswordFragment")
                        .addToBackStack("UpdatePasswordFragment")
                        .commit();

                break;
            case 3:
//                Add update dress types
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvContainer, UserDressTypeFragment.class, null, "UserDressTypeFragment")
                        .addToBackStack("UserDressTypeFragment")
                        .commit();

                break;
            case 4:
//                Logout
                DtsSharedPreferenceUtil.getInstance().clearAllPreferences(requireActivity());
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                break;
        }

    }
}