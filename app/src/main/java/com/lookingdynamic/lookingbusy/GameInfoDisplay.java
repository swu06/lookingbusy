package com.lookingdynamic.lookingbusy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;

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

        blackOutline = new TextPaint();
        blackOutline.setTextSize(100);
        blackOutline.setTextAlign(Paint.Align.CENTER);
        blackOutline.setColor(Color.BLACK);
        blackOutline.setTypeface(Typeface.DEFAULT_BOLD);
        blackOutline.setStyle(Paint.Style.STROKE);
        blackOutline.setStrokeWidth(4);
        blackOutline.setAlpha(200);

        whiteLine = new Paint();
        whiteLine.setColor(Color.WHITE);

        whiteLine.setStyle(Paint.Style.FILL_AND_STROKE);
        whiteLine.setStrokeWidth(10);
        whiteLine.setAlpha(200);

        blackLine = new Paint();
        blackLine.setColor(Color.BLACK);
        blackLine.setStyle(Paint.Style.STROKE);
        blackLine.setStrokeWidth(20);
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

        canvas.drawText(game.getGameplayManager().getScoreDisplayString(), game.getWidth() / 2, 300, whiteFont);
        canvas.drawText(game.getGameplayManager().getScoreDisplayString(), game.getWidth() / 2, 300, blackOutline);
        float height = 300 + blackOutline.descent() - blackOutline.ascent();

        if(game.getGameplayManager().isTimedLevel()) {
            wasOnTimedLevel = true;
            canvas.drawText(game.getGameplayManager().getTimeRemainingDisplayString(), game.getWidth() / 2, height, whiteFont);
            canvas.drawText(game.getGameplayManager().getTimeRemainingDisplayString(), game.getWidth() / 2, height, blackOutline);
            height = height + blackOutline.descent() - blackOutline.ascent();
        } else if(wasOnTimedLevel) {
            game.clearObjects();
            wasOnTimedLevel = false;
        }
        if(game.getGameplayManager().isHighScore()) {
            canvas.drawText("High Score!!", game.getWidth() / 2, height, whiteFont);
            canvas.drawText("High Score!!", game.getWidth() / 2, height, blackOutline);
        }

        if(readyToPause) {
            canvas.drawBitmap(game.getThemeManager().getPauseSign(),
                    startXsecondaryLocation,
                    startYsecondaryLocation,
                    translucentPainter);
            int middleOfPauseSign = MARGIN + game.getThemeManager().getPauseSign().getHeight() / 2;
            canvas.drawLine(MARGIN*2,
                    middleOfPauseSign,
                    startXsecondaryLocation - 10,
                    middleOfPauseSign,
                    blackLine);
            canvas.drawLine(MARGIN*2 + 5,
                    middleOfPauseSign,
                    startXsecondaryLocation - 15,
                    middleOfPauseSign,
                    whiteLine);

        } else {
            canvas.drawBitmap(game.getThemeManager().getPauseSign(), startXprimaryLocation, startYprimaryLocation, translucentPainter);
        }
    }

    public void handleOnDown(float x, float y) {
        Log.d(LOGGER, "Handling the Down");
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
        Log.d(LOGGER, "Handling the Up");
        if(readyToPause && startXsecondaryLocation - MARGIN <= x
                && y <= endYsecondaryLocation + MARGIN){
            game.pause();
        }
        readyToPause = false;
    }
}
