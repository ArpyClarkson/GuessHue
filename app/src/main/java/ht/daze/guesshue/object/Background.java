package ht.daze.guesshue.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ht.daze.guesshue.GameObject;

/**
 * Created by Andrew on 12/28/2016.
 */

public class Background extends GameObject {

    private Bitmap image;
    private int x, y;

    public Background(Bitmap res) {

        image = res;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(image, x, y, null);
    }
}
