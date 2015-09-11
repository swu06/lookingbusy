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
public class Droplet extends PoppableObject {

    private static final String LOGGER = Droplet.class.getSimpleName();
    public static final int VALUE = 15;

    public Droplet(GameTheme theme, int xCoordinate, int yVelocity) {
        bitmapImage = theme.getDroplet();
        this.xCoordinate = xCoordinate - bitmapImage.getWidth() / 2;
        this.yCoordinate = 0;
        this.yVelocity = yVelocity;
        popped = false;
        offScreen = false;
    }

    @Override
    public void setTheme(GameTheme theme) {
        if(popped == false) {
            bitmapImage = theme.getDroplet();
        } else {
            bitmapImage = theme.getPoppedDroplet();
        }
    }

    @Override
    public Bitmap getPoppedImage(GameTheme theme) {
        return theme.getPoppedDroplet();
    }


    // Droplets only move vertically
    public void move(int viewWidth, int viewHeight) {
        yCoordinate = yCoordinate + yVelocity;
        if (yCoordinate - bitmapImage.getHeight() >= viewHeight) {
            offScreen = true;
        }
    }

    @Override
    public int getScoreValue() {
        return 15;
    }

}
