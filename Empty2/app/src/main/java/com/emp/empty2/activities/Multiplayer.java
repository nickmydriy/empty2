package com.emp.empty2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;
import com.emp.empty2.Views.MultiPlayerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by hp on 15.11.2015.
 */
public class Multiplayer extends Activity {
    MultiPlayerView multiPlayerView;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GlobalValues.uWin = false;
        GlobalValues.scale = (float)getResources().getDisplayMetrics().widthPixels / 1280;
        GlobalValues.gametime = 0;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.multiplayer_settings);
        findViewById(R.id.mul_settings).setBackgroundResource(GlobalValues.background);
        GlobalValues.isBreak = false;
        if (GlobalValues.multiplayerError) {
            GlobalValues.multiplayerError = false;
            Toast.makeText(this, "Network Error!", Toast.LENGTH_SHORT).show();
        }
        String val = ReadF("ip");
        if (!val.equals("")) {
            EditText text = (EditText) findViewById(R.id.ip_edit);
            text.setText(val);
        }
        val = ReadF("port");
        if (!val.equals("")) {
            EditText text = (EditText) findViewById(R.id.port_edit);
            text.setText(val);
        }
    }


    public void gameStart (View view) {
        try {
            EditText text = (EditText) findViewById(R.id.ip_edit);
            String ip = text.getText().toString();
            text = (EditText) findViewById(R.id.port_edit);
            int port = Integer.parseInt(text.getText().toString());
            text = (EditText) findViewById(R.id.id_edit);
            byte id = Byte.parseByte(text.getText().toString());
            multiPlayerView = new MultiPlayerView(this, ip, port, id);
            WriteF(((EditText)(findViewById(R.id.ip_edit))).getText().toString(), "ip");
            WriteF(((EditText)(findViewById(R.id.port_edit))).getText().toString(), "port");
            setContentView(multiPlayerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findGames (View view) {
        EditText text = (EditText) findViewById(R.id.ip_edit);
        String ip = text.getText().toString();
        text = (EditText) findViewById(R.id.port_edit);
        (new GetGames(this)).execute(ip, text.getText().toString());
    }

    class GetGames extends AsyncTask<String, Void, Byte[]> {
        Multiplayer activity;
        public GetGames (Multiplayer multiplayer) {
            activity = multiplayer;
        }
        @Override
        protected Byte[] doInBackground(String... params) {
            Byte[] list = null;
            try {
                Socket conn = new Socket(InetAddress.getByName(params[0]),
                        Integer.parseInt(params[1]));
                conn.setTcpNoDelay(true);
                ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());
                out.writeByte(-1);
                out.flush();
                ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
                list = (Byte[]) in.readObject();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        protected void onPostExecute(Byte[] result) {
            ListView games = (ListView) findViewById(R.id.listView);
            if (result == null) {
                games.setVisibility(View.GONE);
                Toast.makeText(activity, "Network Error!", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] list = new String[result.length];
            for (int i = 0; i < result.length; i++) {
                list[i] = result[i].toString();
            }
            if (result.length == 0) {
                games.setVisibility(View.GONE);
                Toast.makeText(activity, "No Games Found!", Toast.LENGTH_SHORT).show();
                return;
            } else {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                        R.layout.list_view_resourse, list);
                games.setAdapter(adapter);
                games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        listPressed(view);
                    }
                });
                TextView err = (TextView) findViewById(R.id.games_err);
                err.setVisibility(View.GONE);
            }
        }
    }

    public void listPressed (View view) {
        try {
            EditText text = (EditText) findViewById(R.id.ip_edit);
            String ip = text.getText().toString();
            text = (EditText) findViewById(R.id.port_edit);
            int port = Integer.parseInt(text.getText().toString());
            byte id = Byte.parseByte(((TextView)view).getText().toString());
            multiPlayerView = new MultiPlayerView(this, ip, port, id);
            WriteF(((EditText)(findViewById(R.id.ip_edit))).getText().toString(), "ip");
            WriteF(((EditText)(findViewById(R.id.port_edit))).getText().toString(), "port");
            setContentView(multiPlayerView);
        } catch (Exception e) {
            e.printStackTrace();
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

    public void onBackPressed () {
        if (multiPlayerView != null) {
            Intent intent = new Intent(getApplicationContext(), Multiplayer.class);
            startActivity(intent);
            GlobalValues.isBreak = true;
            multiPlayerView.multiplayerClient.ThreadDestroy();
            finish();
        } else {
            finish();
        }
    }
}
