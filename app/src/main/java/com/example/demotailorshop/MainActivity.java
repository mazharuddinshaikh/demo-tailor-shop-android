package com.example.demotailorshop;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.demotailorshop.adapter.AppUpdateAdapter;
import com.example.demotailorshop.api.AppUpdateApi;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.databinding.ActivityMainBinding;
import com.example.demotailorshop.databinding.LayoutAppUpdateBinding;
import com.example.demotailorshop.entity.AndroidAppUpdate;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.fragment.HomeFragment;
import com.example.demotailorshop.fragment.UserHelpFragment;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AndroidAppUpdate androidAppUpdate = null;
        final AppUpdateApi appUpdateApi = DtsApiFactory.getRetrofitInstance().create(AppUpdateApi.class);
        final String currentFormattedDate = getCurrentFormattedDate();
        final String updateCheckDate = DtsSharedPreferenceUtil.getInstance().getStringFromPref(this, DtsSharedPreferenceUtil.KEY_UPDATE_CHECKED_DATE);
        if (!DtsUtils.isNullOrEmpty(updateCheckDate) && currentFormattedDate.equals(updateCheckDate)) {
            androidAppUpdate = DtsSharedPreferenceUtil.getInstance().getObjectFromPref(this, DtsSharedPreferenceUtil.KEY_ANDROID_APP_UPDATE, AndroidAppUpdate.class);
        } else {
            removeAppUpdatePref();
        }
        if (androidAppUpdate == null) {
            appUpdateApi.getAppUpdate().enqueue(new Callback<ApiResponse<AndroidAppUpdate>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<AndroidAppUpdate>> call, @NonNull Response<ApiResponse<AndroidAppUpdate>> response) {
                    ApiResponse<AndroidAppUpdate> androidAppUpdateApiResponse = null;
                    AndroidAppUpdate androidAppUpdate = null;
                    PackageInfo packageInfo = null;
                    int httpStatus = response.code();
                    if (httpStatus == ApiUtils.SUCCESS) {
                        removeAppUpdatePref();
                        packageInfo = getPackageInfo();
                        String versionName = packageInfo.versionName;
                        androidAppUpdateApiResponse = response.body();
                        if (androidAppUpdateApiResponse != null) {
                            androidAppUpdate = androidAppUpdateApiResponse.getResult();
                        }
                        if (androidAppUpdate != null) {
                            showAppUpdateDialog(androidAppUpdate);
                            if (!versionName.equals(androidAppUpdate.getUpdateVersion())) {
                                Log.v(TAG, "Update required");
                            } else {
                                Log.v(TAG, "App is up to date");
                            }
                        }
                    } else {
                        ApiError apiError = ApiUtils.getDefaultErrorResponse(response.code(), "Something went wrong! Please retry");
                        Snackbar.make(binding.getRoot(), apiError.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<AndroidAppUpdate>> call, @NonNull Throwable t) {
//                    Snackbar.make(binding.getRoot(), "Something went wrong! Please retry", BaseTransientBottomBar.LENGTH_SHORT).show();
                    Log.e(TAG, "Something went wrong! " + t);
                }
            });
        }
        Intent intent = getIntent();
        String authenticationFailedMessage = null;
        if (intent != null) {
            authenticationFailedMessage = intent.getStringExtra(DtsSharedPreferenceUtil.KEY_AUTHENTICATION_MESSAGE);
        }
        if (!DtsUtils.isNullOrEmpty(authenticationFailedMessage)) {
            Snackbar.make(findViewById(R.id.fcvContainer), authenticationFailedMessage, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
        User user = DtsSharedPreferenceUtil.getInstance().getUserFromPref(this, DtsSharedPreferenceUtil.KEY_USER);
        Log.i(TAG, "MainActivity started");
        if (savedInstanceState == null) {

            if (user == null) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fcvContainer, UserHelpFragment.class, null, "UserHelpFragment")
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fcvContainer, HomeFragment.class, null, "HomeFragment")
                        .commit();
            }

        }
    }

    private void showAppUpdateDialog(final AndroidAppUpdate androidAppUpdate) {
        boolean isRemindLaterEnabled = false;
        int remindLaterTextColor = 0;
        final LayoutAppUpdateBinding appUpdateBinding = LayoutAppUpdateBinding.inflate(getLayoutInflater());
        final AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setView(appUpdateBinding.getRoot());
        builder.setCancelable(false);
        Glide.with(this).load(R.drawable.ic_app_icon_tailor)
                .fallback(R.drawable.ic_no_image)
                .error(R.drawable.ic_error_image)
                .into(appUpdateBinding.sivAppIcon);

        if (androidAppUpdate != null) {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            final List<String> whatsNewList = androidAppUpdate.getWhatsNewList();
            final List<String> improvementList = androidAppUpdate.getImprovementList();
            appUpdateBinding.tvAppVersion.setText(androidAppUpdate.getUpdateVersion());
            if (DtsUtils.isNullOrEmpty(whatsNewList)) {
                appUpdateBinding.tvWhatsNew.setVisibility(View.GONE);
                appUpdateBinding.rvWhatsNew.setVisibility(View.GONE);
            } else {
                appUpdateBinding.tvWhatsNew.setVisibility(View.VISIBLE);
                appUpdateBinding.rvWhatsNew.setVisibility(View.VISIBLE);
                final AppUpdateAdapter whatsNewAdapter = new AppUpdateAdapter();
                whatsNewAdapter.setFeatureList(whatsNewList);
                appUpdateBinding.rvWhatsNew.setLayoutManager(layoutManager);
                appUpdateBinding.rvWhatsNew.setAdapter(whatsNewAdapter);
                whatsNewAdapter.notifyDataSetChanged();
            }

            if (DtsUtils.isNullOrEmpty(improvementList)) {
                appUpdateBinding.tvImprovement.setVisibility(View.GONE);
                appUpdateBinding.rvImprovement.setVisibility(View.GONE);
            } else {
                appUpdateBinding.tvImprovement.setVisibility(View.VISIBLE);
                appUpdateBinding.rvImprovement.setVisibility(View.VISIBLE);
                final AppUpdateAdapter improvementAdapter = new AppUpdateAdapter();
                improvementAdapter.setFeatureList(improvementList);
                appUpdateBinding.rvImprovement.setLayoutManager(layoutManager);
                appUpdateBinding.rvImprovement.setAdapter(improvementAdapter);
                improvementAdapter.notifyDataSetChanged();
            }
            if (!androidAppUpdate.isUpdateCompulsory()) {
                isRemindLaterEnabled = true;
            }
            if (isRemindLaterEnabled) {
                remindLaterTextColor = getResources().getColor(R.color.primaryDarkColor);
            } else {
                remindLaterTextColor = getResources().getColor(R.color.light_grey);
            }
            appUpdateBinding.tvRemindLater.setEnabled(isRemindLaterEnabled);
            appUpdateBinding.tvRemindLater.setTextColor(remindLaterTextColor);
        }
        final AlertDialog appUpdateDialog = builder.create();
        appUpdateBinding.tvRemindLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppUpdatePref(androidAppUpdate);
                appUpdateDialog.dismiss();
            }
        });
        appUpdateBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayStore();
            }
        });
        appUpdateDialog.show();
    }

    private void openPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException activityNotFoundException) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void updateAppUpdatePref(AndroidAppUpdate androidAppUpdate) {
        if (androidAppUpdate != null) {
            String formattedDate = getCurrentFormattedDate();
            DtsSharedPreferenceUtil.getInstance().saveStringToSharedPreference(MainActivity.this, formattedDate, DtsSharedPreferenceUtil.KEY_UPDATE_CHECKED_DATE);
            DtsSharedPreferenceUtil.getInstance().saveUserToSharedPreference(MainActivity.this, androidAppUpdate, DtsSharedPreferenceUtil.KEY_ANDROID_APP_UPDATE);
        }
    }

    private void removeAppUpdatePref() {
        DtsSharedPreferenceUtil.getInstance().remove(MainActivity.this, DtsSharedPreferenceUtil.KEY_UPDATE_CHECKED_DATE);
        DtsSharedPreferenceUtil.getInstance().remove(MainActivity.this, DtsSharedPreferenceUtil.KEY_ANDROID_APP_UPDATE);
    }

    private PackageInfo getPackageInfo() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "e");
        }
        return packageInfo;
    }

    private String getCurrentFormattedDate() {
        Date currentDate = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        return formatter.format(currentDate);
    }

}