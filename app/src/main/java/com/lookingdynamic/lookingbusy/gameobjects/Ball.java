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
public class Ball extends PoppableObject {

    private static final String LOGGER = Ball.class.getSimpleName();
    public static final int TOP_LEFT_START = 0;
    public static final int TOP_RIGHT_START = 1;
    public static final int BOTTOM_RIGHT_START = 2;
    public static final int BOTTOM_LEFT_START = 3;
    public static final int VALUE = 40;

    public Ball(int width, int height, int type, int speed) {
        switch(type) {
            case TOP_LEFT_START:
                xCoordinate = 0;
                yCoordinate = 0;
                xVelocity = speed * 2;
                yVelocity = speed;
                break;
            case TOP_RIGHT_START:
                xCoordinate = width;
                yCoordinate = 0;
                xVelocity = speed * -2;
                yVelocity = speed;
                break;
            case BOTTOM_RIGHT_START:
                xCoordinate = width;
                yCoordinate = height;
                xVelocity = speed * -2;
                yVelocity = speed * -1;
                break;
            case BOTTOM_LEFT_START: default:
                xCoordinate = 0;
                yCoordinate = height;
                xVelocity = speed * 2;
                yVelocity = speed * -1;
                break;
        }

        popped = false;
        offScreen = false;
    }

    @Override
    public Bitmap getImage(ThemeManager theme) {
        Bitmap bitmapImage = theme.getBall();
        if(popped == true) {
            bitmapImage = theme.getPoppedBall();
        }

        return bitmapImage;
    }

    // Balls bounce off walls
    public void move(ThemeManager theme, int viewWidth, int viewHeight) {
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
            if(xVelocity > 0) {
                xVelocity = xVelocity * -1;
            }
        }

        // Check to see if we have fallen off the screen yet
        if (yCoordinate > viewHeight
                || yCoordinate + image.getHeight() < 0) {
            offScreen = true;
        }
    }

    public int getScoreValue() {
        return VALUE;
    }
}