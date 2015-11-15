package com.emp.empty2.ship;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.Basis.Fireball;

/**
 * Created by hp on 16.08.2015.
 */
public class ShipFireball {
    public Fireball[] shipfire;
    public float fireRad;

    public ShipFireball (Resources res, float x, float y, float vector, float fireRad, float speed, int id) {
        shipfire = new Fireball[10];
        for (int i = 0; i < 10; i++) {
            float newVector = vector - (i - 4.5f) * fireRad;
            shipfire[i] = new Fireball(res, id, x, y, newVector, speed);
        }
    }

    public void ShipFireballUpdate () {
        for (int i = 0; i < 10; i++) {
            shipfire[i].fireballUpdate();
        }
    }

    public void Draw(Canvas canvas) {
        for (int i = 0; i < 10; i++) {
            shipfire[i].Draw(canvas);
        }
    }
}
