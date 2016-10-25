package com.gamedev.techtronic.lunargame.Screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.gamedev.techtronic.lunargame.LunarGame;
import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.ui.PushButton;
import com.gamedev.techtronic.lunargame.gage.world.GameObject;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

public class LunarPauseScreen extends GameScreen {

    public enum ScreenState {RUNNING, READY_TO_QUIT}
    private static ScreenState screenState = ScreenState.RUNNING;
    private LunarGame game;
    private GameObject pauseBackground;
    private PushButton resumeButton, bestiaryButton, optionsButton, levelSelectButton, quitGameButton, confirmQuitButton, cancelQuitButton;
    private Paint backgroundPaint, confirmPaint, textPaint;
    private GameScreen lunarGameScreen, lunarLevelSelectScreen;

    //Default constructor for the pause screen
    public LunarPauseScreen(Game game) {
        super("LunarPauseScreen", game);

        fadeFromColour("black", 8, 0, 0);
        super.initViewports(500.0F, 287.0F, 500.0F);
        super.initAssets();
        this.addComponents();
    }

    //Initialise all graphical assets used in the pause screen
    protected void initBitmaps() {

        mAssetManager.loadAndAddBitmap("pauseBackground", "img/menus/options/optionsBackground.png");
        mAssetManager.loadAndAddBitmap("placeholder", "img/menus/options/optionsButtonClose.png");
    }

    //Initialise all audio assets used in the pause screen
    protected void initSounds() {}

    //Add game objects to the pause screen
    protected void addComponents() {

        game = (LunarGame)this.getGame();

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        //Color backgroundColor = new Color();
       // backgroundPaint.setColor(backgroundColor.parseColor("#1d494b"));
        backgroundPaint.setColor(Color.DKGRAY);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(70);
        textPaint.setTextAlign(Paint.Align.CENTER);

        confirmPaint = new Paint();
        confirmPaint.setColor(Color.argb(230, 0, 0, 0));
        confirmPaint.setStyle(Paint.Style.FILL);

        pauseBackground = new GameObject(mLayerViewport.getWidth() * 0.5F, mLayerViewport.getHeight() * 0.5F,
                mLayerViewport.getWidth() * 0.7F, mLayerViewport.getHeight() * 0.8F, mAssetManager.getBitmap("pauseBackground"), this);

        resumeButton = new PushButton(600, 100, 150, 120,"placeholder", "placeholder", this);
        bestiaryButton = new PushButton(600, 220, 150, 120,"placeholder", "placeholder", this);
        optionsButton = new PushButton(600, 340, 150, 120,"placeholder", "placeholder", this);
        levelSelectButton = new PushButton(600, 460, 150, 120,"placeholder", "placeholder", this);
        quitGameButton = new PushButton(600, 580, 150, 120,"placeholder", "placeholder", this);
        confirmQuitButton = new PushButton(440, 400, 150, 120,"placeholder", "placeholder", this);
        cancelQuitButton = new PushButton(840, 400, 150, 120,"placeholder", "placeholder", this);
    }

    private void resolveTouchEvents(LunarGame game) {

        if (screenState == ScreenState.RUNNING) {

            if (resumeButton.pushTriggered()) {
                fadeToColourAndSwitchScreen("black", 8, 0, 0, 0, "LunarGameScreen");
            }
            else if (optionsButton.pushTriggered()) {
                fadeToColourAndSwitchScreen("black", 8, 0, 0, 0, "LunarOptionsScreen");
            }
            else if (bestiaryButton.pushTriggered()) {
                fadeToColourAndSwitchScreen("black", 8, 0, 0, 0, "LunarBestiaryScreen");
            }
            else if (levelSelectButton.pushTriggered()) {
                fadeToColourAndSwitchScreen("black", 8, 0, 0, 0, "LunarLevelSelectScreen");
            }
            else if (quitGameButton.pushTriggered()) {
                screenState = ScreenState.READY_TO_QUIT;
            }
        }
        else if (screenState == ScreenState.READY_TO_QUIT) {

            if(confirmQuitButton.pushTriggered()) {
                fadeToColourAndSwitchScreen("black", 8, 0, 500, 0, "LunarSplashScreen");
            }
            else if (cancelQuitButton.pushTriggered()) {
                screenState = ScreenState.RUNNING;
            }
        }


    }

    //The pause screen's update method
    @Override
    public void update(ElapsedTime elapsedTime){


        if (screenState == ScreenState.RUNNING) {
            resumeButton.update(elapsedTime);
            optionsButton.update(elapsedTime);
            levelSelectButton.update(elapsedTime);
            bestiaryButton.update(elapsedTime);
            quitGameButton.update(elapsedTime);
        }

        if (screenState == ScreenState.READY_TO_QUIT) {
            confirmQuitButton.update(elapsedTime);
            cancelQuitButton.update(elapsedTime);
        }

        resolveTouchEvents(game);

        if (exitScreen) {

            if (screenFadeTo == "LunarSplashScreen") {
                mGame.getScreenManager().addScreen(new LunarSplashScreen(mGame));
                mGame.getScreenManager().setAsCurrentScreen("LunarSplashScreen");
            }
            else if (screenFadeTo == "LunarBestiaryScreen") {
                mGame.getScreenManager().addScreen(new LunarBestiaryScreen(mGame));
                mGame.getScreenManager().setAsCurrentScreen("LunarBestiaryScreen");
            }
            else if (screenFadeTo == "LunarOptionsScreen") {
                LunarOptionsScreen lunarOptionsScreen = new LunarOptionsScreen(mGame);
                lunarOptionsScreen.setPreviousScreen("LunarPauseScreen");
                mGame.getScreenManager().addScreen(lunarOptionsScreen);
                mGame.getScreenManager().setAsCurrentScreen("LunarOptionsScreen");
            }
            else if (screenFadeTo == "LunarLevelSelectScreen") {
                lunarLevelSelectScreen = mGame.getScreenManager().getScreen("LunarLevelSelectScreen");
                mGame.getScreenManager().setAsCurrentScreen("LunarLevelSelectScreen");
                lunarLevelSelectScreen.fadeFromColour("black", 8, 0, 0);
            }
            else if (screenFadeTo == "LunarGameScreen") {
               lunarGameScreen = mGame.getScreenManager().getScreen("LunarGameScreen");
                mGame.getScreenManager().setAsCurrentScreen("LunarGameScreen");
                lunarGameScreen.fadeFromColour("black", 8, 0, 0);
            }
            else {
                Log.d("error", "couldn't fade to desired screen. Screen received was:" + screenFadeTo);
            }

            screenState = ScreenState.RUNNING;
            mGame.getScreenManager().removeScreen(this.mName);
            this.resetFade();
            exitScreen = false;
            System.gc();
        }

    }

    //The pause screen's render method
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top,
                mScreenViewport.right, mScreenViewport.bottom, backgroundPaint);

        pauseBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        resumeButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        bestiaryButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        levelSelectButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        optionsButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        quitGameButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        if (screenState == ScreenState.READY_TO_QUIT) {
            graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top, mScreenViewport.right, mScreenViewport.bottom, confirmPaint);
            graphics2D.drawText("REALLY QUIT GAME?", mScreenViewport.width / 2, (mScreenViewport.height / 10) * 3, textPaint);
            confirmQuitButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            cancelQuitButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        super.draw(elapsedTime, graphics2D);
    }

    @Override
    public void dispose() {
        mGame.getScreenManager().dispose();
    }

}

