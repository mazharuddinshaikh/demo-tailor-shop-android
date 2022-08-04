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
import com.example.demotailorshop.adapter.FilterAdapter;
import com.example.demotailorshop.databinding.FragmentDressFilterBinding;
import com.example.demotailorshop.entity.DressFilter;
import com.example.demotailorshop.viewmodel.DressFilterViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DressFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DressFilterFragment extends Fragment {
    private static final String TAG = "DressFilterFragment";


    public DressFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DressFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DressFilterFragment newInstance() {
        DressFilterFragment fragment = new DressFilterFragment();
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
        Log.v(TAG, "Filter fragment created");
        // Inflate the layout for this fragment
        FragmentDressFilterBinding binding = FragmentDressFilterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        changeVisibility(binding);
        binding.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility(binding);
            }
        });
        binding.btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility(binding);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        FilterAdapter adapter = new FilterAdapter();
        adapter.setContext(requireActivity());
        binding.rvDressFilter.setAdapter(adapter);
        binding.rvDressFilter.setLayoutManager(layoutManager);
        DressFilterViewModel dressFilterViewModel = new ViewModelProvider(requireActivity()).get(DressFilterViewModel.class);
        dressFilterViewModel.getDressFilterListLiveData().observe(requireActivity(), new Observer<List<DressFilter>>() {
            @Override
            public void onChanged(List<DressFilter> dressFilters) {
                Log.v(TAG, "Filter fragment changed");
                adapter.setFilterList(dressFilters);
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private void changeVisibility(FragmentDressFilterBinding binding) {
        if (binding.grpFilter.getVisibility() == View.GONE) {
            binding.grpFilter.setVisibility(View.VISIBLE);
            binding.ivFilter.setImageResource(R.drawable.ic_arrow_drop_up_24);
        } else {
            binding.grpFilter.setVisibility(View.GONE);
            binding.ivFilter.setImageResource(R.drawable.ic_arrow_drop_down_24);
        }
    }
}