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

/**
 * Created by hp on 12.11.2015.
 */
public class MultiplayerOver extends Activity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.gameover_multiplayer);
        findViewById(R.id.gameover_mul).setBackgroundResource(GlobalValues.background);

        TextView textView;
        textView = (TextView) findViewById(R.id.multiplayer_text);
        if (GlobalValues.uWin) {
            textView.setText("You Won!!");
        } else {
            textView.setText("You Lose!!");
        }
    }

    public void onClick (View view) {
        Intent intent = new Intent(getApplicationContext(), Multiplayer.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Intro.class);
        startActivity(intent);
        finish();
    }
}
