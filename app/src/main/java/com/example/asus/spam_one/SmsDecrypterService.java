package com.example.asus.spam_one;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SmsDecrypterService extends IntentService {

    public SmsDecrypterService()
    {
        super("Decrypter");
    }
    public SmsDecrypterService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("DecrypterTag","service entered");
        Context context=this;
        String message=intent.getStringExtra("body");
        String body=message;
        String head=intent.getStringExtra("head");
        int i;
        i=intent.getIntExtra("id",0);
        Socket s = null;
        try {
            s = new Socket(DbHelper.host, 7000);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            String state="11";
            out.write(state);
            out.flush();
            out.write(message);
            out.flush();
            // send an HTTP request to the web server
            DataInputStream dis=null;
            dis=new DataInputStream(s.getInputStream());
            InputStreamReader disR2 = new InputStreamReader(dis);
            BufferedReader br = new BufferedReader(disR2);//create a BufferReader object for input
            StringBuilder sb=new StringBuilder();
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            message=sb.toString();
            dis.close();
            out.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(message.length()<=0)
        {
            Log.e("TAG","zero length");
            return;
        }
        Character bit=message.charAt(message.length()-2);
        Log.e("TAG", "last character "+String.valueOf(message.charAt(message.length()-2)));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Result")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(i, mBuilder.build());
        if(bit.equals('1'))
        {
            DbHelper helper=new DbHelper(context);
            SQLiteDatabase sql=helper.getWritableDatabase();
            String temp="content://com.example.asus.spam_one/spam_table";
            Uri uri=Uri.parse(temp);
            ContentValues values=new ContentValues();
            values.put("SENDER",head);
            values.put("BODY",body);
            Uri uri2=context.getContentResolver().insert(uri,values);
        }
        else if(bit.equals('0'))
        {
            DbHelper helper=new DbHelper(this);
            SQLiteDatabase sql=helper.getWritableDatabase();
            String temp="content://com.example.asus.spam_one/not_spam_table";
            Uri uri=Uri.parse(temp);
            ContentValues values=new ContentValues();
            values.put("SENDER",head);
            values.put("BODY",body);
            Uri uri2=context.getContentResolver().insert(uri,values);
        }
        else Log.e("TAG","there is some problem");
    }
}
