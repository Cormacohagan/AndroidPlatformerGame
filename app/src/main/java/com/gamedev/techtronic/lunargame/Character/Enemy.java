package com.gamedev.techtronic.lunargame.Character;

import android.graphics.Bitmap;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

public class Enemy extends NPC {
    public Enemy(float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping, int health, int damage, String behaviour) {
        super(x, y, bitmap, gameScreen, animationName, isLooping, health, damage, behaviour);

    }

    public Enemy(float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping) {
        super(x, y, bitmap, gameScreen, animationName, isLooping);
    }
}
