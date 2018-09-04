package com.example.asus.spam_one;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class spam_fragment extends Fragment{
    @BindView(R.id.spam_list)
    ListView listView;
    @BindView(R.id.spam_list_text)
    TextView txt;
    LoaderManager.LoaderCallbacks<Cursor> loader3;
    ArrayList<MessageClass> selected_ids;
    ListAdapter listAdapter;
    ArrayList<MessageClass> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.spam_fragment, container, false);
        ButterKnife.bind(this,v);
        selected_ids=new ArrayList<>();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txt.setVisibility(View.GONE);
        listView.setEmptyView(txt);
        Log.e("TAG","spam called");
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                    selectMessage(arrayList.get(position),checked);
                    if(checked)
                    selected_ids.add(arrayList.get(position));
                    else selected_ids.remove(arrayList.get(position));
                    Log.e("SpamTag","position "+String.valueOf(position));
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        deleteSelectedItems();
                        mode.finish(); // Action picked, so close the CAB
                        deleteAll(selected_ids);
                        selected_ids.clear();
                        return true;
                    default:
                        removeAll(selected_ids);
                        selected_ids.clear();
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                    removeAll(selected_ids);
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });
        loader3=new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                Log.e("TAG","thread3 called");
                String temp="content://com.example.asus.spam_one/spam_table";
                Uri uri=Uri.parse(temp);
                return new AsyncListQuery(getContext(),uri);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data1)
            {final Cursor data=data1;
            arrayList=new ArrayList<MessageClass>();
        if(data==null || data.getCount()==0)
            {
                Log.e("TAG","cursor empty");
                listView.setVisibility(View.GONE);
                txt.setVisibility(View.VISIBLE);
                txt.setText("No messages");
            }
        else {
                Log.e("TAG","cursor not empty "+data.getCount());
                txt.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                data.moveToFirst();
                String partBody=data.getString(2);
                partBody=partBody.substring(0,partBody.length()/2)+"...";
                arrayList.add(new MessageClass(data.getString(1),partBody,false,Integer.valueOf(data.getString(0))));
                while(data.moveToNext()){
                    String partBody1=data.getString(2);
                    partBody1=partBody1.substring(0,partBody1.length()/2)+"...";
                    arrayList.add(new MessageClass(data.getString(1),partBody1,false,Integer.valueOf(data.getString(0))));}
                    listAdapter=new ListAdapter(getContext(), arrayList);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent=new Intent(getContext(),MessageActivity.class);
                        data.moveToPosition(i);
                        intent.putExtra("body",data.getString(2));
                        startActivity(intent);
                    }
                });
            }
        }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        };
        getActivity().getSupportLoaderManager().initLoader(3,null,loader3).forceLoad();
    }

    void deleteSelectedItems()
    {
        for(MessageClass i:selected_ids) {
            Uri uri=Uri.parse("content://com.example.asus.spam_one/spam_table/"+String.valueOf(i.id));
            getActivity().getContentResolver().delete(uri, null, null);
        }
    }

    void selectMessage(MessageClass i,boolean state)
    {
        int pos=arrayList.indexOf(i);
        i.selectState=state;
        arrayList.set(pos,i);
        listAdapter.notifyDataSetChanged();
    }

    void removeAll(ArrayList<MessageClass> tbr)
    {
        for(MessageClass i:tbr)
        {
            int pos=arrayList.indexOf(i);
            i.selectState=false;
            arrayList.set(pos,i);}
        listAdapter.notifyDataSetChanged();
    }

    void deleteAll(ArrayList<MessageClass> tbr)
    {
        for(MessageClass i:tbr)
        {
            arrayList.remove(i);}
        listAdapter.notifyDataSetChanged();
    }
}
