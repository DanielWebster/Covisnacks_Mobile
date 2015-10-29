package com.covisint.covisnacks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
    }

}
