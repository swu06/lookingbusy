package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.util.Log;


import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 *
 * This is a balloon model.  It knows everything possible about the balloon
 * but has no brains to do anything about it.  The controller (MainGamePanel)
 * does not know any of this information, but uses it to make the world turn.
 * Created by swu on 9/5/2015.
 */
public class Balloon extends PoppableObject {

    private static final String LOGGER = Balloon.class.getSimpleName();
    public static final int VALUE = 10;
    protected int whichBalloon;

    public Balloon(int xCoordinate, int yCoordinate, int speed, int whichBalloon) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.yVelocity = speed;
        popped = false;
        offScreen = false;
        this.whichBalloon = whichBalloon;
        value = VALUE;

        Log.v(LOGGER, "Balloon Object created at (" + this.xCoordinate + ", " + this.yCoordinate
                + "), using Balloon #" + this.whichBalloon);
    }

    @Override
    public Bitmap getImage(ThemeManager theme) {
        Bitmap bitmapImage = theme.getBalloon(whichBalloon);
        if (popped) {
            bitmapImage = theme.getPoppedBalloon(whichBalloon);
        }

        return bitmapImage;
    }

    /*
     * Balloons only move vertically, so their logic is the simplest.  The only catch
     * is that they are initially created "offscreen", so their first move is a shift
     * of the image up enough that they are now visible.
     */
    public void move(ThemeManager theme, int viewWidth, int viewHeight) {
        int imageHeight = getImage(theme).getHeight();

        yCoordinate = yCoordinate + yVelocity;

        // Shift up the balloons that start off the screen when they first move
        if (yCoordinate > viewHeight - imageHeight) {
            yCoordinate = viewHeight - imageHeight;
        }

        // Check to see if the object has floated off the screen
        if (yCoordinate + imageHeight <= 0) {
            offScreen = true;
            Log.v(LOGGER, "Balloon floated offscreen at (" + xCoordinate + ", " + yCoordinate +")");
        }

    }
}
