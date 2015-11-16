/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emp.empty2.server;

import com.emp.empty2.Basis.NetPac;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;

/**
 *
 * @author hp
 */
public class ServerEmp extends Thread{
    int port;
    ServerEmp (String port) {
        this.port = Integer.parseInt(port);
        this.start();
    }
    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                new ServerEmp(args[0]);
            }
        }).start();
    }
    private static String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        return "LocalAddress: " + inetAddress.getHostAddress() + "\n";
                    }
                }
            }
        } catch (SocketException e) {
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }
        
    ServerSocket server;
    Socket[] games;
    @Override
    public void run() {
        {
            games = new Socket[256];
            try {
                server = new ServerSocket(port, 256, InetAddress.getByName("0.0.0.0"));
            } catch (Exception e) {
                System.out.println("Could not create server socket: " + e.toString());
            }
            while (true) {
                try {
                    new Thread(new Runnable() {
                        public void run() {
                            checkGames();
                        }
                    }).start();
                    Socket conn = server.accept();
                    conn.setTcpNoDelay(true);
                    ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
                    byte id = in.readByte();
                    in = null;
                    if (id != -1) {
                        if (games[id] == null) {
                            games[id] = conn;
                            System.out.println("Host connected. ID: " + ((Byte) id).toString());
                        } else {
                            System.out.println("Player connected. ID: " + ((Byte) id).toString());
                            new Thread(new Runnable() {
                                public void run() {
                                    new Game().game(games[id], conn, id);
                                    games[id] = null;
                                }
                            }).start();
                        }
                    } else {
                        System.out.println(conn.getInetAddress().toString() + " requests for hosts.");
                        new Thread(new Runnable() {                            
                            public void run() {
                                searchGames(conn);
                            }
                        }).start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }            
        }
    }

    
    public void checkGames() {
        for (int i = 0; i < games.length; i++) {
            if (games[i] != null) {
                try {
                    games[i].sendUrgentData(i);
                } catch (Exception e) {
                    games[i] = null;
                }
            }
        }
    }
    
    public void searchGames(Socket client) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            int k = 0;
            Byte[] list = new Byte[games.length];
            for (int i = 0; i < games.length; i++) {
                if (games[i] != null) {
                    list[k] = (byte)i;
                    k++;
                }
            }
            list = Arrays.copyOfRange(list, 0, k);
            out.writeObject(list);
            out.flush();
            out.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}


class Game {
    boolean bonus1 = false, bonus2 = false, isRun = true;
    ObjectInputStream in1, in2;
    ObjectOutputStream out1, out2;
    int bx, by;
    public void game(Socket player1, Socket player2, int id) {        
        try {
            System.out.println("Game started. Game id: " + id);
            out1 = new ObjectOutputStream(player1.getOutputStream());
            out2 = new ObjectOutputStream(player2.getOutputStream());
            in1 = new ObjectInputStream(player1.getInputStream());
            in2 = new ObjectInputStream(player2.getInputStream());
            out1.writeChar('s');
            out1.flush();
            out2.writeChar('s');
            out2.flush();
        } catch (Exception e) {
            System.out.println("Game id: " + id + " finished because of connection problem.");
        }
        try {
            Thread pl1 = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            if (bonus1) {
                                NetPac val = (NetPac) in1.readObject();
                                val.bx = (short)bx;
                                val.by = (short)by;
                                bonus1 = false;
                                out2.writeObject(val);
                            } else {
                                out2.writeObject(in1.readObject());
                            }
                            out2.flush();
                        }
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        isRun = false;
                        try {
                            player1.close();
                        } catch (IOException e) {
                        }
                        try {
                            in1.close();
                        } catch (IOException e) {
                        }
                        try {
                            out2.close();
                        } catch (IOException e) {
                        }
                    }
                }
            });
            Thread pl2 = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {

                            if (bonus2) {
                                NetPac val = (NetPac) in2.readObject();
                                val.bx = (short)bx;
                                val.by = (short)by;
                                bonus2 = false;
                                out1.writeObject(val);
                            } else {
                                out1.writeObject(in2.readObject());
                            }
                            out1.flush();
                        }
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        isRun = false;
                        try {
                            player2.close();
                        } catch (IOException e) {
                        }
                        try {
                            in2.close();
                        } catch (IOException e) {
                        }
                        try {
                            out1.close();
                        } catch (IOException e) {
                        }
                    }
                }
            });
            Random random = new Random();
            Thread.sleep(1000);
            out1.writeInt(Math.abs(random.nextInt(4534634) % 1280));
            out1.flush();
            out2.writeInt(Math.abs(random.nextInt(4534545) % 1280));
            out2.flush();
            pl1.start();
            pl2.start();
            isRun = true;
            while (isRun) {
                sleep(3500);
                bx = Math.abs(random.nextInt((int) System.currentTimeMillis()) % 1000) + 140;
                bonus1 = true;
                by = Math.abs(random.nextInt(((int) System.currentTimeMillis() * 2) % 600)) + 60;
                bonus2 = true;
            }
        } catch (Exception e) {
        } finally {
            System.out.println("Game id: " + id + " finished.");
            try {
                player1.close();
            } catch (IOException e) {
            }
            try {
                player2.close();
            } catch (IOException e) {
            }
        }
    }
}
