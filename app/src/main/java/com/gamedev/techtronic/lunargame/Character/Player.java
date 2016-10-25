package com.gamedev.techtronic.lunargame.Character;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.world.GameObject;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;

import java.lang.*;

public class Player extends Character {

    private int maxHealthValue, maxDamageValue, maxLivesValue;
    private boolean holdingKey, facingRight;
    public int livesLeft, score;


    public Player(float x, float y, Bitmap bitmap, GameScreen gameScreen, String animationName, Boolean isLooping) {
        super(x, y, bitmap, gameScreen, animationName, isLooping);

        maxHealthValue = 100; // starting off value
        health = maxHealthValue;
        maxDamageValue = 20;
        maxLivesValue = 3;
        livesLeft = maxLivesValue;
        score = 0;
        holdingKey = false;
        facingRight = true;

        health = maxHealthValue;
        damage = maxDamageValue;

    }

    // used when player gets a powerup / reward that increases their max HP
    public void upgradeHealth(int upgradeAmount) {
        maxHealthValue += upgradeAmount;
    }

    public void upgradeDamage(int upgradeAmount) {
        maxDamageValue += upgradeAmount;
    }

    public void setLivesLeft(int amountToModifyBy) {
        livesLeft += amountToModifyBy;
    }

    public int getMaxHealthValue() {
        return maxHealthValue;
    }

    public String getMaxDamageValue() {
        return String.valueOf(maxDamageValue);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void addHealth(float health) {
        this.health += health;
        if (this.health > maxHealthValue) {
            this.health = maxHealthValue;
        }
    }

    public void addLives(float lives) {
        this.livesLeft += lives;
        if (this.livesLeft > maxLivesValue) {
            this.livesLeft = maxLivesValue;
        }
    }

    public void setHoldingKey(boolean holdingKey) {
        this.holdingKey = holdingKey;
    }

    public boolean getHoldingKey() {
        return holdingKey;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean state){
        facingRight = state;
    }

    // Update player position


}
