package com.gamedev.techtronic.lunargame.Screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.gamedev.techtronic.lunargame.LunarGame;
import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

public class LunarScoreScreen extends GameScreen {

    private LunarGame game;
    private enum ScreenState {TALLY_PLAYER_SCORE, TALLY_LIVES_LEFT, CHECK_LEVEL_COMPLETED, TALLY_TIME_TAKEN, TALLY_TOTAL_SCORE, DISPLAY_MESSAGE, WAITING_ON_PLAYER_INPUT}
    private ScreenState screenState = ScreenState.TALLY_PLAYER_SCORE;
    private Paint backgroundPaint, textPaint;
    private String deathOrLevelFinish, whatLevelCompleted, commentOnScore;
    private float score, livesLeft, timeToCompleteLevel, totalScore, loopCount, scoreGainedFromTimeTaken, scoreIncrementFromTimeTaken;
    private boolean waitCompleted = false, wasLevelCompleted = true, displayTotalScoreComment = false,
    displayScoreFromLevelCompleted = false, displayMessageAboutTimeTaken = false, reEnabledInputYet = false, displayTouchScreenComment = false;
    GameScreen lunarLevelSelectScreen;

    //Default constructor for the score screen
    public LunarScoreScreen(Game game, String whatLevelCompleted, float score, float timeToCompleteLevel, float livesLeft) {
        super("LunarScoreScreen", game);
        //disableInput = false;
        fadeFromColour("black", 5, 0, 0);
        super.initViewports(500.0F, 287.0F, 500.0F);
        super.initAssets();
        this.whatLevelCompleted = whatLevelCompleted;
        this.score = score;
        this.timeToCompleteLevel = timeToCompleteLevel;
        this.livesLeft = livesLeft;
        this.addComponents();
    }

    //Initialise all graphical assets used in the score screen
    protected void initBitmaps() {

    }

    //Initialise all audio assets used in the score screen
    protected void initSounds() {
        mAssetManager.loadAndAddMusic("bestiaryBackgroundMusic", "music/bestiaryItemsSong.ogg");
        mAssetManager.loadAndAddSound("regularScoreIncrement", "sfx/inGame/dialogueSpeech1.ogg");
        mAssetManager.loadAndAddSound("regularScoreTallied", "sfx/inGame/pickUp1.ogg");
        mAssetManager.loadAndAddSound("livesLevelScoreIncrement", "sfx/inGame/pickUp1.ogg");
        mAssetManager.loadAndAddSound("allScoreTallied", "sfx/inGame/playerHurt.ogg");

      /*  if (!mAssetManager.getMusic("bestiaryBackgroundMusic").isPlaying()) {
            mAssetManager.getMusic("bestiaryBackgroundMusic").play();
            mAssetManager.getMusic("bestiaryBackgroundMusic").setLooping(true);
        }*/
    }

    //Add game objects to the score screen
    protected void addComponents() {

        game = (LunarGame)this.getGame();
        //lunarGameScreen = mGame.getScreenManager().getScreen("LunarGameScreen");

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        Color backgroundColor = new Color();
        backgroundPaint.setColor(Color.parseColor("#1d494b"));

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.LEFT);

        //score = LunarGameScreen.player.score;
        //score = 4320;
        //livesLeft = 3;
        //lives = LunarGameScreen.player.livesLeft;
        timeToCompleteLevel = 192;
        totalScore = 0;

        if(whatLevelCompleted == "died") {
            wasLevelCompleted = false;
            Log.d("L", "LEVEL WASN'T COMPLETEDDDDDDDDDDDDDDDDDD");
        }

        // Calculating how much score the player has earned for how fast they completed the level
        // Faster time = better score
        scoreGainedFromTimeTaken = (1000 - timeToCompleteLevel) * 5;

