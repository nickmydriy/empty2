/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emp.empty2.server;

import com.emp.empty2.Basis.NetPac;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author hp
 */
public class ServerEmp extends Thread{

    JFrame window;
    JButton button;
    int port;
    ServerEmp (String port) {     
        window = new JFrame("Emp Server");
        window.setSize(320, 100);
        window.setLayout(new FlowLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel text = new JLabel("IP: " + getIpAddress() + ":" + "5676");
        window.add(text); 
        window.setVisible(true);
        this.port = Integer.parseInt(port);
        this.start();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ServerEmp(args[0]);
            }
        });
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
            // TODO Auto-generated catch block
            e.printStackTrace();
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
                e.printStackTrace();
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
                    System.out.println(id);
                    if (id != -1) {
                        if (games[id] == null) {
                            games[id] = conn;
                            System.out.println("Host connected. ID: " + ((Byte) id).toString());
                        } else {
                            System.out.println("Player connected. ID: " + ((Byte) id).toString());
                            new Thread(new Runnable() {
                                public void run() {
                                    new Game().game(games[id], conn);
                                    games[id] = null;
                                }
                            }).start();
                        }
                    } else {
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
    public void game(Socket player1, Socket player2) {        
        try {
            System.out.println("starting game");
            out1 = new ObjectOutputStream(player1.getOutputStream());
            out2 = new ObjectOutputStream(player2.getOutputStream());
            System.out.println("outputs opened");
            in1 = new ObjectInputStream(player1.getInputStream());
            in2 = new ObjectInputStream(player2.getInputStream());
            System.out.println("inputs opened");
            out1.writeChar('s');
            out1.flush();
            out2.writeChar('s');
            out2.flush();
        } catch (Exception e) {
            e.printStackTrace();
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
                        System.out.println("1st Thread ERR: " + e.toString());
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
                        System.out.println("2nd Thread ERR: " + e.toString());
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
            System.out.println("Thread over! " + e.toString());
        } finally {
            System.out.println("FINALLY");
            try {
                player1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                player2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
