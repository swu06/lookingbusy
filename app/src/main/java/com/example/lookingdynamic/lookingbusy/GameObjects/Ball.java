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
public class Ball extends PoppableObject {

    private static final String LOGGER = Ball.class.getSimpleName();
    public static final int VALUE = 50;

    public Ball(int xCoordinate, int yCoordinate, int xVelocity, int yVelocity) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
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
        Bitmap image = getImage(theme);
        // Move over and down
        xCoordinate = xCoordinate + xVelocity;
        yCoordinate = yCoordinate + yVelocity;

        // Check to see if we have hit the left wall
        if(xCoordinate <= 0) {
            xCoordinate = 0;
            xVelocity = xVelocity * -1;
        }
        // Check to see if we have hit the right wall
        else if( xCoordinate + image.getWidth() >= viewWidth) {
            xCoordinate = viewWidth - image.getWidth();
            xVelocity = xVelocity * -1;
        }

        // Check to see if we have fallen off the screen yet
        if (yCoordinate > viewHeight
                || yCoordinate + image.getHeight() < 0) {
            offScreen = true;
        }
    }

    @Override
    public int getScoreValue() {
        return 30;
    }
}
