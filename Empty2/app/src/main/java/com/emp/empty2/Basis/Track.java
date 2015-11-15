package com.emp.empty2.Basis;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.emp.empty2.GlobalValues;

/**
 * Created by hp on 16.08.2015.
 */
public class Track {
    Element track;
    List firsttrack, lasttrack;
    int length;
    boolean isTracked;
    float tracktime;
    public Track (Resources res, int bmap, float x, float y, int length) {
        track = new Element(x, y, res, bmap, 1);
        track.setScale(0.5f);
        lasttrack = new List(x, y, null, null);
        firsttrack = new List(x, y, lasttrack, null);
        lasttrack.prev = firsttrack;
        for (int i = 0; i < length; i++) {
            List newitem = new List(x, y, firsttrack, null);
            firsttrack.prev = newitem;
            firsttrack = newitem;
        }
        this.length = length;
        tracktime = 0;
    }

    public void trackUpdate (float x, float y) {
        if (tracktime < GlobalValues.gametime) {
            List newitem = new List(x, y, firsttrack, null);
            firsttrack.prev = newitem;
            firsttrack = newitem;
            lasttrack = lasttrack.prev;
            lasttrack.next = null;
            tracktime += 32;
        }
    }

    public void Draw (Canvas canvas) {
        List thistrack = firsttrack;
        track.setScale(1);
        float scale = 1;
        for (int i = 0; i < 4; i ++) {
            if (Math.abs(thistrack.x - thistrack.next.x) < 100
                    && Math.abs(thistrack.y - thistrack.next.y) < 100) {
                for (int j = 0; j < 4; j++) {
                    track.setPosition(thistrack.x + (thistrack.next.x - thistrack.x) * ((float) j / 4),
                            thistrack.y + (thistrack.next.y - thistrack.y) * ((float) j / 4));
                    track.Draw(canvas);
                    scale -= 0.05;
                    track.setScale(scale);
                }
                thistrack = thistrack.next;
            }
        }
    }
}
