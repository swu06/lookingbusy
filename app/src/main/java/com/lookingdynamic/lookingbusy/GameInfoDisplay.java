package com.lookingdynamic.lookingbusy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;

import com.lookingdynamic.lookingbusy.gameplay.GameplayManager;

/**
 * This class handles displaying all of the necessary game info behind the poppable objects.
 *
 * Created by swu on 9/27/2015.
 */
public class GameInfoDisplay {

    private static final String LOGGER = GameInfoDisplay.class.getSimpleName();

    private PopAllTheThingsGame game;
    private TextPaint whiteFont;
    private TextPaint blackOutline;
    private Paint whiteLine;
    private Paint blackLine;
    public Paint translucentPainter;

    private boolean readyToPause;
    private static final int MARGIN = 100;
    private int startXprimaryLocation;
    private int startYprimaryLocation;
    private int endXprimaryLocation;
    private int endYprimaryLocation;
    private int startXsecondaryLocation;
    private int startYsecondaryLocation;
    private int endYsecondaryLocation;

    private boolean wasOnTimedLevel;

    public GameInfoDisplay(PopAllTheThingsGame game) {
        this.game = game;
        readyToPause = false;
        whiteFont = new TextPaint();
        whiteFont.setTextSize(150);
        whiteFont.setTextAlign(Paint.Align.CENTER);
        whiteFont.setColor(Color.WHITE);
        whiteFont.setTypeface(Typeface.DEFAULT_BOLD);
        whiteFont.setAlpha(200);
        whiteFont.setAntiAlias(true);

        blackOutline = new TextPaint();
        blackOutline.setTextSize(150);
        blackOutline.setTextAlign(Paint.Align.CENTER);
        blackOutline.setColor(Color.BLACK);
        blackOutline.setTypeface(Typeface.DEFAULT_BOLD);
        blackOutline.setStyle(Paint.Style.STROKE);
        blackOutline.setStrokeWidth(2);
        blackOutline.setAlpha(200);
        blackOutline.setAntiAlias(true);

        whiteLine = new Paint();
        whiteLine.setColor(Color.WHITE);
        whiteLine.setStyle(Paint.Style.FILL_AND_STROKE);
        whiteLine.setStrokeWidth(5);
        whiteLine.setAlpha(200);

        blackLine = new Paint();
        blackLine.setColor(Color.BLACK);
        blackLine.setStyle(Paint.Style.STROKE);
        blackLine.setStrokeWidth(10);
        blackLine.setAlpha(200);

        translucentPainter =  new Paint();

        startXprimaryLocation = MARGIN;
        startYprimaryLocation = MARGIN;
        endXprimaryLocation = startXprimaryLocation + game.getThemeManager().getPauseSign().getWidth();
        endYprimaryLocation = startYprimaryLocation + game.getThemeManager().getPauseSign().getHeight();

        startXsecondaryLocation = game.getWidth() - game.getThemeManager().getPauseSign().getWidth() - MARGIN;
        startYsecondaryLocation = MARGIN;
        endYsecondaryLocation = startYsecondaryLocation + game.getThemeManager().getPauseSign().getHeight();

        wasOnTimedLevel = false;

        Log.v(LOGGER, "SecondaryLocation for the Pause Sign calculated as "+ startXsecondaryLocation);

    }

    public void resetAlpha() {
        translucentPainter.setAlpha(200);
    }

    public void draw(Canvas canvas) {
        GameplayManager manager = game.getGameplayManager();
        if (manager.isGameOver()) {
            displayGameOverScreen(canvas, manager);

        } else {
            float width = canvas.getWidth();
            float height = canvas.getHeight();
            String scoreDisplay = manager.getScoreDisplayString();
            resizeTextIfNeeded(scoreDisplay, width - 50);
            canvas.drawText(scoreDisplay, width / 2, height / 4, whiteFont);
            canvas.drawText(scoreDisplay, width / 2, height / 4, blackOutline);
            height = height / 4 + blackOutline.descent() - blackOutline.ascent();

            if (manager.isLifeRestrictedMode()) {
                String livesString = manager.getLivesRemainingString();
                resizeTextIfNeeded(livesString, width - 50);
                canvas.drawText(livesString, width / 2, height, whiteFont);
                canvas.drawText(livesString, width / 2, height, blackOutline);
                height = height + blackOutline.descent() - blackOutline.ascent();
            }

            if (manager.isTimedLevel()) {
                wasOnTimedLevel = true;
                String timeString = manager.getTimeRemainingDisplayString();
                resizeTextIfNeeded(timeString, width - 50);
                canvas.drawText(timeString, width / 2, height, whiteFont);
                canvas.drawText(timeString, width / 2, height, blackOutline);
                height = height + blackOutline.descent() - blackOutline.ascent();
            } else if (wasOnTimedLevel) {
                game.clearObjects();
                wasOnTimedLevel = false;
            }

            if (manager.isHighScore()) {
                String highScoreString = "High Score!!";
                resizeTextIfNeeded(highScoreString, width - 50);

                canvas.drawText(highScoreString, width / 2, height, whiteFont);
                canvas.drawText(highScoreString, width / 2, height, blackOutline);
            }

            if (readyToPause) {
                drawPauseArrow(canvas);
            } else {
                canvas.drawBitmap(game.getThemeManager().getPauseSign(),
                        startXprimaryLocation,
                        startYprimaryLocation,
                        translucentPainter);
            }
        }
    }

