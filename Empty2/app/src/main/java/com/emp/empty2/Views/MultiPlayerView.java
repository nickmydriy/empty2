package com.emp.empty2.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.emp.empty2.Basis.BonusTread;
import com.emp.empty2.Basis.FireThread;
import com.emp.empty2.Basis.JoyStick;
import com.emp.empty2.Basis.NetPac;
import com.emp.empty2.GlobalValues;
import com.emp.empty2.activities.Multiplayer;
import com.emp.empty2.activities.MultiplayerOver;
import com.emp.empty2.R;
import com.emp.empty2.enemy.EnemyShip;
import com.emp.empty2.ship.Ship;

import java.io.BufferedWriter;

/**
 * Created by hp on 12.11.2015.
 */
public class MultiPlayerView extends View {

    public Ship ship;
    public EnemyShip enemyShip;
    public BitmapFactory.Options BmOp;
    public Context context;
    public BufferedWriter bufferedWriter;
    public Matrix matrix;
    public Bitmap space;
    public JoyStick joystick;
    public boolean isTouch, isFireTouch, isTouchDown, isTouchUp;
    public float xTouch, yTouch;
    public boolean running;
    public double proctime, time, gametime;
    public Paint whitePaint, blackPaint;
    public float forMidFps;
    public MultiplayerClient multiplayerClient;
    public BonusTread bonusTread;
    public boolean readytoSend;

    public MultiPlayerView (Context context, String ip, int port, byte id) {
        super(context);
        this.context = context;
        GlobalValues.Width = getResources().getDisplayMetrics().widthPixels;
        GlobalValues.Height = getResources().getDisplayMetrics().heightPixels;
        ship = new Ship(context.getResources(), 0.5f, 25, R.drawable.fire);
        bonusTread = new BonusTread(getResources());
        isTouch = false;
        isFireTouch = false;
        isTouchDown = false;
        BmOp = new BitmapFactory.Options();
        BmOp.inTargetDensity = (int)(GlobalValues.scale * 160);
        space = BitmapFactory.decodeResource(getResources(), GlobalValues.background, BmOp);
        matrix = new Matrix();
        matrix.postScale(1, 1);
        matrix.postTranslate(0, 0);
        joystick = new JoyStick(-100, -100, context.getResources());
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextScaleX(1.5f);
        whitePaint.setTextSize(25 * GlobalValues.scale);
        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        GlobalValues.damageDeal = 0;
        running = true;
        time = SystemClock.uptimeMillis();
        gametime = 0;
        forMidFps = 16;
        multiplayerClient = new MultiplayerClient(ip, port, id, this);
        multiplayerClient.start();
        enemyShip = new EnemyShip(getResources(), 0.5f, 25, R.drawable.enemyshipfire);
        readytoSend = true;
        bonusTread = new BonusTread(getResources());
    }

    public void onDraw(Canvas canvas) {
        try {
            if (multiplayerClient.x != -1) {
                double proctime = SystemClock.uptimeMillis() - time;
                time = SystemClock.uptimeMillis();
                GlobalValues.proctime = proctime;
                gametime += proctime;
                GlobalValues.gametime = gametime;
                int joyM = -1;
                float joyA = 0;
                if (isTouch) {
                    joyM = joystick.JoyMotion(xTouch, yTouch);
                    joyA = joystick.JoyAcc(xTouch, yTouch);
                } else {
                    joyA = 0;
                }
                if (isFireTouch) {
                    ship.setImpulse();
                }

                ship.preRotate(joyM);
                ship.rePosition(joyA);

                FireThread.CheckMultiPlayerClash(ship, enemyShip);
                bonusTread.MultiplayerBonusUpdate();
                bonusTread.BonusClash(ship, enemyShip);
                readytoSend = true;
                canvas.drawBitmap(space, matrix, null);
                ship.Draw(canvas);
                enemyShip.Draw(canvas);
                bonusTread.Draw(canvas);
                canvas.drawRect(0, GlobalValues.scale * 720,
                        GlobalValues.Width, GlobalValues.Height, blackPaint);
                if (!isTouchUp) {
                    joystick.Draw(canvas);
                }

                try {
                    if (!GlobalValues.isBreak && ship.shipHp > 0 && multiplayerClient.conn.isClosed() ) {
                        GlobalValues.uWin = true;
                        Intent intent = new Intent(context, MultiplayerOver.class);
                        multiplayerClient.ThreadDestroy();
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                } catch (Exception e) {
                }
                if (ship.shipHp < 0 || enemyShip.shipHp < 0) {
                    if (enemyShip.shipHp < 0) GlobalValues.uWin = true;
                    Intent intent = new Intent(context, MultiplayerOver.class);
                    multiplayerClient.ThreadDestroy();
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    invalidate();
                }
            } else {
                if (GlobalValues.multiplayerError) {
                    Intent intent = new Intent(context, Multiplayer.class);
                    multiplayerClient.ThreadDestroy();
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                String text = "";
                switch (multiplayerClient.status) {
                    case 'w' : {
                        text = "Try to connect...";
                    } break;
                    case 'o' : {
                        text = "Connected, waiting for opponent...";
                    } break;
                    case 's' : {
                        text = "Wait... The game will start in a few seconds.";
                    } break;
                }
                canvas.drawBitmap(space, matrix, null);
                canvas.drawText(text, 100 * GlobalValues.scale, 100 * GlobalValues.scale, whitePaint);
                canvas.drawRect(0, GlobalValues.scale * 720,
                        GlobalValues.Width, GlobalValues.Height, blackPaint);
                invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            invalidate();
        }
    }

    public void publishData(NetPac val) {
        enemyShip.ship.x = (float)val.x * GlobalValues.scale;
        enemyShip.ship.y = (float)val.y * GlobalValues.scale;
        enemyShip.ship.rotation = (float)val.r;
        enemyShip.shipHp = (float)val.h;
        if (val.f) {
            enemyShip.setImpulse();
        }
        if (val.bx != 0) {
            bonusTread.BonusCreate((int)(val.bx * GlobalValues.scale),
                    (int)(val.by * GlobalValues.scale));
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int type = event.getActionMasked();
        if (type == MotionEvent.ACTION_POINTER_DOWN) {
            isFireTouch = true;
            multiplayerClient.isFire = true;
        }
        if (type == MotionEvent.ACTION_POINTER_UP) {
            isFireTouch = false;
        }
        switch (type) {
            case MotionEvent.ACTION_DOWN: {
                isTouch = true;
                isTouchUp = false;
                xTouch = event.getX();
                yTouch = event.getY();
                joystick = new JoyStick((int)xTouch, (int)yTouch, getResources());
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                isTouch = true;
                isTouchDown = false;
                xTouch = event.getX();
                yTouch = event.getY();
            }
            break;
            case MotionEvent.ACTION_UP: {
                isTouch = true;
                isTouchDown = false;
                isTouchUp = true;
                xTouch = -1;
                yTouch = -1;
            }
        }
        return true;
    }
}
