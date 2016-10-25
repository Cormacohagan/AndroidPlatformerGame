package com.gamedev.techtronic.lunargame.Level.Entities;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gageExtension.AnimatedSprite;

public class JumpPad extends AnimatedSprite {

    private int velocityValue;

    public JumpPad(float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping) {
        super(x, y, bitmap, gameScreen, animationName, isLooping);
        velocityValue = 150;
    }

    public JumpPad(float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping, int velocityValue) {
        super(x, y, bitmap, gameScreen, animationName, isLooping);
        this.velocityValue = velocityValue;
    }

    public int getVelocityValue() {
        return velocityValue;
    }

    public void setVelocityValue(int velocityValue) {
        this.velocityValue = velocityValue;
    }

}
