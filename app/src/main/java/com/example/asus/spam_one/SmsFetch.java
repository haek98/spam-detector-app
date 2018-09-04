package com.example.asus.spam_one;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SmsFetch extends AsyncTaskLoader<Cursor> {
    @SuppressLint("StaticFieldLeak")
    private
    Context context;
    public SmsFetch(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        Cursor cursor=null;
        Uri uriSms = Uri.parse("content://sms/inbox");
        Log.e("TAG","in background "+getContext().getContentResolver().getType(uriSms));
        cursor = context.getContentResolver().query(uriSms, new String[]{"body"},null,null,null);
        return cursor;
    }
}






