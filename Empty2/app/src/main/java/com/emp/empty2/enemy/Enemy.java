package com.emp.empty2.enemy;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.Basis.CrashAnimation;
import com.emp.empty2.Basis.Element;
import com.emp.empty2.Basis.Fireball;
import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;
import com.emp.empty2.Basis.Track;
import com.emp.empty2.ship.Ship;

import java.util.Random;

/**
 * Created by hp on 16.08.2015.
 */
public class Enemy {
    public Element enemy;
    public DeathIcon deathicon;
    public Fireball enemyfire;
    public EnemyHpBar hpBar;
    public CrashAnimation crashEnemy;
    public Track enemytrack;
    public double fireReload, isCrash;
    public float enemySpeed, enemyHealth, enemyacc;
    Resources res;
    public boolean isDead;

    public Enemy (Resources res, int x, int y) {
        enemy = new Element(x, y, res, R.drawable.enemy, 0.5f);
        enemytrack = new Track(res, R.drawable.etrack, 330, 640, 4);
        crashEnemy = new CrashAnimation(res, R.drawable.enemyh3, x, y);
        hpBar = new EnemyHpBar(res);
        enemySpeed = 3.5f * GlobalValues.scale;
        fireReload = 0;
        isCrash = 0;
        enemyacc = 0;
        isDead = false;
        enemyHealth = 100;
        deathicon = new DeathIcon(res);
        this.res = res;
    }

    public void rotate(float alpha) {
        enemy.rotate(alpha);
    }

    public void rePosition(float value) {
        enemyacc += 0.02 * (GlobalValues.proctime / 30);
        if (enemyacc > 1) enemyacc = 1;
        enemy.rePosition(value * enemyacc);
        if (!isDead) {
            enemytrack.trackUpdate((float) (enemy.x - Math.cos(enemy.rotation / 180 * Math.PI) * 27 * GlobalValues.scale),
                    (float) (enemy.y - Math.sin(enemy.rotation / 180 * Math.PI) * 27 * GlobalValues.scale));
        }
    }

    public void HurtMove(double gametime) {
        enemyacc -= 0.06 * (GlobalValues.proctime / 30);
        if (enemyacc < 0) enemyacc = 0;
        rePosition(enemySpeed);
    }

    public void Chase(Ship ship, int rand) {
        enemySpeed += 0.03f * ((float)GlobalValues.proctime / 1000f) * GlobalValues.scale;
        enemySpeed = Math.min(enemySpeed, ship.shipSpeed - 1 * GlobalValues.scale);
        if (enemyfire != null) {
            enemyfire.fireballSpeed += 0.06f  * ((float) GlobalValues.proctime / 1000f) * GlobalValues.scale;
        }
        if (enemyHealth < 0) {
            isCrash = GlobalValues.gametime + 7000;
            enemyHealth = 100;
            isDead = true;
        }
        if (fireReload > GlobalValues.gametime) {
            enemyfire.fireballUpdate();
        }
        if (isCrash > GlobalValues.gametime) {
            HurtMove(GlobalValues.gametime);
            if (isDead) {
                deathicon.DeathIconUpdate();
            }
            return;
        }
        isDead = false;
        if (fireReload < GlobalValues.gametime) {
            float r = getRadi(ship);
            if (Math.abs(r - enemy.rotation) < 15) {
                fireReload = GlobalValues.gametime + 2500;
                enemyfire = new Fireball(res, R.drawable.enemyfire, enemy.x, enemy.y, enemy.rotation, 15 * GlobalValues.scale);
            }
        }


        Random random = new Random((int)(GlobalValues.gametime / 300) + rand);
        int chance = Math.abs(random.nextInt()) % 100;
        int procent = 25;
        if (chance < procent) {
            boolean LeftRight = random.nextBoolean();
            if (LeftRight) {
                enemy.rotate(5);
            } else {
                enemy.rotate(-5);
            }
            rePosition(enemySpeed);
        } else {
            intChase(ship);
        }
    }

    public float getRadi (Ship ship) {
        float sx = ship.ship.x, sy = ship.ship.y, ex = enemy.x, ey = enemy.y;
        float xr = sx - ex, yr = sy - ey;
        float r = (float)(Math.atan((float)(yr / xr)) * 180 / Math.PI);
        if (xr < 0 && yr < 0)
            r -= 180;
        else if (xr < 0)
            r -= 180;
        if (r < 0)
            r += 360;
        return r;
    }

    public void intChase(Ship ship) {
        float r = getRadi(ship);
        if (Math.abs(enemy.rotation - r) > 12)
            if (Math.abs(enemy.rotation - r) > 180)
                if (r < enemy.rotation)
                    enemy.rotate(5);
                else
                    enemy.rotate(-5);
            else
            if (r > enemy.rotation)
                enemy.rotate(5);
            else
                enemy.rotate(-5);
        rePosition(enemySpeed);
    }

    public void Draw (Canvas canvas) {
        if (isCrash > GlobalValues.gametime) {
            crashEnemy.CrashUpdate(enemy.x, enemy.y, isCrash - GlobalValues.gametime, canvas);
        }
        if (fireReload > GlobalValues.gametime) {
            enemyfire.Draw(canvas);
        }

        if (!isDead) {
            hpBar.Draw(canvas, enemy.x, enemy.y, enemyHealth);
            enemytrack.Draw(canvas);
            enemy.Draw(canvas);
        } else {
            deathicon.Draw(canvas, enemy.x, enemy.y, enemy);
        }
    }
}
