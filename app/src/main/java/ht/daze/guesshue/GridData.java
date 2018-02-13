package ht.daze.guesshue;

/*
 * Created by Andrew on 2/9/2018.
 */

import java.util.Random;

import ht.daze.guesshue.object.Tile;

public class GridData {

    private int grid_x;
    private int grid_y;
    private Tile[][] tileMap;

    public GridData(int x, int y) {

        grid_x = x;
        grid_y = y;
        tileMap = new Tile[grid_x][grid_y];

        GenerateData();
    }

    private void GenerateData() {

        for (int y = 0; y < grid_y; y++) {
            for (int x = 0; x < grid_x; x++) {
                // TODO: Replace with a seeded random number generation
                //tileMap[x][y] = new Tile(new Random().nextInt(5));
            }
        }
    }

    public void RecursiveDestroy(int x, int y, int c) {

        //System.out.println("Recursive Destroy: " + x + "," + y + "," + c);
        //updateQueue.add(data[x][y]);
        tileMap[x][y] = null;

        if (x > 0)
            if (tileMap[x - 1][y] != null && tileMap[x - 1][y].GetColor() == c)
                RecursiveDestroy(x - 1, y, c);

        if (x < grid_x - 1)
            if (tileMap[x + 1][y] != null && tileMap[x + 1][y].GetColor() == c)
                RecursiveDestroy(x + 1, y, c);

        if (y > 0)
            if (tileMap[x][y - 1] != null && tileMap[x][y - 1].GetColor() == c)
                RecursiveDestroy(x, y - 1, c);

        if (y < grid_y - 1)
            if (tileMap[x][y + 1] != null && tileMap[x][y + 1].GetColor() == c)
                RecursiveDestroy(x, y + 1, c);

    }

    public void SettleData() {

        for (int i = 0; i < grid_x; i++) {
            RecursiveSettle(i, 0);
        }
    }

    private void RecursiveSettle(int x, int y) {

        // If not end of column
        if (y <= grid_y - 1) {

            //RecursiveSettle(x, y + 1);

            Tile t = GetTile(x, y);

            // If current tile not null
            if (t == null) {

                Tile nt = GetNextTile(x, y);

                if (nt != null) {

                    SetTile(x, y, nt);
                    SetTile(nt.x, nt.y, null);
                }
            } else {
                RecursiveSettle(x, y + 1);
            }
        }
    }

    private Tile GetNextTile(int x, int y) {

        if (y >= grid_y - 1)
            return GetTile(x, y);

        Tile output = GetTile(x, y + 1);

        if (output == null)
            output = GetNextTile(x, y + 1);

        return output;
    }

    public Tile[][] GetTile() {
        return tileMap;
    }

    public Tile GetTile(int x, int y) {
        return tileMap[x][y];
    }

    private void SetTile(int x, int y, Tile t) {
        tileMap[x][y] = t;
    }
}
