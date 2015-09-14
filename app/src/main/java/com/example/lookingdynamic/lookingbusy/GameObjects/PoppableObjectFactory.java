package com.example.lookingdynamic.lookingbusy.gameobjects;

import com.example.lookingdynamic.lookingbusy.gameplay.LevelGuide;
import com.example.lookingdynamic.lookingbusy.gameplay.GameTheme;

import java.util.Random;

/**
 * Created by swu on 9/6/2015.
 */
public class PoppableObjectFactory {

    private static Random rand = new Random();


    /*

    Level 0 (Relaxing):     40% Balloons - 50% at medium speed, 25% slow speed, 25% fast speed
                            40% Water Droplets - 50% medium speed, 25% slow speed, 25% fast speed
                            20% Bouncy Balls - 50% medium speed, 25% slow speed, 25% fast speed
                            Super-fast speed goes away
    Level 1 (100 points):     All Balloons, all slow speed
    Level 2 (200 points):    100% Balloons - 50% at medium speed, 50% slow speed
    Level 3 (300 points):   70% Balloons - 50% at medium speed, 50% slow speed
                            30% Water Droplets - 100% medium speed
    Level 4 (400 points):   70% Balloons - 50% at medium speed, 50% slow speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
    Level 5 (500 points):   50% Balloons - 50% at medium speed, 50% slow speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            20% Bouncy Balls - 100% fast speed
    Level 6 (600 points):   50% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            20% Bouncy Balls - 100% fast speed
    Level 7 (800 points):   50% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            20% Bouncy Balls - 100% fast speed
                            Double the number of items created
    Level 8 (1500 points):  30% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            40% Bouncy Balls - 100% fast speed
                            Double the number of items created
    Level 9 (2000 points):  30% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            40% Bouncy Balls - 100% fast speed
                            Double the number of items created
    Level 10 (3000 points): 30% Balloons - 100% at medium speed
                            30% Water Droplets - 50% medium speed, 50% fast speed
                            40% Bouncy Balls - 50% fast speed, 50% super-fast speed
                            Double the number of items created
    */
    public static PoppableObject generatePoppableObject(GameTheme theme, int level, int width, int height, boolean justStarted) {

        PoppableObject toReturn = null;

        boolean createANewObject = true;

        // When we are first starting the game, we need to ensure something gets on the screen fast
        // Otherwise, we should check the levelling guide to see if we should create a new object
        if(!justStarted) {
            createANewObject = LevelGuide.shouldCreateObject(level);
        }

        // If we aren't going to make a new object, then just exit
        if(createANewObject){
            toReturn = LevelGuide.generateNewObject(theme, level, width, height);
        }

        return toReturn;
    }
}
