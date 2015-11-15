/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emp.empty2.Basis;

import java.io.Serializable;

/**
 *
 * @author hp
 */
public class NetPac implements Serializable {
    public short x;
    public short y;
    public short r;
    public boolean f;
    public short h;
    public short bx;
    public short by;
    public NetPac(double x, double y, double r, double h, int bx, int by, boolean f) {
        this.x = (short)x;
        this.y = (short)y;
        this.r = (short)r;
        this.f = f;
        this.h = (short)h;
        this.bx = (short)bx;
        this.by = (short)by;
    }
}

