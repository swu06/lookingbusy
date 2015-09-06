package com.example.lookingdynamic.lookingbusy.model;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.example.lookingdynamic.lookingbusy.R;

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

    public Balloon(Resources resources, int xCoordinate, int yCoordinate, int yVelocity) {
        bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.balloon);
        this.xCoordinate = xCoordinate - bitmapImage.getWidth() / 2;
        this.yCoordinate = yCoordinate - bitmapImage.getHeight();
        this.yVelocity = yVelocity;
        popped = false;
        offScreen = false;
    }

    public void setPoppedImage(Resources resources) {
        xCoordinate = xCoordinate + bitmapImage.getWidth() / 2;
        yCoordinate = yCoordinate + bitmapImage.getHeight();

        bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.popped_balloon);

        xCoordinate = xCoordinate - bitmapImage.getWidth() / 2;
        yCoordinate = yCoordinate - bitmapImage.getHeight();

    }

    // Balloons only move vertically
    public void move(int viewWidth, int viewHeight) {
        yCoordinate = yCoordinate + yVelocity;
        if (yCoordinate + bitmapImage.getHeight() <= 0) {
            offScreen = true;
        }
    }

    public int getValue() {
        return VALUE;
    }
}
