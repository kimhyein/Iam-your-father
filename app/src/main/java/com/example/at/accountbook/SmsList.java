package com.example.at.accountbook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by AT on 2014-07-13.
 */
public class SmsList extends BroadcastReceiver{

    private static final String ROSA_ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("rosa", "test");

        String test = intent.getAction();

        if(test.equals(ROSA_ACTION_SMS_RECEIVED)){
            Log.w("rosa", "test");
            Bundle bundle = intent.getExtras();
            if(bundle == null){
                Log.w("rosa", "Message bundle is null !!");
                return;
            }else{
                Object[] pdusObj = (Object[])bundle.get("pdus");
                if(pdusObj == null){
                    Log.w("rosa", "Message pdu's are null !!");
                    return;
                }

                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                Log.i("rosa", "retrieving : " + pdusObj.length + "messages");

                for(int i = 0 ; i < messages.length ; i++){
                    messages[i] = SmsMessage.createFromPdu((byte[])pdusObj[i]);
                    Log.i("rosa", messages[i].getOriginatingAddress()
                            + "::" + messages[i].getMessageBody());
                }
            }
        }
    }
}