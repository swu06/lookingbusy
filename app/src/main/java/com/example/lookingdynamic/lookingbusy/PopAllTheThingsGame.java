package com.example.lookingdynamic.lookingbusy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.lookingdynamic.lookingbusy.gameobjects.PoppableObject;
import com.example.lookingdynamic.lookingbusy.gameplay.GameStatistics;
import com.example.lookingdynamic.lookingbusy.gameobjects.PoppableObjectFactory;
import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;

import java.util.Vector;

/**
 * Welcome to The Game!  This SurfaceView is the view for this game.  It also doubles
 * as a controller for now, but that may change as I figure it out!
 * Created by swu on 9/5/2015.
 */
public class PopAllTheThingsGame extends SurfaceView implements
        SurfaceHolder.Callback,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String LOGGER = PopAllTheThingsGame.class.getSimpleName();

    private GameThread thread;
    private GestureDetectorCompat mDetector;
    private Vector<PoppableObject> activePoppableObjects;
    private Vector<PoppableObject> poppedPoppableObjects;
    private GameStatistics stats;
    private TextPaint whiteFont;
    private TextPaint blackOutline;
    private Paint translucentPainter;
    private MenuManager menuManager;
    private GameTheme themes[];
    private int currentTheme;
    private boolean justStarted;

    /*
     * Creating the game is all about creating variables
     * and giving them things to hold.  It's the boss!
     */
    public PopAllTheThingsGame(Context context) {
        super(context);

        setZOrderOnTop(true);
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        // create holder for balloons
        activePoppableObjects = new Vector<PoppableObject>();
        poppedPoppableObjects = new Vector<PoppableObject>();
        stats = new GameStatistics(GameStatistics.CHALLANGING_MODE);

        whiteFont = new TextPaint();
        whiteFont.setTextSize(200);
        whiteFont.setTextAlign(Paint.Align.CENTER);
        whiteFont.setColor(Color.WHITE);
        whiteFont.setTypeface(Typeface.DEFAULT_BOLD);
        whiteFont.setAlpha(90);

        blackOutline = new TextPaint();
        blackOutline.setTextSize(200);
        blackOutline.setTextAlign(Paint.Align.CENTER);
        blackOutline.setColor(Color.BLACK);
        blackOutline.setTypeface(Typeface.DEFAULT_BOLD);
        blackOutline.setStyle(Paint.Style.STROKE);
        blackOutline.setStrokeWidth(2);
        blackOutline.setAlpha(90);

        translucentPainter =  new Paint();
        translucentPainter.setAlpha(90);

        // create the game loop thread
        thread = new GameThread(getHolder(), this);

        menuManager = new MenuManager(getContext());

        // Load all available themes
        loadAvailableThemes();
        currentTheme = 0;
        justStarted = true;

        // make the GamePanel focusable so it can handle events
        setFocusable(true);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mDetector.setOnDoubleTapListener(this);
    }

    public void loadAvailableThemes(){
        String[] availableThemesArray = getResources().getStringArray(R.array.available_game_themes);
        themes = new GameTheme[availableThemesArray.length];

        for(int i=0; i < availableThemesArray.length; i++) {
            themes[i] = new GameTheme(getResources(), availableThemesArray[i]);
        }
    }

    public GameTheme[] getThemes() {
        return themes;
    }

    public int getCurrentThemeID() {
        return currentTheme;
    }

    public GameTheme getCurrentTheme() {
        return themes[currentTheme];
    }


    public void pause() {

        thread.onPause();
        menuManager.showPauseMenu(this);
    }

    public void resume() {
        if(thread.isPausedFlagIsSet()) {
            thread.onResume();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        thread.updateSurfaceHolder(holder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
        thread.onStart();
        resume();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(LOGGER, "Surface is being destroyed");
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        thread.onStop();
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        Log.d(LOGGER, "Thread was shut down cleanly");
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawText(stats.toString(), getWidth() / 2, 500, whiteFont);
        canvas.drawText(stats.toString(), getWidth() / 2, 500, blackOutline);

        canvas.drawBitmap(themes[currentTheme].getPauseSign(), 100, 100, translucentPainter);

        for(PoppableObject poppableObject : activePoppableObjects) {
            poppableObject.draw(themes[currentTheme], canvas);
        }
        for(PoppableObject poppableObject : poppedPoppableObjects) {
            poppableObject.draw(themes[currentTheme], canvas);
        }
    }

    public synchronized void update() {
        //Handle Dead Balloons
        poppedPoppableObjects.removeAllElements();

        //Move Active Balloons, remove popped balloons
        for(int i=0; i < activePoppableObjects.size(); i++) {
            PoppableObject poppableObject = activePoppableObjects.get(i);

            if(poppableObject.isPopped()) {
                stats.addToScore(poppableObject.getScoreValue());
                poppedPoppableObjects.add(poppableObject);
                activePoppableObjects.remove(i);
            } else if(poppableObject.isOffScreen()) {
                stats.addToScore(-1);
                activePoppableObjects.remove(i);
            } else {
                poppableObject.move(themes[currentTheme], getWidth(), getHeight());
            }
        }

        PoppableObject toAdd = PoppableObjectFactory.generatePoppableObject(themes[currentTheme], stats.getLevel(), getWidth(), getHeight(), justStarted);
        if(toAdd != null) {
            activePoppableObjects.add(toAdd);
        }

        justStarted = false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(LOGGER, "onSingleTapConfirmed detected!");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        if(event.getX() >= 100 && event.getY() >= 100 &&
                event.getX() <= 150 && event.getY() <= 200) {
            pause();
        }

        Log.d(LOGGER, "Double-tap detected!");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(LOGGER, "Double-tap event detected!");
        return true;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(LOGGER, "onDown detected!");
        for(PoppableObject poppableObject : activePoppableObjects) {
            if (poppableObject.handleTouch(themes[currentTheme], (int) event.getX(), (int) event.getY())) {
                thread.wakeIfSleeping();
                break;
            }
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(LOGGER, "onSingleTapUp detected!");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(LOGGER, "onFling detected!");
        return false;
    }


    public int getTheme() {
        return currentTheme;
    }

    /*
     * This method switches between themes.  Whenever a new theme is selected, the old theme
     * should be cleaned up to save memory
     */
    public void setTheme(int theme) {
        if(theme >= 0 && theme < themes.length && theme != currentTheme)  {
            themes[currentTheme].unloadImages();
            currentTheme = theme;
        }

    }
    public void setGamePlayMode(int mode) {
        stats.setMode(mode);
    }

    public int getGamePlayMode() {
        return stats.getMode();
    }
}
