package com.example.lookingdynamic.lookingbusy.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lookingdynamic.lookingbusy.R;
import com.example.lookingdynamic.lookingbusy.themes.GameTheme;

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

    public Balloon(GameTheme theme, int xCoordinate, int yCoordinate, int yVelocity) {
        Bitmap bitmapImage = getImage(theme);
        this.xCoordinate = xCoordinate - bitmapImage.getWidth() / 2;
        this.yCoordinate = yCoordinate - bitmapImage.getHeight();
        this.yVelocity = yVelocity;
        popped = false;
        offScreen = false;
    }

    @Override
    public Bitmap getImage(GameTheme theme) {
        Bitmap bitmapImage = theme.getBalloon();
        if(popped == true) {
            bitmapImage = theme.getPoppedBalloon();
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
