package com.example.demotailorshop.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.demotailorshop.R;
import com.example.demotailorshop.adapter.CustomerDressDetailAdapter;
import com.example.demotailorshop.api.DressListApi;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.databinding.FragmentDressDetailBinding;
import com.example.demotailorshop.databinding.LayoutSelectMeasurementBinding;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.Customer;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.DressDetail;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.entity.Invoice;
import com.example.demotailorshop.entity.Measurement;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.listener.CustomDialogListener;
import com.example.demotailorshop.listener.DressDetailListener;
import com.example.demotailorshop.listener.MeasurementListener;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.example.demotailorshop.utils.UriUtils;
import com.example.demotailorshop.view.custom.CustomDialog;
import com.example.demotailorshop.viewmodel.DressDetailViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DressDetailFragment#} factory method to
 * create an instance of this fragment.
 */
public class DressDetailFragment extends Fragment {
    private static final String TAG = "DressDetailFragment";
    private ActivityResultLauncher<Intent> measurementPickerLauncher;
    private ActivityResultLauncher<String[]> storagePermissionLauncher;
    private DressDetailViewModel dressDetailViewModel = null;
    private CustomerDressDetailAdapter customerDressDetailAdapter;
    private DressDetail dressDetail;
    private Dress dress;
    private Map<String, File> fileMap;
    private boolean isNewCustomer;


    public DressDetailFragment() {
        // Required empty public constructor
    }

    public void setNewCustomer(boolean newCustomer) {
        isNewCustomer = newCustomer;
    }

    public boolean isNewCustomer() {
        return isNewCustomer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setDress(Dress dress) {
        this.dress = dress;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fileMap = new HashMap<>();
        FragmentDressDetailBinding binding = FragmentDressDetailBinding.inflate(getLayoutInflater());
        List<DressType> dressTypeList = DtsSharedPreferenceUtil.getInstance().getDressTypesFromSharedPref(requireActivity(), DtsSharedPreferenceUtil.KEY_DRESS_TYPES);
        storagePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                openGallery();
            }
        });
        final int displayWidth = DtsUtils.getScreenWidth(requireActivity());
        int customerId = 0;
        final DressListApi dressListApi = DtsApiFactory.getRetrofitInstance().create(DressListApi.class);
        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (dress != null) {
            if (dress.getCustomer() != null) {
                customerId = dress.getCustomer().getCustomerId();
            }
        }
        List<String> deliveryStatusList = Arrays.asList("DELIVERED", "UNDELIVERED", "CANCELLED");
        List<String> paymentStatusList = Arrays.asList("PAID", "UNPAID");

        // Inflate the layout for this fragment
        if (this.isNewCustomer) {
            binding.btnUpdate.setVisibility(View.VISIBLE);
        }

        View view = binding.getRoot();
