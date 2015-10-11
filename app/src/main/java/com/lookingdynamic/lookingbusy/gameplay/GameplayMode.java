package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * This class loads the information for each mode/type of gameplay.  This includes level specific
 * information that may change during the course of the mode (like how many balloons should be
 * created) as well as overall settings that should remain consistent during the game (like the
 * number of lives allowed before the game ends).
 *
 * The methods in the class are all accessors because this information should not be changed by
 * the gameplay. For items that need additional logic, please see the GameplayManager class.
 *
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
    protected String name;
    protected int icon;
    protected int livesAllowed;

    public GameplayMode(Resources myResources, XmlResourceParser modeXml) {
        name = "";
        livesAllowed = 0;

        int eventType = -1;
        try {
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (modeXml.getEventType() == XmlResourceParser.START_TAG) {
                    loadElements(myResources, modeXml);
                }
                eventType = modeXml.next();
            }
        } catch (XmlPullParserException|IOException e) {
            e.printStackTrace();
        }

        Log.v(LOGGER, "Mode Loaded: " + name);
    }

    private void loadElements(Resources myResources, XmlResourceParser modeXml)
            throws IOException, XmlPullParserException {

        if (modeXml.getName().equalsIgnoreCase(NAME)) {
            name = modeXml.nextText();

        } else if (modeXml.getName().equalsIgnoreCase(ICON)) {
            icon = myResources.getIdentifier(modeXml.nextText(), DRAWABLE_DEF_TYPE, DEF_PACKAGE);

        } else if (modeXml.getName().equalsIgnoreCase(LIVES_ALLOWED)) {
            livesAllowed = Integer.parseInt(modeXml.nextText());

        } else if (modeXml.getName().equalsIgnoreCase(LEVEL_DEFINITION)) {
            String levelList = modeXml.nextText();
            String[] levelsInList = levelList.split(",");

            levels = new Level[levelsInList.length];
            for (int i = 0; i < levels.length; i++){
                levels[i] = new Level(myResources.getXml(
                        myResources.getIdentifier(levelsInList[i], XML_DEF_TYPE, DEF_PACKAGE)));
            }

        }
    }

    public String getName() {
        return name;
    }

    public int getIconImageId() {
        return icon;
    }

    public Level getLevel(int inputLevel) {
        if (inputLevel >= levels.length) {
            inputLevel = levels.length - 1;
        }

        return levels[inputLevel];
    }

    public String getLevelName(int inputLevel) {
        return getLevel(inputLevel).getName();
    }

    public int getPointsToNextLevel(int inputLevel) {
        return getLevel(inputLevel).getPointsToNextLevel();
    }

    public int getTimeToNextLevel(int inputLevel) {
        return getLevel(inputLevel).getTimeToNextLevel();
    }

    public int getTotalObjectsToCreate(int inputLevel) {
        return getLevel(inputLevel).getTotalObjectsToCreate();
    }

    public boolean isBubbleGrid(int inputLevel) {
        return getLevel(inputLevel).isBubbleGrid();
    }

    public int getLivesAllowed() {
        return livesAllowed;
    }
}
