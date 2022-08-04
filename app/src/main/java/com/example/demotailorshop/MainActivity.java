package com.example.demotailorshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.fragment.HomeFragment;
import com.example.demotailorshop.fragment.UserHelpFragment;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


}