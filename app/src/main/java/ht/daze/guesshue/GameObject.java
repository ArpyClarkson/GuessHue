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

    public void SetX(int value) {
        x = value;
    }

    public void SetY(int value) {
        y = value;
    }

    public void SetDX(int value) {
        dx = value;
    }

    public void SetDY(int value) {
        dy = value;
    }

    public void SetWidth(int value) {
        width = value;
    }

    public void SetHeight(int value) {
        height = value;
    }

    public int GetX() {
        return x;
    }

    public int GetY() {
        return y;
    }

    public int GetDX() {
        return dx;
    }

    public int GetDY() {
        return dy;
    }

    public int GetWidth() {
        return width;
    }

    public int GetHeight() {
        return height;
    }

    public Rect GetRect() {
        return new Rect(x, y, x + width, y + height);
    }
}
