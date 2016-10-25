package com.gamedev.techtronic.lunargame.Level;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.util.BoundingBox;
import com.gamedev.techtronic.lunargame.gage.util.GraphicsHelper;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;

public class AnimatedTile extends Tile {

    public String tileName;
    private char encodedCharValue;
    private int startX;
    private int startY;
    private int tileWidth;
    private int tileHeight;
    private boolean hasCollision;
    private boolean isAnimated;
    private boolean isDamaging;
    private Rect tileRect;
    private int frameCount;
    private int frameRate;

    public AnimatedTile(float x, float y, Bitmap bitmap, GameScreen gameScreen,
                        String tileName, char encodedCharValue, int startX, int startY, int tileWidth,
                        int tileHeight, boolean hasCollision, boolean isAnimated, int frameCount, int frameRate, Boolean isDamaging) {
        super(x, y, bitmap, gameScreen, tileName, encodedCharValue, startX, startY, tileWidth, tileHeight, hasCollision, isAnimated, isDamaging);

        this.tileName = tileName;
        this.encodedCharValue = encodedCharValue;
        this.startX = startX;
        this.startY = startY;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.hasCollision = hasCollision;
        this.isAnimated = isAnimated;
        this.frameCount = frameCount;
        this.frameRate = frameRate;
        this.isDamaging = isDamaging;

        tileRect = new Rect(this.startX, this.startY, this.startX +this.tileWidth,this.startY +this.tileHeight);
    }

    public boolean isCollidable() {
        return hasCollision;
    }

    public boolean isDamaging(){return isDamaging;}

    /**
     * Controls how the animation changes frames and should be run every update.
     * @param elapsedTime
     *          The amount of time elapsed since last frame
     */
    private void stepAnimation(ElapsedTime elapsedTime) {

        // Every call work out what frame needs to be displayed and update the tileRect
        // Assumes all animated tiles are always looping
        int frame = (int) (this.frameRate * elapsedTime.totalTime % this.frameCount);
        tileRect.left = this.startX;
        tileRect.top = this.startY;
        tileRect.right = this.startX + this.tileWidth;
        tileRect.bottom = this.startY + this.tileHeight;
        tileRect.offset(this.tileWidth * frame, 0);
    }
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Tiles don't need to move so just deal with animation.
        // Saves a little computation
        if (this.isAnimated){
            stepAnimation(elapsedTime);
        }
    }

    @Override
    public BoundingBox getBound() {
        mBound.x = position.x;
        mBound.y = position.y;
        mBound.halfWidth = this.tileWidth /2;
        mBound.halfHeight = this.tileHeight /2;
        return mBound;
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        // if the tile is visible draw it to screen
        if (GraphicsHelper.getSourceAndScreenRect(this, layerViewport, screenViewport, drawSourceRect, drawScreenRect)) {

            graphics2D.drawBitmap(mBitmap, tileRect, drawScreenRect, null);
        }
    }
}
