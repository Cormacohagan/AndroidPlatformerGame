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
import com.gamedev.techtronic.lunargame.gage.world.Sprite;

/*
 * A tile is the building block of the level, tiles allow for simple level construction.
 * A tile has numerous properties such as collidable/damaging etc.. which can be used for various
 * effects ingame.
 */
public class Tile extends Sprite {
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


    public Tile(float x, float y, Bitmap bitmap, GameScreen gameScreen,
                String tileName, char encodedCharValue, int startX, int startY, int tileWidth,
                int tileHeight, boolean hasCollision, boolean isAnimated, boolean isDamaging) {

        super(x, y, bitmap, gameScreen);

        this.tileName = tileName;
        this.encodedCharValue = encodedCharValue;
        this.startX = startX;
        this.startY = startY;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.hasCollision = hasCollision;
        this.isAnimated = isAnimated;
        this.isDamaging = isDamaging;

        tileRect = new Rect(this.startX, this.startY, this.startX +this.tileWidth,this.startY +this.tileHeight);

    }

    public boolean isCollidable() {return hasCollision;}

    public boolean isDamaging(){return isDamaging;}

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
