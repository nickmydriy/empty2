package com.emp.empty2.Basis;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.R;

/**
 * Created by hp on 16.08.2015.
 */
public class JoyStick {
    Element stick, stickcenter;
    float x, y;
    boolean isTouch;

    public JoyStick (int x, int y, Resources res) {
        stick = new Element(x, y, res, R.drawable.stick, 1);
        stick.setScale(2f);
        stickcenter = new Element(x, y, res, R.drawable.stickcenter, 1);
        this.x = x;
        this.y = y;
    }

    public float JoyAcc (float dx, float dy) {
        if (dx < 0 || dy < 0) {
            return 0;
        }
        float rx = x - dx, ry = y - dy;
        if (Math.sqrt(rx * rx + ry * ry) < 60) {
            return (float)Math.sqrt(rx * rx + ry * ry) / 60;
        } else {
            return 1;
        }
    }
    public int JoyMotion (float dx, float dy) {
        float rx = x - dx, ry = y - dy;
        if ((Math.sqrt(rx * rx + ry * ry) < 150 || isTouch) && dx > 0 && dy > 0) {
            isTouch = true;
            stick.setPosition(dx, dy);
            float r = (float)(Math.atan((float)(ry / rx)) * 180 / Math.PI);
            if (rx < 0 && ry < 0) {
                r -= 180;
            } else if (rx < 0) {
                r -= 180;
            }
            if (r < 0) {
                r += 360;
            }
            r += 180;
            if (r > 360) {
                r -= 360;
            }
            return (int)r;
        } else {
            stick.setPosition(x, y);
            isTouch = false;
        }
        return -1;
    }

    public void Draw (Canvas canvas) {
        stick.Draw(canvas);
        stickcenter.Draw(canvas);
    }
}
