package com.example.lookingdynamic.lookingbusy;

import android.content.Context;
import android.content.res.TypedArray;
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
import com.example.lookingdynamic.lookingbusy.gameplay.GameplayMode;

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
    private Paint lessTranslucentPainter;
    private MenuManager menuManager;
    private GameTheme themes[];
    private int currentTheme;

    /*
     * Creating the game is all about creating variables
     * and giving them things to hold.  It's the boss!
     */
    public PopAllTheThingsGame(Context context) {
        super(context);

        setZOrderOnTop(true);
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        mDetector = new GestureDetectorCompat(getContext(), this);
        mDetector.setOnDoubleTapListener(this);

        // create the game loop thread
        thread = new GameThread(getHolder(), this);

        setUpFormatters();
        startNewGame();
        // make the GamePanel focusable so it can handle events
        setFocusable(true);

    }

    public void setUpFormatters() {
        whiteFont = new TextPaint();
        whiteFont.setTextSize(100);
        whiteFont.setTextAlign(Paint.Align.CENTER);
        whiteFont.setColor(Color.WHITE);
        whiteFont.setTypeface(Typeface.DEFAULT_BOLD);
        whiteFont.setAlpha(150);

        blackOutline = new TextPaint();
        blackOutline.setTextSize(100);
        blackOutline.setTextAlign(Paint.Align.CENTER);
        blackOutline.setColor(Color.BLACK);
        blackOutline.setTypeface(Typeface.DEFAULT_BOLD);
        blackOutline.setStyle(Paint.Style.STROKE);
        blackOutline.setStrokeWidth(2);
        blackOutline.setAlpha(150);

        translucentPainter =  new Paint();
        translucentPainter.setAlpha(200);

        lessTranslucentPainter =  new Paint();
        lessTranslucentPainter.setAlpha(240);

        menuManager = new MenuManager(getContext());

        // Load all available themes
        loadAvailableThemes();
        currentTheme = 0;
    }

    public void startNewGame() {
        // create holder for balloons
        activePoppableObjects = new Vector<PoppableObject>();
        poppedPoppableObjects = new Vector<PoppableObject>();

        stats = new GameStatistics(getResources());

        resume();
    }

    public void loadAvailableThemes(){
        TypedArray availableThemesArray = getResources().obtainTypedArray(R.array.available_game_themes);

        themes = new GameTheme[availableThemesArray.length()];

        for(int i=0; i < availableThemesArray.length(); i++) {
            themes[i] = new GameTheme(getResources(), getResources().getXml(availableThemesArray.getResourceId(i,-1)));
        }
    }

    public GameTheme[] getThemes() {
        return themes;
    }

    public int getCurrentThemeID() {
        return currentTheme;
    }

    public int getCurrentThemeIcon() {
        return themes[currentTheme].getIconImageId();
    }

    public int getCurrentGameplayModeIcon() {
        return stats.getIconImageId();
    }

    public GameTheme getCurrentTheme() {
        return themes[currentTheme];
    }

    public int getTheme() {
        return currentTheme;
    }

    /*
     * This method switches between themes.  eWhenever a new theme is selected, the old theme
     * should be cleaned up to save memory
     */
    public void setTheme(int theme) {
        if(theme >= 0 && theme < themes.length && theme != currentTheme)  {
            themes[currentTheme].unloadImages();
            currentTheme = theme;
        }

    }
    public void setGameplayMode(int mode) {
        stats.setCurrentMode(mode);
    }

    public int getCurrentGamePlayModeID() {
        return stats.getCurrentGameplayMode();
    }
    public GameplayMode[] getGameplayModes() {
        return stats.getGameplayModes();
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
    protected void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawText(stats.toString(), getWidth() / 2, 300, whiteFont);
        canvas.drawText(stats.toString(), getWidth() / 2, 300, blackOutline);

        canvas.drawBitmap(themes[currentTheme].getPauseSign(), 100, 100, translucentPainter);

        synchronized(activePoppableObjects) {
            for (PoppableObject poppableObject : activePoppableObjects) {
                poppableObject.draw(themes[currentTheme], canvas, translucentPainter);
            }
        }
        synchronized (poppedPoppableObjects) {
            for (PoppableObject poppableObject : poppedPoppableObjects) {
                poppableObject.draw(themes[currentTheme], canvas,translucentPainter);
            }
        }
    }

    public void update() {
        synchronized (poppedPoppableObjects) {
            //Handle Dead Balloons
            poppedPoppableObjects.removeAllElements();
        }

        synchronized (activePoppableObjects) {
            //Move Active Balloons, remove popped balloons
            for (int i = 0; i < activePoppableObjects.size(); i++) {
                PoppableObject poppableObject = activePoppableObjects.get(i);

                if (poppableObject.isPopped()) {
                    stats.addToScore(poppableObject.getScoreValue());
                    poppedPoppableObjects.add(poppableObject);
                    activePoppableObjects.remove(i);
                } else if (poppableObject.isOffScreen()) {
                    stats.addToScore(-1);
                    activePoppableObjects.remove(i);
                } else {
                    poppableObject.move(themes[currentTheme], getWidth(), getHeight());
                }
            }

            PoppableObject toAdd = PoppableObjectFactory.generatePoppableObject(stats.getCurrentLevel(), getWidth(), getHeight());
            if (toAdd != null) {
                activePoppableObjects.add(toAdd);
            }
        }

    }

    /*
     * onDown events can trigger balloons to pop
     */
    @Override
    public boolean onDown (MotionEvent event){
        Log.d(LOGGER, "onDown detected!");
        boolean objectPopped = false;
        synchronized (activePoppableObjects) {
            for (PoppableObject poppableObject : activePoppableObjects) {
                if (!poppableObject.isPopped() &&
                        poppableObject.handleTouch(themes[currentTheme], (int) event.getX(), (int) event.getY())) {
                    objectPopped = true;
                    break;
                }
            }
        }

        if(objectPopped) {
            thread.wakeIfSleeping();
        }

        return true;
    }


    /*
     * Double-Tap on the Pause Icon is the trigger to open the PauseMenu
     */
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
    public void onShowPress(MotionEvent e) { }

    @Override
    public boolean onSingleTapUp(MotionEvent e) { return false; }

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


    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(LOGGER, "Double-tap event detected!");
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(LOGGER, "onSingleTapConfirmed detected!");
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
}
