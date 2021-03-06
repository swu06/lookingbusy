package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.util.Log;

import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

import java.util.Random;

/**
 *
 * This is a RandomBot model.  It knows everything possible about the RandomBot
 * but has no brains to do anything about it.  The controller (MainGamePanel)
 * does not know any of this information, but uses it to make the world turn.
 * Created by swu on 9/5/2015.
 */
public class RandomBot extends PoppableObject {

    private static final String LOGGER = RandomBot.class.getSimpleName();
    public static final int VALUE = 50;
    private Random random;
    protected int consecutiveMovesRemaining;
    protected int speed;

    public RandomBot(int width, int height, int speed) {
        random = new Random();
        int locationOffset = random.nextInt(100) - 50;
        xCoordinate = width / 2 + locationOffset;
        yCoordinate = height / 2 + locationOffset;
        this.speed = speed;
        popped = false;
        offScreen = false;
        consecutiveMovesRemaining = 0;
        value = VALUE;

        Log.v(LOGGER, "Droplet Object created at (" + xCoordinate + ", " + yCoordinate + ")");
    }

    @Override
    public Bitmap getImage(ThemeManager theme) {
        Bitmap bitmapImage = theme.getRandomBot();
        if (popped) {
            bitmapImage = theme.getPoppedRandomBot();
        }

        return bitmapImage;
    }

    /*
     * RandomBot needs to move in a semi-random pattern. To make this more appealing to the player,
     * the object should move in the same direction for a short period, and we should reduce the
     * amount of time it stays still.  Like the Ball objects, RandomBots bounce off of the side
     * walls, but they fall off the top and bottom of the screens.
     */
    public void move(ThemeManager theme, int viewWidth, int viewHeight) {
        Bitmap image = getImage(theme);

        // Get the random Direction/Velocity
        // Don't change directions too much and don't stay still too long
        if (consecutiveMovesRemaining == 0
                || (xVelocity == 0 && yVelocity == 0)) {
            xVelocity = (random.nextInt(3) - 1) * speed;
            yVelocity = (random.nextInt(3) - 1) * speed;
            consecutiveMovesRemaining = random.nextInt(50);
        } else {
            consecutiveMovesRemaining--;
        }

        // Move over and down
        xCoordinate = xCoordinate + xVelocity;
        yCoordinate = yCoordinate + yVelocity;

        // Check to see if we have hit the left wall
        if (xCoordinate <= 0) {
            xCoordinate = 0;
            xVelocity = xVelocity * -1;
        }
        // Check to see if we have hit the right wall
        else if (xCoordinate + image.getWidth() >= viewWidth) {
            xCoordinate = viewWidth - image.getWidth();
            xVelocity = xVelocity * -1;
        }

        // Check to see if we have fallen off the screen yet
        if (yCoordinate > viewHeight
                || yCoordinate + image.getHeight() < 0) {
            offScreen = true;
        }
    }
}
