package com.emp.empty2.enemy;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.ship.Ship;


/**
 * Created by hp on 16.08.2015.
 */
public class EnemyArmy {
    public Enemy[] army;
    public int count;

    public EnemyArmy (Resources res, int count) {
        army = new Enemy[count];
        this.count = count;
        for (int i = 0; i < count; i++) {
            army[i] = new Enemy(res, (int)(60 * GlobalValues.scale), (int)(i * 100 * GlobalValues.scale + 50 * GlobalValues.scale));
        }
    }

    public void ArmyStep (Ship ship) {
        for (int i = 0; i < count; i++) {
            army[i].Chase(ship, i + 1);
        }
    }

    public void Draw (Canvas canvas) {
        for (int i = 0; i < count; i++) {
            army[i].Draw(canvas);
        }
    }
}
