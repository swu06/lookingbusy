package com.example.lookingdynamic.lookingbusy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.example.lookingdynamic.lookingbusy.gameobjects.PoppableObject;
import com.example.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.example.lookingdynamic.lookingbusy.gameobjects.PoppableObjectFactory;
import com.example.lookingdynamic.lookingbusy.gameplay.SettingsManager;
import com.example.lookingdynamic.lookingbusy.gameplay.ThemeManager;

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
    private TextPaint whiteFont;
    private TextPaint blackOutline;
    private Paint translucentPainter;
    private Paint lessTranslucentPainter;
    private SettingsManager settings;
    private GameplayManager gameplay;
    private ThemeManager themes;
    private LookingBusyActivity activity;

    private boolean gameStopped = false;
    private boolean firstRun = false;

    /*
     * Creating the game is all about creating variables
     * and giving them things to hold.  It's the boss!
     */
    public PopAllTheThingsGame(LookingBusyActivity activity, boolean firstRun) {
        super((Context) activity);
        this.activity = activity;
        this.firstRun = firstRun;


        setUpGame(firstRun);
        startNewGame();

        if(firstRun) {
            translucentPainter.setAlpha(255);
            activity.showIntroMenu();
        }
    }

    public void setUpGame(boolean firstRun) {
        whiteFont = new TextPaint();
        whiteFont.setTextSize(100);
        whiteFont.setTextAlign(Paint.Align.CENTER);
        whiteFont.setColor(Color.WHITE);
        whiteFont.setTypeface(Typeface.DEFAULT_BOLD);
        whiteFont.setAlpha(200);

        blackOutline = new TextPaint();
        blackOutline.setTextSize(100);
        blackOutline.setTextAlign(Paint.Align.CENTER);
        blackOutline.setColor(Color.BLACK);
        blackOutline.setTypeface(Typeface.DEFAULT_BOLD);
        blackOutline.setStyle(Paint.Style.STROKE);
        blackOutline.setStrokeWidth(4);
        blackOutline.setAlpha(200);

        translucentPainter =  new Paint();
        translucentPainter.setAlpha(200);

        settings = new SettingsManager(getContext());
        gameplay = new GameplayManager(settings, getResources());
        themes = new ThemeManager(settings, getResources());

        mDetector = new GestureDetectorCompat(getContext(), this);
        mDetector.setOnDoubleTapListener(this);

        // create the game loop thread
        thread = new GameThread(this);

        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        setZOrderOnTop(true);
    }

    public void startNewGame() {
        gameplay.storeHighScore();
        thread.onPause();

        // create/clear holder for balloons
        activePoppableObjects = new Vector<PoppableObject>();
        poppedPoppableObjects = new Vector<PoppableObject>();

        resume();
        setFocusable(true);
    }

    public void resumeFirstRun() {
        translucentPainter.setAlpha(200);
        firstRun = false;
    }


    public ThemeManager getThemeManager() {
        return themes;
    }

    public GameplayManager getGameplayManager() {
        return gameplay;
    }

    public void setRandomBotImage(String path, Bitmap image) {
        settings.setRandomBotLocation(path);
        themes.setRandomBotImage(image);
    }

    public void pause() {
        if(!thread.isPausedFlagIsSet()) {
            thread.onPause();
            activity.showPauseMenu();
        }
    }

    public void resume() {
        if(thread.isPausedFlagIsSet()) {
            thread.onResume();
            setFocusable(true);
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
        if(!thread.isRunning()) {
            thread.updateSurfaceHolder(holder);
            thread.onStart();
        }

    }

    /*
     * I don't think anything needs done here until the new surface appears
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(LOGGER, "Surface is being destroyed");
        pause();
    }

    public boolean isStopped() {
        return gameStopped;
    }

    public boolean isPaused() {
        return thread.isInPausedState();
    }

    public void stop() {
        gameplay.storeHighScore();
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        if(thread.isRunning()) {
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
        }
        gameStopped = true;
        Log.d(LOGGER, "Thread was shut down cleanly");
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // fills the canvas with black
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawText(gameplay.getDisplayString(), getWidth() / 2, 300, whiteFont);
        canvas.drawText(gameplay.getDisplayString(), getWidth() / 2, 300, blackOutline);

        if(gameplay.isHighScore()) {
            float y = 300 + blackOutline.descent() - blackOutline.ascent();
            canvas.drawText("New High Score!!", getWidth() / 2, y, whiteFont);
            canvas.drawText("New High Score!!", getWidth() / 2, y, blackOutline);
        }

        canvas.drawBitmap(themes.getPauseSign(), 100, 100, translucentPainter);

        synchronized (activePoppableObjects) {
            for (PoppableObject poppableObject : activePoppableObjects) {
                poppableObject.draw(themes, canvas, translucentPainter);
            }
        }
        synchronized (poppedPoppableObjects) {
            for (PoppableObject poppableObject : poppedPoppableObjects) {
                poppableObject.draw(themes, canvas, translucentPainter);
            }
        }

    }

    public void update() {
        synchronized (poppedPoppableObjects) {
            //Handle Dead Balloons
            poppedPoppableObjects.removeAllElements();
        }

        boolean newHighScoreAchieved = false;
        synchronized (activePoppableObjects) {
            //Move Active Balloons, remove popped balloons
            for (int i = 0; i < activePoppableObjects.size(); i++) {
                PoppableObject poppableObject = activePoppableObjects.get(i);

                if (poppableObject.isPopped()) {
                    if(gameplay.addToScore(poppableObject.getScoreValue())) {
                        newHighScoreAchieved = true;
                    }
                    poppedPoppableObjects.add(poppableObject);
                    activePoppableObjects.remove(i);
                } else if (poppableObject.isOffScreen()) {
                    gameplay.addToScore(-1);
                    activePoppableObjects.remove(i);
                } else {
                    poppableObject.move(themes, getWidth(), getHeight());
                }
            }

            PoppableObject toAdd =
                    PoppableObjectFactory.generatePoppableObject(gameplay,
                                                                 getWidth(),
                                                                 getHeight(),
                                                                 themes.getRandomBot() != null);

            if (toAdd != null && !firstRun) {
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
                        poppableObject.handleTouch(themes, (int) event.getX(), (int) event.getY())) {
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
