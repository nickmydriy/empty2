package com.emp.empty2.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emp.empty2.Basis.List;
import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hp on 20.08.2015.
 */
public class GameSettings extends Activity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);
        findViewById(R.id.settings_layout).setBackgroundResource(GlobalValues.background);
        ListView background = (ListView) findViewById(R.id.background);
        String[] list = {"space1",
                "space2",
                "space3",
                "space4",
                "space5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_view_resourse, list);
        background.setAdapter(adapter);
        background.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                WriteF(((TextView)view).getText().toString(), "background");
                switch (((TextView)view).getText().toString()) {
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
                }
                LinearLayout layout = (LinearLayout) findViewById(R.id.settings_layout);
                layout.setBackgroundResource(GlobalValues.background);
            }
        });
        String aa = ReadF("AA");
        if (aa.equals("")) {
            WriteF("t", "AA");
        } else {
            CheckBox checkBox = (CheckBox) findViewById(R.id.aabox);
            if (aa.equals("t")) {
                checkBox.setChecked(true);
                GlobalValues.antiAliasing = true;
            } else {
                checkBox.setChecked(false);
                GlobalValues.antiAliasing = false;
            }
        }
    }

    public void setAA (View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            GlobalValues.antiAliasing = true;
            WriteF("t", "AA");
        } else {
            GlobalValues.antiAliasing = false;
            WriteF("f", "AA");
        }
    }

    public void clearResult (View view) {
        WriteF("0.0", "time");
        WriteF("0.0", "dmg");
        Toast.makeText(this, "Result cleared!", Toast.LENGTH_SHORT).show();
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

