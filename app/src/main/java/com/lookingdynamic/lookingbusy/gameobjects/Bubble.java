package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 *
 * This is a balloon model.  It knows everything possible about the balloon
 * but has no brains to do anything about it.  The controller (MainGamePanel)
 * does not know any of this information, but uses it to make the world turn.
 * Created by swu on 9/5/2015.
 */
public class Bubble extends PoppableObject {

    private static final String LOGGER = Bubble.class.getSimpleName();
    public static final int VALUE = 1;
    private final int timeToStayPopped;
    private int poppedCountsRemaining;
    private boolean temporarilyPopped;

    public Bubble(int xCoordinate, int yCoordinate, int objectsOnScreen) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        popped = false;
        temporarilyPopped = false;
        offScreen = false;
        poppedCountsRemaining = 0;
        timeToStayPopped = 3 * objectsOnScreen; // Scales for large and small screens
    }

    @Override
    public Bitmap getImage(ThemeManager theme) {
        return theme.getBubble();
    }

    // Bubbles don't move, but they do eventually regenerate
    @Override
    public void move(ThemeManager theme, int viewWidth, int viewHeight) {
        if(temporarilyPopped) {
            poppedCountsRemaining--;

            if(poppedCountsRemaining <= 0) {
                temporarilyPopped = false;
            }
        }
    }

    @Override
    public void draw(ThemeManager theme, Canvas canvas, Paint painter) {
        if(!temporarilyPopped) {
            canvas.drawBitmap(getImage(theme), xCoordinate, yCoordinate, painter);
        }
    }

    @Override
    public boolean handleTouch(ThemeManager theme, int eventX, int eventY) {
        boolean justPopped = false;
        if(!temporarilyPopped) {
            justPopped = super.handleTouch(theme, eventX, eventY);
            if (justPopped) {
                temporarilyPopped = true;
                poppedCountsRemaining = timeToStayPopped;
                popped = false;
            }
        }

        return justPopped;
    }

    public int getScoreValue() {
        int score = 0;
        if(poppedCountsRemaining == timeToStayPopped) {
            score = VALUE;
        }

        return score;
    }
}
