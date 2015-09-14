package com.example.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by swu on 9/10/2015.
 */
public class GameTheme {

    private static final String LOGGER = GameTheme.class.getSimpleName();
    public static final String DRAWABLE_DEF_TYPE = "drawable";
    public static final String XML_DEF_TYPE = "xml";
    public static final String DEF_PACKAGE = "com.example.lookingdynamic.lookingbusy";
    private static final String NAME_TAG = "name";
    private static final String ICON_TAG = "icon";
    private static final String BALL_TAG = "ball";
    private static final String BALLOON_TAG = "balloon";
    private static final String DROPLET_TAG = "droplet";
    private static final String POPPED_BALL_TAG = "popped_ball";
    private static final String POPPED_BALLOON_TAG = "popped_balloon";
    private static final String POPPED_DROPLET_TAG = "popped_droplet";
    private static final String PAUSE_SIGN_TAG = "pause";

    protected String name = "";
    protected int iconImage = -1;
    protected int ballImage = -1;
    protected int balloonImage = -1;
    protected int dropletImage = -1;
    protected int poppedBallImage = -1;
    protected int poppedBalloonImage = -1;
    protected int poppedDropletImage = -1;
    protected int pauseSignImage = -1;

    private Resources myResources;
    protected Bitmap ball = null;
    protected Bitmap balloon = null;
    protected Bitmap droplet = null;
    protected Bitmap popped_ball = null;
    protected Bitmap popped_balloon = null;
    protected Bitmap popped_droplet = null;
    protected Bitmap pause_sign = null;

//    public GameTheme(){  }

    public GameTheme(Resources otherResources, String themeLabel) {
        this.myResources = otherResources;

        XmlResourceParser themeXml = myResources.getXml(
                myResources.getIdentifier(themeLabel, XML_DEF_TYPE, DEF_PACKAGE));

        int eventType = -1;
        try {
            while(eventType != XmlResourceParser.END_TAG) {
                if (themeXml.getEventType() == XmlResourceParser.START_TAG) {
                    if(themeXml.getName().equalsIgnoreCase(NAME_TAG)) {
                        name = themeXml.nextText();
                    } else if(themeXml.getName().equalsIgnoreCase(ICON_TAG)) {
                        iconImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if(themeXml.getName().equalsIgnoreCase(BALL_TAG)) {
                        ballImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if(themeXml.getName().equalsIgnoreCase(BALLOON_TAG)) {
                        balloonImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if(themeXml.getName().equalsIgnoreCase(DROPLET_TAG)) {
                        dropletImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if(themeXml.getName().equalsIgnoreCase(POPPED_BALL_TAG)) {
                        poppedBallImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if(themeXml.getName().equalsIgnoreCase(POPPED_BALLOON_TAG)) {
                        poppedBalloonImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if(themeXml.getName().equalsIgnoreCase(POPPED_DROPLET_TAG)) {
                        poppedDropletImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if(themeXml.getName().equalsIgnoreCase(PAUSE_SIGN_TAG)) {
                        pauseSignImage = myResources.getIdentifier(themeXml.nextText(),
                                                            DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    }
                }
                eventType = themeXml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public int getIconImageId() {
        return iconImage;
    }

    /*
     * Bitmap Access Methods use Lazy Loading to reduce the amount of memory
     * in use when this theme is inactive.  Note that the icon is never loaded
     * as a bitmap image. Instead, the menu adaptop only loads it when needed.
     */

    public Bitmap getBall() {
        if(ball == null) {
            ball = BitmapFactory.decodeResource(myResources, ballImage);
        }
        return ball;
    }

    public Bitmap getBalloon() {
        if(balloon == null) {
            balloon = BitmapFactory.decodeResource(myResources, balloonImage);
        }
        return balloon;
    }

    public Bitmap getDroplet() {
        if(droplet == null) {
            droplet = BitmapFactory.decodeResource(myResources, dropletImage);
        }
        return droplet;
    }

    public Bitmap getPoppedBall() {
        if(popped_ball == null) {
            popped_ball = BitmapFactory.decodeResource(myResources, poppedBallImage);
        }
        return popped_ball;
    }

    public Bitmap getPoppedBalloon() {
        if(popped_balloon == null) {
            popped_balloon = BitmapFactory.decodeResource(myResources, poppedBalloonImage);
        }
        return popped_balloon;
    }

    public Bitmap getPoppedDroplet() {
        if(popped_droplet == null) {
            popped_droplet = BitmapFactory.decodeResource(myResources, poppedDropletImage);
        }
        return popped_droplet;
    }

    public Bitmap getPauseSign() {
        if(pause_sign == null) {
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
        balloon = null;
        droplet = null;
        popped_ball = null;
        popped_balloon = null;
        popped_droplet = null;
        pause_sign = null;
    }
}
