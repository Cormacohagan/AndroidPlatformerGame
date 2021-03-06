package com.gamedev.techtronic.lunargame.Screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.gamedev.techtronic.lunargame.LunarGame;
import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.ui.PushButton;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gageExtension.AnimatedSprite;

public class LunarCharacterSelectScreen extends GameScreen {

    private LunarGame game;
    LunarGameScreen lunarGameScreen;
    private Paint backgroundPaint, textPaint;
    private PushButton characterOne, characterTwo;
    private AnimatedSprite character1, character2;
    public String levelChosen;
    private int tempCharChosen;

    //Default constructor for the character select screen
    public LunarCharacterSelectScreen(Game game) {
        super("LunarCharacterSelectScreen", game);

        fadeFromColour("black", 5, 0, 0);

        //super.initViewports(500.0F, 287.0F, 500.0F);
        super.initViewports(50.0F, 50.0F, 100.0F);
        super.initAssets();
        this.addComponents();
    }

    //Initialise all graphical assets used in the character select screen
    protected void initBitmaps() {

        mAssetManager.loadAndAddBitmap("invisibleBorder", "img/menus/shared/invisibleBorder.png");
        //mAssetManager.loadAndAddBitmap("selectionBorder", "img/menus/shared/selectionBorder.png");
        mAssetManager.loadAndAddBitmap("character1", "img/inGame/levels/shared/playerSheet.png");
        mAssetManager.loadAndAddBitmap("character2", "img/inGame/levels/shared/playerSheet2.png");
    }

    //Initialise all audio assets used in the character select screen
    protected void initSounds() {

        mAssetManager.loadAndAddSound("characterChosen", "sfx/menus/startGame.ogg");
    }

    //Add game objects to the character select screen
    protected void addComponents() {

        game = (LunarGame)this.getGame();

        fadeFromColour("black", 5, 0, 0);
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.BLACK);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);

        characterOne = new PushButton(380, 375, 450, 500, "invisibleBorder", "invisibleBorder", this);
        characterTwo = new PushButton(925, 375, 450, 500, "invisibleBorder", "invisibleBorder", this);

        character1 = new AnimatedSprite(10, 50, mAssetManager.getBitmap("character1"), this, "player_walking_right", true);
        character2 = new AnimatedSprite(95, 50, mAssetManager.getBitmap("character2"), this, "player_walking_left", true);
        //character1.setAnimation("player_walking_left", true);
    }

    private void resolveTouchEvents(LunarGame game) {

            if (characterOne.pushTriggered()) {

                fadeToColourAndSwitchScreen("white", 2, 0, 0, 0, "LunarGameScreen");
                disableInput = true;
                tempCharChosen = 0;
                character1.setAnimation("player_jump_right", true);
                mAssetManager.getSound("characterChosen").play();

            }
            else if(characterTwo.pushTriggered()) {

                fadeToColourAndSwitchScreen("white", 2, 0, 0, 0, "LunarGameScreen");
                disableInput = true;
                tempCharChosen = 1;
                character2.setAnimation("player_jump_left", true);
                mAssetManager.getSound("characterChosen").play();

            }
    }


    //The character select screen's update method
    @Override
    public void update(ElapsedTime elapsedTime){

        characterOne.update(elapsedTime);
        characterTwo.update(elapsedTime);
        character1.update(elapsedTime);
        character2.update(elapsedTime);

        if (exitScreen) {

            if (tempCharChosen == 0) {
                lunarGameScreen = new LunarGameScreen(game, levelChosen, "CHARACTER_ONE");
                Log.d("char select screen", "char 1 chosen by user");
                //character2.setAnimation();
            }
            else if (tempCharChosen == 1) {
                lunarGameScreen = new LunarGameScreen(game, levelChosen, "CHARACTER_TWO");
                Log.d("char select screen", "char 2 chosen by user");
                //character1.setAnimation();
            }
            else {
                Log.d("char select error", "no character was successfully selected");
                exitScreen = false;
                return;
            }

            mGame.getScreenManager().removeScreen(this.mName);
            Log.d("t", "test");
            game.getScreenManager().addScreen(lunarGameScreen);
            game.getScreenManager().setAsCurrentScreen("LunarGameScreen");

            this.resetFade();
            exitScreen = false;
        }

        if (!disableInput) {
            resolveTouchEvents(game);
        }
    }

    //The character select screen's render method
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top,
                mScreenViewport.right, mScreenViewport.bottom, backgroundPaint);

        graphics2D.drawText("CHOOSE YOUR CHARACTER", mScreenViewport.width/2, (mScreenViewport.height/10)*2, textPaint);

        characterOne.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        characterTwo.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        character1.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        character2.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        super.draw(elapsedTime, graphics2D);
    }

    @Override
    public void dispose() {
        mGame.getScreenManager().dispose();
    }

}

