package com.emp.empty2.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;
import com.emp.empty2.Views.ComonView;
import com.emp.empty2.Views.MultiPlayerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hp on 17.08.2015.
 */
public class Intro extends Activity {

    ComonView comonView;
    MultiPlayerView multiPlayerView;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GlobalValues.uWin = false;
        GlobalValues.scale = (float)getResources().getDisplayMetrics().widthPixels / 1280;
        GlobalValues.gametime = 0;

        String background = ReadF("background");
        switch(background) {
            case "space1" : {
                GlobalValues.background = R.drawable.space1;
            } break;
            case "space2" : {
                GlobalValues.background = R.drawable.space2;
            } break;
            case "space3" : {
                GlobalValues.background = R.drawable.space3;
            } break;
            case "space4" : {
                GlobalValues.background = R.drawable.space4;
            } break;
            case "space5" : {
                GlobalValues.background = R.drawable.space;
            } break;
            default : {
                WriteF("space5", "background");
            }
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.intro);
        findViewById(R.id.intro).setBackgroundResource(GlobalValues.background);
    }

    public void onResume() {
        super.onResume();
        findViewById(R.id.intro).setBackgroundResource(GlobalValues.background);
    }

    public void onClick (View view) {
        comonView = new ComonView(this);
        setContentView(comonView);
    }

    public void Versus (View view) {
        Intent intent = new Intent(getApplicationContext(), Multiplayer.class);
        startActivity(intent);
    }

    public void records (View view) {
        Intent intent = new Intent(getApplicationContext(), Records.class);
        startActivity(intent);
    }

    public void settings (View view) {
        Intent intent = new Intent(getApplicationContext(), GameSettings.class);
        startActivity(intent);
    }

    public void rules (View view) {
        Intent intent = new Intent(getApplicationContext(), Rules.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        if (comonView != null) {
            comonView.running = false;
            Intent intent = new Intent(getApplicationContext(), GameOver.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    public String ReadF (String file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(file)));
            return reader.readLine();
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    public void WriteF (String f, String file) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE)));
            writer.write(f);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            return;
        }
    }
}
