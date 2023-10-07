package com.example.demotailorshop.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.demotailorshop.R;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.api.UserDressTypesApi;
import com.example.demotailorshop.databinding.FragmentHomeBinding;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean isLoggedIn = false;
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        UserDressTypesApi userDressTypesApi = DtsApiFactory.getRetrofitInstance().create(UserDressTypesApi.class);
        final User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(requireActivity(), DtsSharedPreferenceUtil.KEY_USER);
        if (user != null) {
            isLoggedIn = true;
            List<DressType> dressTypeList = DtsSharedPreferenceUtil.getInstance().getDressTypesFromSharedPref(requireActivity(), DtsSharedPreferenceUtil.KEY_DRESS_TYPES);
            if (DtsUtils.isNullOrEmpty(dressTypeList)) {
                Map<String, String> headersMap = new HashMap<>();
                headersMap.put(DtsSharedPreferenceUtil.KEY_AUTHORIZATION, user.getAuthenticationToken());

                userDressTypesApi.getUserDressType(headersMap, user.getUserId()).enqueue(new Callback<ApiResponse<List<DressType>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<List<DressType>>> call, @NonNull Response<ApiResponse<List<DressType>>> response) {
                        List<DressType> dressTypeList = null;
                        ApiError apiError = null;
                        int httpStatus = response.code();
                        String statusMessage = null;
                        if (httpStatus == 200) {
                            ApiResponse<List<DressType>> apiResponse = response.body();
                            if (apiResponse != null) {
                                statusMessage = apiResponse.getMessage();
                                dressTypeList = apiResponse.getResult();
                                if (!DtsUtils.isNullOrEmpty(dressTypeList)) {
                                    DtsSharedPreferenceUtil.getInstance().saveUserToSharedPreference(requireActivity(), dressTypeList, DtsSharedPreferenceUtil.KEY_DRESS_TYPES);
                                }
                            }
                            Log.v(TAG, statusMessage);
                        } else if (httpStatus == 204) {
                            statusMessage = "No User Dress type Found";
                            Log.v(TAG, statusMessage);
                            Snackbar.make(binding.getRoot(), statusMessage, BaseTransientBottomBar.LENGTH_SHORT).show();
                        } else if (httpStatus == 401) {
                            apiError = ApiUtils.getApiErrorResponse(response);
                            statusMessage = apiError.getMessage();
                            Log.v(TAG, statusMessage);
                            Snackbar.make(binding.getRoot(), statusMessage, BaseTransientBottomBar.LENGTH_SHORT).show();
                        } else {
                            apiError = ApiUtils.getDefaultErrorResponse(response.code(), "Something went wrong! Please retry");
                            Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<List<DressType>>> call, @NonNull Throwable t) {
                        Log.e(TAG, "Api-Error " + t.fillInStackTrace());
                        Snackbar.make(binding.getRoot(), "Something went wrong", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
            }
        }
        setBottomNavigationItemSelectedOperation(isLoggedIn);
        setBottomNavigationItemReSelectedOperation();
        if (!isLoggedIn) {
            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fcvHomeContainer, UserHelpFragment.class, null, "UserHelpFragment")
                    .addToBackStack("UserHelpFragment")
                    .commit();
        } else {
//            addOptionMenu();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addOptionMenu() {
        binding.mtbToolBar.inflateMenu(R.menu.menu_top_app_bar);
        binding.mtbToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean isSelected = false;
                //            Show search box
                //                    case R.id.mSearch:
                //                        isSelected = true;
                //                        break;
                //                Shoe sort and filter fragment
                if (item.getItemId() == R.id.mSortAndFilter) {
                    isSelected = true;
                    getChildFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fcvFilterContainer, DressFilterFragment.class, null, "SigninFragment")
                            .commit();
                }
                return isSelected;
            }
        });
    }


    //Overriding the default behaviour of bottom item selected
    private void setBottomNavigationItemReSelectedOperation() {
        binding.bnvHome.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
//                setBottomNavigation(item);
            }
        });
    }

    private void setBottomNavigationItemSelectedOperation(boolean isLoggedIn) {
        binding.bnvHome.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return setBottomNavigation(item, isLoggedIn);
            }
        });
    }

    private void setDefaultSelectBottomNavigationItem(FragmentHomeBinding binding) {
        binding.bnvHome.setSelectedItemId(R.id.mDress);
    }

    private boolean setBottomNavigation(@NonNull MenuItem item, boolean isLoggedIn) {
        boolean isSelected = false;
        if (isLoggedIn) {
            if(item.getItemId() == R.id.mAccount) {
                isSelected = true;
                Log.i(TAG, "Filter menu Clicked");
                getChildFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvHomeContainer, UserAccountFragment.class, null, "UserAccountFragment")
                        .commit();
            }
            if(item.getItemId() == R.id.mDress) {
                isSelected = true;
                Log.i(TAG, "Dress menu clicked");
                getChildFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fcvHomeContainer, DressListFragment.class, null, "DressListFragment")
                        .commit();
            }
            if(item.getItemId() == R.id.mAddDress) {
                isSelected = true;
                AddDressFragment addDressFragment = new AddDressFragment();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fcvHomeContainer, addDressFragment, "AddDressFragment")
                        .addToBackStack("AddDressFragment")
                        .commit();
            }
        } else {
            Snackbar.make(binding.getRoot(), "Please login", BaseTransientBottomBar.LENGTH_SHORT).show();
        }

        return isSelected;
    }
}