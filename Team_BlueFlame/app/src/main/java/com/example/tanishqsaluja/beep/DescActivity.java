package com.example.tanishqsaluja.beep;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by tanishqsaluja on 16/3/18.
 */

public class DescActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desc_activity);
        TextView textView=findViewById(R.id.desc);
        textView.setText(getIntent().getStringExtra("desc"));
    }
}
