package com.gamedev.techtronic.lunargame.Screens;

import android.graphics.Color;
import android.util.Log;

import com.gamedev.techtronic.lunargame.LunarGame;
import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.ui.PushButton;
import com.gamedev.techtronic.lunargame.gage.ui.ToggleButton;
import com.gamedev.techtronic.lunargame.gage.world.GameObject;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

// Allows user to config settings in the game. Currently allows user to change audio settings
public class LunarOptionsScreen extends GameScreen {
    private LunarGame game;
    private GameObject optionsBackground, muteMusic, muteSfx;
    private PushButton closeOptionsScreenButton;
    private ToggleButton toggleMusicButton, toggleSfxButton;
    private String previousScreen;

    //Default constructor for the options screen
    public LunarOptionsScreen(Game game) {
        super("LunarOptionsScreen", game);

        fadeFromColour("black", 8, 0, 0);
        super.initViewports(800.0F, 460.0F, 800.0F);
        super.initAssets();
        this.addComponents();
    }

    //Initialise all graphical assets used in the options screen
    protected void initBitmaps() {
        mAssetManager.loadAndAddBitmap("optionsMuteMusic", "img/menus/options/optionsTextMuteMusic.png");
        mAssetManager.loadAndAddBitmap("optionsMuteSfx", "img/menus/options/optionsTextMuteSfx.png");
        mAssetManager.loadAndAddBitmap("optionsNotMute", "img/menus/options/optionsButtonNotMuted.png");
        mAssetManager.loadAndAddBitmap("optionsMute", "img/menus/options/optionsButtonMuted.png");
        mAssetManager.loadAndAddBitmap("optionsBackground", "img/menus/options/optionsBackground.png");
        mAssetManager.loadAndAddBitmap("optionsClose", "img/menus/options/optionsButtonClose.png");
    }

    //Initialise all audio assets used in the options screen
    protected void initSounds() {
        mAssetManager.loadAndAddSound("menuBack", "sfx/menus/menuBack.ogg");
        mAssetManager.loadAndAddSound("menuForward", "sfx/menus/menuForward.ogg");
        mAssetManager.loadAndAddSound("toggleButton", "sfx/menus/toggleButton.ogg");
        mAssetManager.loadAndAddSound("startGame", "sfx/menus/startGame.ogg");
        //mAssetManager.loadAndAddMusic("creditsMenuSong", "music/creditsMenuSong.ogg");
    }

    //Add game objects to the options screen
    protected void addComponents() {
        float layerViewportWidth = mLayerViewport.getWidth();
        float layerViewportHeight = mLayerViewport.getHeight();
        float layerViewportGridWidth = layerViewportWidth / 100.0F;
        float layerViewportGridHeight = layerViewportHeight / 100.0F;

        optionsBackground = new GameObject(layerViewportWidth * 0.5F, layerViewportHeight * 0.5F, layerViewportWidth * 0.7F, layerViewportHeight * 0.8F, mAssetManager.getBitmap("optionsBackground"), this);
        muteMusic = new GameObject(layerViewportWidth * 0.4F, layerViewportHeight * 0.6F, layerViewportWidth * 0.2F, layerViewportHeight * 0.1F, mAssetManager.getBitmap("optionsMuteMusic"), this);
        muteSfx = new GameObject(layerViewportWidth * 0.4F, layerViewportHeight * 0.45F, layerViewportWidth * 0.2F, layerViewportHeight * 0.1F, mAssetManager.getBitmap("optionsMuteSfx"), this);

        toggleMusicButton = new ToggleButton(layerViewportGridWidth * 20, layerViewportGridHeight * 32, 100, 100, "optionsNotMute", "optionsNotMute", "optionsMute", "optionsMute", "toggleButton", "toggleButton", this);
        toggleSfxButton = new ToggleButton(layerViewportGridWidth * 20, layerViewportGridHeight * 44, 100, 100, "optionsNotMute", "optionsNotMute", "optionsMute", "optionsMute", "toggleButton", "toggleButton", this);
        closeOptionsScreenButton = new PushButton(layerViewportGridWidth * 63, layerViewportGridHeight * 16, layerViewportWidth/15, layerViewportWidth/20, "optionsClose", "optionsClose", "menuBack", this);
    }

    public void setPreviousScreen(String previousScreen) {
        this.previousScreen = previousScreen;
    }

    // updating the options screen's elements (handling user input & changing audio settings
    // according to what user has selected)
    @Override
    public void update(ElapsedTime elapsedTime){
        game = (LunarGame)this.getGame();

        if (game.isMusicOn) {
            toggleMusicButton.setToggled(false);
        }
        else {
            toggleMusicButton.setToggled(true);
        }

        if (game.isSFXOn) {
            toggleSfxButton.setToggled(false);
        }
        else {
            toggleSfxButton.setToggled(true);
        }

        if (exitScreen) {
            if (screenFadeTo == "LunarMenuScreen") {
                mGame.getScreenManager().addScreen(new LunarMenuScreen(mGame));
                mGame.getScreenManager().setAsCurrentScreen("LunarMenuScreen");
            }
            else if (screenFadeTo == "LunarPauseScreen") {
                mGame.getScreenManager().addScreen(new LunarPauseScreen(mGame));
                mGame.getScreenManager().setAsCurrentScreen("LunarPauseScreen");
            }
            else {
                Log.d("error", "no screen to fade to, incorrect arguments received in update()");
            }
            mGame.getScreenManager().removeScreen(this.getName());
            exitScreen = false;
            System.gc();
        }

        closeOptionsScreenButton.update(elapsedTime);
        toggleMusicButton.update(elapsedTime);
        toggleSfxButton.update(elapsedTime);

        resolveTouchEvents(game);
    }

    //Resolve any on screen touch events
    private void resolveTouchEvents(LunarGame game) {
        if (closeOptionsScreenButton.pushTriggered()) {
            mAssetManager.getSound("menuBack").play();

            if (previousScreen == "LunarPauseScreen") {
                fadeToColourAndSwitchScreen("black", 8, 0, 0, 0, "LunarPauseScreen");
            }
            else if(previousScreen == "LunarMenuScreen") {
                fadeToColourAndSwitchScreen("black", 8, 0, 0, 0, "LunarMenuScreen");
            }
            System.gc();
        }

        game.isMusicOn = !toggleMusicButton.isToggledOn();

        if(toggleSfxButton.isToggledOn()) {
            mAssetManager.getSound("menuBack").setVolume(0);
            mAssetManager.getSound("menuForward").setVolume(0);
            mAssetManager.getSound("startGame").setVolume(0);
            mAssetManager.getSound("toggleButton").setVolume(0);
            game.isSFXOn = false;
        }
        else {
            mAssetManager.getSound("menuBack").setVolume(1);
            mAssetManager.getSound("menuForward").setVolume(1);
            mAssetManager.getSound("startGame").setVolume(1);
            mAssetManager.getSound("toggleButton").setVolume(1);
            game.isSFXOn = true;
        }
    }

    // drawing any elements in the options screen (all of them, since they'll always be visible
    // while the options screen is displayed)
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        graphics2D.clear(Color.argb(200, 0, 10, 30));
        graphics2D.clipRect(mScreenViewport.toRect());

        optionsBackground.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        muteMusic.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        muteSfx.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        closeOptionsScreenButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        toggleMusicButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        toggleSfxButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        super.draw(elapsedTime, graphics2D);
    }

    @Override
    public void dispose() {

    }
}