package com.emp.empty2.Basis;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.GlobalValues;

/**
 * Created by hp on 16.08.2015.
 */
public class Fireball {
    public Element fireball;
    public float fireballSpeed, x, y;

    public Fireball (Resources res, int bmap, float x, float y, float vector,float speed) {
        fireball = new Element(x, y, res, bmap, 1);
        fireball.setRotation(vector);
        this.x = (float)Math.cos(vector / 180 * Math.PI);
        this.y = (float)Math.sin(vector / 180 * Math.PI);
        fireballSpeed = speed;
    }

    public void fireballUpdate() {
        if (fireball.y > -100 && fireball.x > -100 && fireball.x < GlobalValues.Width + 100 && fireball.y < GlobalValues.Height + 100) {
            fireball.setPosition((float)(fireball.x + x * fireballSpeed * (GlobalValues.proctime / 30)),
                    (float)(fireball.y + y * fireballSpeed * (GlobalValues.proctime / 30)));
        }
    }

    public void Draw (Canvas canvas) {
        float dx = fireball.x, dy = fireball.y;
        if (fireball.y > - 40 && fireball.x > - 40
                && fireball.x < GlobalValues.Width + 40 && fireball.y < GlobalValues.Height + 40) {
            fireball.setPosition(dx, dy);
            fireball.Draw(canvas);
        }
    }
}
