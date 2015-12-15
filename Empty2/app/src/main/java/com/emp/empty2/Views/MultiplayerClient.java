package com.emp.empty2.Views;

import android.util.Log;

import com.emp.empty2.Basis.NetPac;
import com.emp.empty2.GlobalValues;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by hp on 12.11.2015.
 */
public class MultiplayerClient extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket conn;
    String ip;
    char status;
    MultiPlayerView multiplayerView;
    boolean isFire;
    int port;
    byte id;
    int x;
    Thread inputState, outputState;
    public MultiplayerClient(String ip, int port, byte id, MultiPlayerView multiPlayerView) {
        try {
            this.id = id;
            status = 'w';
            x = -1;
            this.ip = ip;
            this.port = port;
            isFire = false;
            this.multiplayerView = multiPlayerView;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public void ThreadDestroy() {
        try {
            inputState.interrupt();
            outputState.interrupt();
            interrupt();
        } catch (Exception e) {
            Log.i("ThreadDestroyERR", e.toString());
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void run() {
        try {
            conn = new Socket(InetAddress.getByName(ip), port);
            conn.setTcpNoDelay(true);
            out = new ObjectOutputStream(conn.getOutputStream());
            status = 'o';
        } catch (Exception e) {
            Log.d("err", e.toString());
            GlobalValues.multiplayerError = true;
            interrupt();
        }
        try {
            out.writeByte(id);
            out.flush();
            in = new ObjectInputStream(conn.getInputStream());
            out = new ObjectOutputStream(conn.getOutputStream());
            status = in.readChar();
            x = (int)(in.readInt() * GlobalValues.scale);
            System.out.println(((Integer)x).toString());
            multiplayerView.ship.ship.x = x;
            inputState = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            NetPac val = (NetPac) in.readObject();
                            multiplayerView.publishData(val);
                        }
                    } catch(Exception e){
                        Log.i("inputStateERR", e.toString());
                        Thread.currentThread().interrupt();
                    } finally{
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                        try {
                            conn.close();
                        } catch (IOException e) {
                        }
                    }
                }
            });
            outputState = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            if (multiplayerView.readytoSend) {

                                if (isFire) {
                                    out.writeObject(new NetPac(multiplayerView.ship.ship.x / GlobalValues.scale,
                                            multiplayerView.ship.ship.y / GlobalValues.scale,
                                            multiplayerView.ship.ship.rotation,
                                            multiplayerView.ship.shipHp,
                                            0,
                                            0,
                                            true));
                                    isFire = false;
                                } else {
                                    out.writeObject((new NetPac(multiplayerView.ship.ship.x / GlobalValues.scale,
                                            multiplayerView.ship.ship.y / GlobalValues.scale,
                                            multiplayerView.ship.ship.rotation,
                                            multiplayerView.ship.shipHp,
                                            0,
                                            0,
                                            false)));
                                }
                                out.flush();
                                sleep(30);
                                multiplayerView.readytoSend = false;
                            }
                        }
                    } catch (Exception e) {
                            Log.i("outputStateERR", e.toString());
                            Thread.currentThread().interrupt();
                    } finally {
                        try {
                            out.close();
                        } catch (IOException e) {
                        }
                        try {
                            conn.close();
                        } catch (IOException e) {
                        }
                    }
                    }
                });
            inputState.start();
            outputState.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
