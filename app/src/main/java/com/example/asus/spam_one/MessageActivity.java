package com.example.asus.spam_one;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity {
    @BindView(R.id.messageText)
    TextView text;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar=findViewById(R.id.messageToolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        ButterKnife.bind(this);
        text.setText(intent.getStringExtra("body"));
    }
}
