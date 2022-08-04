package com.example.demotailorshop.view.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.demotailorshop.listener.CustomDialogListener;

public class CustomDialog {

    public static AlertDialog getUpdateDialog(Context context, String title, String message, String positiveButtonTitle, String negativeButtonTitle, CustomDialogListener dialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onPositiveClick();
            }
        });
        builder.setNegativeButton(negativeButtonTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onNegativeClick();
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}
