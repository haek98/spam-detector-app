package com.example.asus.spam_one;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public static int msgState=0;
    protected static final int SMS_PERMISSION_CODE = 1;
    Context context;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        msgState=0;
        if(savedInstanceState!=null)
        {
            flag=savedInstanceState.getInt("key");
            msgState=savedInstanceState.getInt("state");
        }
        DbHelper helper;
        SQLiteDatabase sql;
        helper=new DbHelper(this);
        sql=helper.getReadableDatabase();
        Log.e("TAG","created activity "+String.valueOf(flag));
        context=this;
        drawerLayout=findViewById(R.id.g_drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        flag=R.id.update;
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        navigationView.getMenu().getItem(0).setChecked(false);
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        int itemId=menuItem.getItemId();
                        flag=itemId;
                        Fragment fragment=null;
                        switch(itemId)
                        {
                            case R.id.update:
                                fragment=new update_fragment();
                                break;
                            case R.id.spam_folder:
                                fragment=new spam_fragment();
                                break;
                            case R.id.not_spam_folder:
                                fragment=new not_spam_fragment();
                                break;
                        }
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();

                        return true;
                    }
                });
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment fragment=null;
            Log.e("TAG","Entered");
            switch(flag)
            {
                case R.id.update:
                    fragment=new update_fragment();
                    break;
                case R.id.spam_folder:
                    fragment=new spam_fragment();
                    break;
                case R.id.not_spam_folder:
                    fragment=new not_spam_fragment();
                    break;
            }
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
        }

    }
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE :
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("TAG","code granted");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    Fragment fragment=new update_fragment();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key",flag);
        outState.putInt("state",msgState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    public void setState(int pos)
    {
        msgState=pos;
        Log.e("TAG","state called "+String.valueOf(msgState));
    }
    public int getState() {
        Log.e("TAG","got message "+String.valueOf(msgState));
        return msgState;
    }
}
