package com.gamedev.techtronic.lunargame.Level.Entities;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gageExtension.AnimatedSprite;

public class ScorePickUp extends AnimatedSprite {

    private int scoreValue;

    public ScorePickUp (float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping) {
        super(x, y, bitmap, gameScreen, animationName, isLooping);
        scoreValue = 100; // default score that is given to player for getting a score pickup
    }

    public ScorePickUp (float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping, int scoreValue) {
        super(x, y, bitmap, gameScreen, animationName, isLooping);
        this.scoreValue = scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public int getScoreValue() {
        return scoreValue;
    }

}
