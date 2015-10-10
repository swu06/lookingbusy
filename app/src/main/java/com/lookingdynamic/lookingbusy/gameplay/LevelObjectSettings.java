package com.lookingdynamic.lookingbusy.gameplay;

/**
 * This class holds information for each Level Object so it can be accessed together
 * for each object type (ie, Ball, Droplet, etc)
 * Created by swu on 9/14/2015.
 */
public class LevelObjectSettings {

    protected int percentCreated;
    protected int percentSlow;
    protected int percentMedium;
    protected int percentFast;
    protected int percentSuperFast;

    LevelObjectSettings() {
        percentCreated = 0;
        percentSlow = 0;
        percentMedium = 0;
        percentFast = 0;
        percentSuperFast = 0;
    }

    public int getPercentCreated(){
        return percentCreated;
    }

    public void setPercentCreated(int percentCreated) {
        this.percentCreated = percentCreated;
    }

    public int getPercentSlow(){
        return percentSlow;
    }

    public void setPercentSlow(int percentSlow) {
        this.percentSlow = percentSlow;
    }

    public int getPercentMedium(){
        return percentMedium;
    }

    public void setPercentMedium(int percentMedium) {
        this.percentMedium = percentMedium;
    }

    public int getPercentFast(){
        return percentFast;
    }

    public void setPercentFast(int percentFast) {
        this.percentFast = percentFast;
    }

    public int getPercentSuperFast(){
        return percentSuperFast;
    }

    public void setPercentSuperFast(int percentSuperFast) {
        this.percentSuperFast = percentSuperFast;
    }
}
