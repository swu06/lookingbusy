package com.example.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;

import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;

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
    private int whichBalloon;

    public Balloon(int xCoordinate, int yCoordinate, int yVelocity, int whichBalloon) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.yVelocity = yVelocity;
        popped = false;
        offScreen = false;
        this.whichBalloon = whichBalloon;
    }

    @Override
    public Bitmap getImage(GameTheme theme) {
        Bitmap bitmapImage = theme.getBalloon(whichBalloon);
        if(popped == true) {
            bitmapImage = theme.getPoppedBalloon(whichBalloon);
        }

        return bitmapImage;
    }

    // Balloons only move vertically
    public void move(GameTheme theme, int viewWidth, int viewHeight) {
        yCoordinate = yCoordinate + yVelocity;
        if (yCoordinate + getImage(theme).getHeight() <= 0) {
            offScreen = true;
        }
    }

    @Override
    public int getScoreValue() {
        return 10;
    }
}