        // Calculating how much to increase the total score by per "tick" when the total score is being calculated
        // This is used for visual appeal when tallying the score
        scoreIncrementFromTimeTaken = (scoreGainedFromTimeTaken / timeToCompleteLevel)*10;


    }

    private void resolveTouchEvents(LunarGame game) {
        if (screenState == ScreenState.WAITING_ON_PLAYER_INPUT) {
            if (mGame.getInput().existsTouch(0)) {
                fadeToColourAndSwitchScreen("black", 4, 1000, 800, 0, "LunarLevelSelectScreen");
                disableInput = true;
            }
        }
    }

    //The score screen's update method
    @Override
    public void update(ElapsedTime elapsedTime) {

        if (exitScreen) {
            if (screenFadeTo == "LunarLevelSelectScreen") {
                lunarLevelSelectScreen = mGame.getScreenManager().getScreen("LunarLevelSelectScreen");
                mGame.getScreenManager().removeScreen(this.mName);
                mGame.getScreenManager().removeScreen("LunarGameScreen");
                mGame.getScreenManager().setAsCurrentScreen("LunarLevelSelectScreen");
                lunarLevelSelectScreen.fadeFromColour("black", 8, 0, 0);
            }

            this.resetFade();
            exitScreen = false;
            System.gc();
        }
        if (!disableInput) {
            resolveTouchEvents(game);
        }

        if (screenState == ScreenState.TALLY_PLAYER_SCORE) {
            loopCount++;
            if (loopCount >=3) {
                Log.d("test", "test");
                if (score >=100) {
                    score-=100;
                    totalScore+=100;
                    mAssetManager.getSound("regularScoreIncrement").play();
                }
                else if (score <100 && score > 0) {
                    totalScore += score;
                    score = 0;
                    //mAssetManager.getSound("regularScoreTallied").play();
                }
                else {
                    screenState = ScreenState.TALLY_LIVES_LEFT;
                    waitCompleted = false;
                }
                loopCount = 0;
            }
        }
        else if (screenState == ScreenState.TALLY_LIVES_LEFT) {
            if (!waitCompleted) {
                pauseTally(100);
            }
            loopCount++;
            if (loopCount >= 40) {
                if (livesLeft > 0) {
                    livesLeft--;
                    totalScore+=1000;
                    mAssetManager.getSound("livesLevelScoreIncrement").play();
                }
                else {
                    screenState = ScreenState.CHECK_LEVEL_COMPLETED;
                    waitCompleted = false;
                    loopCount = 0;
                }
                loopCount = 0;
            }
        }
        else if (screenState == ScreenState.CHECK_LEVEL_COMPLETED) {
            if (!waitCompleted) {
                pauseTally(1000);
                Log.d("thread sleep debug", "thread sleeping, state is check_level_completed");
            }
            if (wasLevelCompleted) {
                totalScore *= 2;
                mAssetManager.getSound("livesLevelScoreIncrement").play();
            }
            else {
                mAssetManager.getSound("livesLevelScoreIncrement").play();
            }
            displayScoreFromLevelCompleted = true;
            waitCompleted = false;
            screenState = ScreenState.TALLY_TIME_TAKEN;

        }
        else if (screenState == ScreenState.TALLY_TIME_TAKEN) {
            if (!waitCompleted) {
                pauseTally(1000);
                Log.d("thread sleep debug", "thread sleeping, state is tally time taken");
            }
            if (wasLevelCompleted) {
                loopCount++;
                if (loopCount >= 3) {
                    Log.d("test", "test");
                    if (timeToCompleteLevel >= 10) {
                        timeToCompleteLevel -= 10;
                        totalScore += scoreIncrementFromTimeTaken;
                        mAssetManager.getSound("regularScoreIncrement").play();
                    } else if (timeToCompleteLevel < 10 && timeToCompleteLevel > 0) {
                        totalScore += (scoreIncrementFromTimeTaken * 0.1 * timeToCompleteLevel);
                        timeToCompleteLevel = 0;
                        mAssetManager.getSound("regularScoreIncrement").play();
                        //mAssetManager.getSound("regularScoreTallied").play();
                    } else {
                        pauseTally(600);
                        screenState = ScreenState.DISPLAY_MESSAGE;
                        waitCompleted = false;
                    }
                    loopCount = 0;
                }
            }
            else {
                displayMessageAboutTimeTaken = true;
                pauseTally(600);
                screenState = ScreenState.DISPLAY_MESSAGE;
                waitCompleted = false;
            }
        }
        else if (screenState == ScreenState.DISPLAY_MESSAGE) {
            if (!waitCompleted) {
                pauseTally(1000);
                Log.d("thread sleep debug", "thread sleeping, state is display message");
            }
            if (totalScore >= 18000) {
                commentOnScore = "good score";
            }
            else if (totalScore <18000 && totalScore >=9000) {
                commentOnScore = "ok score";
            }
            else {
                commentOnScore = "bad score";
            }
            screenState = ScreenState.WAITING_ON_PLAYER_INPUT;
            waitCompleted = false;
            displayTotalScoreComment = true;
        }
        else if (screenState == ScreenState.WAITING_ON_PLAYER_INPUT) {
            if (!waitCompleted) {
                pauseTally(1500);
            }
            displayTouchScreenComment = true;
            if (!reEnabledInputYet) {
                disableInput = false;
                reEnabledInputYet = true;
            }
            if (!waitCompleted) {
                pauseTally(1000);
            }
        }
    }

    //The score screen's render method
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top,
                mScreenViewport.right, mScreenViewport.bottom, backgroundPaint);

        textPaint.setTextSize(40);
        textPaint.setColor(Color.WHITE);
        graphics2D.drawText("Score: " + score, mScreenViewport.centerX()-600,
                mScreenViewport.centerY() - 200, textPaint);
        graphics2D.drawText("Lives left: " + livesLeft, mScreenViewport.centerX()-600,
                mScreenViewport.centerY() - 130, textPaint);
        graphics2D.drawText("Completed the level: " + wasLevelCompleted, mScreenViewport.centerX()-600,
                mScreenViewport.centerY() - 60, textPaint);
        graphics2D.drawText("Time taken to finish level: " + timeToCompleteLevel + " seconds", mScreenViewport.centerX()-600,
                mScreenViewport.centerY() + 10, textPaint);
        textPaint.setTextSize(80);
        graphics2D.drawText("Total Score: " + totalScore, mScreenViewport.centerX()-600,
                mScreenViewport.centerY() + 110, textPaint);

        textPaint.setTextSize(30);
        textPaint.setColor(Color.RED);
        if (displayTotalScoreComment) {
            graphics2D.drawText("" + commentOnScore, mScreenViewport.centerX() +100,
                    mScreenViewport.centerY() + 210, textPaint);
        }

        if (displayScoreFromLevelCompleted) {
            if (wasLevelCompleted) {
                graphics2D.drawText("LEVEL COMPLETED, SCORE DOUBLED", mScreenViewport.centerX() + 100,
                        mScreenViewport.centerY() -60, textPaint);
            }
            else {
                graphics2D.drawText("LEVEL FAILED.. NO BONUS :(", mScreenViewport.centerX() + 100,
                        mScreenViewport.centerY() -60, textPaint);
            }
        }

        if (displayMessageAboutTimeTaken) {
            graphics2D.drawText("LEVEL FAILED.. TIME NOT COUNTED :(", mScreenViewport.centerX() + 85,
                    mScreenViewport.centerY() + 55, textPaint);
        }

        if (displayTouchScreenComment) {
            textPaint.setTextSize(60);
            textPaint.setColor(Color.WHITE);
            graphics2D.drawText("Thanks for playing! (Touch Screen)", mScreenViewport.centerX() - 500,
                    mScreenViewport.centerY() + 300, textPaint);
        }

        super.draw(elapsedTime, graphics2D);

    }

    @Override
    public void dispose() {
        mGame.getScreenManager().dispose();
    }

    public void pause() {
        if (mAssetManager.getMusic("bestiaryBackgroundMusic").isPlaying()) {

            Log.d("test", "score screen paused");
            mAssetManager.getMusic("bestiaryBackgroundMusic").stop();

        }

    }

    private void pauseTally(int pauseTime) {
        try {
            Thread.sleep(pauseTime);
            waitCompleted = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

