package ht.daze.guesshue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Andrew on 12/28/2016.
 */

public class Tile extends GameObject {

    private Bitmap image;
    private double dya;
    private boolean isFalling;
    private int c;

    public Tile(Bitmap res, int w, int h, int x, int y, int c) {
        image = res;
        width = w;
        height = h;
        this.x = x;
        this.y = y;
        this.c = c;
        isFalling = false;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public int getC(){
        return c;
    }

    public void moveDown(){
        y += height;
    }
}
