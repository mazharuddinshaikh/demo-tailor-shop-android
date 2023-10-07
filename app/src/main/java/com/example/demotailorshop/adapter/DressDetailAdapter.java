package com.example.demotailorshop.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemDressDetailBinding;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.entity.Measurement;
import com.example.demotailorshop.listener.DressDetailListener;
import com.example.demotailorshop.listener.MeasurementListener;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

public class DressDetailAdapter extends RecyclerView.Adapter<DressDetailAdapter.DressDetailViewHolder> {
    private FragmentManager fragmentManager;
    private List<Dress> dressList;
    private int displayWidth;
    private Context context;
    private boolean isNewCustomer;
    private List<String> deliveryStatusList;
    private List<String> paymentStatusList;
    private List<DressType> dressTypeList;
    private MeasurementListener measurementListener;
    private DressDetailListener dressDetailListener;

    public void setDressList(List<Dress> dressList) {
        this.dressList = dressList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DressDetailAdapter() {
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public void setNewCustomer(boolean newCustomer) {
        isNewCustomer = newCustomer;
    }

    public void setMeasurementListener(MeasurementListener measurementListener) {
        this.measurementListener = measurementListener;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setDeliveryStatusList(List<String> deliveryStatusList) {
        this.deliveryStatusList = deliveryStatusList;
    }

    public void setPaymentStatusList(List<String> paymentStatusList) {
        this.paymentStatusList = paymentStatusList;
    }

    public void setDressDetailListener(DressDetailListener dressDetailListener) {
        this.dressDetailListener = dressDetailListener;
    }

    public void setDressTypeList(List<DressType> dressTypeList) {
        this.dressTypeList = dressTypeList;
    }

    @NonNull
    @Override
    public DressDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDressDetailBinding binding = ItemDressDetailBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new DressDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DressDetailViewHolder holder, int position) {
        ItemDressDetailBinding binding = holder.getBinding();
        binding.tilDressType.getEditText().setEnabled(false);
        binding.tilOrderDate.getEditText().setEnabled(false);
        binding.tilDeliveryDate.getEditText().setEnabled(false);
        binding.tilDeliveryStatus.getEditText().setEnabled(false);
        binding.tilPaymentStatus.getEditText().setEnabled(false);
        binding.tilAmount.getEditText().setEnabled(false);

        Dress dress = dressList.get(position);
//        if (!isNewCustomer) {
            binding.tilDiscountedAmount.getEditText().setEnabled(false);
            binding.tilComment.getEditText().setEnabled(false);
            binding.tilDressCount.getEditText().setEnabled(false);

//            binding.tilOrderDate.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
//            binding.tilDeliveryDate.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
//            binding.tilDeliveryStatus.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
//            binding.tilPaymentStatus.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
//            binding.tilAmount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
//            binding.tilDiscountedAmount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
//            binding.tilComment.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
//            binding.tilDressCount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);


            if (dress != null) {

                DressType dressType = dress.getDressType();
                if (dressType != null) {
                    String dressTypeName = dressType.getTypeName();
                    if (!DtsUtils.isNullOrEmpty(dressType.getTypeDescription())) {
                        dressTypeName += "(" + dressType.getTypeDescription() + ")";
                    }
                    binding.tvDressType.setText(MessageFormat.format("{0} {1}", dress.getNumberOfDress(), dressTypeName));
                    binding.tilDressType.getEditText().setText(dress.getDressType().getTypeName());
                }
                binding.tilDressCount.getEditText().setText(String.valueOf(dress.getNumberOfDress()));
                if (!DtsUtils.isNullOrEmpty(dress.getOrderDate())) {
                    binding.tilOrderDate.getEditText().setText(dress.getOrderDate());
                    binding.tilOrderDate.setVisibility(View.VISIBLE);
                }

                if (!DtsUtils.isNullOrEmpty(dress.getDeliveryDate())) {
                    binding.tilDeliveryDate.getEditText().setText(dress.getDeliveryDate());
                    binding.tilDeliveryDate.setVisibility(View.VISIBLE);
                }
                if (!DtsUtils.isNullOrEmpty(dress.getDeliveryStatus())) {
                    binding.tilDeliveryStatus.getEditText().setText(dress.getDeliveryStatus());
                    binding.tilDeliveryDate.setVisibility(View.VISIBLE);
                }

                if (!DtsUtils.isNullOrEmpty(dress.getPaymentStatus())) {
                    binding.tilPaymentStatus.getEditText().setText(dress.getPaymentStatus());
                    binding.tilPaymentStatus.setVisibility(View.VISIBLE);
                }
                binding.tilAmount.getEditText().setText(String.valueOf(dress.getPrice()));
                binding.tilDiscountedAmount.getEditText().setText(String.valueOf(dress.getDiscountedPrice()));
                if (!DtsUtils.isNullOrEmpty(dress.getComment())) {
                    binding.tilComment.getEditText().setText(dress.getComment());
                    binding.tilComment.setVisibility(View.VISIBLE);
                }
//                Measurement measurement = dress.getMeasurement();
//                MeasurementAdapter measurementAdapter = new MeasurementAdapter();
//                measurementAdapter.setMeasurement(measurement);
//                measurementAdapter.setDisplayWidth(displayWidth);
//                measurementAdapter.setContext(context);
//                measurementAdapter.setMeasurementListener(measurementListener);
//                measurementAdapter.setDressId(dress.getDressId());
//                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                binding.rvMeasurement.setAdapter(measurementAdapter);
//                binding.rvMeasurement.setLayoutManager(layoutManager);
//                measurementAdapter.notifyDataSetChanged();
            }
//        }
//        else {
//            binding.tilDiscountedAmount.getEditText().setEnabled(true);
//            binding.tilComment.getEditText().setEnabled(true);
//            binding.tilDressCount.getEditText().setEnabled(true);
//
//            Measurement measurement = dress.getMeasurement();
//            MeasurementAdapter measurementAdapter = new MeasurementAdapter();
//            measurementAdapter.setMeasurement(measurement);
//            measurementAdapter.setDisplayWidth(displayWidth);
//            measurementAdapter.setContext(context);
//            measurementAdapter.setMeasurementListener(measurementListener);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            binding.rvMeasurement.setAdapter(measurementAdapter);
//            binding.rvMeasurement.setLayoutManager(layoutManager);
//            measurementAdapter.notifyDataSetChanged();
//        }

        Measurement measurement = dress.getMeasurement();
//        if(measurement == null) {
//            measurement = new Measurement();
//        }
        MeasurementAdapter measurementAdapter = new MeasurementAdapter();
        measurementAdapter.setMeasurement(measurement);
        measurementAdapter.setDisplayWidth(displayWidth);
        measurementAdapter.setContext(context);
        measurementAdapter.setMeasurementListener(measurementListener);
        measurementAdapter.setDressId(dress.getDressId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvMeasurement.setAdapter(measurementAdapter);
        binding.rvMeasurement.setLayoutManager(layoutManager);
        measurementAdapter.notifyDataSetChanged();
        binding.tilDressType.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilDressType.getEditText().setEnabled(true);
                binding.tilDressType.getEditText().setFocusable(false);
                PopupMenu popupMenu = new PopupMenu(context, binding.tilDressType);
                for (DressType dressType : dressTypeList) {
                    popupMenu.getMenu().add(dressType.getTypeName());
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        DressType dressType = null;
                        for (DressType dressType1 : dressTypeList) {
                            if (dressType1.getTypeName().contentEquals(item.getTitle())) {
                                dressType = dressType1;
                            }
                        }
                        dress.setDressType(dressType);
                        dressDetailListener.onUpdateDress(dress);
                        CharSequence status = item.getTitle();
                        binding.tilDressType.getEditText().setText(status);
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        binding.tilDressType.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!DtsUtils.isNullOrEmpty(s.toString())) {
                    String name = s.toString();
                    DressType dressType = null;
                    for (DressType type : dressTypeList) {
                        if (type.getTypeName().equals(name)) {
                            dressType = type;
                        }
                    }
                    dress.setDressType(dressType);
                    double price = 0.0;
                    if (dress.getNumberOfDress() > 0) {
                        price = dressType.getPrice() * dress.getNumberOfDress();
                    } else {
                        price = dressType.getPrice();
                    }
                    dress.setPrice(price);
                    binding.tilAmount.getEditText().setText(String.valueOf(price));
                }
                dressDetailListener.onUpdateDress(dress);
            }
        });

        binding.tilDressCount.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilDressCount.getEditText().setEnabled(true);
            }
        });
        binding.tilDressCount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!DtsUtils.isNullOrEmpty(s.toString())) {
                    int numberOfDress = Integer.parseInt(s.toString());
                    dress.setNumberOfDress(numberOfDress);
                    DressType dressType = dress.getDressType();

                    if (dressType != null) {
                        double price = 0.0;
                        if (numberOfDress > 0) {
                            price = dressType.getPrice() * numberOfDress;
                        } else {
                            price = dressType.getPrice();
                        }
                        dress.setPrice(price);
                        binding.tilAmount.getEditText().setText(String.valueOf(price));
                    }

                }

                dressDetailListener.onUpdateDress(dress);
            }
        });
        binding.tilOrderDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilOrderDate.getEditText().setEnabled(true);
                binding.tilOrderDate.getEditText().setFocusable(false);
                addDatePicker("Select Order Date", binding.tilOrderDate.getEditText());
            }
        });
        binding.tilOrderDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDatePicker("Select Order Date", binding.tilOrderDate.getEditText());
            }
        });
        binding.tilOrderDate.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dress.setOrderDate(s.toString());
                dressDetailListener.onUpdateDress(dress);
            }
        });
        binding.tilDeliveryDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilDeliveryDate.getEditText().setEnabled(true);
                binding.tilDeliveryDate.getEditText().setFocusable(false);
                addDatePicker("Select Deliver Date", binding.tilDeliveryDate.getEditText());
            }
        });
        binding.tilDeliveryDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDatePicker("Select Delivery Date", binding.tilDeliveryDate.getEditText());
            }
        });
        binding.tilDeliveryDate.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dress.setDeliveryDate(s.toString());
                dressDetailListener.onUpdateDress(dress);
            }
        });
        binding.tilDeliveryStatus.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilDeliveryStatus.getEditText().setEnabled(true);
                binding.tilDeliveryStatus.getEditText().setFocusable(false);
                showPopupMenu(binding.tilDeliveryStatus, deliveryStatusList);
            }
        });
        binding.tilDeliveryStatus.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(binding.tilDeliveryStatus, deliveryStatusList);
            }
        });
        binding.tilDeliveryStatus.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dress.setDeliveryStatus(s.toString());
                dressDetailListener.onUpdateDress(dress);
            }
        });
        binding.tilPaymentStatus.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilPaymentStatus.getEditText().setEnabled(true);
                binding.tilPaymentStatus.getEditText().setFocusable(false);
                showPopupMenu(binding.tilPaymentStatus, paymentStatusList);
            }
        });
        binding.tilPaymentStatus.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(binding.tilPaymentStatus, paymentStatusList);
            }
        });
        binding.tilPaymentStatus.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dress.setPaymentStatus(s.toString());
                dressDetailListener.onUpdateDress(dress);
            }
        });

        binding.tilDiscountedAmount.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilDiscountedAmount.getEditText().setEnabled(true);
            }
        });
        binding.tilDiscountedAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!DtsUtils.isNullOrEmpty(s.toString())) {
                    dress.setDiscountedPrice(Double.parseDouble(s.toString()));
                }
                dressDetailListener.onUpdateDress(dress);
            }
        });
        binding.tilComment.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilComment.getEditText().setEnabled(true);
            }
        });
        binding.tilComment.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dress.setComment(s.toString());
                dressDetailListener.onUpdateDress(dress);
            }
        });
        binding.tvDressType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.grpDressDetail.getVisibility() == View.GONE) {
                    binding.grpDressDetail.setVisibility(View.VISIBLE);
                } else {
                    binding.grpDressDetail.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return DtsUtils.isNullOrEmpty(dressList) ? 0 : dressList.size();
    }

    private void addDatePicker(String title, EditText tietEditText) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(title).build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                String date = DtsUtils.getFormattedDate(datePicker.getHeaderText(), "dd MMM yyyy", "dd/MMM/yyyy");
                if (!DtsUtils.isNullOrEmpty(date)) {
                    tietEditText.setText(date);
                }
            }
        });

        datePicker.show(fragmentManager, title);

    }

    private void showPopupMenu(TextInputLayout textInputLayout, List<String> statusList) {
        PopupMenu popupMenu = new PopupMenu(context, textInputLayout);
        for (String deliveryStatus : statusList) {
            popupMenu.getMenu().add(deliveryStatus);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CharSequence status = item.getTitle();
                textInputLayout.getEditText().setText(status);
                return false;
            }
        });
        popupMenu.show();
    }

    public static class DressDetailViewHolder extends RecyclerView.ViewHolder {
        private final ItemDressDetailBinding binding;

        public DressDetailViewHolder(ItemDressDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemDressDetailBinding getBinding() {
            return binding;
        }
    }
}
