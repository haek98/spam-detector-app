package com.example.asus.spam_one;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ASUS on 1/28/2018.
 */

public class DbCP extends ContentProvider {
    DbHelper helper;
    SQLiteDatabase sql;
    UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        helper=new DbHelper(getContext());
        sql=helper.getReadableDatabase();
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.asus.spam_one","spam_table",1);
        uriMatcher.addURI("com.example.asus.spam_one","not_spam_table",2);
        uriMatcher.addURI("com.example.asus.spam_one","spam_table/#",3);
        uriMatcher.addURI("com.example.asus.spam_one","not_spam_table/#",4);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int match=uriMatcher.match(uri);
        Cursor cursor=null;
        switch(match)
        {
            case 1:
                cursor=sql.query("spam_table",strings,null,null,null,null,null);

                break;
            case 2:
                cursor=sql.query("not_spam_table",strings,null,null,null,null,null);

                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase sql1=helper.getWritableDatabase();
        switch(uriMatcher.match(uri))
        {
            case 1:
                long newRowId = sql1.insert("spam_table",null,values);
                Log.e("TAG",String.valueOf(newRowId));
                return ContentUris.withAppendedId(uri,newRowId);
            case 2:
                long newRowId1 = sql1.insert("not_spam_table",null,values);
                Log.e("TAG",String.valueOf(newRowId1));
                return ContentUris.withAppendedId(uri,newRowId1);
            default:
                throw new IllegalStateException("operation failed");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        {
            SQLiteDatabase sql1=helper.getWritableDatabase();
            switch(uriMatcher.match(uri))
            {
                case 1:
                    return sql1.delete("spam_table",selection,selectionArgs);
                case 2:
                    return sql1.delete("not_spam_table",selection,selectionArgs);
                case 3:
                    selection=DbHelper.id+"=?";
                    selectionArgs=new String[] { String.valueOf(ContentUris.parseId(uri)) };
                    return  sql1.delete(DbHelper.SPAM_TABLE,selection,selectionArgs);
                case 4:
                    selection=DbHelper.id+"=?";
                    selectionArgs=new String[] { String.valueOf(ContentUris.parseId(uri)) };
                    return  sql1.delete(DbHelper.NOT_SPAM_TABLE,selection,selectionArgs);
                default:
                    throw new IllegalStateException("Can't be deleted");
            }
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sql=helper.getWritableDatabase();
        switch(uriMatcher.match(uri))
        {
            case 1:
                return sql.update("spam_table",contentValues,selection,selectionArgs);

            case 2:
                return sql.update("not_spam_table",contentValues,selection,selectionArgs);
            default:
                throw new IllegalStateException("non updatable");
        }
    }
}
