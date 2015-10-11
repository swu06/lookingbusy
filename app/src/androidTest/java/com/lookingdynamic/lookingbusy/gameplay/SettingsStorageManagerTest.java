package com.lookingdynamic.lookingbusy.gameplay;

import android.app.Activity;
import android.test.ActivityTestCase;

/**
 *
 * Created by swu on 9/20/2015.
 */
public class SettingsStorageManagerTest extends ActivityTestCase{

    @Override
    public void setUp() {
        getInstrumentation().getTargetContext().getSharedPreferences("BOOT_PREF",
                Activity.MODE_PRIVATE).edit().clear().commit();
    }

    public void testCreateSettingsStorageManager() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        assertNotNull("Test Failure: SettingsStorageManager creation failed", settings);
    }

    public void testGetDefaultIntValue() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        assertEquals("Test Failure: Int get failed", 0, settings.getIntValueOrDefault("testGetDefaultIntValue"));
    }

    public void testGetSetIntValue() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        settings.setIntValue("testGetSetIntValue", 1);

        assertEquals("Test Failure: Int set or get failed", 1, settings.getIntValueOrDefault("testGetSetIntValue"));
    }

    public void testGetDefaultStringValue() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        assertEquals("Test Failure: String get failed", null, settings.getStringValueOrDefault("testGetDefaultStringValue"));
    }

    public void testGetSetStringValue() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        settings.setStringValue("testGetSetStringValue", "test");

        assertEquals("Test Failure: Int set or get failed", "test", settings.getStringValueOrDefault("testGetSetStringValue"));
    }

    public void testGetDefaultTheme() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        assertEquals("Test Failure: Default Theme is wrong", 0, settings.getTheme());
    }

    public void testGetSetTheme() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        settings.setTheme(1);

        assertEquals("Test Failure: Theme set or get failed", 1, settings.getTheme());
    }

    public void testGetDefaultGameplay() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        assertEquals("Test Failure: Default Theme is wrong", 0, settings.getGameplay());
    }

    public void testGetSetGameplay() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        settings.setGameplay(1);

        assertEquals("Test Failure: Theme set or get failed", 1, settings.getGameplay());
    }

    public void testGetDefaultHighScore() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        assertEquals("Test Failure: Default Theme is wrong", 0, settings.getHighScore(0));
    }

    public void testGetSetHighScore() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        settings.setHighScore(0, 1);

        assertEquals("Test Failure: Theme set or get failed", 1, settings.getHighScore(0));
    }

    public void testGetDefaultRandomBotLocation() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        assertEquals("Test Failure: Default Theme is wrong", null, settings.getRandomBotLocation());
    }

    public void testGetSetRandomBotLocation() {
        SettingsStorageManager settings = new SettingsStorageManager(getInstrumentation().getTargetContext());

        settings.setRandomBotLocation("path");

        assertEquals("Test Failure: Theme set or get failed", "path", settings.getRandomBotLocation());
    }

}
