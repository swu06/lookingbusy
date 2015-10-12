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
    protected boolean temporarilyPopped;
    protected int poppedCountsRemaining;
    protected final int timeToStayPopped;

    public Bubble(int xCoordinate, int yCoordinate, int objectsOnScreen) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        offScreen = false;
        popped = false;
        value = VALUE;
        temporarilyPopped = false;
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
     * when they are truly unpopped.  The in-between "temporarily popped" state should not be
     * affected.  This is different from most other objects, so an override is needed.
     *
     * This also means that users can rack up a huge score if they keep touching the bubble's
     * location, so we set the value to nothing except just after the bubble is touched.
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
                value = VALUE;
                Log.v(LOGGER, "Bubble Popped at (" + xCoordinate + ", " + yCoordinate +")");
            }
        } else {
            value = 0;
        }

        return justPopped;
    }
}
