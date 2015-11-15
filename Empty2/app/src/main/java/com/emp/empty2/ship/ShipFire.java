package com.emp.empty2.ship;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.GlobalValues;

/**
 * Created by hp on 16.08.2015.
 */
public class ShipFire {
    public ShipFireball[] shipfire;
    public double reloadtime, refreshtime;
    public int iter;
    public int id;
    Resources res;
    public boolean reduceBonus, isReduceBonus;

    public ShipFire (Resources res, int id) {
        shipfire = new ShipFireball[6];
        this.id = id;
        reloadtime = 0;
        refreshtime = 0;
        this.res = res;
        reduceBonus = false;
        isReduceBonus = false;
    }

    public void FireImpulse (float x, float y, float vector, float fireRad, float speed) {
        if (!reduceBonus) {
            if (refreshtime < GlobalValues.gametime) {
                if (iter == 6) {
                    iter = 0;
                }
                if (reloadtime < GlobalValues.gametime) {
                    isReduceBonus = false;
                    shipfire[iter] = new ShipFireball(res, x, y, vector, fireRad, speed, id);
                    iter++;
                    reloadtime = GlobalValues.gametime + 200;
                    if (iter == 6) {
                        refreshtime = GlobalValues.gametime + 2100;
                    }
                }
            }
        } else {
            if (refreshtime < GlobalValues.gametime) {
                if (reloadtime < GlobalValues.gametime) {
                    reduceBonus = false;
                    isReduceBonus = true;
                    refreshtime = GlobalValues.gametime + 2100;
                    for (int i = 0; i < 6; i++) {
                        shipfire[i] = new ShipFireball(res, x, y, i * 60, 6, speed, id);
                    }
                    iter = 6;
                }
            }
        }
    }

    public void ShipFireUpdate () {
        for (int i = 0; i < iter; i++) {
            shipfire[i].ShipFireballUpdate();
        }
    }

    public void Draw (Canvas canvas) {
        for (int i = 0; i < iter; i++) {
            shipfire[i].Draw(canvas);
        }
    }
}
