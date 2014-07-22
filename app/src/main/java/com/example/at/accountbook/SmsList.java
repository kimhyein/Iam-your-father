package com.example.at.accountbook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsList extends BroadcastReceiver {

    static final String logTag = "SmsReceiver";
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";


    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(ACTION)) {
            Bundle bundle = intent.getExtras();

            if (bundle == null) {
                return;
            }
            Object[] pdusObj = (Object[]) bundle.get("pdus");

            if (pdusObj == null) {
                return;
            }

            String str = "";

            SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
            for (int i = 0; i < pdusObj.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                Log.e(logTag, "NEW SMS " + i + "th");
                Log.e(logTag, "DisplayOriginationgAddress : " + smsMessages[i].getDisplayOriginatingAddress());
                Log.e(logTag, "DisplayMessageBody : " + smsMessages[i].getDisplayMessageBody());
                Log.e(logTag, "MessageBody : " + smsMessages[i].getMessageBody());


                Toast.makeText(context, smsMessages[i].getMessageBody(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}


