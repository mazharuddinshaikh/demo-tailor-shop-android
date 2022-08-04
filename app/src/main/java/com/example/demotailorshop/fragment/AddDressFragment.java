package com.example.demotailorshop.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demotailorshop.MainActivity;
import com.example.demotailorshop.R;
import com.example.demotailorshop.databinding.FragmentAddDressBinding;
import com.example.demotailorshop.databinding.FragmentSigninBinding;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDressFragment extends Fragment {
    private static final String TAG = "AddDressFragment";

    public AddDressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddDressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDressFragment newInstance() {
        AddDressFragment fragment = new AddDressFragment();
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
        FragmentAddDressBinding binding = FragmentAddDressBinding.inflate(getLayoutInflater());
        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (user == null) {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.putExtra(DtsSharedPreferenceUtil.KEY_AUTHENTICATION_MESSAGE, DtsSharedPreferenceUtil.VALUE_AUTHENTICATION_MESSAGE);
            startActivity(intent);
        }
        binding.btnAddDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Adding new dress and customer");
                DressDetailFragment dressDetailFragment = new DressDetailFragment();
                dressDetailFragment.setNewCustomer(true);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fcvContainer, dressDetailFragment, "DressDetailFragment")
                        .addToBackStack("DressDetailFragment")
                        .commit();
            }
        });
        return binding.getRoot();
    }
}