package com.example.asus.spam_one;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class update_fragment extends Fragment {
    Button button;
    TextView textView;
    int i = 0,flag=0;
    SharedPreferences sharedPref;
    int prev,pos=0;
    Button b1,b2;
    Cursor cursor;
    LoaderManager.LoaderCallbacks<Cursor> loader2;
    LoaderManager.LoaderCallbacks<Cursor> loader1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.update_fragment, container, false);
//        setRetainInstance(true);
        ButterKnife.bind(this,view);
        MainActivity obj=new MainActivity();
        pos=obj.getState();
        Log.e("TAG",String.valueOf(pos));
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TAG","created "+String.valueOf(pos));
        textView = getActivity().findViewById(R.id.textView);
        b1=getActivity().findViewById(R.id.button1);
        b2=getActivity().findViewById(R.id.button2);
        sharedPref = getActivity().getSharedPreferences("com.example.asus.spam_one",Context.MODE_PRIVATE);
        prev=sharedPref.getInt("prev",0);
        loader1=new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                return new SmsFetch(getActivity());
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                cursor=data;
                cursor.moveToPosition(pos);
                textView.setText(cursor.getString(0));
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        };
            if(prev==0)
            {
                getActivity().getSupportLoaderManager().initLoader(0, null, loader1).forceLoad();
            }


        loader2=new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                String state_res=args.getString("state_res");
                String message=args.getString("msg");
                return new SmsDbmaker(getActivity(),state_res,message);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                if(cursor!=null && cursor.getPosition()<cursor.getCount())
                {
                    cursor.moveToNext();
                    if(cursor!=null && cursor.getPosition()<cursor.getCount()) {
                        MainActivity obj=new MainActivity();
                        obj.setState(cursor.getPosition());
                        textView.setText(cursor.getString(0));
                    }
                    else{
                        textView.setText("Thank you for your cooperation");
                        b1.setVisibility(View.GONE);
                        b2.setVisibility(View.GONE);
                       /* Bundle bundle=new Bundle();
                        bundle.putString("state_res","21");
                        bundle.putString("msg","abc");*/
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("prev", 1);
                        editor.apply();
//                        getActivity().getSupportLoaderManager().restartLoader(1, bundle, loader2).forceLoad();
                    }}
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        };

    }
/*
    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG","started");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG","resumed");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG","paused");
    }

*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG","destroyed");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("TAG","detach");
    }

    @OnClick(R.id.button1)
    public void spam(View view)
    {
        if(cursor==null || cursor.getPosition()>=cursor.getCount())
        {
            textView.setText("Thank you for your cooperation");
            b1.setVisibility(View.GONE);
            b2.setVisibility(View.GONE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("prev", 1);
            editor.apply();
        }
        else{
            Bundle bundle=new Bundle();
            String state_res=String.valueOf(prev)+"1";
            bundle.putString("state_res",state_res);
            bundle.putString("msg",cursor.getString(0));
            if(flag==0) {
                getActivity().getSupportLoaderManager().initLoader(1, bundle, loader2).forceLoad();
                flag = 1;
            }
            else getActivity().getSupportLoaderManager().restartLoader(1, bundle, loader2).forceLoad();
        }
    }
    @OnClick(R.id.button2)
    public void nspam(View view)
    {
        if(cursor==null || cursor.getPosition()>=cursor.getCount())
        {
            textView.setText("Thank you for your cooperation");
            b1.setVisibility(View.GONE);
            b2.setVisibility(View.GONE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("prev", 1);
            editor.apply();
        }
        else{
            Bundle bundle=new Bundle();
            String state_res=String.valueOf(prev)+"0";
            bundle.putString("state_res",state_res);
            bundle.putString("msg",cursor.getString(0));
            if(flag==0) {
                getActivity().getSupportLoaderManager().initLoader(1, bundle, loader2).forceLoad();
                flag = 1;
            }
            else getActivity().getSupportLoaderManager().restartLoader(1, bundle, loader2).forceLoad();
        }
    }
}
