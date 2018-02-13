package ht.daze.guesshue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ht.daze.guesshue.object.Background;
import ht.daze.guesshue.object.Tile;

/**
 * Created by Andrew on 12/28/2016.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 1920;
    public static final int GAME_COLUMNS = 9;
    public static final int GAME_ROWS = 13;
    public static final int GAME_TILE_WIDTH = 108;
    public static final int GAME_TILE_HEIGHT = 108;
    private GameThread gameThread;
    private Background background;
    private Bitmap tile_blue;
    private Bitmap tile_green;
    private Bitmap tile_orange;
    private Bitmap tile_purple;
    private Bitmap tile_red;
    private Tile[][] tiles;
    private List<Tile> updateQueue;


    public GamePanel(Context context) {

        // Call SurfaceView's constructor
        super(context);

        // Add the callback to surface holder to intercept events
        getHolder().addCallback(this);

        // Instantiate gameThread
        gameThread = new GameThread(getHolder(), this);

        // Make GamePanel focusable to handle events
        setFocusable(true);
    }

    public void update() {

        //tile.update();
        background.update();
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        // Create scale factor of device size / game size
        final float scaleFactorX = (getWidth() / (GAME_WIDTH * 1.0f));
        final float scaleFactorY = (getHeight() / (GAME_HEIGHT * 1.0f));

        if (canvas != null) {

            // Save the canvas state and scale it; keeping aspect ratio
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorX);

            // Draw background image
            background.draw(canvas);

            for (Tile ts[] : tiles) {
                for (Tile t : ts) {
                    if (t != null)
                        t.draw(canvas);
                }
            }
            //tile_blue.draw(canvas);

            // Restore original canvas size
            canvas.restoreToCount(savedState);
        }
    }

    // If SimpleGameEngine Activity is paused/stopped
    // shutdown our gameThread.
    public void pause() {
        gameThread.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            //Log.e("Error:", "joining gameThread");
        }

    }

    // If SimpleGameEngine Activity is started then
    // start our gameThread.
    public void resume() {
        gameThread.setRunning(true);
        //gameThread = new GameThread(getHolder(), this);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        // Try to close gameThread
        boolean retry = true;
        while (retry) {
            try {

                // Close gameThread
                gameThread.setRunning(false);
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg1));

        tile_blue = BitmapFactory.decodeResource(getResources(), R.drawable.tile_blue);
        tile_green = BitmapFactory.decodeResource(getResources(), R.drawable.tile_green);
        tile_orange = BitmapFactory.decodeResource(getResources(), R.drawable.tile_orange);
        tile_purple = BitmapFactory.decodeResource(getResources(), R.drawable.tile_purple);
        tile_red = BitmapFactory.decodeResource(getResources(), R.drawable.tile_red);

        tiles = new Tile[GAME_COLUMNS][GAME_ROWS];
        updateQueue = new ArrayList<Tile>();

        for (int x = 0; x < GAME_COLUMNS; x++) {
            for (int y = 0; y < GAME_ROWS; y++) {
                createTile(x, y);
            }
        }

        // Start game loop
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() > 54.0f && event.getX() < 1026.0f && event.getY() > 132.0f && event.getY() < 1536.0f) {

                float ex = event.getX();
                float ey = event.getY();
                int ix, iy;

                ex -= 54.0f;
                ey -= 132.0f;

                ix = (int) Math.floor(ex) / GAME_TILE_WIDTH;
                iy = (int) Math.floor(ey) / GAME_TILE_HEIGHT;

                iy = Math.abs(iy - (GAME_ROWS - 1));

                //System.out.println("Down: " + ix + "," + iy);

                if (tiles[ix][iy] != null) {
                    recursiveDestroy(ix, iy, tiles[ix][iy].GetColor());
                }

                for (Tile t : updateQueue) {

                }
            }
        }

        return super.onTouchEvent(event);
    }

    public void recursiveDestroy(int x, int y, int c) {

        //System.out.println("Recursive Destroy: " + x + "," + y + "," + c);
        updateQueue.add(tiles[x][y]);
        tiles[x][y] = null;

        if (x > 0)
            if (tiles[x - 1][y] != null && tiles[x - 1][y].GetColor() == c)
                recursiveDestroy(x - 1, y, c);

        if (x < GAME_COLUMNS - 1)
            if (tiles[x + 1][y] != null && tiles[x + 1][y].GetColor() == c)
                recursiveDestroy(x + 1, y, c);

        if (y > 0)
            if (tiles[x][y - 1] != null && tiles[x][y - 1].GetColor() == c)
                recursiveDestroy(x, y - 1, c);

        if (y < GAME_ROWS - 1)
            if (tiles[x][y + 1] != null && tiles[x][y + 1].GetColor() == c)
                recursiveDestroy(x, y + 1, c);

    }

    public void createTile(int x, int y) {

        Random rnd = new Random();
        Bitmap img;
        int c;

        switch (rnd.nextInt(5)) {
            case 0:
                img = tile_blue;
                c = 0;
                break;
            case 1:
                img = tile_green;
                c = 1;
                break;
            case 2:
                img = tile_orange;
                c = 2;
                break;
            case 3:
                img = tile_purple;
                c = 3;
                break;
            case 4:
                img = tile_red;
                c = 4;
                break;
            default:
                img = tile_blue;
                c = 0;
        }

        //tiles[x][y] = new Tile(GAME_TILE_WIDTH, GAME_TILE_HEIGHT, c);
    }
}
