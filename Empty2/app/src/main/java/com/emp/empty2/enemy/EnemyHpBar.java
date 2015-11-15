package com.emp.empty2.enemy;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.emp.empty2.GlobalValues;
import com.emp.empty2.R;

/**
 * Created by hp on 17.08.2015.
 */
public class EnemyHpBar {
    Bitmap hpbar;

    public EnemyHpBar (Resources res) {
        BitmapFactory.Options BmOp = new BitmapFactory.Options();
        BmOp.inTargetDensity = (int)(GlobalValues.scale * 160);
        hpbar = BitmapFactory.decodeResource(res, R.drawable.healthbar, BmOp);
    }

    public void Draw (Canvas canvas, float x, float y, float hp) {
        Rect src = new Rect(0, 0, (int)(hp / 100 * 60 * GlobalValues.scale), (int)(6 * GlobalValues.scale));
        RectF dst = new RectF(x - (int)(30 * GlobalValues.scale), y - (int)(30 * GlobalValues.scale),
                x - (int)(30 * GlobalValues.scale)
                        + (int)(hp / 100 * 60 * GlobalValues.scale),y - (int)(24 * GlobalValues.scale));
        canvas.drawBitmap(hpbar, src, dst, null);
    }
}
