package com.emp.empty2.enemy;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.Basis.CrashAnimation;
import com.emp.empty2.Basis.Element;
import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;
import com.emp.empty2.Basis.Track;
import com.emp.empty2.ship.ShipFire;

/**
 * Created by hp on 12.11.2015.
 */
public class EnemyShip {
    public Element ship, shipmirror, repel, haste;
    public EnemyHpBar hpBar;
    public CrashAnimation crashAnimation;
    public Track shiptrack;
    public ShipFire shipfire;
    public float shipSpeed, acceleration, fireRad, fireSpeed, shipHp, firePower;
    public double repeltime, hastetime, isCrash;

    public EnemyShip (Resources res,float fireRad,float fireSpeed, int id) {
        ship = new Element(640 * GlobalValues.scale, 360 * GlobalValues.scale, res, R.drawable.enemy, 0.5f);
        shipmirror = new Element(640, 360, res, R.drawable.enemy, 0.5f);
        haste = new Element(640, 360, res, R.drawable.enemyhasteship, 1);
        repel = new Element(640, 360, res, R.drawable.repelenemyship, 1);
        hpBar = new EnemyHpBar(res);
        shipHp = 100;
        isCrash = 0;
        repeltime = 0;
        hastetime = 0;
        firePower = 1f;
        crashAnimation = new CrashAnimation(res, R.drawable.enemyh3, 0, 0);
        shiptrack = new Track(res, R.drawable.etrack, 330, 640, 4);
        shipSpeed = 5f;
        shipfire = new ShipFire(res, id);
        this.fireRad = fireRad;
        this.fireSpeed = fireSpeed * (GlobalValues.scale);
    }

    public void setImpulse() {
        float dx = ship.x + (float)Math.cos(ship.rotation / 180 * Math.PI) * 35,
                dy = ship.y + (float)Math.sin(ship.rotation / 180 * Math.PI) * 35;
        shipfire.FireImpulse(dx, dy, ship.rotation, fireRad, fireSpeed);
    }


    public void Draw(Canvas canvas) {
        shiptrack.trackUpdate((float)(ship.x - Math.cos(ship.rotation / 180 * Math.PI) * 27 * GlobalValues.scale),
                (float)(ship.y - Math.sin(ship.rotation / 180 * Math.PI) * 27 * GlobalValues.scale));
        shipfire.ShipFireUpdate();
        if (ship.x < 0)
            ship.setPosition(GlobalValues.Width + ship.x, ship.y);
        if (ship.x > GlobalValues.Width)
            ship.setPosition(ship.x - GlobalValues.Width, ship.y);
        if (ship.y < 0)
            ship.setPosition(ship.x, ship.y + 720 * GlobalValues.scale);
        if (ship.y > 720 * GlobalValues.scale)
            ship.setPosition(ship.x, ship.y - 720 * GlobalValues.scale);
        boolean boardCHK = false;
        if (ship.x < 30 * GlobalValues.scale)
        {
            boardCHK = true;
            shipmirror.setRotation(ship.rotation);
            shipmirror.setPosition(GlobalValues.Width + ship.x, ship.y);
        }
        if (ship.x > GlobalValues.Width - 30 * GlobalValues.scale)
        {
            boardCHK = true;
            shipmirror.setRotation(ship.rotation);
            shipmirror.setPosition(-(GlobalValues.Width - ship.x), ship.y);
        }
        if (ship.y < 30 * GlobalValues.scale)
        {
            boardCHK = true;
            shipmirror.setRotation(ship.rotation);
            shipmirror.setPosition(ship.x, 720 * GlobalValues.scale + ship.y);
        }
        if (ship.y > 720 * GlobalValues.scale - 30 * GlobalValues.scale)
        {
            boardCHK = true;
            shipmirror.setRotation(ship.rotation);
            shipmirror.setPosition(ship.x, -(GlobalValues.Height - ship.y));
        }

        shiptrack.Draw(canvas);
        if (repeltime > GlobalValues.gametime) {
            repel.setPosition(ship.x, ship.y);
            repel.setRotation(ship.rotation);
            repel.Draw(canvas);
        }

        if (boardCHK) {
            shipmirror.Draw(canvas);
        }
        ship.Draw(canvas);
        if (hastetime > GlobalValues.gametime) {
            haste.setPosition(ship.x, ship.y);
            haste.setRotation(ship.rotation);
            haste.Draw(canvas);
        }
        hpBar.Draw(canvas, ship.x, ship.y, shipHp);

        shipfire.Draw(canvas);

        if (isCrash > GlobalValues.gametime) {
            crashAnimation.CrashUpdate(ship.x, ship.y, isCrash - GlobalValues.gametime, canvas);
        }
    }
}