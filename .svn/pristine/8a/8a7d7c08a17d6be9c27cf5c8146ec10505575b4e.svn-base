package com.gamedev.techtronic.lunargame.Screens;

import android.graphics.Color;

import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.ui.PushButton;
import com.gamedev.techtronic.lunargame.gage.world.GameObject;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

// Simple screen that displays developer credits; can only currently be accessed from the main menu
public class LunarCreditsScreen extends GameScreen {
    GameObject background, creditsCMC, creditsCE, creditsCOH, creditsJAMK;
    PushButton backButton;

    //Default constructor for the credits screen
    public LunarCreditsScreen(Game game) {
        super("LunarCreditsScreen", game);

        fadeFromColour("black", 5, 500, 0);
        this.initViewports(500.0F, 287.0F, 500.0F);
        super.initAssets();
        this.addComponents();
    }

    //Initialise all graphical assets used in the credits screen
    protected void initBitmaps() {
        mAssetManager.loadAndAddBitmap("creditsCMC", "img/menus/credits/creditsTextCMC.png");
        mAssetManager.loadAndAddBitmap("creditsCE", "img/menus/credits/creditsTextCE.png");
        mAssetManager.loadAndAddBitmap("creditsCOH", "img/menus/credits/creditsTextCOH.png");
        mAssetManager.loadAndAddBitmap("creditsJAMK", "img/menus/credits/creditsTextJAMK.png");
        mAssetManager.loadAndAddBitmap("creditsBack", "img/menus/credits/creditsButtonBack.png");
        mAssetManager.loadAndAddBitmap("menuBackground", "img/menus/main/mainMenuBackground.png");
    }

    //Initialise all audio assets used in the credits screen
    protected void initSounds() {
       // mAssetManager.loadAndAddMusic("creditsMenuSong", "music/creditsMenuSong.ogg");
        mAssetManager.loadAndAddSound("menuBack", "sfx/menus/menuBack.ogg");
    }

    //Add game objects to the credits screen
    protected void addComponents() {
        background = new GameObject(0, 0, mAssetManager.getBitmap("menuBackground"), this);
        creditsCMC = new GameObject(mLayerViewport.getWidth()/10*3, mLayerViewport.getHeight()/10*7,
               mLayerViewport.getWidth()/5, mLayerViewport.getHeight()/5, mAssetManager.getBitmap("creditsCMC"), this);
        creditsCE = new GameObject(mLayerViewport.getWidth()/10*7, mLayerViewport.getHeight()/10*7,
                mLayerViewport.getWidth()/5, mLayerViewport.getHeight()/5, mAssetManager.getBitmap("creditsCE"), this);
        creditsCOH = new GameObject(mLayerViewport.getWidth()/10*3, mLayerViewport.getHeight()/10*3,
                mLayerViewport.getWidth()/5, mLayerViewport.getHeight()/5, mAssetManager.getBitmap("creditsCOH"), this);
        creditsJAMK = new GameObject(mLayerViewport.getWidth()/10*7, mLayerViewport.getHeight()/10*3,
                mLayerViewport.getWidth()/4, mLayerViewport.getHeight()/5, mAssetManager.getBitmap("creditsJAMK"), this);
        backButton = new PushButton(mLayerViewport.getWidth()/10, mLayerViewport.getHeight()/10*2,
                mLayerViewport.getWidth()/10, mLayerViewport.getWidth()/10, "creditsBack", "creditsBack", "menuBack", this);

       // mAssetManager.getMusic("creditsMenuSong").play();
       // mAssetManager.getMusic("creditsMenuSong").setLooping(true);
    }

    // updating any elements in the credits screen and handling user input
    @Override
    public void update(ElapsedTime elapsedTime){
        backButton.update(elapsedTime);

        if (exitScreen) {
            mGame.getScreenManager().removeScreen(this.mName);
            //mGame.getScreenManager().addScreen(new LunarMenuScreen(mGame));
            fadeFromColour("black", 5, 0, 0);
            mGame.getScreenManager().removeScreen(this.mName);
            mGame.getScreenManager().addScreen(new LunarMenuScreen(mGame));
            mGame.getScreenManager().setAsCurrentScreen("LunarMenuScreen");
            exitScreen = false;
        }

        resolveTouchEvents();
    }

    //Resolve any on screen touch events
    private void resolveTouchEvents() {
        if (backButton.pushTriggered()) {

            fadeToColourAndSwitchScreen("black", 5, 0, 0, 0, "LunarMenuScreen");
            //mAssetManager.getMusic("creditsMenuSong").pause();
            //mAssetManager.getMusic("mainmenuSong").play();

            System.gc();
        }
    }

    // rendering the credits screen's contents
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        background.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        creditsCMC.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        creditsCE.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        creditsCOH.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        creditsJAMK.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        backButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        super.draw(elapsedTime, graphics2D);
    }

    @Override
    public void dispose() {
        //mGame.getScreenManager().dispose();
    }
}
