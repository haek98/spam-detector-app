package com.example.asus.spam_one;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class AsyncListQuery extends AsyncTaskLoader<Cursor> {
    Uri uri;
    public AsyncListQuery(Context context,Uri uri) {
        super(context);
        this.uri=uri;
    }

    @Override
    public Cursor loadInBackground() {
//        Log.e("TAG","in background "+getContext().getContentResolver().getType(uri));
        String[] strings={DbHelper.id,"SENDER","BODY"};
        return getContext().getContentResolver().query(uri,strings , null, null, null);
    }
}
