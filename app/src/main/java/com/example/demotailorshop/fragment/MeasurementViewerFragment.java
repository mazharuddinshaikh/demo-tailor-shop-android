package com.example.demotailorshop.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.demotailorshop.R;
import com.example.demotailorshop.adapter.MeasurementViewAdapter;
import com.example.demotailorshop.databinding.FragmentMeasurementViewerBinding;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class MeasurementViewerFragment extends Fragment {
    private final List<String> imageUrlList;
    private final List<Uri> imageUriList;
    private final int itemPosition;


    public MeasurementViewerFragment(List<String> imageUrlList, List<Uri> imageUriList, int itemPosition) {
        this.imageUrlList = imageUrlList;
        this.imageUriList = imageUriList;
        this.itemPosition = itemPosition;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final int displayWidth = DtsUtils.getScreenWidth(requireActivity());
        FragmentMeasurementViewerBinding binding = FragmentMeasurementViewerBinding.inflate(getLayoutInflater());
        binding.mtbToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        MeasurementViewAdapter measurementViewAdapter = new MeasurementViewAdapter();
        measurementViewAdapter.setImageUrlList(imageUrlList);
        measurementViewAdapter.setImageUriList(imageUriList);
        measurementViewAdapter.setContext(requireActivity());
        measurementViewAdapter.setDisplayWidth(displayWidth);
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvMeasurement.setAdapter(measurementViewAdapter);
        binding.rvMeasurement.setLayoutManager(layoutManager);
        measurementViewAdapter.setMeasurementClickListener(new MeasurementViewAdapter.MeasurementClickListener() {
            @Override
            public void onMeasurementClick(List<String> imageUrlList, List<Uri> imageUriList, int position) {

                if (!DtsUtils.isNullOrEmpty(imageUrlList) && position < imageUrlList.size() && DtsUtils.isNullOrEmpty(imageUriList)) {
                    Glide.with(requireActivity()).load(imageUrlList.get(position))
                            .fallback(R.drawable.ic_no_photography_24)
                            .error(R.drawable.pant)
                            .into(binding.pzivMeasurement);

                } else if (!DtsUtils.isNullOrEmpty(imageUriList) && position < imageUriList.size() && DtsUtils.isNullOrEmpty(imageUrlList)) {
                    Glide.with(requireActivity()).load(imageUriList.get(position))
                            .fallback(R.drawable.ic_no_photography_24)
                            .error(R.drawable.pant)
                            .into(binding.pzivMeasurement);

                } else if (!DtsUtils.isNullOrEmpty(imageUrlList) && !DtsUtils.isNullOrEmpty(imageUriList)) {
                    if (position < imageUrlList.size()) {
                        Glide.with(requireActivity()).load(imageUrlList.get(position))
                                .fallback(R.drawable.ic_no_photography_24)
                                .error(R.drawable.pant)
                                .into(binding.pzivMeasurement);

                    } else {
                        int uriPosition = position - imageUrlList.size();
                        Glide.with(requireActivity()).load(imageUriList.get(uriPosition))
                                .fallback(R.drawable.ic_no_photography_24)
                                .error(R.drawable.pant)
                                .into(binding.pzivMeasurement);

                    }
                }

            }
        });
        measurementViewAdapter.notifyDataSetChanged();
        binding.rvMeasurement.smoothScrollToPosition(itemPosition);

        return binding.getRoot();
    }
}