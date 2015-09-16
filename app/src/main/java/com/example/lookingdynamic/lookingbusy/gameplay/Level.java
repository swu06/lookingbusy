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
public class Level {

    private static final String NAME = "name";
    private static final String POINTS_TO_NEXT_LEVEL = "pointsToNextLevel";
    private static final String PERCENT_CHANCE_OF_CREATION = "chanceToCreate";
    private static final String BALL_DEFINITION = "ballDefinition";
    private static final String BALLOON_DEFINITION = "balloonDefinition";
    private static final String DROPLET_DEFINITION = "dropletDefinition";
    private static final String PERCENT_CREATED = "percentCreated";
    private static final String PERCENT_SLOW = "percentSlow";
    private static final String PERCENT_MEDIUM = "percentMedium";
    private static final String PERCENT_FAST = "percentFast";
    private static final String PERCENT_SUPER_FAST = "percentSuperFast";

    protected String name;
    protected int pointsToNextLevel;
    protected int percentChanceOfCreation;
    protected LevelObjectSettings ballSettings;
    protected LevelObjectSettings balloonSettings;
    protected LevelObjectSettings dropletSettings;

    public Level(XmlResourceParser levelXml) {
        name = "";
        pointsToNextLevel = 0;
        percentChanceOfCreation = 0;
        ballSettings = new LevelObjectSettings();
        balloonSettings = new LevelObjectSettings();
        dropletSettings = new LevelObjectSettings();

        int eventType = -1;
        try {
            while(eventType != XmlResourceParser.END_DOCUMENT) {
                if (levelXml.getEventType() == XmlResourceParser.START_TAG) {
                    if(levelXml.getName().equalsIgnoreCase(NAME)) {
                        name = levelXml.nextText();
                    } else if(levelXml.getName().equalsIgnoreCase(POINTS_TO_NEXT_LEVEL)) {
                        pointsToNextLevel = Integer.parseInt(levelXml.nextText());
                    } else if(levelXml.getName().equalsIgnoreCase(PERCENT_CHANCE_OF_CREATION)) {
                        percentChanceOfCreation = Integer.parseInt(levelXml.nextText());
                    } else if(levelXml.getName().equalsIgnoreCase(BALL_DEFINITION)) {
                        loadObjectSettings(ballSettings, levelXml, BALL_DEFINITION);
                    } else if(levelXml.getName().equalsIgnoreCase(BALLOON_DEFINITION)) {
                        loadObjectSettings(balloonSettings, levelXml, BALLOON_DEFINITION);
                    } else if(levelXml.getName().equalsIgnoreCase(DROPLET_DEFINITION)) {
                        loadObjectSettings(dropletSettings, levelXml, DROPLET_DEFINITION);
                    }
                }
                eventType = levelXml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadObjectSettings(LevelObjectSettings settingsObject,
                                    XmlResourceParser levelXml,
                                    String nameTag) throws XmlPullParserException, IOException{
        int eventType = levelXml.next();
        while(eventType != XmlResourceParser.END_TAG
                || !levelXml.getName().equalsIgnoreCase(nameTag)) {
            if(levelXml.getName().equalsIgnoreCase(PERCENT_CREATED)) {
                settingsObject.setPercentCreated(Integer.parseInt(levelXml.nextText()));
            } else if(levelXml.getName().equalsIgnoreCase(PERCENT_FAST)) {
                settingsObject.setPercentFast(Integer.parseInt(levelXml.nextText()));
            } else if(levelXml.getName().equalsIgnoreCase(PERCENT_MEDIUM)) {
                settingsObject.setPercentMedium(Integer.parseInt(levelXml.nextText()));
            } else if(levelXml.getName().equalsIgnoreCase(PERCENT_SLOW)) {
                settingsObject.setPercentSlow(Integer.parseInt(levelXml.nextText()));
            } else if(levelXml.getName().equalsIgnoreCase(PERCENT_SUPER_FAST)) {
                settingsObject.setPercentSuperFast(Integer.parseInt(levelXml.nextText()));
            }
            eventType = levelXml.next();
        }
    }

    public String getName() {
        return name;
    }

    public int getPointsToNextLevel() {
        return pointsToNextLevel;
    }

    public int getPercentChanceOfCreation() {
        return percentChanceOfCreation;
    }

    public int getBallPercentCreated() {
        return ballSettings.getPercentCreated();
    }

    public int getBallPercentSlow(){
        return ballSettings.getPercentSlow();
    }

    public int getBallPercentMedium(){
        return ballSettings.getPercentMedium();
    }

    public int getBallPercentFast(){
        return ballSettings.getPercentFast();
    }

    public int getBallPercentSuperFast(){
        return ballSettings.getPercentSuperFast();
    }

    public int getBalloonPercentCreated() {
        return balloonSettings.getPercentCreated();
    }

    public int getBalloonPercentSlow(){
        return balloonSettings.getPercentSlow();
    }

    public int getBalloonPercentMedium(){
        return balloonSettings.getPercentMedium();
    }

    public int getBalloonPercentFast(){
        return balloonSettings.getPercentFast();
    }

    public int getBalloonPercentSuperFast(){
        return balloonSettings.getPercentSuperFast();
    }

    public int getDropletPercentCreated() {
        return dropletSettings.getPercentCreated();
    }

    public int getDropletPercentSlow(){
        return dropletSettings.getPercentSlow();
    }

    public int getDropletPercentMedium(){
        return dropletSettings.getPercentMedium();
    }

    public int getDropletPercentFast(){
        return dropletSettings.getPercentFast();
    }

    public int getDropletPercentSuperFast(){
        return dropletSettings.getPercentSuperFast();
    }

}
