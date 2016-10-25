package com.gamedev.techtronic.lunargame.Level.Entities;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

public class InvincibilityPickUp extends PickUp {

    private float invincibilityTime; // in ms

    public InvincibilityPickUp(float x, float y, float width, float height, Bitmap bitmap,
                        GameScreen gameScreen, String pickUpType1, int invincibilityTime) {
        super(x, y, width, height, bitmap, gameScreen, pickUpType1);
        this.invincibilityTime = invincibilityTime;
    }

    public void setInvincibilityTime(float invincibilityTime) {
        this.invincibilityTime = invincibilityTime;
    }

    public float getInvincibilityTime() {
        return invincibilityTime;
    }
}
