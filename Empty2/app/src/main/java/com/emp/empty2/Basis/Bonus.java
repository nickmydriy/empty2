package com.emp.empty2.Basis;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.R;

/**
 * Created by hp on 17.08.2015.
 */
public class Bonus {
    Element bonus, bonusSphere;

    public Bonus (Resources res, int bmap, float x, float y) {
        bonus = new Element(x, y, res, bmap, 1);
        bonusSphere = new Element(x, y, res, R.drawable.bonussphere, 1);
    }

    public void BonusUptade () {
        bonusSphere.rotate(3);
    }

    public void Draw (Canvas canvas) {
        bonus.Draw(canvas);
        bonusSphere.Draw(canvas);
    }
}
