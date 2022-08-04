package com.example.demotailorshop.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DtsUtils {
    public static final String TAG = "DtsUtils";
    public static final int VIEW_TYPE_CHECKBOX = 0;
    public static final int VIEW_TYPE_DATE = 1;
    public static final int VIEW_TYPE_DATE_RANGE = 2;
    public static final String FILTER = "FILTER";
    public static final String SORT = "SORT";

    public static <E> boolean isNullOrEmpty(Collection<E> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T, U> boolean isNullOrEmpty(Map<T, U> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static int getScreenWidth(final Context context) {
        int displayWidth = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        return displayWidth;
    }

    public static boolean isNumber(String value) {
        boolean isNumber = false;
        try {
            if (!isNullOrEmpty(value)) {
                int number = Integer.parseInt(value);
                isNumber = true;
            }
        } catch (NumberFormatException ignored) {
        }
        return isNumber;
    }

    public static String getFormattedDate(String date, String dateFormat, String requiredFormat) {
        String result = null;
        SimpleDateFormat existingDateForm = new SimpleDateFormat(dateFormat, Locale.US);
        SimpleDateFormat requiredDateFormat = new SimpleDateFormat(requiredFormat, Locale.US);
        try {
            Date localDate = existingDateForm.parse(date);
            if (localDate != null) {
                result = requiredDateFormat.format(localDate);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error while parsing date");
        }
        return result;
    }

}
