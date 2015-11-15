package com.emp.empty2.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;


/**
 * Created by hp on 15.11.2015.
 */
public class Rules extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rules);
        findViewById(R.id.bonus).setBackgroundResource(GlobalValues.background);
    }
}
