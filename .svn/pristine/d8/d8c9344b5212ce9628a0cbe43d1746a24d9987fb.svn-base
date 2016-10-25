package com.gamedev.techtronic.lunargame.Level.Entities;

import android.graphics.Bitmap;
import android.util.Log;

import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gage.world.Sprite;

/* Used for items the player can pick up in game
 Has 3 versions that can be set: floating (object floats up and down),
 shrink_and_grow (object gets bigger and smaller),
 and static (object doesn't do anything, just stays stationary)
 */
public class PickUp extends Sprite {

    protected enum PickUpType{FLOAT, SHRINK_AND_GROW, STATIC}
    protected PickUpType pickUpType = PickUpType.FLOAT;
    protected float floatAmountTracker, floatAmount, floatRate, floatRateCount;
    protected float pickUpWidth, pickUpHeight, shrinkGrowAmount, shrinkGrowAmountTracker, shrinkAndGrowRate, shrinkAndGrowCount;
    protected boolean currentlyFloatingDown;
    protected boolean currentlyShrinking;
    protected boolean collected;
    protected float pickUpX, pickUpY;

/*    public PickUp(float x, float y, Bitmap bitmap, GameScreen gameScreen) {
        super(x, y, bitmap, gameScreen);
        pickUpX = x;
        pickUpY = y;
    }*/

    public PickUp(float x, float y, float width, float height, Bitmap bitmap,
                  GameScreen gameScreen, String pickUpType1) {
        super(x, y, width, height, bitmap, gameScreen);
        pickUpX = x;
        pickUpY = y;
        collected = false;

        if (pickUpType1 == "FLOAT") {
            pickUpType = PickUpType.FLOAT;

            floatAmountTracker = 0;
            floatRate = 10;
            floatAmount = 5;
            currentlyFloatingDown = false;
        }
        else if (pickUpType1 == "SHRINK_AND_GROW") {
            pickUpType = PickUpType.SHRINK_AND_GROW;

            shrinkGrowAmountTracker = 0;
            shrinkAndGrowRate = 10;
            shrinkGrowAmount = 5;
            pickUpWidth = width;
            pickUpHeight = height;
            currentlyShrinking = false;
        }
        else if (pickUpType1 == "STATIC") {
            pickUpType = PickUpType.STATIC;
        }
        else {
            Log.d("PickUp error", "Instantiating PickUp correctly failed: invalid pickup args for constructor. Setting PickUp type to default (floating)");
            pickUpType = PickUpType.FLOAT;
        }
    }

    public void update(ElapsedTime elapsedTime) {

        // used to ensure object disappears. temporary code, removed when pickups are added to level building
        if(collected = true) {
            pickUpType = PickUpType.STATIC;
        }

        if (pickUpType == PickUpType.FLOAT) {
            floatRateCount++;
            if (floatRate == 0) {}
            else if (floatRateCount >= floatRate) {
                floatPickup();
                floatRateCount = 0;
            }
        }
        else if (pickUpType == PickUpType.FLOAT.SHRINK_AND_GROW) {
            shrinkAndGrowCount++;
            if (shrinkAndGrowRate == 0) {}
            else if (shrinkAndGrowCount >= shrinkAndGrowRate) {
                shrinkAndGrowPickup();
                shrinkAndGrowCount = 0;
            }
        }
        else if (pickUpType == PickUpType.STATIC) {}

        super.update(elapsedTime);
    }


    public void floatPickup() {

        floatAmountTracker++;

        if (!currentlyFloatingDown) {
            setPosition(pickUpX, pickUpY++);
            if (floatAmountTracker >= floatAmount) {
                currentlyFloatingDown = true;
                floatAmountTracker = 0;
            }
        }
        else {
            setPosition(pickUpX, pickUpY--);
            if (floatAmountTracker >= floatAmount) {
                currentlyFloatingDown = false;
                floatAmountTracker = 0;
            }
        }
    }

    public void shrinkAndGrowPickup() {

        shrinkGrowAmountTracker++;

        if(!currentlyShrinking) {
            pickUpWidth++;
            pickUpHeight++;
            setWidthAndHeight(pickUpWidth, pickUpHeight);
            if (shrinkGrowAmountTracker >= shrinkGrowAmount) {
                currentlyShrinking = true;
                shrinkGrowAmountTracker = 0;
            }
        }
        else {
            pickUpWidth--;
            pickUpHeight--;
            setWidthAndHeight(pickUpWidth, pickUpHeight);
            if (shrinkGrowAmountTracker >= shrinkGrowAmount) {
                currentlyShrinking = false;
                shrinkGrowAmountTracker = 0;
            }
        }
    }

    public void setFloatRate(float floatRate) {
        this.floatRate = floatRate;
    }

    public void setFloatAmount(float floatAmount) {
        this.floatAmount = floatAmount;
    }

    public void setShrinkAndGrowRate(float shrinkAndGrowRate) {
        this.shrinkAndGrowRate = shrinkAndGrowRate;
    }

    public void setShrinkGrowAmount(float shrinkGrowAmount){
        this.shrinkGrowAmount = shrinkGrowAmount;
    }

   public void setCollected(boolean collected) {
        this.collected = collected;
    }

}
