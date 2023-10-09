package com.example.demotailorshop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotailorshop.R;
import com.example.demotailorshop.databinding.ItemDressListBinding;
import com.example.demotailorshop.databinding.ItemDressListLoadingBinding;
import com.example.demotailorshop.databinding.ItemNoDressListBinding;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.Measurement;
import com.example.demotailorshop.listener.DressListClickListener;
import com.example.demotailorshop.listener.RetryListener;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.ArrayList;
import java.util.List;

public class DressListAdapter extends RecyclerView.Adapter {
    private List<Dress> dressList;
    private ApiError apiError;
    private DressListClickListener listener;
    private RetryListener retryListener;
    private Context context;
    private int displayWidth;
    private static final int VIEW_TYPE_NO_DRESS_LIST = 0;
    private static final int VIEW_TYPE_DRESS_LIST_LOADING = 1;
    private static final int VIEW_TYPE_DRESS_LIST = 2;

    public DressListAdapter() {
    }

    public void setDressList(List<Dress> dressList) {
        this.dressList = dressList;
    }

    public void setListener(DressListClickListener listener) {
        this.listener = listener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setApiError(ApiError apiError) {
        this.apiError = apiError;
    }

    public void addDress(Dress dress) {
        if (dressList != null) {
            dressList.add(dress);
        }
    }

    public void removeDress(int position) {
        if (dressList != null) {
            dressList.remove(position);
        }
    }

    public void setRetryListener(RetryListener retryListener) {
        this.retryListener = retryListener;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
//dress list empty
//        no dress
//        dress list not empty and current position object null then loading
//        dress list not empty and current position object not null then show dress
        if (!DtsUtils.isNullOrEmpty(dressList) && position == 0 && dressList.get(0) == null) {
            viewType = VIEW_TYPE_NO_DRESS_LIST;
        } else if ((!DtsUtils.isNullOrEmpty(dressList) && position != 0 && dressList.get(position) == null)) {
            viewType = VIEW_TYPE_DRESS_LIST_LOADING;
        } else if (!DtsUtils.isNullOrEmpty(dressList)) {
            viewType = VIEW_TYPE_DRESS_LIST;
        }

        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NO_DRESS_LIST:
                ItemNoDressListBinding noDressListBinding = ItemNoDressListBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new NoDressViewHolder(noDressListBinding);
            case VIEW_TYPE_DRESS_LIST:
                ItemDressListBinding dressListBinding = ItemDressListBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new DressViewHolder(dressListBinding);
            case VIEW_TYPE_DRESS_LIST_LOADING:
                ItemDressListLoadingBinding dressListLoadingBinding = ItemDressListLoadingBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new DressLoadingViewHolder(dressListLoadingBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case VIEW_TYPE_DRESS_LIST:
                DressViewHolder dressViewHolder = (DressViewHolder) holder;
                Dress dress = dressList.get(position);
                ItemDressListBinding binding = dressViewHolder.binding;
                binding.tvName.setText(dress.getCustomer().getFirstName());
                binding.tvInvoiceNo.setText(String.valueOf(dress.getCustomer().getInvoiceId()));
                binding.tvDeliveryStatus.setText(dress.getDeliveryStatus());
                binding.tvPaymentStatus.setText(dress.getPaymentStatus());
                binding.tvDeliveryDate.setText(dress.getDeliveryDate());
                Measurement measurement = dress.getMeasurement();
                DressListItemAdapter dressListItemAdapter = new DressListItemAdapter();
                List<String> dressListItemImageList = null;
                if(measurement != null && measurement.getMeasurementImage() != null) {
                    Measurement.MeasurementImage measurementImage= measurement.getMeasurementImage();
                    dressListItemImageList = getDressListItemImageList(measurementImage);
                }
                dressListItemAdapter.setContext(this.context);
                dressListItemAdapter.setDisplayWidth(displayWidth);
                dressListItemAdapter.setImageList(dressListItemImageList);
                dressListItemAdapter.setListener(listener);
                dressListItemAdapter.setDress(dress);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                binding.rvDressImageList.setAdapter(dressListItemAdapter);
                binding.rvDressImageList.setLayoutManager(layoutManager);
                dressListItemAdapter.notifyDataSetChanged();
                View view = binding.getRoot();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDressClicked(dress);
                    }
                });

