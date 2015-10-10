package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 *
 * This is a Bubble model.  It knows everything possible about the bubble
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

        Log.v(LOGGER, "Bubble Object created at (" + xCoordinate + ", " + yCoordinate + ")");
    }

    /*
     * Bubbles have no "popped" image and no truly  "popped" state. Instead, the draw method
     * determines whether to draw the bubble or not based on a temporarilyPopped state.
     */
    @Override
    public Bitmap getImage(ThemeManager theme) {
        return theme.getBubble();
    }

    /*
     * Bubbles don't actually move, but they do slowly regenerate during the time the other objects
     * would pop, so we override the default method to accommodate this.
     */
    @Override
    public void move(ThemeManager theme, int viewWidth, int viewHeight) {
        if (temporarilyPopped) {
            poppedCountsRemaining--;

            if (poppedCountsRemaining <= 0) {
                temporarilyPopped = false;
            }
        }
    }

    /*
     * The main point of the "temporarilyPopped" state is that the object is still here, but it is
     * not visible to the user for interaction currently.  This means it shouldn't be drawn like
     * it would be for other objects.
     */
    @Override
    public void draw(ThemeManager theme, Canvas canvas, Paint painter) {
        if (!temporarilyPopped) {
            canvas.drawBitmap(getImage(theme), xCoordinate, yCoordinate, painter);
        }
    }

    /*
     * Bubbles don't truly pop, so they are always "touchable". This touch only affects the object
     * when they are truly unpopped.  The in-between "temporarilypopped" state should not be
     * affected.  This is different from most other objects, so an override is needed.
     */
    @Override
    public boolean handleTouch(ThemeManager theme, int eventX, int eventY) {
        boolean justPopped = false;
        if (!temporarilyPopped) {
            justPopped = super.handleTouch(theme, eventX, eventY);
            if (justPopped) {
                temporarilyPopped = true;
                poppedCountsRemaining = timeToStayPopped;
                popped = false;
                Log.v(LOGGER, "Bubble Popped at (" + xCoordinate + ", " + yCoordinate +")");
            }
        }

        return justPopped;
    }

    /*
     * Since bubble objects are never really popped, they exist in an unpopped and temporarily
     * popped states.  They only has a score value when they are unpopped.  This is different from
     * how the other poppable objects behave, so we have to override the default method.
     */
    @Override
    public int getScoreValue() {
        int score = 0;
        if (temporarilyPopped) {
            score = VALUE;
        }

        return score;
    }
}
