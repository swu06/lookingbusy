package com.lookingdynamic.lookingbusy.purchasing.util;

/**
 * A Wrapper around the Android log for ease of use
 * 
 * @author Blundell
 * 
 */
public class Log {

    private static final String TAG = "SIAPv3";

    public static void d(String msg) {
        android.util.Log.d(TAG, msg);
    }

}
