package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;

import com.lookingdynamic.lookingbusy.gameplay.GameTheme;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 *
 * This is a balloon model.  It knows everything possible about the balloon
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
    }

    @Override
    public Bitmap getImage(ThemeManager theme) {
        Bitmap bitmapImage = theme.getDroplet();
        if(popped == true) {
            bitmapImage = theme.getPoppedDroplet();
        }

        return bitmapImage;
    }

    // Droplets only move vertically
    public void move(ThemeManager theme, int viewWidth, int viewHeight) {
        yCoordinate = yCoordinate + yVelocity;
        if (yCoordinate - getImage(theme).getHeight() >= viewHeight) {
            offScreen = true;
        }
    }

    public int getScoreValue() {
        return VALUE;
    }

}
