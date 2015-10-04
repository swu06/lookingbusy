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
    private int endXsecondaryLocation;
    private int endYsecondaryLocation;

    private boolean wasOnTimedLevel;

    public GameInfoDisplay(PopAllTheThingsGame game) {
        this.game = game;
        readyToPause = false;
        whiteFont = new TextPaint();
        whiteFont.setTextSize(100);
        whiteFont.setTextAlign(Paint.Align.CENTER);
        whiteFont.setColor(Color.WHITE);
        whiteFont.setTypeface(Typeface.DEFAULT_BOLD);
        whiteFont.setAlpha(200);
        whiteFont.setAntiAlias(true);

        blackOutline = new TextPaint();
        blackOutline.setTextSize(100);
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
        endXsecondaryLocation = startXsecondaryLocation + game.getThemeManager().getPauseSign().getWidth();
        endYsecondaryLocation = startYsecondaryLocation + game.getThemeManager().getPauseSign().getHeight();

        wasOnTimedLevel = false;

        Log.d(LOGGER, "SecondaryLocation = "+ startXsecondaryLocation);

    }

    public void setAlpha(int alpha) {
        translucentPainter.setAlpha(alpha);
    }

    public void draw(Canvas canvas) {
        GameplayManager manager = game.getGameplayManager();
        if(manager.isGameOver()) {
            displayGameOverScreen(canvas, manager);

        } else {
            canvas.drawText(manager.getScoreDisplayString(), game.getWidth() / 2, 300, whiteFont);
            canvas.drawText(manager.getScoreDisplayString(), game.getWidth() / 2, 300, blackOutline);
            float height = 300 + blackOutline.descent() - blackOutline.ascent();

            if (manager.isLifeRestrictedMode()) {
                canvas.drawText(manager.getLivesRemainingString(), game.getWidth() / 2, height, whiteFont);
                canvas.drawText(manager.getLivesRemainingString(), game.getWidth() / 2, height, blackOutline);
                height = height + blackOutline.descent() - blackOutline.ascent();
            }

            if (manager.isTimedLevel()) {
                wasOnTimedLevel = true;
                canvas.drawText(manager.getTimeRemainingDisplayString(), game.getWidth() / 2, height, whiteFont);
                canvas.drawText(manager.getTimeRemainingDisplayString(), game.getWidth() / 2, height, blackOutline);
                height = height + blackOutline.descent() - blackOutline.ascent();
            } else if (wasOnTimedLevel) {
                game.clearObjects();
                wasOnTimedLevel = false;
            }

            if (manager.isHighScore()) {
                canvas.drawText("High Score!!", game.getWidth() / 2, height, whiteFont);
                canvas.drawText("High Score!!", game.getWidth() / 2, height, blackOutline);
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
        canvas.drawText("GAME OVER", game.getWidth() / 2, game.getHeight() / 2, whiteFont);
        canvas.drawText("GAME OVER", game.getWidth() / 2, game.getHeight() / 2, blackOutline);
        if (manager.isHighScore()) {
            whiteFont.setTextSize(75);
            blackOutline.setTextSize(75);
            float height = game.getHeight() - (game.getHeight() / 4);
            canvas.drawText("New High Score:",
                    game.getWidth() / 2,
                    height,
                    whiteFont);
            canvas.drawText("New High Score:",
                    game.getWidth() / 2,
                    height,
                    blackOutline);
            height = height + blackOutline.descent() - blackOutline.ascent();
            whiteFont.setTextSize(100);
            blackOutline.setTextSize(100);
            canvas.drawText("" + manager.getNewHighScore(),
                    game.getWidth() / 2,
                    height,
                    whiteFont);
            canvas.drawText("" + manager.getNewHighScore(),
                    game.getWidth() / 2,
                    height,
                    blackOutline);
        }
        whiteFont.setAlpha(200);
        blackOutline.setAlpha(200);
    }

    public void handleOnDown(float x, float y) {
        if(startXprimaryLocation <= x && x <= endXprimaryLocation
                && startYprimaryLocation <= y && y <= endYprimaryLocation){
            Log.d(LOGGER, "Ready to Pause");
            readyToPause = true;
        }
    }

    /*
     * The "OnUp" part is very permissive to give a better user experience
     * In reality, if the user moves the pause button at all, they probably intend
     * to pause, so we'll give them the benefit of the doubt
     */
    public void handleOnUp(float x, float y) {
        if(readyToPause && startXsecondaryLocation - MARGIN <= x
                && y <= endYsecondaryLocation + MARGIN){
            game.pause();
        }
        readyToPause = false;
    }
}
