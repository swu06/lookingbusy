package com.example.lookingdynamic.lookingbusy.themes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lookingdynamic.lookingbusy.R;

/**
 * Created by swu on 9/10/2015.
 */
public class BrightGameTheme extends GameTheme {

    private static final String NAME = "Bright Theme";
    private static final int BALL_IMAGE = R.drawable.bright_ball;
    private static final int BALLOON_IMAGE = R.drawable.bright_balloon;
    private static final int DROPLET_IMAGE = R.drawable.bright_droplet;
    private static final int POPPED_BALL_IMAGE = R.drawable.bright_popped_ball;
    private static final int POPPED_BALLOON_IMAGE = R.drawable.bright_popped_balloon;
    private static final int POPPED_DROPLET_IMAGE = R.drawable.bright_popped_droplet;
    private static final int PAUSED_SIGN_IMAGE = R.drawable.bright_pause;
    private static final int ICON_IMAGE = R.drawable.bright_icon;

    private Bitmap ball;
    private Bitmap balloon;
    private Bitmap droplet;
    private Bitmap popped_ball;
    private Bitmap popped_balloon;
    private Bitmap popped_droplet;
    private Bitmap paused_sign;
    private Bitmap icon;

    public BrightGameTheme(Resources myResources) {
        ball = BitmapFactory.decodeResource(myResources, BALL_IMAGE);
        balloon = BitmapFactory.decodeResource(myResources, BALLOON_IMAGE);
        droplet = BitmapFactory.decodeResource(myResources, DROPLET_IMAGE);
        popped_ball = BitmapFactory.decodeResource(myResources, POPPED_BALL_IMAGE);
        popped_balloon = BitmapFactory.decodeResource(myResources, POPPED_BALLOON_IMAGE);
        popped_droplet = BitmapFactory.decodeResource(myResources, POPPED_DROPLET_IMAGE);
        paused_sign = BitmapFactory.decodeResource(myResources, PAUSED_SIGN_IMAGE);
        icon = BitmapFactory.decodeResource(myResources, ICON_IMAGE);
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
    public Bitmap getPauseSign() {
        return paused_sign;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getIconImageId() {
        return ICON_IMAGE;
    }

}
