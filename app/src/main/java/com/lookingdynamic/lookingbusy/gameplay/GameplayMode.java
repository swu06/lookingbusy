package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by swu on 9/15/2015.
 */
public class GameplayMode {

    private static final String LOGGER = Level.class.getSimpleName();
    private static final String DRAWABLE_DEF_TYPE = "drawable";
    private static final String XML_DEF_TYPE = "xml";
    private static final String DEF_PACKAGE = "com.lookingdynamic.lookingbusy";
    private static final String NAME = "name";
    private static final String ICON = "icon";
    private static final String LIVES_ALLOWED = "livesAllowed";
    private static final String LEVEL_DEFINITION = "levelDefinition";

    protected Level[] levels;
    private String name;
    private int icon;
    private int livesAllowed;

    public GameplayMode(Resources myResources, XmlResourceParser modeXml) {
        name = "";
        livesAllowed = 0;

        int eventType = -1;
        try {
            while(eventType != XmlResourceParser.END_DOCUMENT) {
                if (modeXml.getEventType() == XmlResourceParser.START_TAG) {
                    if(modeXml.getName().equalsIgnoreCase(NAME)) {
                        name = modeXml.nextText();
                    } else if(modeXml.getName().equalsIgnoreCase(ICON)) {
                        icon = myResources.getIdentifier(modeXml.nextText(),
                                DRAWABLE_DEF_TYPE, DEF_PACKAGE);
                    } else if(modeXml.getName().equalsIgnoreCase(LIVES_ALLOWED)) {
                        livesAllowed = Integer.parseInt(modeXml.nextText());
                    } else if(modeXml.getName().equalsIgnoreCase(LEVEL_DEFINITION)) {
                        String levelList = modeXml.nextText();
                        String[] levelsInList = levelList.split(",");

                        levels = new Level[levelsInList.length];
                        for(int i=0; i<levels.length; i++){
                            levels[i] = new Level(myResources.getXml(
                                                    myResources.getIdentifier(levelsInList[i],
                                                            XML_DEF_TYPE, DEF_PACKAGE)));
                        }
                    }
                }
                eventType = modeXml.next();
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
        return icon;
    }

    public Level getLevel(int i) {
        return levels[safeLevel(i)];
    }

    public int safeLevel(int inputLevel){
        if(inputLevel >= levels.length) {
            inputLevel = levels.length - 1;
        }
        return inputLevel;
    }

    public String getLevelName(int currentLevel) {
        return levels[safeLevel(currentLevel)].getName();
    }

    public int getPointsToNextLevel(int currentLevel) {
        return levels[safeLevel(currentLevel)].getPointsToNextLevel();
    }

    public int getTimeToNextLevel(int currentLevel) {
        return levels[safeLevel(currentLevel)].getTimeToNextLevel();
    }

    public int getTotalObjectsToCreate(int currentLevel) {
        return levels[safeLevel(currentLevel)].getTotalObjectsToCreate();
    }

    public int getLivesAllowed() {
        return livesAllowed;
    }
}
