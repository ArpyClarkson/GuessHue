package ht.daze.guesshue;

import android.graphics.Rect;

/**
 * Created by Andrew on 12/28/2016.
 */

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;

    public void setX(int value) {
        x = value;
    }

    public void setY(int value) {
        y = value;
    }

    public void setDX(int value) {
        dx = value;
    }

    public void setDY(int value) {
        dy = value;
    }

    public void setWidth(int value) {
        width = value;
    }

    public void setHeight(int value) {
        height = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDX() {
        return dx;
    }

    public int getDY() {
        return dy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rect getRect() {
        return new Rect(x, y, x + width, y + height);
    }
}
