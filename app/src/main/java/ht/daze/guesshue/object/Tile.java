package ht.daze.guesshue.object;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import ht.daze.guesshue.GameObject;
import ht.daze.guesshue.R;

/*
 * Created by Andrew on 12/28/2016.
 */

public class Tile extends GameObject {

    private int color;

    public Tile(int _posX, int _posY, int _color, int _width, int _height) {
        SetX(_posX);
        SetY(_posY);
        SetColor(_color);
        SetWidth(_width);
        SetHeight(_height);
    }

    public void update() {

    }

    public void draw(Canvas canvas) {

    }

    public int GetColor() {
        return color;
    }

    private void SetColor(int i) {

        if (i < 0 || i > 5)
            i = 0;

        color = i;
    }

    public void SetPos(int _posX, int _posY) {
        SetX(_posX);
        SetY(_posY);
    }
}
