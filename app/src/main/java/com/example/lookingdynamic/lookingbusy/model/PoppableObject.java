package com.example.lookingdynamic.lookingbusy.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.lookingdynamic.lookingbusy.R;
import com.example.lookingdynamic.lookingbusy.themes.GameTheme;

/**
 * Created by swu on 9/6/2015.
 */
public abstract class PoppableObject {

    protected Bitmap bitmapImage;
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
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmapImage, xCoordinate, yCoordinate, null);
    }
    public boolean handleTouch(int eventX, int eventY) {
        if (eventX >= (xCoordinate - bitmapImage.getWidth() / 2) && eventX <= (xCoordinate + bitmapImage.getWidth()/2)
                && eventY >= (yCoordinate - bitmapImage.getHeight() / 2) && (yCoordinate <= (yCoordinate + bitmapImage.getHeight() / 2))) {
            popped = true;
        }

        return popped;
    }

    public void setPoppedImage(GameTheme theme) {
        xCoordinate = xCoordinate + bitmapImage.getWidth() / 2;
        yCoordinate = yCoordinate + bitmapImage.getHeight();

        bitmapImage = getPoppedImage(theme);

        xCoordinate = xCoordinate - bitmapImage.getWidth() / 2;
        yCoordinate = yCoordinate - bitmapImage.getHeight();

    }

    abstract public void setTheme(GameTheme theme);
    abstract public Bitmap getPoppedImage(GameTheme theme);
    abstract public void move(int viewWidth, int viewHeight);
    abstract public int getScoreValue();

}
