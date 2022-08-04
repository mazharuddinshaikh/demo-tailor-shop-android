package com.example.demotailorshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demotailorshop.R;
import com.example.demotailorshop.databinding.FragmentUserHelpBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHelpFragment extends Fragment {
    FragmentUserHelpBinding binding;

    public UserHelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserHelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHelpFragment newInstance() {
        UserHelpFragment fragment = new UserHelpFragment();
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
        binding = FragmentUserHelpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.btnAppGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvContainer, SignupFragment.class, null, "SignupFragment")
                        .commit();
            }
        });
        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvContainer, SigninFragment.class, null, "SigninFragment")
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