package com.example.asus.spam_one;

;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SmsDbmaker extends AsyncTaskLoader<Cursor> {
    String message,state_res;
    public SmsDbmaker(Context context,String state_res,String message) {
        super(context);
        this.message=message;
        this.state_res=state_res;
    }

    @Override
    public Cursor loadInBackground() {
        Socket s = null;
        try {
            s = new Socket(DbHelper.host, 7000);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            out.write(state_res);
            out.flush();
            out.write(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
