package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 * This abstract class contains the general code for PoppableObjects as well as the
 * required methods the extended classes will have to implement in order to work within
 * the constraints of the game.  This allows the game to interact with one general type
 * instead of having different behaviors based on which sub-type is needed. The only other
 * classes that should have to deal with the individual objects are the Factory and the Theme
 * implementations.
 * Created by swu on 9/6/2015.
 */
public abstract class PoppableObject {

    private static final String LOGGER = PoppableObject.class.getSimpleName();

    /*==============================================================================================
     * Shared variables:  These objects are set and used by the individual implementations as well
     * as the general methods below
     */
    protected int xCoordinate = 0;
    protected int yCoordinate = 0;
    protected int xVelocity = 0;
    protected int yVelocity = 0;
    protected boolean popped = false;
    protected boolean offScreen = false;
    protected int value = 0;
    //==============================================================================================


    /*==============================================================================================
     * General accessors:  These variables are generally set by the individual implementations
     * but they are all accesses the same way.
     */
    public boolean isPopped() {
        return popped;
    }

    public boolean isOffScreen() {
        return offScreen;
    }
    //==============================================================================================


    /*==============================================================================================
     *  Default Implementations: These methods are the general use case for most objects.  They can
     *  (and are) over-written by the sub-types in order to allow for more complex behavior, but
     *  most implementations use these methods
     */
    public void draw(ThemeManager theme, Canvas canvas, Paint painter) {
        canvas.drawBitmap(getImage(theme), xCoordinate, yCoordinate, painter);
    }

    public boolean handleTouch(ThemeManager theme, int eventX, int eventY) {
        Bitmap bitmapImage = getImage(theme);

        if ( eventX >= xCoordinate && eventX <= (xCoordinate + bitmapImage.getWidth())
                && eventY >= yCoordinate && (eventY <= (yCoordinate + bitmapImage.getHeight())) ) {
            popped = true;
            Log.v(LOGGER, "Object Popped at (" + xCoordinate + ", " + yCoordinate + ")");
        }

        return popped;
    }

    public int getScoreValue() {
        return value;
    }
    //==============================================================================================


    /*==============================================================================================
     * Abstract Methods: These methods must be implemented by each individual object. There is no
     * "standard way" to do these methods that could work for multiple objects.
     */
    abstract public Bitmap getImage(ThemeManager theme);
    abstract public void move(ThemeManager theme, int viewWidth, int viewHeight);
    //==============================================================================================
}
