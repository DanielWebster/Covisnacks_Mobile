package com.covisint.covisnacks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Settings extends AppCompatActivity {

    public String empty_file = "empty.txt";
    public String half_file = "half.txt";
    public String full_file = "full.txt";
    ToggleButton emptyToggle;
    ToggleButton halfToggle;
    ToggleButton fullToggle;
    int emptyValue;
    int halfValue;
    int fullValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        emptyToggle = (ToggleButton) findViewById(R.id.empty);
        halfToggle = (ToggleButton) findViewById(R.id.half);
        fullToggle = (ToggleButton) findViewById(R.id.full);

    }

    public void empty(View view) {
        if (emptyToggle.isChecked()) {
            emptyValue = 1;
        }
        else {
            emptyValue = 0;
        }

        writeData(this, emptyValue, empty_file);
    }

    public void half(View view) {
        if (halfToggle.isChecked()) {
            halfValue = 1;
        }
        else {
            halfValue = 0;
        }

        writeData(this, halfValue, half_file);
    }

    public void full(View view) {
        if (fullToggle.isChecked()) {
            fullValue = 1;
        }
        else {
            fullValue = 0;
        }

        writeData(this, fullValue, full_file);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    public void writeData(Context context, int value, String fileName) {

        BufferedWriter bufferWriter = null;
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bufferWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferWriter.write(Integer.toString(value));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }finally
        {
            try {
                bufferWriter.close();
            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    public int readData(Context context, String fileName) {

        BufferedReader bufferReader = null;
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            bufferReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferReader.readLine()) != null) {
                result.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result.toString().equals("")) {
            return 0;
        }
        else {
            return Integer.parseInt(result.toString());
        }
    }

}
