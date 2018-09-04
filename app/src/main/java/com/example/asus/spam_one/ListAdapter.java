package com.example.asus.spam_one;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context c;
    ArrayList<MessageClass> list;
    public ListAdapter(Context c,ArrayList<MessageClass> list) {
        this.c=c;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class holder{
        TextView t1,t2;
        ImageView photo;
        public holder(View view)
        {
            t1=view.findViewById(R.id.title);
            t2=view.findViewById(R.id.messagePart);
            photo=view.findViewById(R.id.contactImage);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row=view;
        holder h=null;
        if(row==null)
        {
            LayoutInflater inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                row= inflater.inflate(R.layout.listelement,viewGroup,false);
            }
            h=new holder(row);
            row.setTag(h);
        }
        else{
            h= (holder) row.getTag();
        }
        try{
            h.t1.setText((( list.get(i)).title));
            h.t2.setText((( list.get(i)).messagePart));
            boolean state=list.get(i).selectState;
            if(state)
                h.photo.setImageResource(R.drawable.selected);
            else h.photo.setImageResource(R.drawable.contact);
        }
        catch (NullPointerException e)
        {
            Log.e("TAG","nullstring");
        }
        return row;
    }
}

