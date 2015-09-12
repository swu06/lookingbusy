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
public class Ball extends PoppableObject {

    private static final String LOGGER = Ball.class.getSimpleName();
    public static final int VALUE = 50;

    public Ball(GameTheme theme, int xCoordinate, int yCoordinate, int xVelocity, int yVelocity) {
        Bitmap bitmapImage = getImage(theme);
        this.xCoordinate = xCoordinate;
        if(this.xCoordinate > 0 ) {
            this.xCoordinate = this.xCoordinate - bitmapImage.getWidth();
        }
        this.yCoordinate = yCoordinate;
        if(this.yCoordinate > 0 ) {
            this.yCoordinate = this.yCoordinate - bitmapImage.getHeight();
        }
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        popped = false;
        offScreen = false;
    }

    @Override
    public Bitmap getImage(GameTheme theme) {
        Bitmap bitmapImage = theme.getBall();
        if(popped == true) {
            bitmapImage = theme.getPoppedBall();
        }

        return bitmapImage;
    }

    // Balls bounce off walls
    public void move(GameTheme theme, int viewWidth, int viewHeight) {
        // Check to see if we are against a wall
        if(xCoordinate < 0
                || xCoordinate + getImage(theme).getWidth() > viewWidth) {
            xVelocity = xVelocity * -1;
        }

        // Move over and down
        xCoordinate = xCoordinate + xVelocity;
        yCoordinate = yCoordinate + yVelocity;

        // Check to see if we have fallen off the screen yet
        if (yCoordinate > viewHeight
                || yCoordinate < 0) {
            offScreen = true;
        }
    }

    @Override
    public int getScoreValue() {
        return 30;
    }
}
