package com.emp.empty2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Records extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.records);
        findViewById(R.id.records).setBackgroundResource(GlobalValues.background);

        TextView textView, textView1;
        textView = (TextView) findViewById(R.id.trec);
        textView.setText("Time: " + Float.toString(ReadF("time")));
        textView1 = (TextView) findViewById(R.id.grec);
        textView1.setText("Damage: " + Float.toString(ReadF("dmg")));
    }

    public float ReadF (String file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(file)));
            return (Float.valueOf(reader.readLine()));
        } catch (FileNotFoundException e) {
            return 0;
        } catch (IOException e) {
            return 0;
        }
    }

    public void onClick(View view) {
        finish();
    }
}
