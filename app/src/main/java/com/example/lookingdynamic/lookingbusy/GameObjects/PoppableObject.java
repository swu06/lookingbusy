package com.example.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.lookingdynamic.lookingbusy.themes.GameTheme;

/**
 * Created by swu on 9/6/2015.
 */
public abstract class PoppableObject {

    protected int xCoordinate;
    protected int yCoordinate;
    protected int xVelocity;
    protected int yVelocity;
    protected boolean popped;
    protected boolean offScreen;

    public boolean isPopped() {
        return popped;
    }

    public boolean isOffScreen() {
        return offScreen;
    }
    public void draw(GameTheme theme, Canvas canvas) {
        Bitmap bitmapImage = getImage(theme);
        canvas.drawBitmap(getImage(theme), xCoordinate, yCoordinate, null);
    }
    public boolean handleTouch(GameTheme theme, int eventX, int eventY) {
        Bitmap bitmapImage = getImage(theme);
        if (eventX >= (xCoordinate - bitmapImage.getWidth() / 2) && eventX <= (xCoordinate + bitmapImage.getWidth()/2)
                && eventY >= (yCoordinate - bitmapImage.getHeight() / 2) && (yCoordinate <= (yCoordinate + bitmapImage.getHeight() / 2))) {
            popped = true;
        }

        return popped;
    }

    abstract public Bitmap getImage(GameTheme theme);
    abstract public void move(GameTheme theme, int viewWidth, int viewHeight);
    abstract public int getScoreValue();

}