package com.lookingdynamic.lookingbusy.gameplay;

import android.test.ActivityTestCase;

/**
 * Created by swu on 9/20/2015.
 */
public class GameplayManagerTest extends ActivityTestCase{

    public SettingsStorageManager settings;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        settings = new SettingsStorageManager(getInstrumentation().getTargetContext());
        settings.setGameplay(0);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        settings.setGameplay(0);
        settings.setHighScore(0, 0);
        settings.setHighScore(1, 0);
    }

    public void beforeClass() {
        settings.setGameplay(0);
        settings.setHighScore(0, 0);
        settings.setHighScore(1, 0);
    }

    public void testCreateGameplayManager() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        assertNotNull("Test Failure: SettingsStorageManager failed to initiatlize", gameplay.settings);
        assertNotNull("Test Failure: GamplayModes failed to initiatlize", gameplay.modes);
        assertEquals("Test Failure: currentHighScore should start at 0", 0, gameplay.currentHighScore);
        assertEquals("Test Failure: score should start at 0", 0, gameplay.score);
        assertEquals("Test Failure: newHighScore should start at 0", 0, gameplay.newHighScore);
        assertEquals("Test Failure: currentLevel should start at 0", 0, gameplay.currentLevel);
        assertEquals("Test Failure: currentMode should start at 0", 0, gameplay.currentMode);
        assertTrue("Test Failure: pointsToNextLevel should start at 0 or greater", 0 <= gameplay.pointsToNextLevel);
    }

    public void testAddToScoreIncreasesScore() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.addToScore(10);
        assertEquals("Test Failure: Score should be added directly", 10, gameplay.score);
    }

    public void testAddToScoreLevelsUp() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.pointsToNextLevel = 5;
        gameplay.addToScore(10);
        assertEquals("Test Failure: Score should increase currentLevel by 1", 1, gameplay.getLevel());
    }

    public void testAddToScoreDoesntLevelUp() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.pointsToNextLevel = 0;
        gameplay.addToScore(10);
        assertEquals("Test Failure: Score should increase currentLevel by 1", 0, gameplay.getLevel());
    }

    public void testAddToScoreDoesntLevelUpAgain() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.pointsToNextLevel = 20;
        gameplay.addToScore(10);
        assertEquals("Test Failure: Score should increase currentLevel by 1", 0, gameplay.getLevel());
    }

    public void testAddToScoreAddsToHighScore() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.addToScore(10);
        assertEquals("Test Failure: Score should increase currentLevel by 1", 10, gameplay.newHighScore);
    }

    public void testAddToScoreSignalsNewHighScore() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.currentHighScore = 1;
        boolean newHighScore = gameplay.addToScore(10);
        assertTrue("Test Failure: Should have a new high score", newHighScore);
    }

    public void testAddToScoreNoResignal() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.currentHighScore = 1;
        gameplay.newHighScore = 10;
        boolean newHighScore = gameplay.addToScore(10);
        assertFalse("Test Failure: Should not re-signal when a score is reached again", newHighScore);
    }

    public void testGetDisplayStringWithLevelName() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        assertEquals("Test Failure: Level Name should be prepended to score", "Lvl 1: 0", gameplay.getScoreDisplayString());
    }

    public void testGetDisplayStringWithoutLevelName() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.setGameplayMode(3);
        assertEquals("Test Failure: Did not handle blank levelname", "0", gameplay.getScoreDisplayString());
    }

    public void testLevelUp() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.setGameplayMode(3);
        gameplay.pointsToNextLevel = 1;
        gameplay.levelUp();
        assertEquals("Test Failure: Did not increment currentLevel", 1, gameplay.getLevel());
        assertEquals("Test Failure: Did not reset pointsToNextLevel", 0, gameplay.pointsToNextLevel);
    }

    public void testSetGameplayMode() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.score = 1;
        gameplay.newHighScore = 2;
        gameplay.currentHighScore = 1;
        gameplay.currentLevel = 100;

        gameplay.setGameplayMode(1);
        assertEquals("Test Failure: HighScore Should be saved", 2, settings.getHighScore(0));
        assertEquals("Test Failure: Mode should be updated", 1, gameplay.getCurrentID());
        assertEquals("Test Failure: Mode should be saved", 1, settings.getGameplay());
        assertEquals("Test Failure: Score should be reset", 0, gameplay.score);
        assertEquals("Test Failure: NewHighScore should be reset", 0, gameplay.newHighScore);
        assertEquals("Test Failure: CurrentHighScore should be reset", 0, gameplay.currentHighScore);
        assertEquals("Test Failure: Level should be reset", 0, gameplay.getLevel());
    }

    public void testStoreHighScoresWithNewHighScore() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.newHighScore = 2;
        gameplay.currentHighScore = 1;
        gameplay.storeHighScore();

        assertEquals("Test Failure: HighScore Should be saved", 2, settings.getHighScore(0));
    }

    public void testStoreHighScoresWithoutNewHighScore() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        gameplay.newHighScore = 2;
        gameplay.currentHighScore = 2;
        gameplay.storeHighScore();

        assertEquals("Test Failure: HighScore Should not be saved", 0, settings.getHighScore(0));
    }

    public void testDisplayScores() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        settings.setHighScore(1, 200);

        String[] scores = gameplay.getHighScores();
        assertEquals("Test Failure: There should be scores for each mode", 4, scores.length);
        assertTrue("Test Failure: HighScore should be saved", scores[0].endsWith(" 0"));
        assertTrue("Test Failure: HighScore should be saved", scores[1].endsWith(" 200"));
    }

    public void testClearScores() {
        beforeClass();
        GameplayManager gameplay = new GameplayManager(settings,
                getInstrumentation().getTargetContext().getResources());

        settings.setHighScore(0, 100);
        settings.setHighScore(1, 200);

        gameplay.clearAllScores();
        String[] scores = gameplay.getHighScores();
        assertEquals("Test Failure: There should be cleared scores for each mode", 4, scores.length);
        assertTrue("Test Failure: HighScore should be cleared", scores[0].endsWith(" 0"));
        assertTrue("Test Failure: HighScore should be cleared", scores[1].endsWith(" 0"));
    }


}
