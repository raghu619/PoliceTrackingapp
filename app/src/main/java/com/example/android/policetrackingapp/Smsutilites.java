package com.example.android.policetrackingapp;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by raghvendra on 13/3/18.
 */

public class Smsutilites {


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
     public static void  sendmessage(Context context,String message,String Phone_no) {


         if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);


             return;
         }
         SmsManager smsManager = SmsManager.getDefault();
         smsManager.sendTextMessage(Phone_no, null, message, null, null);


         Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
     }
}
