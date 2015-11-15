package com.emp.empty2.Basis;

/**
 * Created by hp on 16.08.2015.
 */
public class List {
    List next;
    List prev;
    float x, y;
    public List(float x, float y, List next, List prev) {
        this.x = x;
        this.y = y;
        this.next = next;
        this.prev = prev;
    }
}
