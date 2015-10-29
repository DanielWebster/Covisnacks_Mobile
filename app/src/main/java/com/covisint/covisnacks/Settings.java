package com.covisint.covisnacks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class Settings extends AppCompatActivity {
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        layout = (LinearLayout)findViewById(R.id.settings);

        SeekBar seek = new SeekBar(this);
        seek.setMax(100);
        layout.addView(seek);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView text = new TextView(Settings.this);
                text.setText("Alert me when there is " + progress + "% remaining snacks in the jar");
                layout.addView(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
