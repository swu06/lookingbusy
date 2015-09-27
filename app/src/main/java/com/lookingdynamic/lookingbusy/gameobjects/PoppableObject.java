package com.lookingdynamic.lookingbusy.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.lookingdynamic.lookingbusy.gameplay.GameTheme;
import com.lookingdynamic.lookingbusy.gameplay.ThemeManager;

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
    protected int VALUE;

    public boolean isPopped() {
        return popped;
    }

    public boolean isOffScreen() {
        return offScreen;
    }

    public void draw(ThemeManager theme, Canvas canvas, Paint painter) {
        canvas.drawBitmap(getImage(theme), xCoordinate, yCoordinate, painter);
    }
    public boolean handleTouch(ThemeManager theme, int eventX, int eventY) {
        Bitmap bitmapImage = getImage(theme);
        if (eventX >= xCoordinate && eventX <= (xCoordinate + bitmapImage.getWidth())
                && eventY >= yCoordinate && (yCoordinate <= (yCoordinate + bitmapImage.getHeight()))) {
            popped = true;
        }

        return popped;
    }

    abstract public int getScoreValue();
    abstract public Bitmap getImage(ThemeManager theme);
    abstract public void move(ThemeManager theme, int viewWidth, int viewHeight);

}
