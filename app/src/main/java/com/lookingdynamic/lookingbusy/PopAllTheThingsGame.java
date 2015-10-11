package com.lookingdynamic.lookingbusy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lookingdynamic.lookingbusy.gameobjects.PoppableObject;
import com.lookingdynamic.lookingbusy.gameplay.GameplayManager;
import com.lookingdynamic.lookingbusy.gameplay.SettingsStorageManager;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

import java.util.Vector;

/**
 * Welcome to The Game!  This SurfaceView is the view for this game.  It also doubles
 * as a controller for now, but that may change as I figure it out!
 * Created by swu on 9/5/2015.
 */
public class PopAllTheThingsGame extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String LOGGER = PopAllTheThingsGame.class.getSimpleName();

    private GameThread thread;
    private Vector<PoppableObject> activePoppableObjects;
    private Vector<PoppableObject> poppedPoppableObjects;
    private SettingsStorageManager settings;
    private GameplayManager gameplay;
    private ThemeManager themes;
    private GameInfoDisplay gameInfo;
    private LookingBusyActivity activity;
    private MediaPlayer mp;

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

        setUpGame();
        startNewGame();

        if (firstRun) {
            activity.showIntroMenu();
        }
    }

    public void setUpGame() {

        settings = new SettingsStorageManager(getContext());
        gameplay = new GameplayManager(settings, getResources());
        themes = new ThemeManager(settings, getResources());
        mp = MediaPlayer.create(activity, R.raw.pop);

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
        clearObjects();
        gameplay.clearCurrentStats();

        thread.onResume();
        setFocusable(true);
    }

    public void clearObjects() {
        activePoppableObjects = new Vector<>();
        poppedPoppableObjects = new Vector<>();
    }

    public void resumeFirstRun() {
        gameInfo.setAlpha(200);
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

    public void clearRandomBotImage() {
        themes.clearRandomBotImage();
    }

    public void pause() {
        if (!thread.isPausedFlagIsSet()) {
            thread.onPause();
            if (gameplay.isGameOver()) {
                activity.showGameOverMenu();
            } else {
                activity.showPauseMenu();
            }
        }
    }

    public void resume() {
        if (thread.isPausedFlagIsSet()) {
            thread.onResume();
            setFocusable(true);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        thread.updateSurfaceHolder(holder);
        gameInfo = new GameInfoDisplay(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
        if (!thread.isRunning()) {
            thread.updateSurfaceHolder(holder);
            gameInfo = new GameInfoDisplay(this);
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
        if (thread.isRunning()) {
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
        gameInfo.draw(canvas);

        if (!gameplay.isGameOver()) {

            synchronized (activePoppableObjects) {
                for (PoppableObject poppableObject : activePoppableObjects) {
                    if (poppableObject == null) {
                        Log.d(LOGGER, "poppableObject is null");
                    } else if (themes == null) {
                        Log.d(LOGGER, "themes is null");
                    } else {
                        poppableObject.draw(themes, canvas, themes.getPainter());
                    }
                }
            }
            synchronized (poppedPoppableObjects) {
                for (PoppableObject poppableObject : poppedPoppableObjects) {
                    poppableObject.draw(themes, canvas, themes.getPainter());
                }
            }
        }
    }

    public void update() {
        if (!gameplay.isGameOver()) {
            synchronized (poppedPoppableObjects) {
                //Handle Dead Balloons
                poppedPoppableObjects.removeAllElements();
            }

            synchronized (activePoppableObjects) {
                //Move Active Balloons, remove popped balloons
                for (int i = 0; i < activePoppableObjects.size(); i++) {
                    PoppableObject poppableObject = activePoppableObjects.get(i);

                    if (poppableObject.isPopped()) {
                        poppedPoppableObjects.add(poppableObject);
                        activePoppableObjects.remove(i);
                    } else if (poppableObject.isOffScreen()) {
                        gameplay.missedObject(poppableObject.getScoreValue());
                        activePoppableObjects.remove(i);
                    } else {
                        poppableObject.move(themes, getWidth(), getHeight());
                    }
                }

                Vector<PoppableObject> toAdd =
                        PoppableObjectFactory.generatePoppableObject(themes,
                                gameplay,
                                activePoppableObjects.size(),
                                getWidth(),
                                getHeight(),
                                themes.getRandomBot() != null);

                if (toAdd != null && !firstRun) {
                    activePoppableObjects.addAll(toAdd);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameplay.isGameOver()) {
            pause();
        } else {
            boolean objectPopped = false;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(LOGGER, "onUp detected!");
                gameInfo.handleOnUp(event.getX(), event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(LOGGER, "onDown detected!");
                synchronized (activePoppableObjects) {
                    for (PoppableObject poppableObject : activePoppableObjects) {
                        if (!poppableObject.isPopped() &&
                                poppableObject.handleTouch(themes, (int) event.getX(), (int) event.getY())) {
                            gameplay.addToScore(poppableObject.getScoreValue());
                            objectPopped = true;
                            break;
                        }
                    }
                }

                gameInfo.handleOnDown(event.getX(), event.getY());
            } else if (gameplay.isBubbleGrid()) {
                synchronized (activePoppableObjects) {
                    for (PoppableObject poppableObject : activePoppableObjects) {
                        if (!poppableObject.isPopped() &&
                                poppableObject.handleTouch(themes, (int) event.getX(), (int) event.getY())) {
                            gameplay.addToScore(poppableObject.getScoreValue());
                            objectPopped = true;
                            break;
                        }
                    }
                }
            }
            if (objectPopped) {
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                        mp = MediaPlayer.create(activity, R.raw.pop);
                    } mp.start();
                } catch(Exception e) { e.printStackTrace(); }
                thread.wakeIfSleeping();
            }
        }
        return true;
    }

}
