package com.example.lookingdynamic.lookingbusy.gameplay;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;

/**
 * Created by swu on 9/15/2015.
 */
public class GameplayMode {

    private static final String LOGGER = Level.class.getSimpleName();
    private static final String XML_DEF_TYPE = "xml";
    private static final String DEF_PACKAGE = "com.example.lookingdynamic.lookingbusy";

    private Level[] levels;
    private String name;

    public GameplayMode(Resources myResources, int gamePlayModeArray) {
        TypedArray items = myResources.obtainTypedArray(gamePlayModeArray);

        if(items.length() > 0) {
            name = myResources.getString(items.getResourceId(0, -1));
            levels = new Level[items.length()-1];
        }for(int i=1;i<items.length();i++){
            levels[i-1] = new Level(myResources.getXml(items.getResourceId(i,-1)));
        }

    }

    public String getName() {
        return name;
    }

    public Level getLevel(int i) {
        if(i > levels.length) {
            i = levels.length - 1;
        }
        return levels[i];
    }
}
