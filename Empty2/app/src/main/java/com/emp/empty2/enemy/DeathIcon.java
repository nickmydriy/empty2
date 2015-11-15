package com.emp.empty2.enemy;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.emp.empty2.Basis.Element;
import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;

/**
 * Created by hp on 17.08.2015.
 */
public class DeathIcon {
    Element icon;
    Paint trp;
    public float scaleR;

    public DeathIcon (Resources res) {
        icon = new Element(0, 0, res, R.drawable.dethicon, 1);
        trp = new Paint();
        scaleR = -0.02f;
    }

    public void DeathIconUpdate () {
        if (scaleR > 0 && icon.scale > 1f) {
            scaleR = -scaleR;
        }
        if (scaleR < 0 && icon.scale < 0.6f) {
            scaleR = -scaleR;
        }
        icon.scale((float)(scaleR * GlobalValues.proctime / 30));
        icon.rotate(5);
    }

    public void Draw (Canvas canvas, float x, float y, Element enemy) {
        icon.setPosition(x, y);
        icon.Draw(canvas);
        trp.setAlpha(100);
        enemy.Draw(canvas, trp);
    }
}
