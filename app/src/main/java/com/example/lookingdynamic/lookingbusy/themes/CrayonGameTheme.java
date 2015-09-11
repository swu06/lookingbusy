package com.example.lookingdynamic.lookingbusy.themes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/10/2015.
 */
public class CrayonGameTheme implements GameTheme {

    private Resources myResources;
    private Bitmap ball;
    private Bitmap balloon;
    private Bitmap droplet;
    private Bitmap popped_ball;
    private Bitmap popped_balloon;
    private Bitmap popped_droplet;
    private Bitmap paused_sign;
    private Bitmap icon;

    public CrayonGameTheme(Resources myResources) {
        this.myResources = myResources;
        ball = BitmapFactory.decodeResource(myResources, R.drawable.ball);
        balloon = BitmapFactory.decodeResource(myResources, R.drawable.balloon);
        droplet = BitmapFactory.decodeResource(myResources, R.drawable.droplet);
        popped_ball = BitmapFactory.decodeResource(myResources, R.drawable.popped_ball);
        popped_balloon = BitmapFactory.decodeResource(myResources, R.drawable.popped_balloon);
        popped_droplet = BitmapFactory.decodeResource(myResources, R.drawable.popped_droplet);
        paused_sign = BitmapFactory.decodeResource(myResources, R.drawable.pause);
        icon= BitmapFactory.decodeResource(myResources, R.drawable.ball);
    }

    @Override
    public Bitmap getBall() {
        return ball;
    }

    @Override
    public Bitmap getBalloon() {
        return balloon;
    }

    @Override
    public Bitmap getDroplet() {
        return droplet;
    }

    @Override
    public Bitmap getPoppedBall() {
        return popped_ball;
    }

    @Override
    public Bitmap getPoppedBalloon() {
        return popped_balloon;
    }

    @Override
    public Bitmap getPoppedDroplet() {
        return popped_droplet;
    }

    @Override
    public Bitmap getPausedSign() {
        return paused_sign;
    }

    @Override
    public Bitmap getIcon() {
        return icon;
    }
}
