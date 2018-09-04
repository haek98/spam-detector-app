package com.example.asus.spam_one;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

    /**
     * Created by ASUS on 1/23/2018.
     */

    public class DbHelper extends SQLiteOpenHelper implements BaseColumns {
        final static String DB_NAME="messages.db";
        static String id=BaseColumns._ID;
        static String SPAM_TABLE="spam_table";
        static String NOT_SPAM_TABLE="not_spam_table";
        static String host="192.168.43.130";
        public DbHelper(Context context) {
            super(context,DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            String create= "CREATE TABLE spam_table ("+id +" INTEGER PRIMARY KEY AUTOINCREMENT, SENDER TEXT, BODY TEXT)";
            Log.e("TAG","spam_table created");
            try {
                sqLiteDatabase.execSQL(create);
            }catch (SQLiteException e)
            {
                Log.e("TAG","Table not created");
            }
            String create1= "CREATE TABLE not_spam_table ("+id +" INTEGER PRIMARY KEY AUTOINCREMENT, SENDER TEXT, BODY TEXT)";
            sqLiteDatabase.execSQL(create1);
            Log.e("TAG","spam_table created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

