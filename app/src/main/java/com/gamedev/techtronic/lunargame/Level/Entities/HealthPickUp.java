package com.gamedev.techtronic.lunargame.Level.Entities;


import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

public class HealthPickUp extends PickUp {

    private float healthIncreaseValue;

    public HealthPickUp(float x, float y, float width, float height, Bitmap bitmap,
                  GameScreen gameScreen, String pickUpType1, float healthIncreaseValue) {
        super(x, y, width, height, bitmap, gameScreen, pickUpType1);
        this.healthIncreaseValue = healthIncreaseValue;
    }

    public void setHealthIncreaseValue(float healthIncreaseValue) {
        this.healthIncreaseValue = healthIncreaseValue;
    }

    public float getHealthIncreaseValue() {
        return healthIncreaseValue;
    }
}
