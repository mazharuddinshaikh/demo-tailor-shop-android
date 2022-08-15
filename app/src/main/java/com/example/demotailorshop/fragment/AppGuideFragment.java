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

import com.example.demotailorshop.adapter.AppGuideAdapter;
import com.example.demotailorshop.api.AppGuideApi;
import com.example.demotailorshop.databinding.FragmentAppGuideBinding;
import com.example.demotailorshop.entity.AppGuide;
import com.example.demotailorshop.utils.DtsUtils;
import com.example.demotailorshop.viewmodel.AppGuideViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppGuideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppGuideFragment extends Fragment {
    private static final String TAG = "AppGuideFragment";

    public AppGuideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AppGuideFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppGuideFragment newInstance() {
        AppGuideFragment fragment = new AppGuideFragment();
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
        FragmentAppGuideBinding binding = FragmentAppGuideBinding.inflate(getLayoutInflater());
        addListenerForNavigation(binding);
        AppGuideViewModel appGuideViewModel = new ViewModelProvider(requireActivity()).get(AppGuideViewModel.class);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        AppGuideAdapter appGuideAdapter = new AppGuideAdapter();
        appGuideAdapter.setContext(requireActivity());
        binding.rvAppGuide.setLayoutManager(layoutManager);
        binding.rvAppGuide.setAdapter(appGuideAdapter);
        appGuideViewModel.getUserHelpListMutableLiveData().observe(requireActivity(), new Observer<List<AppGuide>>() {
            @Override
            public void onChanged(List<AppGuide> appGuides) {
                if (!DtsUtils.isNullOrEmpty(appGuides)) {
                    appGuideAdapter.setAppGuideList(appGuides);
                    appGuideAdapter.notifyDataSetChanged();
                }
                Log.v(TAG, "app guide");
            }
        });
        return binding.getRoot();
    }

    private void addListenerForNavigation(FragmentAppGuideBinding binding) {
        binding.mtbToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}