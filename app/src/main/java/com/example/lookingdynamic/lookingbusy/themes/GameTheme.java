package com.example.lookingdynamic.lookingbusy.themes;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.example.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/10/2015.
 */
public interface GameTheme {

    public Bitmap getBall();
    public Bitmap getBalloon();
    public Bitmap getDroplet();
    public Bitmap getPoppedBall();
    public Bitmap getPoppedBalloon();
    public Bitmap getPoppedDroplet();
    public Bitmap getPausedSign();
    public Bitmap getIcon();

}
