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
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.view.MotionEvent;

import com.emp.empty2.Basis.BonusTread;
import com.emp.empty2.Basis.FireThread;
import com.emp.empty2.Basis.JoyStick;
import com.emp.empty2.activities.GameOver;
import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;
import com.emp.empty2.enemy.EnemyArmy;
import com.emp.empty2.ship.Ship;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by hp on 20.08.2015.
 */
public class ComonView extends View {
    public Ship ship;
    public BitmapFactory.Options BmOp;
    public Context context;
    public BufferedWriter bufferedWriter;
    public BonusTread bonusTread;
    public EnemyArmy enemyarmy;
    public Matrix matrix;
    public Bitmap space;
    public JoyStick joystick;
    public boolean isTouch, isFireTouch, isTouchDown, isTouchUp;
    public float xTouch, yTouch;
    public boolean running;
    public float proctime, time, gametime;
    public Paint redPaint, blackPaint;
    public float forMidFps;
    public ComonView (Context context) {
        super(context);
        this.context = context;
        time = SystemClock.uptimeMillis();
        GlobalValues.Width = getResources().getDisplayMetrics().widthPixels;
        GlobalValues.Height = getResources().getDisplayMetrics().heightPixels;
        ship = new Ship(context.getResources(), 0.5f, 25, R.drawable.fire);
        bonusTread = new BonusTread(getResources());
        enemyarmy = new EnemyArmy(getResources(), 5);
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
        redPaint = new Paint();
        redPaint.setColor(Color.WHITE);
        redPaint.setTextScaleX(2);
        redPaint.setTextSize(25 * GlobalValues.scale);
        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        GlobalValues.damageDeal = 0;
        File sdPath = Environment.getExternalStorageDirectory();
        if (sdPath != null) {
            sdPath = new File(sdPath.getAbsolutePath() + "/" + "AppLog95");
            sdPath.mkdirs();
            File sdFile = new File(sdPath, "log.txt");
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(sdFile));
                bufferedWriter.write("its CommonView");
                bufferedWriter.newLine();
            } catch (IOException e) {
                return;
            }
        }
        running = true;

        gametime = 0;
        forMidFps = 16;
    }

    public void onDraw(Canvas canvas) {
        try {
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
            bonusTread.BonusUpdate();
            bonusTread.BonusClash(ship);
            enemyarmy.ArmyStep(ship);
            FireThread.checkClash(enemyarmy, ship);

            ship.preRotate(joyM);
            ship.rePosition(joyA);

            canvas.drawBitmap(space, matrix, null);
            bonusTread.Draw(canvas);
            ship.Draw(canvas);
            enemyarmy.Draw(canvas);
            canvas.drawRect(0, GlobalValues.scale * 720,
                    GlobalValues.Width, GlobalValues.Height, blackPaint);
            String text = String.valueOf(GlobalValues.gametime / 1000);

            if (!isTouchUp) {
                joystick.Draw(canvas);
            }
            canvas.drawText(text, 30 * GlobalValues.scale, 30 * GlobalValues.scale, redPaint);

            if (ship.shipHp == 0) {
                running = false;
                Intent intent = new Intent(context, GameOver.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            } else {
                invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int type = event.getActionMasked();
        if (type == MotionEvent.ACTION_POINTER_DOWN) {
            isFireTouch = true;
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
