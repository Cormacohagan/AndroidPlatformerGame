package com.gamedev.techtronic.lunargame.Level.Entities;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

public class PlayerProjectile extends Projectile {

    public PlayerProjectile(float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping) {
        super(x, y, bitmap, gameScreen, animationName, isLooping);


    }
}
