package com.emp.empty2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hp on 17.08.2015.
 */
public class GameOver extends Activity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.gameover);
        findViewById(R.id.gameover).setBackgroundResource(GlobalValues.background);

        TextView textView, textView1;
        textView = (TextView) findViewById(R.id.gtime);
        textView1 = (TextView) findViewById(R.id.dmg);
        System.out.println(((Float)ReadF("time")).toString());
        if (ReadF("time") <= GlobalValues.gametime / 1000) {
            textView.setText("Your Time: " + Double.toString(GlobalValues.gametime / 1000) + " (new Record!)");
            WriteF((float)GlobalValues.gametime / 1000, "time");
        } else {
            textView.setText("Your Time: " + Float.toString((float)GlobalValues.gametime / 1000)
                    + " (Record: " + Float.toString(ReadF(("time"))) + ")");
        }
        if (ReadF("dmg") <= GlobalValues.damageDeal) {
            textView1.setText("Damage deal: " + Float.toString(GlobalValues.damageDeal) + " (new Record!)");
            WriteF(GlobalValues.damageDeal, "dmg");
        } else {
            textView1.setText("Damage deal: " + Float.toString(GlobalValues.damageDeal)
                    + " (Record: " + Float.toString(ReadF(("dmg"))) + ")");
        }
    }

    public void onClick (View view) {
        Intent intent = new Intent(getApplicationContext(), Intro.class);
        startActivity(intent);
        finish();
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

    public void WriteF (float f, String file) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE)));
            writer.write(Float.toString(f));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            return;
        }
    }
}