                break;
            case VIEW_TYPE_NO_DRESS_LIST:
                if (!DtsUtils.isNullOrEmpty(dressList) && position == 0 && dressList.get(0) == null && apiError == null) {
                    NoDressViewHolder noDressViewHolder = (NoDressViewHolder) holder;
                    ItemNoDressListBinding noDressListBinding = noDressViewHolder.getBinding();
                    noDressListBinding.btnRetry.setVisibility(View.GONE);
                    noDressListBinding.tvNoDressList.setText("Dress List Loading...");
                } else if (apiError != null) {
                    NoDressViewHolder noDressViewHolder = (NoDressViewHolder) holder;
                    ItemNoDressListBinding noDressListBinding = noDressViewHolder.getBinding();
                    String statusMessage = apiError.getHttpStatus() + " - " + apiError.getMessage();
                    noDressListBinding.tvNoDressList.setText(statusMessage);
                    noDressListBinding.btnRetry.setVisibility(View.VISIBLE);
                    noDressListBinding.btnRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            retryListener.retry();
                        }
                    });
                }
                break;
            case VIEW_TYPE_DRESS_LIST_LOADING:
                String loadingMessage = null;
                DressLoadingViewHolder dressLoadingViewHolder = (DressLoadingViewHolder) holder;
                ItemDressListLoadingBinding dressListLoadingBinding = dressLoadingViewHolder.getBinding();
                if (apiError != null) {
                    loadingMessage = apiError.getMessage();
                } else {
                    loadingMessage = "Loading";
                }
                dressListLoadingBinding.getRoot().setText(loadingMessage);
                break;
        }
    }

    private List<String> getDressListItemImageList(Measurement.MeasurementImage measurementImage) {
        List<String> dressListItemImageList = new ArrayList<>();
            dressListItemImageList.add(getFirstImage(measurementImage.getMeasurementImageList()));
            dressListItemImageList.add(getFirstImage(measurementImage.getRawImageList()));
            dressListItemImageList.add(getFirstImage(measurementImage.getPatternImageList()));
            dressListItemImageList.add(getFirstImage(measurementImage.getSeavedImageList()));
        return dressListItemImageList;
    }

    @Override
    public int getItemCount() {

        return DtsUtils.isNullOrEmpty(dressList) ? 1 : dressList.size();
    }

    private String getFirstImage(List<String> imageList) {
        String imagePath = null;
        if (!DtsUtils.isNullOrEmpty(imageList)) {
            imagePath = imageList.get(0);
        }
        return imagePath;
    }

    public static class NoDressViewHolder extends RecyclerView.ViewHolder {
        ItemNoDressListBinding binding;

        public NoDressViewHolder(@NonNull ItemNoDressListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemNoDressListBinding getBinding() {
            return binding;
        }
    }

    public static class DressViewHolder extends RecyclerView.ViewHolder {
        ItemDressListBinding binding;

        public DressViewHolder(ItemDressListBinding binding) {
//        public DressViewHolder(View view) {
            super(binding.getRoot());
            this.binding = binding;
//            ivMeasurementImage = view.findViewById(R.id.ivMeasurementImage);
//            ivRawImage = view.findViewById(R.id.ivRawImage);
//            ivPatternImage = view.findViewById(R.id.ivPatternImage);
//            ivSeavedImage = view.findViewById(R.id.ivSeavedImage);
//            tvName = view.findViewById(R.id.tvName);
//            tvInvoiceNo = view.findViewById(R.id.tvInvoiceNo);
//            tvPaymentStatus = view.findViewById(R.id.tvPaymentStatus);
//            tvDeliveryStatus = view.findViewById(R.id.tvDeliveryStatus);
        }
    }

    public static class DressLoadingViewHolder extends RecyclerView.ViewHolder {
        private ItemDressListLoadingBinding binding;

        public DressLoadingViewHolder(@NonNull ItemDressListLoadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemDressListLoadingBinding getBinding() {
            return binding;
        }
    }
}


