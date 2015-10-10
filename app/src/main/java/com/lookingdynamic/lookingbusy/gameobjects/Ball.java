package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.util.Log;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

/**
 *
 * This is a ball model.  It knows everything possible about the ball
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
        if (type == TOP_LEFT_START) {
            xCoordinate = 0;
            yCoordinate = 0;
            xVelocity = speed * 2;
            yVelocity = speed;
        } else if (type == TOP_RIGHT_START) {
            xCoordinate = width;
            yCoordinate = 0;
            xVelocity = speed * -2;
            yVelocity = speed;
        } else if (type == BOTTOM_RIGHT_START) {
            xCoordinate = width;
            yCoordinate = height;
            xVelocity = speed * -2;
            yVelocity = speed * -1;
        } else {
            xCoordinate = 0;
            yCoordinate = height;
            xVelocity = speed * 2;
            yVelocity = speed * -1;
        }

        popped = false;
        offScreen = false;
        value = VALUE;

        Log.v(LOGGER, "Ball Object created at (" + xCoordinate + ", " + yCoordinate +")");
    }

    /*
     * The Ball Objects are very hard to pop, so people suspect the game is buggy.
     * To resolve this, I am added a small fudge factor so they are easier to hit.
     */
    @Override
    public boolean handleTouch(ThemeManager theme, int eventX, int eventY) {
        int eh = 10;
        Bitmap bitmapImage = getImage(theme);
        if (eventX >= xCoordinate - eh
                && eventX <= (xCoordinate + bitmapImage.getWidth() + eh)
                && eventY >= yCoordinate - eh
                && (eventY <= (yCoordinate + bitmapImage.getHeight() + eh))) {
            popped = true;
            Log.v(LOGGER, "Ball Popped at (" + xCoordinate + ", " + yCoordinate +")");
        }

        return popped;
    }

    @Override
    public Bitmap getImage(ThemeManager theme) {
        Bitmap bitmapImage = theme.getBall();
        if (popped) {
            bitmapImage = theme.getPoppedBall();
        }

        return bitmapImage;
    }

    /*
     *  Balls move side to side. They bounce off walls, but they fall off of the bottom and top of
     *  the screen.
     */
    public void move(ThemeManager theme, int viewWidth, int viewHeight) {
        Bitmap image = getImage(theme);

        // Move over and down
        xCoordinate = xCoordinate + xVelocity;
        yCoordinate = yCoordinate + yVelocity;

        // Check to see if we have hit the left or right walls
        if (xCoordinate <= 0) {
            xCoordinate = 0;
            xVelocity = xVelocity * -1;
        } else if (xCoordinate + image.getWidth() >= viewWidth) {
            xCoordinate = viewWidth - image.getWidth();
            if(xVelocity > 0) {
                xVelocity = xVelocity * -1;
            }
        }

        // Check to see if we have fallen off the screen yet
        if (yCoordinate > viewHeight
                || yCoordinate + image.getHeight() < 0) {
            offScreen = true;
            Log.v(LOGGER, "Ball fell offscreen at (" + xCoordinate + ", " + yCoordinate +")");
        }
    }
}
