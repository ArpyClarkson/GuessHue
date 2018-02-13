package ht.daze.guesshue;

/*
 * Created by Andrew on 2/9/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ht.daze.guesshue.object.Tile;

public class GridView extends GameObject {
    // TODO: Tile physics/animation
    // TODO: Tile hashID logic

    private int gridX;
    private int gridY;
    private int tileX;
    private int tileY;
    private long seed;
    private Tile[][] tileMap;
    private List<Tile> tiles;
    private List<Tile> tileBuffer;
    private ColorFilter filter;

    public GridView(Context context, int _posX, int _posY, long _seed) {
        seed = _seed;
        gridX = context.getResources().getInteger(R.integer.grid_size_x);
        gridY = context.getResources().getInteger(R.integer.grid_size_y);
        tileX = context.getResources().getInteger(R.integer.tile_size_x);
        tileY = context.getResources().getInteger(R.integer.tile_size_y);
        tileMap = new Tile[gridX][gridY];
        tiles = new ArrayList<>();
        tileBuffer = new ArrayList<>();

        SetX(_posX);
        SetY(_posY);
        SetWidth(_posX + (tileX * gridX));
        SetHeight(_posY + (tileY * gridY));

        GenerateTiles(seed);
    }

    public void draw(Canvas canvas, Bitmap spritesheet, Paint paint) {

        // TODO: Fix all of this shit

        for (int y = 0; y < gridY; y++) {
            for (int x = 0; x < gridX; x++) {

                if (GetTile(x, y) != null) {
                    switch (GetTile(x, y).GetColor()) {
                        case 0:
                            //paint.setARGB(255, 22, 117, 211);
                            filter = new PorterDuffColorFilter(Color.rgb(22, 117, 211), PorterDuff.Mode.OVERLAY);
                            break;
                        case 1:
                            //paint.setARGB(255, 54, 211, 22);
                            filter = new PorterDuffColorFilter(Color.rgb(54, 211, 22), PorterDuff.Mode.OVERLAY);
                            break;
                        case 2:
                            //paint.setARGB(255, 255, 108, 3);
                            filter = new PorterDuffColorFilter(Color.rgb(255, 108, 3), PorterDuff.Mode.OVERLAY);
                            break;
                        case 3:
                            //paint.setARGB(255, 103, 40, 166);
                            filter = new PorterDuffColorFilter(Color.rgb(103, 40, 166), PorterDuff.Mode.OVERLAY);
                            break;
                        case 4:
                            //paint.setARGB(255, 211, 22, 22);
                            filter = new PorterDuffColorFilter(Color.rgb(211, 22, 22), PorterDuff.Mode.OVERLAY);
                            break;
                        default:
                            //paint.setARGB(255, 127, 127, 127);
                            filter = new PorterDuffColorFilter(Color.rgb(127, 127, 127), PorterDuff.Mode.OVERLAY);
                            break;
                    }

                    paint.setColorFilter(filter);
                    canvas.drawBitmap(spritesheet,
                            new Rect(0, 0, tileX, tileY),
                            new Rect(GetX() + (tileX * x),
                                    (GetY() + (tileY * gridY)) - (tileY * (y + 1)),
                                    tileX + (GetX() + (tileX * x)),
                                    (GetY() + (tileY * gridY)) - (tileY * y)),
                            paint);
                    //new Rect(GetX() + (tileX * x), GetY() + (tileY * y), tileX + (GetX() + (tileX * x)), tileY + (GetY() + (tileY * y))),
                    //canvas.drawBitmap(spritesheet, 54 + (tileX * x), 36 + (tileY * y), paint);
                }
            }
        }
    }

    private void GenerateTiles(long _seed) {

        Random rand = new Random(_seed);

        for (int y = 0; y < gridY; y++) {
            for (int x = 0; x < gridX; x++) {

                boolean b = SetTile(x, y, new Tile(x, y, rand.nextInt(5), tileX, tileY));
                if (b)
                    tiles.add(GetTile(x, y));
            }
        }
    }

    public Tile GetTile(int _posX, int _posY) {

        Tile _out;

        if (_posX >= 0 && _posX < gridX && _posY >= 0 && _posY < gridY) {
            _out = tileMap[_posX][_posY];
        } else {
            _out = null;
        }

        return _out;
    }

    private boolean SetTile(int _posX, int _posY, Tile _tile) {

        Boolean _out;

        if (_posX >= 0 && _posX < gridX && _posY >= 0 && _posY < gridY) {
            tileMap[_posX][_posY] = _tile;
            _out = true;
        } else {
            _out = false;
        }

        return _out;
    }

    public void DestroyAt(float _posX, float _posY) {

        // TODO: Move this code to somewhere nice
        float fx, fy;
        int ix, iy;

        fx = _posX;
        fy = _posY;

        fx -= GetX();
        fy -= GetY();

        ix = Math.round(fx) / tileX;
        iy = Math.round(fy) / tileY;

        // Inverts vertical tile number
        iy = Math.abs(iy - (gridY - 1));

        // Clear the tileBuffer before we begin, just in case
        tileBuffer.clear();

        // Populate tileBuffer with connecting tiles
        if (GetTile(ix, iy) != null)
            RecursiveDestroy(ix, iy);

        // Loop through tileBuffer and remove tiles
        if (tileBuffer.size() > 1) {
            for (Tile t : tileBuffer) {
                if (tiles.contains(t))
                    tiles.remove(t);
                SetTile(t.GetX(), t.GetY(), null);
            }
        }

        // Clear tileBuffer again to make sure it's empty
        tileBuffer.clear();

        Stack();
    }

    private void RecursiveDestroy(int _posX, int _posY) {

        Tile t = GetTile(_posX, _posY);

        // Add the tile to the tileBuffer if it doesn't exist, if it does return
        if (!tileBuffer.contains(t)) {
            tileBuffer.add(t);
        } else {
            return;
        }

        Tile u = Up(t);
        Tile d = Down(t);
        Tile l = Left(t);
        Tile r = Right(t);

        if (u != null && u.GetColor() == t.GetColor())
            RecursiveDestroy(u.GetX(), u.GetY());
        if (d != null && d.GetColor() == t.GetColor())
            RecursiveDestroy(d.GetX(), d.GetY());
        if (l != null && l.GetColor() == t.GetColor())
            RecursiveDestroy(l.GetX(), l.GetY());
        if (r != null && r.GetColor() == t.GetColor())
            RecursiveDestroy(r.GetX(), r.GetY());
    }

    private void Stack() {
        for (int x = 0; x < gridX; x++) {

            // TODO: Add horizontal stacking
            RecursiveStack(x, 0);
        }
    }

    private void RecursiveStack(int _posX, int _posY) {

        // TODO: Add horizontal stacking
        if (_posY >= gridY)
            return;

        Tile t = GetTile(_posX, _posY);

        if (t == null && _posY < gridY - 1) {
            // Null and not last tile
            Tile nt = NextTile(_posX, _posY);

            if (nt == null) {
                return;
            } else {
                ShiftTile(_posX, _posY, nt);
                RecursiveStack(_posX, _posY + 1);
            }
        } else if (t == null && _posY == gridY - 1) {
            // Null and last tile
            return;
        } else {
            // Not null
            RecursiveStack(_posX, _posY + 1);
        }
    }

    private Tile NextTile(int _posX, int _posY) {
        if (_posY >= gridY - 1)
            return null;

        Tile t = GetTile(_posX, _posY + 1);

        if (t == null) {
            return NextTile(_posX, _posY + 1);
        } else {
            return t;
        }
    }

    private void ShiftTile(int _ax, int _ay, Tile _b) {

        int bx = _b.GetX();
        int by = _b.GetY();

        _b.SetPos(_ax, _ay);

        SetTile(_ax, _ay, _b);
        SetTile(bx, by, null);
    }

    private Tile Up(Tile _tile) {
        return GetTile(_tile.GetX(), _tile.GetY() - 1);
    }

    private Tile Down(Tile _tile) {
        return GetTile(_tile.GetX(), _tile.GetY() + 1);
    }

    private Tile Left(Tile _tile) {
        return GetTile(_tile.GetX() - 1, _tile.GetY());
    }

    private Tile Right(Tile _tile) {
        return GetTile(_tile.GetX() + 1, _tile.GetY());
    }
}
