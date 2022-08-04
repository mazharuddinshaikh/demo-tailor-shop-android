package com.example.demotailorshop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DtsSharedPreferenceUtil {
    public static final String KEY_USER = "USER";
    public static final String KEY_DRESS_TYPES = "DRESS_TYPE";
    public static final String KEY_AUTHENTICATION_MESSAGE = "AUTHENTICATION_MESSAGE";
    public static final String VALUE_AUTHENTICATION_MESSAGE = "Something went wrong while login! Please login again";
    public static final String KEY_AUTHORIZATION = "Authorization";

    private static DtsSharedPreferenceUtil dtsSharedPreferenceUtil;

    private DtsSharedPreferenceUtil() {

    }

    public static DtsSharedPreferenceUtil getInstance() {
        if (dtsSharedPreferenceUtil == null) {
            return new DtsSharedPreferenceUtil();
        }
        return dtsSharedPreferenceUtil;
    }

    public List<DressType> getDressTypesFromSharedPref(Activity activity, String key) {
        List<DressType> dressTypeList = new ArrayList<>();
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String jsonString = sharedPref.getString(key, null);
        if (!DtsUtils.isNullOrEmpty(jsonString)) {
            dressTypeList = DtsJsonUtils.getDressTypeListFromJson(jsonString);
        }
        return dressTypeList;
    }

    public User getUserFromPref(Activity activity, String key) {
        User user = null;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String userJson = sharedPref.getString(key, null);
        if (!DtsUtils.isNullOrEmpty(userJson)) {
            user = DtsJsonUtils.getObject(userJson, User.class);
        }
        return user;
    }

    public <T> boolean saveUserToSharedPreference(Activity activity, T t, String key) {
        boolean isSaved = false;
        String userJson = DtsJsonUtils.getJsonFromObject(t);
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (!DtsUtils.isNullOrEmpty(userJson)) {
            editor.putString(key, userJson);
            editor.apply();
            isSaved = true;
        }
        return isSaved;
    }

    public void clearAllPreferences(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    public void remove(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }


}