    private void drawPauseArrow(Canvas canvas) {

        Paint triangle = new Paint();
        triangle.setStrokeWidth(2);
        triangle.setColor(Color.WHITE);
        triangle.setStyle(Paint.Style.FILL_AND_STROKE);
        triangle.setAntiAlias(true);
        triangle.setAlpha(200);

        int middleOfPauseSign = MARGIN + game.getThemeManager().getPauseSign().getHeight() / 2;
        canvas.drawLine(MARGIN * 2,
                middleOfPauseSign,
                startXsecondaryLocation - 30,
                middleOfPauseSign,
                blackLine);
        canvas.drawLine(MARGIN * 2 + 5,
                middleOfPauseSign,
                startXsecondaryLocation - 30,
                middleOfPauseSign,
                whiteLine);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(startXsecondaryLocation - 35, middleOfPauseSign - 15);
        path.lineTo(startXsecondaryLocation - 5, middleOfPauseSign);
        path.lineTo(startXsecondaryLocation - 35, middleOfPauseSign + 15);
        path.lineTo(startXsecondaryLocation - 35, middleOfPauseSign - 15);
        path.close();

        canvas.drawPath(path, blackLine);
        canvas.drawPath(path, triangle);

        canvas.drawBitmap(game.getThemeManager().getPauseSign(),
                startXsecondaryLocation,
                startYsecondaryLocation,
                translucentPainter);
    }

    private void displayGameOverScreen(Canvas canvas, GameplayManager manager) {
        whiteFont.setAlpha(255);
        blackOutline.setAlpha(255);
        int width = canvas.getWidth();
        String gameOverString = "GAME OVER";
        resizeTextIfNeeded(gameOverString, width - 50);

        canvas.drawText(gameOverString, width / 2, game.getHeight() / 2, whiteFont);
        canvas.drawText(gameOverString, width / 2, game.getHeight() / 2, blackOutline);
        if (manager.isHighScore()) {
            float height = game.getHeight() - (game.getHeight() / 4);

            String newScore = "New High Score:";
            resizeTextIfNeeded(newScore, width - 50);
            canvas.drawText(newScore, width / 2, height, whiteFont);
            canvas.drawText(newScore, width / 2, height, blackOutline);
            height = height + blackOutline.descent() - blackOutline.ascent();

            String score = "" + manager.getNewHighScore();
            resizeTextIfNeeded(score, width - 50);
            canvas.drawText(score, width / 2, height, whiteFont);
            canvas.drawText(score, width / 2, height, blackOutline);
        }
        whiteFont.setAlpha(200);
        blackOutline.setAlpha(200);
    }

    public void handleOnDown(float x, float y) {
        if (startXprimaryLocation <= x && x <= endXprimaryLocation
                && startYprimaryLocation <= y && y <= endYprimaryLocation) {
            Log.v(LOGGER, "Ready to Pause");
            readyToPause = true;
        }
    }

    /*
     * The "OnUp" part is very permissive to give a better user experience
     * In reality, if the user moves the pause button at all, they probably intend
     * to pause, so we'll give them the benefit of the doubt
     */
    public void handleOnUp(float x, float y) {
        if (readyToPause && startXsecondaryLocation - MARGIN <= x
                && y <= endYsecondaryLocation + MARGIN) {
            game.pause();
        }
        readyToPause = false;
    }

    private void resizeTextIfNeeded(String str, float maxWidth) {
        float size = whiteFont.getTextSize();

        while (whiteFont.measureText(str) > maxWidth) {
            size--;
            whiteFont.setTextSize(size);
            blackOutline.setTextSize(size);
        }
    }
}