//        Move to home page when click on back or cancel (cross) icon
        addListenerForNavigation(binding);
        LinearLayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        customerDressDetailAdapter = new CustomerDressDetailAdapter();
        customerDressDetailAdapter.setContext(requireActivity());
        customerDressDetailAdapter.setDisplayWidth(displayWidth);
        customerDressDetailAdapter.setFragmentManager(requireActivity().getSupportFragmentManager());
        customerDressDetailAdapter.setNewCustomer(isNewCustomer);
        customerDressDetailAdapter.setDeliveryStatusList(deliveryStatusList);
        customerDressDetailAdapter.setPaymentStatusList(paymentStatusList);
        customerDressDetailAdapter.setDressTypeList(dressTypeList);
        binding.rvDressDetail.setLayoutManager(layoutManager);
        binding.rvDressDetail.setAdapter(customerDressDetailAdapter);
        dressDetailViewModel = new ViewModelProvider(requireActivity()).get(DressDetailViewModel.class);
        if (isNewCustomer && dressDetail == null) {
            dressDetail = new DressDetail();
            Customer customer = new Customer();
            customer.setCustomerId(0);
            dressDetail.setCustomer(customer);
            Invoice invoice = new Invoice();
            invoice.setInvoiceId(0);
            dressDetail.setCustomerInvoice(invoice);
            Dress emptyDress = new Dress();
            emptyDress.setDressId(0);
            Measurement measurement = new Measurement();
            measurement.setMeasurementId(0);
            emptyDress.setMeasurement(measurement);
            List<Dress> emptyDressList = new ArrayList<>();
            emptyDressList.add(emptyDress);
            dressDetail.setDressList(emptyDressList);
            dressDetailViewModel.setDressDetail(dressDetail);
        }

        dressDetailViewModel.getDressDetailMutableLiveData(user, customerId, isNewCustomer).observe(requireActivity(), new Observer<DressDetail>() {
            @Override
            public void onChanged(DressDetail dressDetail1) {
                if (dressDetail1 != null) {
                    if (dressDetail1.getCustomer() != null) {
                        updateCustomerTitle(binding, dressDetail1.getCustomer().getFirstName());
                    }
                    customerDressDetailAdapter.setDressDetail(dressDetail1);
                    customerDressDetailAdapter.notifyDataSetChanged();
                    binding.btnUpdate.setVisibility(View.VISIBLE);
                }
            }
        });

        customerDressDetailAdapter.setMeasurementListener(new MeasurementListener() {
            @Override
            public void onAddMeasurement(String type, int dressId) {
                showDialog(type, dressId);
            }


            @Override
            public void showMeasurement(List<String> imageUrlList, List<Uri> imageUriList, int itemPosition) {

                MeasurementViewerFragment measurementViewerFragment = new MeasurementViewerFragment(imageUrlList, imageUriList, itemPosition);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fcvContainer, measurementViewerFragment, "MeasurementViewerFragment")
                        .addToBackStack("MeasurementViewerFragment")
                        .commit();
            }

            @Override
            public void customerCall(Customer customer) {
                String mobileNo = null;
                if (customer != null) {
                    mobileNo = customer.getMobileNo();
                }
                if (!DtsUtils.isNullOrEmpty(mobileNo)) {
                    callCustomer(mobileNo);

                } else {
                    Snackbar.make(binding.getRoot(), "Please enter valid mobile number", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                Log.v(TAG, "Calling toi customer");
            }
        });
        customerDressDetailAdapter.setDressDetailListener(new DressDetailListener() {
            @Override
            public void onUpdateDressDetail(final DressDetail dressDetail1) {
                Log.v(TAG, "Update dress detail");
                setDressDetail(dressDetail1);
            }

            @Override
            public void onUpdateDress(Dress dress) {
                Log.v(TAG, "Update dress");
                List<Dress> updatedDressList = null;
                List<Dress> dressList = dressDetail.getDressList();
                if (!DtsUtils.isNullOrEmpty(dressList)) {
                    updatedDressList = new ArrayList<>();
                    for (Dress d : dressList) {
                        if (d.getDressId() == dress.getDressId()) {
                            updatedDressList.add(dress);
                        } else {
                            updatedDressList.add(d);
                        }

                    }
                } else {
                    updatedDressList = new ArrayList<>();
                    updatedDressList.add(dress);
                }
                if (!DtsUtils.isNullOrEmpty(updatedDressList)) {
                    double invoiceAmount = 0;
                    Invoice invoice = dressDetail.getCustomerInvoice();
                    for (Dress updatedDress : updatedDressList) {
                        double price = updatedDress.getPrice();
                        invoiceAmount += price;
                    }
                    invoice.setBillAmount(invoiceAmount);
                    invoice.setTotalAmount(invoiceAmount);
                    dressDetail.setCustomerInvoice(invoice);
                }
                dressDetail.setDressList(updatedDressList);
                setDressDetail(dressDetail);
            }


        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Dress> dressList = dressDetail.getDressList();
                boolean isDressTypeAvailable = isDressTypeAvailable(dressList);
                if (!isDressTypeAvailable) {
                    showSnackMessage(binding, "Please select valid dress type");
                }
                if (DtsUtils.isNullOrEmpty(dressDetail.getCustomer().getFirstName())) {
                    showSnackMessage(binding, "Please enter valid customer name");
                }

                if (!DtsUtils.isNullOrEmpty(dressDetail.getCustomer().getFirstName()) && isDressTypeAvailable) {
                    showUpdateDressDetailDialog(binding, user, dressListApi);
                }
            }
        });

        return view;
    }

    private void showUpdateDressDetailDialog(FragmentDressDetailBinding binding, User user, DressListApi dressListApi) {
        CustomDialogListener listener = new CustomDialogListener() {
            @Override
            public void onPositiveClick() {
                Log.v(TAG, "Updating dress details");
                binding.btnUpdate.setEnabled(false);
                String authorizationHeader = user.getAuthenticationToken();
                Map<String, String> headersMap = new HashMap<>();
                headersMap.put(DtsSharedPreferenceUtil.KEY_AUTHORIZATION, authorizationHeader);
                dressListApi.updateDressDetail(headersMap, user.getUserId(), dressDetail).enqueue(new Callback<ApiResponse<DressDetail>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<DressDetail>> call, @NonNull Response<ApiResponse<DressDetail>> response) {
                        Log.v(TAG, "Dress detail updated successfully");
                        int customerId = 0;
                        List<MultipartBody.Part> parts = null;
                        Map<String, File> updatedHashMap = null;
                        if (!DtsUtils.isNullOrEmpty(fileMap)) {
                            updatedHashMap = getUpdatedHashMap(response);
                            if (!DtsUtils.isNullOrEmpty(updatedHashMap)) {
                                parts = getFilePart(updatedHashMap);
                            }
                        }
                        if (!DtsUtils.isNullOrEmpty(parts)) {
                            if (response.body() != null && response.body().getResult() != null) {
                                customerId = response.body().getResult().getCustomer().getCustomerId();
                            }
                            updateDressImage(customerId, parts, user, binding);
                        } else {
                            updatedResponse(response, binding);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<DressDetail>> call, @NonNull Throwable t) {
                        binding.btnUpdate.setEnabled(true);
                        Log.v(TAG, "Something went wrong while updating dress details");
                        Log.v(TAG, "Failed update " + t.fillInStackTrace());
                        showSnackMessage(binding, "Something went wrong");
                    }
                });
            }

            @Override
            public void onNegativeClick() {
                binding.btnUpdate.setEnabled(false);
            }
        };
        AlertDialog dialog = CustomDialog.getUpdateDialog(requireActivity(), "Add/Update Dress", "Are you sure want to add / update dress?", "Yes", "Cancel", listener);
        dialog.show();
    }

    private void updateDressImage(int customerId, List<MultipartBody.Part> parts, User user, FragmentDressDetailBinding binding) {
        Log.v(TAG, "Updating dress images");
        String authorizationHeader = user.getAuthenticationToken();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(DtsSharedPreferenceUtil.KEY_AUTHORIZATION, authorizationHeader);
        DressListApi dressListApi = DtsApiFactory.getRetrofitInstance().create(DressListApi.class);
        dressListApi.updateDressImagesNew(headersMap, user.getUserId(), customerId, parts).enqueue(new Callback<ApiResponse<DressDetail>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<DressDetail>> call, @NonNull Response<ApiResponse<DressDetail>> response) {
                Log.v(TAG, "Dress images updated successfully");
                fileMap.clear();
                updatedResponse(response, binding);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<DressDetail>> call, @NonNull Throwable t) {
                binding.btnUpdate.setEnabled(true);
                Log.v(TAG, "Something went wrong while updating dress images");
                Log.v(TAG, "Failed update " + t.fillInStackTrace());
                showSnackMessage(binding, "Something went wrong");
            }
        });
    }

    private void updatedResponse(@NonNull Response<ApiResponse<DressDetail>> response, FragmentDressDetailBinding binding) {
        binding.btnUpdate.setEnabled(true);
        DressDetail updatedDressDetail = getUpdatedDressDetail(response);
        dressDetailViewModel.setDressDetail(updatedDressDetail);
        if (updatedDressDetail.getCustomer() != null) {

            updateCustomerTitle(binding, updatedDressDetail.getCustomer().getFirstName());
        }
        showSnackMessage(binding, "Dress details updated successfully");
    }

    private DressDetail getUpdatedDressDetail(@NonNull Response<ApiResponse<DressDetail>> response) {
        DressDetail updatedDressDetail = null;
        ApiError apiError = null;
        int httpStatus = response.code();
        String statusMessage = null;
        if (httpStatus == 200) {
            ApiResponse<DressDetail> apiResponse = response.body();
            if (apiResponse != null) {
                statusMessage = apiResponse.getMessage();
                updatedDressDetail = apiResponse.getResult();
            }
            Log.v(TAG, statusMessage);
        } else if (httpStatus == 204) {
            statusMessage = "No Dress details Found";
            Log.v(TAG, statusMessage);
        } else if (httpStatus == 401) {
            apiError = ApiUtils.getDefaultErrorResponse(httpStatus, statusMessage);
            statusMessage = apiError.getMessage();
            Log.v(TAG, statusMessage);
        } else {
            String responseString = null;
            try {
                responseString = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            apiError = ApiUtils.getDefaultErrorResponse(responseString);
            statusMessage = apiError.getMessage();
            Log.v(TAG, statusMessage);
        }
        return updatedDressDetail;
    }

    private Map<String, File> getUpdatedHashMap(@NonNull Response<ApiResponse<DressDetail>> response) {
        Map<String, File> updatedHashMap = null;
        if (isNewCustomer) {
//                                    Set<String> keySet = fileMap.keySet();
            if (response.body() != null && response.body().getResult() != null) {

                List<Dress> updatedDressList = response.body().getResult().getDressList();
                if (!DtsUtils.isNullOrEmpty(updatedDressList)) {
                    updatedHashMap = getFileMap(updatedDressList);
                }
            }
        } else {
            updatedHashMap = fileMap;
        }
        return updatedHashMap;
    }

    private boolean isDressTypeAvailable(List<Dress> dressList) {
        boolean isDressTypeAvailable = true;
        for (Dress dress1 : dressList) {
            if (dress1.getDressType() == null) {
                isDressTypeAvailable = false;
                break;
            }
        }
        return isDressTypeAvailable;
    }

    private void showSnackMessage(FragmentDressDetailBinding binding, String message) {
        Snackbar.make(binding.getRoot(), message, BaseTransientBottomBar.LENGTH_SHORT).show();

    }

    private void updateCustomerTitle(FragmentDressDetailBinding binding, String title) {
        binding.ctlCustomerName.setTitle(title);
    }

    private List<MultipartBody.Part> getFilePart(Map<String, File> updatedHashMap) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String key : updatedHashMap.keySet()) {
            File file = updatedHashMap.get(key);
            if (file != null) {
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                file
                        );
                parts.add(MultipartBody.Part.createFormData("files", key, requestFile));
            }
        }
        return parts;
    }

    private Map<String, File> getFileMap(List<Dress> updatedDressList) {
        Set<String> keySet = fileMap.keySet();
        Map<String, File> fileHashMap = new HashMap<>();
        for (Dress d : updatedDressList) {
            for (String key : keySet) {
                String[] splitNames = key.split("_");
                int id = Integer.parseInt(splitNames[1].substring(0));
                String newKey = key.replace("_" + id + "_", "_" + d.getDressId() + "_");
                fileHashMap.put(newKey, fileMap.get(key));
            }
        }
        return fileHashMap;
    }

    private void callCustomer(String mobileNo) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + mobileNo));
        requireActivity().startActivity(callIntent);
    }

    public void setDressDetail(DressDetail dressDetail) {
        this.dressDetail = dressDetail;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dressDetailViewModel = new ViewModelProvider(requireActivity()).get(DressDetailViewModel.class);
        Log.v(TAG, "On attach ");
        measurementPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.v(TAG, "On activity result ");

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    Uri uri = intent.getData();
                    List<Uri> uriList = new ArrayList<>();
                    uriList.add(uri);
                    dressDetailViewModel.updateUriMap(uriList);
                    File uploadFile = new File(UriUtils.getPathFromUri(requireActivity(), uri));
                    int i = 1;
                    String fileName = "D_" + dressDetailViewModel.getDressId() + "_" + dressDetailViewModel.getType() + "_" + i + "." + uploadFile.getName().substring(uploadFile.getName().lastIndexOf(".") + 1);
                    boolean isPresent = false;
                    do {
                        File tempFile = fileMap.get(fileName);
                        if (tempFile != null) {
                            isPresent = true;
                            i++;
                            fileName = "D_" + dressDetailViewModel.getDressId() + "_" + dressDetailViewModel.getType() + "_" + i + "." + uploadFile.getName().substring(uploadFile.getName().lastIndexOf(".") + 1);
                        } else {
                            isPresent = false;
                            fileMap.put(fileName, uploadFile);
                        }

                    } while (isPresent);
                    Log.v(TAG, "Uri data authority-" + uri.getAuthority());
                    Log.v(TAG, "Uri data encoded path-" + uri.getEncodedPath());
                    Log.v(TAG, "Uri data encoded authority-" + uri.getEncodedAuthority());
                    Log.v(TAG, "Uri data host-" + uri.getHost());
                    Log.v(TAG, "Uri data path-" + uri.getPath());
                    Log.v(TAG, "Uri data relative path-" + uri.isRelative());
                    Log.v(TAG, "Image selected uri" + uri);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }


    private void showDialog(String type, int dressId) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(requireActivity());
        LayoutSelectMeasurementBinding dialogBinding = LayoutSelectMeasurementBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());
        Glide.with(requireActivity()).load(R.drawable.ic_camera_24)
                .fallback(R.drawable.ic_no_photography_24)
                .into(dialogBinding.cdliGallery);
        AlertDialog selectMeasurementDialog = builder.create();
        dialogBinding.cdliGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMeasurementDialog.dismiss();
                dressDetailViewModel.addMeasurement(dressId, type);
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else if (shouldShowRequestPermissionRationale("")) {

                } else {
//                    ActivityCompat.requestPermissions(
//                            requireActivity(),
//                            new String[]{
//                                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                            },
//                            1
//                    );
                    storagePermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                }
            }
        });


        selectMeasurementDialog.show();

    }


    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (measurementPickerLauncher != null) {
            measurementPickerLauncher.launch(galleryIntent);
        }
    }


    private void addListenerForNavigation(FragmentDressDetailBinding binding) {
        binding.mtbCustomerName.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}