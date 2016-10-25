package com.gamedev.techtronic.lunargame.Level.Entities;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.Screens.LunarGameScreen;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.util.BoundingBox;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

public class Key extends PickUp {


    private float playerX, playerY;
    private BoundingBox playerBoundingBox;
    private LunarGameScreen lunarGameScreen;
    private boolean keyBeenSetStatic;

    public Key(float x, float y, float width, float height, Bitmap bitmap,
               GameScreen gameScreen, String pickUpType1) {
        super(x, y, width, height, bitmap, gameScreen, pickUpType1);

        /*ScreenManager screenManager = this.mGameScreen.getGame().getScreenManager();
        lunarGameScreen = screenManager.getCurrentScreen();*/

        pickUpX = x;
        pickUpY = y;
        keyBeenSetStatic = false;
    }

    public void update(ElapsedTime elapsedTime) {

        if (!collected) {
        }
        else {
            if (!keyBeenSetStatic) {
                pickUpType = PickUpType.STATIC;
                keyBeenSetStatic = true;
            }
            playerBoundingBox = lunarGameScreen.player.getBound();
            playerX = playerBoundingBox.x;
            playerY = playerBoundingBox.y;
            if (lunarGameScreen.player.isFacingRight()) {
                playerX += playerBoundingBox.halfWidth;
            }
            else {
                playerX -= playerBoundingBox.halfWidth;
            }
            setPosition(playerX, playerY);
        }

        super.update(elapsedTime);
    }

}
