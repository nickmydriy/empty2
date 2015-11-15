package com.emp.empty2.Basis;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.emp.empty2.GlobalValues;

/**
 * Created by hp on 16.08.2015.
 */
public class Element {
    public float x, y, rotation, scale, Width, Height, ClashConst;
    public Bitmap texture;
    public Matrix matrix;
    public Paint paint;

    public Element (float x, float y, Resources resourse, int bmap, float ClashConst) {
        BitmapFactory.Options BmOp = new BitmapFactory.Options();
        BmOp.inTargetDensity = (int)(GlobalValues.scale * 160);
        texture = BitmapFactory.decodeResource(resourse, bmap, BmOp);
        this.x = x;
        this.y = y;
        rotation = 0;
        scale = 1;
        Width = texture.getWidth();
        Height = texture.getHeight();
        matrix = new Matrix();
        this.ClashConst = ClashConst;
        paint = new Paint();
        paint.setAntiAlias(GlobalValues.antiAliasing);
    }
    public void Draw (Canvas canvas) {
        float dx = Width / 2, dy = Height / 2;
        matrix.reset();
        matrix.postScale(scale, scale);
        matrix.postTranslate(x - dx, y - dy);
        matrix.postRotate(rotation, x, y);
        canvas.drawBitmap(texture, matrix, paint);
    }

    public void Draw (Canvas canvas, Paint paint) {
        float dx = Width / 2, dy = Height / 2;
        matrix.reset();
        matrix.postScale(scale, scale);
        matrix.postTranslate(x - dx, y - dy);
        matrix.postRotate(rotation, x, y);
        canvas.drawBitmap(texture, matrix, paint);
    }

    public void setRotation (float Alpha) {
        rotation = Alpha;
        if (rotation > 360) {
            rotation -= 360;
        }
        if (rotation < 0) {
            rotation += 360;
        }
    }

    public void rotate (float Alpha) {
        rotation += Alpha * (GlobalValues.proctime / 30);
        if (rotation > 360) {
            rotation -= 360;
        }
        if (rotation < 0) {
            rotation += 360;
        }
    }

    public void rePosition (float Distanse) {
        x += Math.cos(rotation / 180 * Math.PI) * Distanse * (GlobalValues.proctime / 30);
        y += Math.sin(rotation / 180 * Math.PI) * Distanse * (GlobalValues.proctime / 30);
    }

    public void setPosition (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setScale (float scale) {
        Width *= scale / this.scale;
        Height *= scale / this.scale;
        this.scale = scale;
    }

    public boolean isClash (Element element) {
        float dx = x - element.x, dy = y - element.y;
        float r = (float)Math.atan(x / y), dthis = 0, dthat = 0;
        float rthis = rotation - r, rthat = element.rotation - r;
        dthis = ClashConst * (Width / 2) + (float)Math.cos(rthis) * (1 - ClashConst);
        dthat = element.ClashConst * (element.Width / 2) + (float)Math.cos(rthat) * (1 - element.ClashConst);
        if (Math.sqrt(dx * dx + dy * dy) < dthis + dthat) {
            return true;
        } else {
            return false;
        }
    }

    public void scale(float value) {
        setScale(scale + value);
    }
}
