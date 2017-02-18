package com.hackathon.cardless;

/**
 * Created by Jack on 18/02/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Jack on 18/02/2017.
 */

public class SimpleAlertDialog {
    public static void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title)
                .setMessage(message)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                // Setting OK Button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        // Showing Alert Message
        AlertDialog alert = dialog.create();
        alert.show();
    }
}
