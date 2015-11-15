package com.emp.empty2.Basis;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;
import com.emp.empty2.ship.Ship;

import java.util.Random;

/**
 * Created by hp on 17.08.2015.
 */
public class BonusTread {
    Bonus[] bonuses;
    double bonustime;
    int iter;
    boolean[] isBonus;

    public BonusTread (Resources res) {
        bonuses = new Bonus[6];
        bonuses[0] = new Bonus(res, R.drawable.repel, 0, 0);
        bonuses[1] = new Bonus(res, R.drawable.haste, 0, 0);
        bonuses[2] = new Bonus(res, R.drawable.speed, 0, 0);
        bonuses[3] = new Bonus(res, R.drawable.reduce, 0, 0);
        bonuses[4] = new Bonus(res, R.drawable.splash, 0, 0);
        bonuses[5] = new Bonus(res, R.drawable.firebonus, 0, 0);
        iter = 0;
        bonustime = 3000;
        isBonus = new boolean[6];
        for (int i = 0; i < 6; i++) {
            isBonus[i] = false;
        }
    }

    public void BonusUpdate () {
        for (int i = 0; i < 6; i++) {
            if (isBonus[i]) {
                bonuses[i].BonusUptade();
            }
        }
        if (GlobalValues.gametime > bonustime) {
            Random random = new Random((int)(GlobalValues.gametime * GlobalValues.proctime));
            int x = Math.abs(random.nextInt() % (GlobalValues.Width - (int)(200 * GlobalValues.scale)))
                    + (int)(100 * GlobalValues.scale),
                    y = Math.abs(random.nextInt() % ((int)(GlobalValues.scale * 720 - 100))
                            + (int)(100 * GlobalValues.scale));
            bonuses[iter].bonus.setPosition(x, y);
            bonuses[iter].bonusSphere.setPosition(x, y);
            isBonus[iter] = true;
            iter++;
            if (iter == 6) {
                iter = 0;
            }
            bonustime = GlobalValues.gametime + 3000;
        }
    }

    public void MultiplayerBonusUpdate () {
        for (int i = 0; i < 6; i++) {
            if (isBonus[i]) {
                bonuses[i].BonusUptade();
            }
        }
    }

    public void BonusCreate (int x, int y) {
        bonuses[iter].bonus.setPosition(x, y);
        bonuses[iter].bonusSphere.setPosition(x, y);
        isBonus[iter] = true;
        iter++;
        if (iter == 6) {
            iter = 0;
        }
    }

    public void BonusClash (Ship ship) {
        if (isBonus[0]) {
            if (ship.ship.isClash(bonuses[0].bonusSphere)) {
                isBonus[0] = false;
                ship.repeltime = GlobalValues.gametime + 5000;
            }
        }
        if (isBonus[1]) {
            if (ship.ship.isClash(bonuses[1].bonusSphere)) {
                isBonus[1] = false;
                ship.hastetime = GlobalValues.gametime + 5000;
            }
        }
        if (isBonus[2]) {
            if (ship.ship.isClash(bonuses[2].bonusSphere)) {
                isBonus[2] = false;
                ship.shipSpeed += 0.2 * (GlobalValues.scale);
            }
        }
        if (isBonus[3]) {
            if (ship.ship.isClash(bonuses[3].bonusSphere)) {
                isBonus[3] = false;
                ship.shipfire.reduceBonus = true;
            }
        }
        if (isBonus[4]) {
            if (ship.ship.isClash(bonuses[4].bonusSphere)) {
                isBonus[4] = false;
                ship.fireRad += 0.2;
            }
        }
        if (isBonus[5]) {
            if (ship.ship.isClash(bonuses[5].bonusSphere)) {
                isBonus[5] = false;
                ship.fireSpeed += 3 * (GlobalValues.scale);
                ship.firePower += 1;
            }
        }
    }

    public void BonusClash (Ship ship, com.emp.empty2.enemy.EnemyShip enemyShip) {
        if (isBonus[0]) {
            if (ship.ship.isClash(bonuses[0].bonusSphere)) {
                isBonus[0] = false;
                ship.repeltime = GlobalValues.gametime + 5000;
            }
            if (enemyShip.ship.isClash(bonuses[0].bonusSphere)) {
                isBonus[0] = false;
                enemyShip.repeltime = GlobalValues.gametime + 5000;
            }
        }
        if (isBonus[1]) {
            if (ship.ship.isClash(bonuses[1].bonusSphere)) {
                isBonus[1] = false;
                ship.hastetime = GlobalValues.gametime + 5000;
            }
            if (enemyShip.ship.isClash(bonuses[1].bonusSphere)) {
                isBonus[1] = false;
                enemyShip.hastetime = GlobalValues.gametime + 5000;
            }
        }
        if (isBonus[2]) {
            if (ship.ship.isClash(bonuses[2].bonusSphere)) {
                isBonus[2] = false;
                ship.shipSpeed += 0.2 * (GlobalValues.scale);
            }
            if (enemyShip.ship.isClash(bonuses[2].bonusSphere)) {
                isBonus[2] = false;
            }
        }
        if (isBonus[3]) {
            if (ship.ship.isClash(bonuses[3].bonusSphere)) {
                isBonus[3] = false;
                ship.shipfire.reduceBonus = true;
            }
            if (enemyShip.ship.isClash(bonuses[3].bonusSphere)) {
                isBonus[3] = false;
                enemyShip.shipfire.reduceBonus = true;
            }
        }
        if (isBonus[4]) {
            if (ship.ship.isClash(bonuses[4].bonusSphere)) {
                isBonus[4] = false;
                ship.fireRad += 0.2;
            }
            if (enemyShip.ship.isClash(bonuses[4].bonusSphere)) {
                isBonus[4] = false;
                enemyShip.fireRad += 0.2;
            }
        }
        if (isBonus[5]) {
            if (ship.ship.isClash(bonuses[5].bonusSphere)) {
                isBonus[5] = false;
                ship.fireSpeed += 3 * (GlobalValues.scale);
                ship.firePower += 1;
            }
            if (enemyShip.ship.isClash(bonuses[5].bonusSphere)) {
                isBonus[5] = false;
                enemyShip.fireSpeed += 3;
                enemyShip.firePower += 1;
            }
        }
    }

    public void Draw (Canvas canvas) {
        for (int i = 0; i < 6; i++) {
            if (isBonus[i]) {
                bonuses[i].Draw(canvas);
            }
        }
    }
}
