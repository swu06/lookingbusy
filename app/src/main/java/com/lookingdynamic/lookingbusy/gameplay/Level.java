package com.lookingdynamic.lookingbusy.gameplay;

import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * This class loads the information for each level from the xml resource and holds it in memory.
 * There are no setters for this class because these items should not be edited by the game.  If
 * there is an special circumstance or additional logic that cannot be handled within the level, it
 * should be handled by the GameplayManager class.
 *
 * Created by swu on 9/10/2015.
 */
public class Level {

    private static final String LOGGER = Level.class.getSimpleName();
    private static final String NAME = "name";
    private static final String BUBBLE_GRID = "bubbleGrid";
    private static final String POINTS_TO_NEXT_LEVEL = "pointsToNextLevel";
    private static final String TIME_TO_NEXT_LEVEL = "timeToNextLevel";
    private static final String TOTAL_OBJECTS_TO_CREATE = "totalObjectsToCreate";
    private static final String PERCENT_CHANCE_OF_CREATION = "chanceToCreate";
    private static final String BALL_DEFINITION = "ballDefinition";
    private static final String BALLOON_DEFINITION = "balloonDefinition";
    private static final String DROPLET_DEFINITION = "dropletDefinition";
    private static final String RANDOM_BOT_DEFINITION = "randomBotDefinition";
    private static final String PERCENT_CREATED = "percentCreated";
    private static final String PERCENT_SLOW = "percentSlow";
    private static final String PERCENT_MEDIUM = "percentMedium";
    private static final String PERCENT_FAST = "percentFast";
    private static final String PERCENT_SUPER_FAST = "percentSuperFast";

    protected String name;
    protected boolean bubbleGrid;
    protected int pointsToNextLevel;
    protected int timeToNextLevel;
    protected int totalObjectsToCreate;
    protected int percentChanceOfCreation;
    protected LevelObjectSettings ballSettings;
    protected LevelObjectSettings balloonSettings;
    protected LevelObjectSettings dropletSettings;
    protected LevelObjectSettings randomBotSettings;

    public Level(XmlResourceParser levelXml) {
        name = "";
        bubbleGrid = false;
        pointsToNextLevel = 0;
        percentChanceOfCreation = 0;
        timeToNextLevel = 0;
        ballSettings = new LevelObjectSettings();
        balloonSettings = new LevelObjectSettings();
        dropletSettings = new LevelObjectSettings();
        randomBotSettings = new LevelObjectSettings();

        int eventType = -1;
        try {
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (levelXml.getEventType() == XmlResourceParser.START_TAG) {
                    loadElements(levelXml);
                }
                eventType = levelXml.next();
            }
        } catch (XmlPullParserException|IOException e) {
            e.printStackTrace();
        }

        Log.v(LOGGER, "Level Loaded: " + name);
    }

    private void loadElements(XmlResourceParser levelXml)
                        throws IOException, XmlPullParserException {
        if (levelXml.getName().equalsIgnoreCase(NAME)) {
            name = levelXml.nextText();

        } else if (levelXml.getName().equalsIgnoreCase(BUBBLE_GRID)) {
            bubbleGrid = true;

        } else if (levelXml.getName().equalsIgnoreCase(POINTS_TO_NEXT_LEVEL)) {
            pointsToNextLevel = Integer.parseInt(levelXml.nextText());

        } else if (levelXml.getName().equalsIgnoreCase(TIME_TO_NEXT_LEVEL)) {
            timeToNextLevel = Integer.parseInt(levelXml.nextText());

        } else if (levelXml.getName().equalsIgnoreCase(TOTAL_OBJECTS_TO_CREATE)) {
            totalObjectsToCreate = Integer.parseInt(levelXml.nextText());

        } else if (levelXml.getName().equalsIgnoreCase(PERCENT_CHANCE_OF_CREATION)) {
            percentChanceOfCreation = Integer.parseInt(levelXml.nextText());

        } else if (levelXml.getName().equalsIgnoreCase(BALL_DEFINITION)) {
            loadObjectSettings(ballSettings, levelXml, BALL_DEFINITION);

        } else if (levelXml.getName().equalsIgnoreCase(BALLOON_DEFINITION)) {
            loadObjectSettings(balloonSettings, levelXml, BALLOON_DEFINITION);

        } else if (levelXml.getName().equalsIgnoreCase(DROPLET_DEFINITION)) {
            loadObjectSettings(dropletSettings, levelXml, DROPLET_DEFINITION);

        } else if (levelXml.getName().equalsIgnoreCase(RANDOM_BOT_DEFINITION)) {
            loadObjectSettings(randomBotSettings, levelXml, RANDOM_BOT_DEFINITION);

        }
    }

    private void loadObjectSettings(LevelObjectSettings settingsObject,
                                    XmlResourceParser levelXml,
                                    String nameTag) throws XmlPullParserException, IOException{
        int eventType = levelXml.next();
        while (eventType != XmlResourceParser.END_TAG
                || !levelXml.getName().equalsIgnoreCase(nameTag)) {
            if (levelXml.getName().equalsIgnoreCase(PERCENT_CREATED)) {
                settingsObject.setPercentCreated(Integer.parseInt(levelXml.nextText()));

            } else if (levelXml.getName().equalsIgnoreCase(PERCENT_FAST)) {
                settingsObject.setPercentFast(Integer.parseInt(levelXml.nextText()));

            } else if (levelXml.getName().equalsIgnoreCase(PERCENT_MEDIUM)) {
                settingsObject.setPercentMedium(Integer.parseInt(levelXml.nextText()));

            } else if (levelXml.getName().equalsIgnoreCase(PERCENT_SLOW)) {
                settingsObject.setPercentSlow(Integer.parseInt(levelXml.nextText()));

            } else if (levelXml.getName().equalsIgnoreCase(PERCENT_SUPER_FAST)) {
                settingsObject.setPercentSuperFast(Integer.parseInt(levelXml.nextText()));

            }
            eventType = levelXml.next();
        }
    }

    public String getName() {
        return name;
    }

    public boolean isBubbleGrid() {
        return bubbleGrid;
    }

    public int getPointsToNextLevel() {
        return pointsToNextLevel;
    }

    public int getTimeToNextLevel() {
        return timeToNextLevel;
    }

    public int getTotalObjectsToCreate() {
        return totalObjectsToCreate;
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

    public int getRandomBotPercentCreated() {
        return randomBotSettings.getPercentCreated();
    }

    public int getRandomBotPercentSlow(){
        return randomBotSettings.getPercentSlow();
    }

    public int getRandomBotPercentMedium(){
        return randomBotSettings.getPercentMedium();
    }

    public int getRandomBotPercentFast(){
        return randomBotSettings.getPercentFast();
    }

    public int getRandomBotPercentSuperFast(){
        return randomBotSettings.getPercentSuperFast();
    }

}
