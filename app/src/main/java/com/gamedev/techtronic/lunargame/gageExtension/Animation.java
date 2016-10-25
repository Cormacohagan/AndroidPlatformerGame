package com.gamedev.techtronic.lunargame.gageExtension;


public class Animation {
    public String name;
    private int startX;
    private int startY;
    private int spriteWidth;
    private int spriteHeight;
    private int frameCount;
    private int frameRate;

    public Animation(String name, int startX, int startY, int spriteWidth, int spriteHeight, int frameCount, int frameRate) {
        this.name = name;
        this.startX = startX;
        this.startY = startY;
        this.spriteWidth = spriteWidth;
        this.frameCount = frameCount;
        this.spriteHeight = spriteHeight;
        this.frameRate = frameRate;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getFrameRate() {
        return frameRate;
    }
}
