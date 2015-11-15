package com.emp.empty2.ship;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;

/**
 * Created by hp on 17.08.2015.
 */
public class EnergyBar {
    Bitmap energybar;
    int x, y;
    Paint cyanPaint;

    public EnergyBar (Resources res, int x, int y) {
        BitmapFactory.Options BmOp = new BitmapFactory.Options();
        BmOp.inTargetDensity = (int)(GlobalValues.scale * 160);
        energybar = BitmapFactory.decodeResource(res, R.drawable.energy, BmOp);
        this.x = x;
        this.y = y;
        cyanPaint = new Paint();
        cyanPaint.setColor(Color.CYAN);
        cyanPaint.setTextScaleX(0.6f);
        cyanPaint.setTextSize(40 * GlobalValues.scale);
        cyanPaint.setAlpha(80);
    }

    public void Draw (Canvas canvas, int count, double time) {
        if (time > 0) {
            canvas.drawText("RELOAD>>>", x, y + 30 * GlobalValues.scale, cyanPaint);
        } else if (count == 0) {
            count = 6;
        }
        Rect src = new Rect(0, 0, (int)(count * 20 * GlobalValues.scale),
                (int)(40 * GlobalValues.scale)), dst = new Rect(x, y, (int)(count * 20 * GlobalValues.scale) + x,
                y + (int)(40 * GlobalValues.scale));
        canvas.drawBitmap(energybar, src, dst, null);
    }
}
