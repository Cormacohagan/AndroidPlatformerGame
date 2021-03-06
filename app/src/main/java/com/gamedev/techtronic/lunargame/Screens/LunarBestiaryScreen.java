package com.gamedev.techtronic.lunargame.Screens;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.gamedev.techtronic.lunargame.Level.Triggers.DialogueBox;
import com.gamedev.techtronic.lunargame.LunarGame;
import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.ui.PushButton;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class LunarBestiaryScreen extends GameScreen {

    public enum ScreenState {RUNNING, DISPLAYINGINFO}
    private static ScreenState screenState = ScreenState.RUNNING;
    private LunarGame game;
    private List<PushButton> enemyList;
    private List<PushButton> itemList;
    private PushButton exitButton, playerButton;
    private Paint titlePaint, backgroundPaint;
    private Rect enemyRect, itemRect, playerRect, borderRect;
    private DialogueBox infoBox;
    private Thread infoThread;
    private boolean infoThreadStarted = false;
    private Paint infoPaint;
    public float dialogueBoxPositionX, dialogueBoxPositionY;
    private String livesLeft, healthLeft;

    //Default constructor for the bestiary screen
    public LunarBestiaryScreen(Game game) {
        super("LunarBestiaryScreen", game);

        fadeFromColour("black", 8, 0, 0);

        super.initViewports(500.0F, 287.0F, 500.0F);
        super.initAssets();
        this.addComponents();
    }

    //Initialise all graphical assets used in the bestiary screen
    protected void initBitmaps() {

        // background bitmap
        mAssetManager.loadAndAddBitmap("background", "img/menus/options/optionsBackground.png");

        // exit button bitmap
        mAssetManager.loadAndAddBitmap("exitButton", "img/menus/options/optionsButtonClose.png");

        // enemy bitmaps (all load in the same enemy for now, will load in different sheets when I
        // have more enemies
        mAssetManager.loadAndAddBitmap("enemy1", "img/menus/bestiaryAndItems/enemy1.png");
        mAssetManager.loadAndAddBitmap("enemy2", "img/menus/bestiaryAndItems/enemy2.png");
        mAssetManager.loadAndAddBitmap("enemy3", "img/menus/bestiaryAndItems/enemy3.png");
        mAssetManager.loadAndAddBitmap("enemy4", "img/menus/bestiaryAndItems/enemy4.png");
        mAssetManager.loadAndAddBitmap("enemy5", "img/menus/bestiaryAndItems/enemy5.png");
        mAssetManager.loadAndAddBitmap("enemy6", "img/menus/bestiaryAndItems/enemy6.png");
        mAssetManager.loadAndAddBitmap("enemy7", "img/menus/bestiaryAndItems/enemy7.png");

        mAssetManager.loadAndAddBitmap("item1", "img/menus/bestiaryAndItems/item1.png");
        mAssetManager.loadAndAddBitmap("item2", "img/menus/bestiaryAndItems/item2.png");
        mAssetManager.loadAndAddBitmap("item3", "img/menus/bestiaryAndItems/item3.png");
        mAssetManager.loadAndAddBitmap("item4", "img/menus/bestiaryAndItems/item4.png");
        mAssetManager.loadAndAddBitmap("item5", "img/menus/bestiaryAndItems/item5.png");
        mAssetManager.loadAndAddBitmap("item6", "img/menus/bestiaryAndItems/item6.png");
        mAssetManager.loadAndAddBitmap("item7", "img/menus/bestiaryAndItems/item7.png");

        // textLog bitmap
        mAssetManager.loadAndAddBitmap("textLog", "img/menus/main/mainMenuPlanet.png");

        // item bitmaps
        mAssetManager.loadAndAddBitmap("strangeItem", "img/menus/main/mainMenuPlanet.png");
        mAssetManager.loadAndAddBitmap("strangeItem2", "img/menus/main/mainMenuPlanet.png");

        // player bitmap
        mAssetManager.loadAndAddBitmap("playerBestiary", "img/menus/bestiaryAndItems/playerBestiary.png");
        mAssetManager.loadAndAddBitmap("player", "img/menus/bestiaryAndItems/playerBestiary.png");

        // infoBox box
        mAssetManager.loadAndAddBitmap("infoBox", "img/menus/bestiaryAndItems/infoBox.png");
    }

    //Initialise all audio assets used in the bestiary screen
    protected void initSounds() {
        // background music loop
        mAssetManager.loadAndAddMusic("bestiaryBackgroundMusic", "music/bestiaryItemsSong.ogg");

        if (!mAssetManager.getMusic("bestiaryBackgroundMusic").isPlaying()) {
            mAssetManager.getMusic("bestiaryBackgroundMusic").play();
            mAssetManager.getMusic("bestiaryBackgroundMusic").setLooping(true);
        }

        // sfx for: touching icon for more info
        mAssetManager.loadAndAddSound("infoStart", "sfx/inGame/dialogueStart.ogg");

        // sfx for: printing each character in the info box
        mAssetManager.loadAndAddSound("letterPrinted", "sfx/inGame/dialogueSpeech2.ogg");
    }

    //Add game objects to the bestiary screen
    protected void addComponents() {

        game = (LunarGame)this.getGame();

        exitButton = new PushButton(1090, 120, 140, 140,"exitButton", "exitButton", this);
        playerButton = new PushButton(1090, 340, 180, 180,"playerBestiary", "player", this);

        enemyList = new ArrayList<PushButton>();
        enemyList.add(new PushButton(110, 220, 100, 100, "enemy1", "player", this));
        enemyList.add(new PushButton(230, 220, 100, 100, "enemy2", "player", this));
        enemyList.add(new PushButton(350, 220, 100, 100, "enemy3", "player", this));
        enemyList.add(new PushButton(470, 220, 100, 100, "enemy4", "player", this));
        enemyList.add(new PushButton(590, 220, 100, 100, "enemy5", "player", this));
        enemyList.add(new PushButton(710, 220, 100, 100, "enemy6", "player", this));
        enemyList.add(new PushButton(830, 220, 100, 100, "enemy7", "player", this));

        itemList = new ArrayList<PushButton>();
        itemList.add(new PushButton(110, 570, 100, 100, "item1", "player", this));
        itemList.add(new PushButton(230, 570, 100, 100, "item2", "player", this));
        itemList.add(new PushButton(350, 570, 100, 100, "item3", "player", this));
        itemList.add(new PushButton(470, 570, 100, 100, "item4", "player", this));
        itemList.add(new PushButton(590, 570, 100, 100, "item5", "player", this));
        itemList.add(new PushButton(710, 570, 100, 100, "item6", "player", this));
        itemList.add(new PushButton(830, 570, 100, 100, "item7", "player", this));

        dialogueBoxPositionX =  mLayerViewport.getWidth()/2;
        dialogueBoxPositionY =  mLayerViewport.getHeight()/3;

        infoBox = new DialogueBox(dialogueBoxPositionX, dialogueBoxPositionY, mAssetManager.getBitmap("infoBox"), this, "info_box", true, mLayerViewport);

        healthLeft = "" + LunarGameScreen.player.health;
        livesLeft = "" + LunarGameScreen.player.livesLeft;


        // For drawing title text on screen
        titlePaint = new Paint();
        titlePaint.setStyle(Paint.Style.STROKE);
        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextSize(50);

        // For drawing the background
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        Color backgroundColor = new Color();

        infoPaint = new Paint();
        infoPaint.setColor(Color.argb(220, 0, 0, 0));
        infoPaint.setStyle(Paint.Style.FILL);

        backgroundPaint.setColor(Color.parseColor("#1d494b"));
        //"#a3c1b8" light bg
        //"#1d494b" dark bg

        // Rectangle that acts as a border to segregate different areas of the menu
        borderRect = new Rect((int) mLayerViewport.getLeft()+20, (int) mLayerViewport.getTop()+20,
                (int) mLayerViewport.getRight()-20, (int) mLayerViewport.getBottom()-20);

    }

    private void resolveTouchEvents(LunarGame game) {
        if (exitButton.pushTriggered()) {

            if (mAssetManager.getMusic("bestiaryBackgroundMusic").isPlaying()) {
                mAssetManager.getMusic("bestiaryBackgroundMusic").stop();
            }

            fadeToColourAndSwitchScreen("black", 8, 0, 0, 0, "LunarPauseScreen");

            System.gc();
        }
        else if (playerButton.pushTriggered()) {
            infoThreadStarted = false;
            screenState = ScreenState.DISPLAYINGINFO;

        }

/*        for (int k = 0; k < enemyList.size(); k++) {
            if(enemyList.get(k).pushTriggered()) {

               // enemyList.get(k).
            }
        }*/

    }

    //The bestiary screen's update method
    @Override
    public void update(ElapsedTime elapsedTime){

        if(screenState == ScreenState.RUNNING) {
            exitButton.update(elapsedTime);
        }
        else if(screenState == ScreenState.DISPLAYINGINFO) {

            if (!infoThreadStarted) {
                mAssetManager.getSound("dialogueStart").play();

                // parsing xml for text goes here
                String testText = "info on enemy!";

                infoBox.activateDialogueBox("info on enemy!");
                infoThread = new Thread(infoBox);
                infoThread.start();
                infoThreadStarted = true;
            }

            infoBox.update(elapsedTime);
        }

        playerButton.update(elapsedTime);
        for (int k = 0; k < itemList.size(); k++) {
            itemList.get(k).update(elapsedTime);
        }

        for (int l = 0; l < enemyList.size(); l++) {
            enemyList.get(l).update(elapsedTime);
        }

        if (exitScreen) {
            if (screenFadeTo == "LunarPauseScreen") {
                mGame.getScreenManager().addScreen(new LunarPauseScreen(mGame));
                mGame.getScreenManager().setAsCurrentScreen("LunarPauseScreen");
            }
            else if (screenFadeTo == "LunarMenuScreen") {
                mGame.getScreenManager().addScreen(new LunarMenuScreen(mGame));
                mGame.getScreenManager().setAsCurrentScreen("LunarMenuScreen");
            }
            else if (screenFadeTo == "LunarGameScreen") {
                mGame.getScreenManager().setAsCurrentScreen("LunarGameScreen");
            }
            else {
                Log.d("error", "no screen to fade to, incorrect arguments received in update()");
            }
            mGame.getScreenManager().removeScreen(this.getName());
            this.resetFade();
            exitScreen = false;
            System.gc();
        }

        resolveTouchEvents(game);

 /*       if (!musicActivated) {
            mAssetManager.getSound("backgroundMusic").play(0.6f);
            musicActivated = true;

        }*/

    }

    //The bestiary screen's render method
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top,
                mScreenViewport.right, mScreenViewport.bottom, backgroundPaint);

        graphics2D.drawRect(30, 40, 1250, 710, titlePaint);
        graphics2D.drawLine(40, 400, 900, 400, titlePaint);
        graphics2D.drawLine(920, 60, 920, 690, titlePaint);
        graphics2D.drawLine(940, 200, 1240, 200, titlePaint);

        titlePaint.setTextSize(50);
        titlePaint.setFakeBoldText(true);
        graphics2D.drawText("BESTIARY & INVENTORY", 370, 60, titlePaint);

        titlePaint.setFakeBoldText(false);
        titlePaint.setTextSize(40);
        graphics2D.drawText("DEFEATED ENEMIES", 270, 110, titlePaint);
        graphics2D.drawText("ITEMS COLLECTED", 280, 450, titlePaint);
        graphics2D.drawText("PLAYER", 1010, 245, titlePaint);
        graphics2D.drawText("Current HP: " + healthLeft, 940, 500, titlePaint);
        graphics2D.drawText("Lives Left: " + livesLeft, 970, 550, titlePaint);

        exitButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        playerButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        for (int j = 0; j < itemList.size(); j++) {
            itemList.get(j).draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        if (screenState == ScreenState.DISPLAYINGINFO) {

            graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top, mScreenViewport.right, mScreenViewport.bottom, infoPaint);
            infoBox.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

        super.draw(elapsedTime, graphics2D);
    }

    public static void setScreenState(ScreenState state) {
        screenState = state;
    }

    public void pause() {
        if (mAssetManager.getMusic("bestiaryBackgroundMusic").isPlaying()) {

            Log.d("test", "bestiary screen paused");
            mAssetManager.getMusic("bestiaryBackgroundMusic").stop();

        }

    }

    @Override
    public void dispose() {
        mGame.getScreenManager().dispose();
    }

}