package com.emp.empty2.ship;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;

/**
 * Created by hp on 17.08.2015.
 */
public class ShipHpBar {
    public Bitmap healthbar;

    public ShipHpBar (Resources res) {
        BitmapFactory.Options BmOp = new BitmapFactory.Options();
        BmOp.inTargetDensity = (int)(GlobalValues.scale * 160);
        healthbar = BitmapFactory.decodeResource(res, R.drawable.shiphp, BmOp);
    }

    public void Draw(Canvas canvas, float hp) {
        Rect src = new Rect(0, 0, (int)(hp / 100 * 120 * GlobalValues.scale), (int)(10 * GlobalValues.scale)),
                dst = new Rect(GlobalValues.Width - (int)(140 * GlobalValues.scale),
                        (int)(720 *GlobalValues.scale - (int)(75 * GlobalValues.scale)),
                        GlobalValues.Width - (int)(140 * GlobalValues.scale) + (int)(hp / 100 * 120 * GlobalValues.scale),
                        (int)(720 * GlobalValues.scale - 65 * GlobalValues.scale));
        canvas.drawBitmap(healthbar, src, dst, null);
    }
}
