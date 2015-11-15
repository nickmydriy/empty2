package com.emp.empty2.Basis;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.GlobalValues;

import java.util.Random;

/**
 * Created by hp on 16.08.2015.
 */
public class CrashAnimation {
    Element blink;

    public CrashAnimation (Resources res, int bmap, float x, float y) {
        blink = new Element(x, y, res, bmap, 1);
    }

    public void CrashUpdate (float x, float y, double Time, Canvas canvas) {
        for (int i = 0; i < 10; i++)
        {
            Random random = new Random((int)(GlobalValues.gametime % 1000 / 50) * (i + 1));
            int xc = Math.abs(random.nextInt() % (int)(30 * GlobalValues.scale)) - (int)(15 * GlobalValues.scale),
                    yc = Math.abs(random.nextInt() % (int)(30 * GlobalValues.scale)) - (int)(15 * GlobalValues.scale),
                    rc = Math.abs(random.nextInt() % 360);
            blink.setRotation(rc);
            blink.setPosition(x - xc, y + yc);
            blink.Draw(canvas);
        }
    }


}
