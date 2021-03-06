package com.gamedev.techtronic.lunargame.Screens;

import android.graphics.Color;
import android.graphics.Paint;

import com.gamedev.techtronic.lunargame.LunarGame;
import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.engine.input.TouchEvent;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gageExtension.AnimatedSprite;

import java.util.List;

public class LunarSplashScreen extends GameScreen {

    private LunarGame game;
    private AnimatedSprite techtronicLogo;
    private Paint backgroundPaint, techtronicPaint;
    private int loopsToWait, splashScreenDisplayTime;
    private boolean fadedYet = false;
    private List<TouchEvent> touchEvents;

    //Default constructor for the splash screen
    public LunarSplashScreen(Game game) {
        super("LunarSplashScreen", game);

        fadeFromColour("black", 5, 1300, 1000);

        super.initViewports(500.0F, 287.0F, 500.0F);
        super.initAssets();
        this.addComponents();
    }

    //Initialise all graphical assets used in the splash screen
    protected void initBitmaps() {

        mAssetManager.loadAndAddBitmap("splashScreenLogo", "img/menus/main/mainMenuPlanet.png");
        mAssetManager.loadAndAddSound("splashScreenSound", "sfx/menus/toggleButton.ogg");
    }

    //Initialise all audio assets used in the splash screen
    protected void initSounds() {}

    //Add game objects to the splash screen
    protected void addComponents() {

        game = (LunarGame)this.getGame();

        splashScreenDisplayTime = 150;

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.WHITE);

        techtronicPaint = new Paint();
        techtronicPaint.setStyle(Paint.Style.STROKE);
        techtronicPaint.setColor(Color.BLACK);
        techtronicPaint.setTextSize(50);
        techtronicPaint.setTextAlign(Paint.Align.CENTER);

        techtronicLogo = new AnimatedSprite(mScreenViewport.width/2, mScreenViewport.height/2, mAssetManager.getBitmap("splashScreenLogo"), this);
        techtronicLogo.setAnimation("mainmenu_start_game_planet", true);

    }

    private void resolveTouchEvents(LunarGame game) {

        touchEvents = mGame.getInput().getTouchEvents();

        if (mGame.getInput().existsTouch(0)) {
            exitScreen = true;

        }
    }

    //The splash screen's update method
    @Override
    public void update(ElapsedTime elapsedTime){

        loopsToWait++;

        //Log.d("splash screen", "time to wait is: " + timeToWait);
        if (loopsToWait >= splashScreenDisplayTime ) {
            //Log.d("splash screen", "time to wait has hit 200");
            if (!fadedYet) {
                fadeToColourAndSwitchScreen("black", 5, 0, 1000, 0, "LunarMenuScreen");
                fadedYet = true;
            }
        }

        if (exitScreen) {

                //mGame.getScreenManager().removeScreen("LunarGameScreen");
                mGame.getScreenManager().removeScreen(this.mName);
                mGame.getScreenManager().addScreen(new LunarMenuScreen(mGame));
                mGame.getScreenManager().setAsCurrentScreen("LunarMenuScreen");
        }

        techtronicLogo.update(elapsedTime);

        resolveTouchEvents(game);

    }

    //The splash screen's render method
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top,
                mScreenViewport.right, mScreenViewport.bottom, backgroundPaint);

        techtronicLogo.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        graphics2D.drawText("TECHTRONIC\nP L A C E H O L D E R B O I S", mScreenViewport.width / 2, (mScreenViewport.height / 10) * 7, techtronicPaint);

        super.draw(elapsedTime, graphics2D);

    }

    @Override
    public void dispose() {
        mGame.getScreenManager().dispose();
    }

    public void setSplashScreenDisplayTime(int waitTime) {
        splashScreenDisplayTime = waitTime;
    }

}

