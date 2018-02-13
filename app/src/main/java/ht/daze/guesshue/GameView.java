package ht.daze.guesshue;

/*
 * Created by Andrew on 2/9/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import ht.daze.guesshue.object.Background;
import ht.daze.guesshue.object.Tile;

public class GameView extends SurfaceView implements Runnable {

    // TODO: Build hashID table
    // TODO: Clean out old code, streamline
    // TODO: Menus/additional activities

    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private boolean running;
    private Canvas canvas;
    private Paint paint;
    private long framesPerSecond;
    private long secondsThisFrame;
    private long secondsFrameCap;

    public static final int GAME_WIDTH = 1080;
    public static final int GAME_HEIGHT = 1920;
    public static final int GAME_GRID_X = 9;
    public static final int GAME_GRID_Y = 13;
    public static final int GAME_TILE_WIDTH = 108;
    public static final int GAME_TILE_HEIGHT = 108;
    private Bitmap tile_blue;
    private Bitmap tile_green;
    private Bitmap tile_orange;
    private Bitmap tile_purple;
    private Bitmap tile_red;
    private GridView gridView;

    private Bitmap spritesheet;
    private Background background;

    public GameView(Context context) {

        super(context);

        surfaceHolder = getHolder();
        paint = new Paint();

        secondsFrameCap = 50;
        gridView = new GridView(context,54, 36, 0);

        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg1));
        spritesheet = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet);
        tile_blue = BitmapFactory.decodeResource(getResources(), R.drawable.tile_blue);
        tile_green = BitmapFactory.decodeResource(getResources(), R.drawable.tile_green);
        tile_orange = BitmapFactory.decodeResource(getResources(), R.drawable.tile_orange);
        tile_purple = BitmapFactory.decodeResource(getResources(), R.drawable.tile_purple);
        tile_red = BitmapFactory.decodeResource(getResources(), R.drawable.tile_red);

        setFocusable(true);
        running = true;
    }

    @Override
    public void run() {
        while(running) {

            long frameStartTime = System.currentTimeMillis();

            update();
            draw();

            secondsThisFrame = System.currentTimeMillis() - frameStartTime;
            if (secondsThisFrame > secondsFrameCap)
                framesPerSecond = 1000 / secondsThisFrame;
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {

        background.update();
    }

    public void draw() {

        if (surfaceHolder.getSurface().isValid()) {

            // Create scale factor of device size / game size
            final float scaleFactorX = (getWidth() / (GAME_WIDTH * 1.0f));
            final float scaleFactorY = (getHeight() / (GAME_HEIGHT * 1.0f));

            canvas = surfaceHolder.lockCanvas();

            // Save the canvas state and scale it; keeping aspect ratio
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorX);

            background.draw(canvas);
            gridView.draw(canvas, spritesheet, paint);

            //ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(this, R.color.blue), PorterDuff.Mode.SRC_IN);
            //paint.setColorFilter(filter);

            //canvas.drawBitmap(spritesheet, 0, 0, paint);

            // TODO: Send tile draw logic to GridView
            /*for (int y = 0; y < GAME_GRID_Y; y++) {
                for ( int x = 0; x < GAME_GRID_X; x++) {
                    if (gridView.GetTile(x, y) != null) {

                        Bitmap i;

                        switch (gridView.GetTile(x, y).GetColor()) {
                            case 0:
                                i = tile_blue;
                                break;
                            case 1:
                                i = tile_green;
                                break;
                            case 2:
                                i = tile_orange;
                                break;
                            case 3:
                                i = tile_purple;
                                break;
                            case 4:
                                i = tile_red;
                                break;
                            default:
                                i = tile_blue;
                        }

                        canvas.drawBitmap(i, (54 + (GAME_TILE_WIDTH * x)) , (1440 - (GAME_TILE_HEIGHT * y)), null);
                    }
                }
            }*/

            // Restore original canvas size
            canvas.restoreToCount(savedState);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                Rect target = gridView.GetRect();

                //event.getX() > 54.0f && event.getX() < 1026.0f && event.getY() > 132.0f && event.getY() < 1536.0f

                // TODO: Send tile press logic directly to GridView
                if (target.contains((int)event.getX(), (int)event.getY())) {

                    gridView.DestroyAt(event.getX(), event.getY());

                    //float ex = event.getX();
                    //float ey = event.getY();
                    //int ix, iy;

                    //ex -= gridView.GetX();
                    //ey -= gridView.GetY();

                    //ix = (int) Math.floor(ex) / GAME_TILE_WIDTH;
                    //iy = (int) Math.floor(ey) / GAME_TILE_HEIGHT;

                    //iy = Math.abs(iy - (GAME_GRID_Y - 1));

                    //System.out.println("Down: " + ix + "," + iy);

                    //if (gridView.GetTile(ix, iy) != null) {
                        //gridView.RecursiveDestroy(ix, iy, gridView.GetTile(ix, iy).GetColor());
                    //}

                    //gridView.SettleData();
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    // TODO: Send recursive destroy logic to GridData
    public void recursiveDestroy(int x, int y, int c) {

        //System.out.println("Recursive Destroy: " + x + "," + y + "," + c);
        //updateQueue.add(data[x][y]);
        //data[x][y] = null;

        //if (x > 0)
            //if (data[x - 1][y] != null && data[x - 1][y].GetColor() == c)
                //recursiveDestroy(x - 1, y, c);

        //if (x < GAME_COLUMNS - 1)
            //if (data[x + 1][y] != null && data[x + 1][y].GetColor() == c)
                //recursiveDestroy(x + 1, y, c);

        //if (y > 0)
            //if (data[x][y - 1] != null && data[x][y - 1].GetColor() == c)
                //recursiveDestroy(x, y - 1, c);

        //if (y < GAME_ROWS - 1)
            //if (data[x][y + 1] != null && data[x][y + 1].GetColor() == c)
                //recursiveDestroy(x, y + 1, c);

    }

    // TODO: Send tile creation logic to GridData
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

        //data[x][y] = new Tile(img, GAME_TILE_WIDTH, GAME_TILE_HEIGHT, (54 + (GAME_TILE_WIDTH * x)), (1440 - (GAME_TILE_HEIGHT * y)), c);
    }
}
