package com.example.asus.spam_one;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        Log.e("BroadcastTag","message received");
        if (intentExtras != null) {
            /* Get Messages */
            Object[] sms = (Object[]) intentExtras.get("pdus");

            assert sms != null;
            for (int i = 0; i < sms.length; ++i) {
                /* Parse Each Message */
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String phone = smsMessage.getOriginatingAddress();
                String message = smsMessage.getMessageBody();
                Intent decrypterIntent=new Intent(context,SmsDecrypterService.class);
                decrypterIntent.putExtra("body",message);
                decrypterIntent.putExtra("head",phone);
                decrypterIntent.putExtra("id",i);
                context.startService(decrypterIntent);
            }
        }
    }
}
