package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.util.Log;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 *
 * This is a Droplet model.  It knows everything possible about the droplet
 * but has no brains to do anything about it.  The controller (MainGamePanel)
 * does not know any of this information, but uses it to make the world turn.
 * Created by swu on 9/5/2015.
 */
public class Droplet extends PoppableObject {

    private static final String LOGGER = Droplet.class.getSimpleName();
    public static final int VALUE = 25;

    public Droplet(int xCoordinate, int yVelocity) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = 0;
        this.yVelocity = yVelocity;
        popped = false;
        offScreen = false;
        value = VALUE;

        Log.v(LOGGER, "Droplet Object created at (" + this.xCoordinate + ", "
                + this.yCoordinate + ")");
    }

    @Override
    public Bitmap getImage(ThemeManager theme) {
        Bitmap bitmapImage = theme.getDroplet();
        if (popped) {
            bitmapImage = theme.getPoppedDroplet();
        }

        return bitmapImage;
    }

    /*
     * Droplets only move vertically. The only thing to watch for is when they fall off of the
     * bottom of the screen
     */
    public void move(ThemeManager theme, int viewWidth, int viewHeight) {
        yCoordinate = yCoordinate + yVelocity;

        // Check to see if the droplet is off the bottom of the screen
        if (yCoordinate - getImage(theme).getHeight() >= viewHeight) {
            offScreen = true;
            Log.v(LOGGER, "Droplet fell offscreen at (" + xCoordinate + ", " + yCoordinate +")");
        }
    }

}
