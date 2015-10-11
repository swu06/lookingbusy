package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * This class contains the images that make up a particular theme.  These images are used by the
 * poppable objects and other UI items.
 * Created by swu on 9/10/2015.
 */
public class GameTheme {

    private static final String LOGGER = GameTheme.class.getSimpleName();
    private static final String DRAWABLE_DEF_TYPE = "drawable";
    private static final String DEF_PACKAGE = "com.lookingdynamic.lookingbusy";
    private static final String NAME_TAG = "name";
    private static final String ALPHA_TAG = "alpha";
    private static final String ICON_TAG = "icon";
    private static final String BALL_TAG = "ball";
    private static final String BALLOONS_TAG = "balloons";
    private static final String DROPLET_TAG = "droplet";
    private static final String BUBBLE_TAG = "bubble";
    private static final String POPPED_BALL_TAG = "popped_ball";
    private static final String POPPED_BALLOONS_TAG = "popped_balloons";
    private static final String POPPED_DROPLET_TAG = "popped_droplet";
    private static final String PAUSE_SIGN_TAG = "pause";

    protected String name = "";
    protected int iconImage = -1;
    protected int ballImage = -1;
    protected int[] balloonImages;
    protected int dropletImage = -1;
    protected int bubbleImage = -1;
    protected int poppedBallImage = -1;
    protected int[] poppedBalloonImages;
    protected int poppedDropletImage = -1;
    protected int pauseSignImage = -1;

    private Resources myResources;
    private Paint painter;
    protected Bitmap ball = null;
    protected Bitmap[] balloons;
    protected Bitmap droplet = null;
    protected Bitmap bubble = null;
    protected Bitmap popped_ball = null;
    protected Bitmap[] popped_balloons = null;
    protected Bitmap popped_droplet = null;
    protected Bitmap pause_sign = null;

    public GameTheme(Resources otherResources, XmlResourceParser themeXml) {
        this.myResources = otherResources;
        painter = new Paint();

        int eventType = -1;
        try {
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (themeXml.getEventType() == XmlResourceParser.START_TAG) {
                    if (themeXml.getName().equalsIgnoreCase(NAME_TAG)) {
                        name = themeXml.nextText();
                    } else if (themeXml.getName().equalsIgnoreCase(ALPHA_TAG)) {
                        painter.setAlpha(Integer.parseInt(themeXml.nextText()));
                    } else if (themeXml.getName().equalsIgnoreCase(ICON_TAG)) {
                        iconImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if (themeXml.getName().equalsIgnoreCase(BALL_TAG)) {
                        ballImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if (themeXml.getName().equalsIgnoreCase(BALLOONS_TAG)) {
                        parseBalloons(themeXml.nextText());
                    }  else if (themeXml.getName().equalsIgnoreCase(DROPLET_TAG)) {
                        dropletImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    }  else if (themeXml.getName().equalsIgnoreCase(BUBBLE_TAG)) {
                        bubbleImage = myResources.getIdentifier(themeXml.nextText(),
                                DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if (themeXml.getName().equalsIgnoreCase(POPPED_BALL_TAG)) {
                        poppedBallImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if (themeXml.getName().equalsIgnoreCase(POPPED_BALLOONS_TAG)) {
                        parsePoppedBalloons(themeXml.nextText());
                    } else if (themeXml.getName().equalsIgnoreCase(POPPED_DROPLET_TAG)) {
                        poppedDropletImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if (themeXml.getName().equalsIgnoreCase(PAUSE_SIGN_TAG)) {
                        pauseSignImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    }
                }
                eventType = themeXml.next();
            }
        } catch (XmlPullParserException|IOException e) {
            e.printStackTrace();
        }
    }

    private void parseBalloons(String listString) {
        String[] imageNames = listString.split(",");
        balloonImages = new int[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            balloonImages[i] = myResources.getIdentifier(imageNames[i],
                                                    DRAWABLE_DEF_TYPE, DEF_PACKAGE);
        }
    }

    private void parsePoppedBalloons(String listString) {
        String[] imageNames = listString.split(",");
        poppedBalloonImages = new int[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            poppedBalloonImages[i] = myResources.getIdentifier(imageNames[i],
                                                    DRAWABLE_DEF_TYPE, DEF_PACKAGE);
        }
    }

    public String getName() {
        return name;
    }

    public Paint getPainter() {
        return painter;
    }

    public int getIconImageId() {
        return iconImage;
    }

    /*
     * Bitmap Access Methods use Lazy Loading to reduce the amount of memory
     * in use when this theme is inactive.  Note that the icon is never loaded
     * as a bitmap image. Instead, the menu adaptor only loads it when needed.
     */

    public Bitmap getBall() {
        if (ball == null) {
            ball = BitmapFactory.decodeResource(myResources, ballImage);
        }
        return ball;
    }

    public Bitmap getBalloon(int whichOne) {
        if (balloons == null) {
            loadBalloons();
        }

        if (whichOne >= balloons.length) {
            whichOne =  whichOne % balloons.length;
        }

        return balloons[whichOne];
    }

    private void loadBalloons() {
        balloons = new Bitmap[balloonImages.length];
        for (int i = 0; i < balloonImages.length; i++) {
            balloons[i] = BitmapFactory.decodeResource(myResources, balloonImages[i]);
        }
    }

    public Bitmap getDroplet() {
        if (droplet == null) {
            droplet = BitmapFactory.decodeResource(myResources, dropletImage);
        }
        return droplet;
    }

    public Bitmap getBubble() {
        if (bubble == null) {
            bubble = BitmapFactory.decodeResource(myResources, bubbleImage);
        }
        return bubble;
    }

    public Bitmap getPoppedBall() {
        if (popped_ball == null) {
            popped_ball = BitmapFactory.decodeResource(myResources, poppedBallImage);
        }
        return popped_ball;
    }

    public Bitmap getPoppedBalloon(int whichOne) {
        if (popped_balloons == null) {
            loadPoppedBalloons();
        }

        if (whichOne >= popped_balloons.length) {
            whichOne = whichOne % popped_balloons.length;
        }

        return popped_balloons[whichOne];
    }

    private void loadPoppedBalloons() {
        popped_balloons = new Bitmap[poppedBalloonImages.length];
        for (int i = 0; i < poppedBalloonImages.length; i++) {
            popped_balloons[i] = BitmapFactory.decodeResource(myResources, poppedBalloonImages[i]);
        }
    }

    public Bitmap getPoppedDroplet() {
        if (popped_droplet == null) {
            popped_droplet = BitmapFactory.decodeResource(myResources, poppedDropletImage);
        }
        return popped_droplet;
    }

    public Bitmap getPauseSign() {
        if (pause_sign == null) {
            pause_sign = BitmapFactory.decodeResource(myResources, pauseSignImage);
        }
        return pause_sign;
    }

    /*
     * This method removes pointers to the bitmaps so the memory can be reclaimed
     * Note that the Name and icon image files stay loaded as they are used as
     * meta information about the theme in menus
     */
    public void unloadImages() {
        ball = null;
        for (int i = 0; balloons != null && i < balloons.length; i++) {
            balloons[i] = null;
        }
        balloons = null;
        droplet = null;
        bubble = null;
        popped_ball = null;
        for (int i = 0; popped_balloons != null && i < popped_balloons.length; i++) {
            popped_balloons[i] = null;
        }
        popped_balloons = null;
        popped_droplet = null;
        pause_sign = null;
    }
}
