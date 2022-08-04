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

import com.example.demotailorshop.databinding.ItemCustomerDetailBinding;
import com.example.demotailorshop.databinding.ItemCustomerDressDetailBinding;
import com.example.demotailorshop.databinding.ItemCustomerInvoiceDetailBinding;
import com.example.demotailorshop.databinding.ItemNoDressListBinding;
import com.example.demotailorshop.entity.Customer;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.DressDetail;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.entity.Invoice;
import com.example.demotailorshop.listener.DressDetailListener;
import com.example.demotailorshop.listener.MeasurementListener;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CustomerDressDetailAdapter extends RecyclerView.Adapter {
    private FragmentManager fragmentManager;
    private int displayWidth;
    private Context context;
    private DressDetail dressDetail;
    private MeasurementListener measurementListener;
    private DressDetailListener dressDetailListener;
    private boolean isNewCustomer;
    private List<String> deliveryStatusList;
    private List<String> paymentStatusList;
    private List<DressType> dressTypeList;
    private static final int DRESS_DETAIL_LOADING = -1;
    private static final int CUSTOMER_DETAIL = 0;
    private static final int INVOICE_DETAIL = 1;
    private static final int CUSTOMER_DRESS_DETAIL = 2;


    public CustomerDressDetailAdapter() {
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setNewCustomer(boolean newCustomer) {
        isNewCustomer = newCustomer;
    }

    public void setDeliveryStatusList(List<String> deliveryStatusList) {
        this.deliveryStatusList = deliveryStatusList;
    }

    public void setPaymentStatusList(List<String> paymentStatusList) {
        this.paymentStatusList = paymentStatusList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public void setDressDetail(DressDetail dressDetail) {
        this.dressDetail = dressDetail;
    }

    public void setMeasurementListener(MeasurementListener measurementListener) {
        this.measurementListener = measurementListener;
    }

    public void setDressDetailListener(DressDetailListener dressDetailListener) {
        this.dressDetailListener = dressDetailListener;
    }

    public void setDressTypeList(List<DressType> dressTypeList) {
        this.dressTypeList = dressTypeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DRESS_DETAIL_LOADING:
                ItemNoDressListBinding noDressListBinding = ItemNoDressListBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new NoDressViewHolder(noDressListBinding);
            case CUSTOMER_DETAIL:
                ItemCustomerDetailBinding customerDetailBinding = ItemCustomerDetailBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new CustomerDetailViewHolder(customerDetailBinding);
            case INVOICE_DETAIL:
                ItemCustomerInvoiceDetailBinding invoiceDetailBinding = ItemCustomerInvoiceDetailBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new InvoiceDetailViewHolder(invoiceDetailBinding);
            case CUSTOMER_DRESS_DETAIL:
                ItemCustomerDressDetailBinding dressDetailBinding = ItemCustomerDressDetailBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new CustomerDressDetailViewHolder(dressDetailBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (viewType == DRESS_DETAIL_LOADING && holder instanceof NoDressViewHolder && !isNewCustomer) {
            NoDressViewHolder noDressViewHolder = (NoDressViewHolder) holder;
            ItemNoDressListBinding noDressListBinding = noDressViewHolder.getBinding();
            noDressListBinding.btnRetry.setVisibility(View.GONE);
            noDressListBinding.tvNoDressList.setText("Dress List Loading...");
        }
        if (viewType == CUSTOMER_DETAIL && holder instanceof CustomerDetailViewHolder) {
            CustomerDetailViewHolder customerDetailViewHolder = (CustomerDetailViewHolder) holder;
            setCustomerDetail(customerDetailViewHolder);
        }
        if (viewType == INVOICE_DETAIL && holder instanceof InvoiceDetailViewHolder) {
            InvoiceDetailViewHolder invoiceDetailViewHolder = (InvoiceDetailViewHolder) holder;
            setInvoiceDetail(invoiceDetailViewHolder);
        }
        if (viewType == CUSTOMER_DRESS_DETAIL && holder instanceof CustomerDressDetailViewHolder) {
            CustomerDressDetailViewHolder dressDetailViewHolder = (CustomerDressDetailViewHolder) holder;
            setDressDetail(dressDetailViewHolder);
        }
    }

    private void setDressDetail(CustomerDressDetailViewHolder dressDetailViewHolder) {
        ItemCustomerDressDetailBinding binding = dressDetailViewHolder.getBinding();
        DressDetailAdapter adapter;
        adapter = new DressDetailAdapter();
        adapter.setDisplayWidth(displayWidth);
        adapter.setContext(context);
        adapter.setMeasurementListener(measurementListener);
        adapter.setDressDetailListener(dressDetailListener);
        adapter.setFragmentManager(fragmentManager);
        adapter.setNewCustomer(isNewCustomer);
        adapter.setDeliveryStatusList(deliveryStatusList);
        adapter.setPaymentStatusList(paymentStatusList);
        adapter.setDressTypeList(dressTypeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvDressDetail.setLayoutManager(layoutManager);
        binding.rvDressDetail.setAdapter(adapter);
        List<Dress> dressList = dressDetail.getDressList();
        adapter.setDressList(dressList);
        adapter.notifyDataSetChanged();
        binding.tvLabelDressDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.grpDress.getVisibility() == View.GONE) {
                    binding.grpDress.setVisibility(View.VISIBLE);
                } else {
                    binding.grpDress.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setInvoiceDetail(InvoiceDetailViewHolder invoiceDetailViewHolder) {
        ItemCustomerInvoiceDetailBinding binding = invoiceDetailViewHolder.getBinding();
        binding.tilInvoiceNo.getEditText().setEnabled(false);
        binding.tilBillAmount.getEditText().setEnabled(false);
        binding.tilTotalAmount.getEditText().setEnabled(false);
        if (!isNewCustomer) {
            binding.tilAdvancedAmount.getEditText().setEnabled(false);
            binding.tilDiscountedAmount.getEditText().setEnabled(false);
            binding.tilPaidAmount.getEditText().setEnabled(false);
            binding.tilBalanceAmount.getEditText().setEnabled(false);

            binding.tilAdvancedAmount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilBillAmount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilDiscountedAmount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilTotalAmount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilPaidAmount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilBalanceAmount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            //            Add focus
            binding.tilAdvancedAmount.getEditText().setEnabled(true);
            binding.tilDiscountedAmount.getEditText().setEnabled(true);
            binding.tilPaidAmount.getEditText().setEnabled(true);
        }
        binding.tilAdvancedAmount.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilAdvancedAmount.getEditText().setEnabled(true);
            }
        });
        binding.tilAdvancedAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Invoice invoice = getInvoice();
                if (!DtsUtils.isNullOrEmpty(s.toString())) {
                    double advance = Double.parseDouble(s.toString().trim());
                    invoice.setAdvance(advance);
                }
                dressDetail.setCustomerInvoice(invoice);
                dressDetailListener.onUpdateDressDetail(dressDetail);

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
                Invoice invoice = getInvoice();
                if (!DtsUtils.isNullOrEmpty(s.toString())) {
                    double discountedAmount = Double.parseDouble(s.toString());
                    invoice.setDiscountedAmount(discountedAmount);
                }
                dressDetail.setCustomerInvoice(invoice);
                dressDetailListener.onUpdateDressDetail(dressDetail);

            }
        });
        binding.tilPaidAmount.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilPaidAmount.getEditText().setEnabled(true);
            }
        });
        binding.tilPaidAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Invoice invoice = getInvoice();
                if (!DtsUtils.isNullOrEmpty(s.toString())) {
                    double paidAmount = Double.parseDouble(s.toString());
                    invoice.setPaidAmount(paidAmount);
                }
                dressDetail.setCustomerInvoice(invoice);
                dressDetailListener.onUpdateDressDetail(dressDetail);

            }
        });

        Invoice invoice = dressDetail.getCustomerInvoice();
        if (invoice != null) {
            binding.tilInvoiceNo.getEditText().setText(String.valueOf(invoice.getInvoiceId()));
            binding.tilAdvancedAmount.getEditText().setText(String.valueOf(invoice.getAdvance()));
            binding.tilBillAmount.getEditText().setText(String.valueOf(invoice.getBillAmount()));
            binding.tilDiscountedAmount.getEditText().setText(String.valueOf(invoice.getDiscountedAmount()));
            binding.tilTotalAmount.getEditText().setText(String.valueOf(invoice.getTotalAmount()));
            binding.tilPaidAmount.getEditText().setText(String.valueOf(invoice.getPaidAmount()));
            double balanceAmount = invoice.getDiscountedAmount() - invoice.getPaidAmount();
            binding.tilBalanceAmount.getEditText().setText(String.valueOf(balanceAmount));
        }
        binding.tvLabelInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.grpInvoice.getVisibility() == View.GONE) {
                    binding.grpInvoice.setVisibility(View.VISIBLE);
                } else {
                    binding.grpInvoice.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setCustomerDetail(CustomerDetailViewHolder holder) {
        String customerName = null;
        String mobileNo = null;
        String email = null;
        String orderDate = null;
        String deliveryDate = null;
        String deliveryStatus = null;
        ItemCustomerDetailBinding binding = holder.getBinding();
        binding.tilInvoiceNo.getEditText().setEnabled(false);
        binding.tilOrderDate.getEditText().setEnabled(false);
        binding.tilDeliveryDate.getEditText().setEnabled(false);
        binding.tilDeliveryStatus.getEditText().setEnabled(false);
        if (!isNewCustomer) {
            binding.tilCustomerName.getEditText().setEnabled(false);
            binding.tilCustomerMobileNo.getEditText().setEnabled(false);
            binding.tilCustomerEmail.getEditText().setEnabled(false);
            binding.tilDressCount.getEditText().setEnabled(false);

            binding.tilCustomerName.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilCustomerMobileNo.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilCustomerEmail.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilOrderDate.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilDeliveryDate.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilDeliveryStatus.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            binding.tilDressCount.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            //            Add focus
            binding.tilCustomerName.getEditText().setEnabled(true);
            binding.tilCustomerMobileNo.getEditText().setEnabled(true);
            binding.tilCustomerEmail.getEditText().setEnabled(true);
            binding.tilDressCount.getEditText().setEnabled(true);

        }

        binding.tvCustomerMobileCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measurementListener.customerCall(dressDetail.getCustomer());
            }
        });
        binding.tilCustomerName.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilCustomerName.getEditText().setEnabled(true);
            }
        });
        binding.tilCustomerName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
                if (!DtsUtils.isNullOrEmpty(name)) {
                    Customer customer = getCustomer();
                    if (!name.equalsIgnoreCase(customer.getFirstName())) {
                        customer.setFirstName(name);
                        dressDetail.setCustomer(customer);
                        dressDetailListener.onUpdateDressDetail(dressDetail);
                    }
                }
            }
        });
        binding.tilCustomerEmail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilCustomerEmail.getEditText().setEnabled(true);
            }
        });
        binding.tilCustomerEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString();
                if (!DtsUtils.isNullOrEmpty(email)) {
                    Customer customer = getCustomer();
                    if (!email.equalsIgnoreCase(customer.getEmail())) {
                        customer.setEmail(email);
                        dressDetail.setCustomer(customer);
                        dressDetailListener.onUpdateDressDetail(dressDetail);
                    }
                }
            }
        });
        binding.tilCustomerMobileNo.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilCustomerMobileNo.getEditText().setEnabled(true);
            }
        });
        binding.tilCustomerMobileNo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mobileNo = s.toString();
                if (!DtsUtils.isNullOrEmpty(mobileNo)) {
                    Customer customer = getCustomer();
                    if (!mobileNo.equalsIgnoreCase(customer.getMobileNo())) {
                        customer.setMobileNo(mobileNo);
                        dressDetail.setCustomer(customer);
                        dressDetailListener.onUpdateDressDetail(dressDetail);
                    }
                }
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
        binding.tilOrderDate.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString();
                if (!DtsUtils.isNullOrEmpty(result)) {
                    Customer customer = getCustomer();
                    if (!result.equalsIgnoreCase(customer.getOrderDate())) {
                        customer.setOrderDate(result);
                        dressDetail.setCustomer(customer);
                        dressDetailListener.onUpdateDressDetail(dressDetail);
                    }
                }
            }
        });
        binding.tilDeliveryDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tilDeliveryDate.getEditText().setEnabled(true);
                binding.tilDeliveryDate.getEditText().setFocusable(false);
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
                String result = s.toString();
                if (!DtsUtils.isNullOrEmpty(result)) {
                    Customer customer = getCustomer();
                    if (!result.equalsIgnoreCase(customer.getDeliveryDate())) {
                        customer.setDeliveryDate(result);
                        dressDetail.setCustomer(customer);
                        dressDetailListener.onUpdateDressDetail(dressDetail);
                    }
                }
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
        binding.tilDeliveryStatus.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString();
                if (!DtsUtils.isNullOrEmpty(result)) {
                    List<Dress> dressList = dressDetail.getDressList();
                    if (!DtsUtils.isNullOrEmpty(dressList)) {
                        List<Dress> updatedDressList = new ArrayList<>();
                        for (Dress dress : dressList) {
                            dress.setDeliveryStatus(result);
                            updatedDressList.add(dress);
                        }
                        dressDetail.setDressList(updatedDressList);
                        dressDetailListener.onUpdateDressDetail(dressDetail);
                    }
                }
            }
        });
        Customer customer = dressDetail.getCustomer();
        if (customer != null) {
            customerName = customer.getFirstName();
            mobileNo = customer.getMobileNo();
            email = customer.getEmail();
            orderDate = customer.getOrderDate();
            deliveryDate = customer.getDeliveryDate();
            if (!DtsUtils.isNullOrEmpty(customerName)) {
                binding.tilCustomerName.getEditText().setText(customerName);
            }
            if (!DtsUtils.isNullOrEmpty(mobileNo)) {
                binding.tilCustomerMobileNo.getEditText().setText(mobileNo);

            }
            if (!DtsUtils.isNullOrEmpty(email)) {
                binding.tilCustomerEmail.getEditText().setText(email);
            }
            if (!DtsUtils.isNullOrEmpty(orderDate)) {
                binding.tilOrderDate.getEditText().setText(orderDate);
            }
            if (!DtsUtils.isNullOrEmpty(deliveryDate)) {
                binding.tilDeliveryDate.getEditText().setText(deliveryDate);
            }
            List<Dress> dressList = dressDetail.getDressList();
            if (!DtsUtils.isNullOrEmpty(dressList)) {
                Dress dress = dressList.get(0);
                if (dress != null) {
                    deliveryStatus = dress.getDeliveryStatus();
                }
            }
            if (!DtsUtils.isNullOrEmpty(deliveryStatus)) {
                binding.tilDeliveryStatus.getEditText().setText(deliveryStatus);
            }
        }
        Invoice invoice = dressDetail.getCustomerInvoice();
        if (invoice != null) {
            binding.tilInvoiceNo.getEditText().setText(String.valueOf(invoice.getInvoiceId()));
        }
        List<Dress> dressList = dressDetail.getDressList();
        if (!DtsUtils.isNullOrEmpty(dressList)) {
            int dressCount = getDressCount(dressList);
            binding.tilDressCount.getEditText().setText(String.valueOf(dressCount));
        }
        binding.tvLabelCustomerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.grpCustomer.getVisibility() == View.GONE) {
                    binding.grpCustomer.setVisibility(View.VISIBLE);
                } else {
                    binding.grpCustomer.setVisibility(View.GONE);
                }
            }
        });
    }

    private int getDressCount(List<Dress> dressList) {
        int dressCount = 0;
        if (!DtsUtils.isNullOrEmpty(dressList)) {
            for (Dress dress : dressList) {
                dressCount += dress.getNumberOfDress();
            }
        }
        return dressCount;
    }

    @Override
    public int getItemCount() {
        int itemCount = 1;
        if (dressDetail != null) {
            itemCount = 3;
        }
        return itemCount;

    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (dressDetail == null && !isNewCustomer) {
            viewType = DRESS_DETAIL_LOADING;
        } else {
            switch (position) {
                case 0:
                    viewType = CUSTOMER_DETAIL;
                    break;
                case 1:
                    viewType = INVOICE_DETAIL;
                    break;
                case 2:
                    viewType = CUSTOMER_DRESS_DETAIL;
                    break;
            }
        }
        return viewType;
    }

    private void addDatePicker(String title, EditText tietEditText) {
//        String title = "Update order date";
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

    private Customer getCustomer() {
        Customer customer = null;
        if (dressDetail != null) {
            customer = dressDetail.getCustomer();
        }
        if (customer == null) {
            customer = new Customer();
        }
        return customer;
    }

    private Invoice getInvoice() {
        Invoice invoice = null;
        if (dressDetail != null) {
            invoice = dressDetail.getCustomerInvoice();
        }
        if (invoice == null) {
            invoice = new Invoice();
        }
        return invoice;
    }

    private static class CustomerDetailViewHolder extends RecyclerView.ViewHolder {
        private final ItemCustomerDetailBinding binding;

        public CustomerDetailViewHolder(ItemCustomerDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemCustomerDetailBinding getBinding() {
            return binding;
        }
    }

    private static class InvoiceDetailViewHolder extends RecyclerView.ViewHolder {
        private final ItemCustomerInvoiceDetailBinding binding;

        public InvoiceDetailViewHolder(ItemCustomerInvoiceDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemCustomerInvoiceDetailBinding getBinding() {
            return binding;
        }
    }

    private static class CustomerDressDetailViewHolder extends RecyclerView.ViewHolder {
        private final ItemCustomerDressDetailBinding binding;

        public CustomerDressDetailViewHolder(ItemCustomerDressDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemCustomerDressDetailBinding getBinding() {
            return binding;
        }
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
}
