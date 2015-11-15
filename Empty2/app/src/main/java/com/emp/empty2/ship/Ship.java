package com.emp.empty2.ship;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.Basis.CrashAnimation;
import com.emp.empty2.Basis.Element;
import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;
import com.emp.empty2.Basis.Track;

/**
 * Created by hp on 16.08.2015.
 */
public class Ship {
    public Element ship, shipmirror, repel, haste;
    public CrashAnimation crashAnimation;
    public EnergyBar ebar;
    public ShipHpBar hpBar;
    public Track shiptrack;
    public ShipFire shipfire;
    public float shipSpeed, acceleration, fireRad, fireSpeed, shipHp, firePower;
    public double repeltime, hastetime, isCrash;

    public Ship (Resources res,float fireRad,float fireSpeed, int id) {
        ship = new Element(640 * GlobalValues.scale, 360 * GlobalValues.scale, res, R.drawable.ship, 0.5f);
        shipmirror = new Element(640, 360, res, R.drawable.ship, 0.5f);
        haste = new Element(640, 360, res, R.drawable.hasteship, 1);
        repel = new Element(640, 360, res, R.drawable.repelship, 1);
        ebar = new EnergyBar(res, (int)(GlobalValues.Width - 140 * GlobalValues.scale),
                (int)(660 * GlobalValues.scale));
        hpBar = new ShipHpBar(res);
        shipHp = 100;
        isCrash = 0;
        repeltime = 0;
        hastetime = 0;
        firePower = 1f;
        crashAnimation = new CrashAnimation(res, R.drawable.shiphurt, 0, 0);
        shiptrack = new Track(res, R.drawable.track, 330, 640, 4);
        shipSpeed = 5f * GlobalValues.scale;
        shipfire = new ShipFire(res, id);
        this.fireRad = fireRad;
        this.fireSpeed = fireSpeed * GlobalValues.scale;
    }

    public void rotate(float alpha) {
        ship.rotate(alpha);
    }

    public void preRotate(int r) {
        if (r == -1) {
            return;
        }
        if (Math.abs((int)ship.rotation - r) > 6) {
            if (Math.abs(ship.rotation - r) > 180) {
                if (r < ship.rotation) {
                    if (hastetime > GlobalValues.gametime) {
                        ship.rotate(8);
                    } else {
                        ship.rotate(6);
                    }
                } else {
                    if (hastetime > GlobalValues.gametime) {
                        ship.rotate(-8);
                    } else {
                        ship.rotate(-6);
                    }
                }
            } else if (r > ship.rotation) {
                if (hastetime > GlobalValues.gametime) {
                    ship.rotate(8);
                } else {
                    ship.rotate(6);
                }
            } else {
                if (hastetime > GlobalValues.gametime) {
                    ship.rotate(-8);
                } else {
                    ship.rotate(-6);
                }
            }
        }
    }

    public void rePosition(float acc) {
        acceleration += (acc - 0.5f) / 40 * (GlobalValues.proctime / 30);
        if (acceleration < 0) acceleration = 0;
        if (acceleration > 1) acceleration = 1;
        if (hastetime > GlobalValues.gametime) {
            ship.rePosition((shipSpeed + 2.5f * (GlobalValues.scale)) * acceleration);
        } else {
            ship.rePosition(shipSpeed * acceleration);
        }
        shiptrack.trackUpdate((float)(ship.x - Math.cos(ship.rotation / 180 * Math.PI) * 27 * GlobalValues.scale),
                (float)(ship.y - Math.sin(ship.rotation / 180 * Math.PI) * 27 * GlobalValues.scale));
        shipfire.ShipFireUpdate();
    }

    public void setImpulse() {
        float dx = ship.x + (float)Math.cos(ship.rotation / 180 * Math.PI) * 35,
                dy = ship.y + (float)Math.sin(ship.rotation / 180 * Math.PI) * 35;
        shipfire.FireImpulse(dx, dy, ship.rotation, fireRad, fireSpeed);
    }


    public void Draw(Canvas canvas) {
        if (ship.x < 0)
            ship.setPosition(GlobalValues.Width + ship.x, ship.y);
        if (ship.x > GlobalValues.Width)
            ship.setPosition(ship.x - GlobalValues.Width, ship.y);
        if (ship.y < 0)
            ship.setPosition(ship.x, ship.y + 720 * GlobalValues.scale);
        if (ship.y > GlobalValues.Height)
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
            shipmirror.setPosition(ship.x, -(720 * GlobalValues.scale - ship.y));
        }

        hpBar.Draw(canvas, shipHp);
        ebar.Draw(canvas, 6 - shipfire.iter, shipfire.refreshtime - GlobalValues.gametime);

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
        shipfire.Draw(canvas);

        if (isCrash > GlobalValues.gametime) {
            crashAnimation.CrashUpdate(ship.x, ship.y, isCrash - GlobalValues.gametime, canvas);
        }
    }
}
