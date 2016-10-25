package com.gamedev.techtronic.lunargame.gageExtension;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.util.BoundingBox;
import com.gamedev.techtronic.lunargame.gage.util.GraphicsHelper;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;
import com.gamedev.techtronic.lunargame.gage.world.Sprite;

/*
 * Work in progress!<p>
 * Simple class for animating a sprite by scrolling the source rect over the image sheet.<p>
 * Best suited to representing flowing liquid.<p>
 */

// TODO: CE: Allow for variable speeds, directions
// TODO: CE: Make class less crude and more general
public class ScrollSprite extends Sprite {

    private Rect mSpriteRect = new Rect();
    private int width;
    private int height;

    public ScrollSprite(float x, float y, int width, int height, Bitmap bitmap, GameScreen gameScreen) {
        super(x, y, bitmap, gameScreen);
        mSpriteRect = new Rect(0,mBitmap.getHeight()-126,mBitmap.getWidth(),mBitmap.getHeight());
        this.width=width;
        this.height=height;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        mSpriteRect.top-=3;
        mSpriteRect.bottom-=3;

        if (mSpriteRect.top<=0){
            mSpriteRect.top=mBitmap.getHeight()-126;
            mSpriteRect.bottom=mBitmap.getHeight();
        }

    }

    @Override
    public BoundingBox getBound() {
        mBound.x = position.x;
        mBound.y = position.y;
        mBound.halfWidth = this.width/2;
        mBound.halfHeight = this.height/2;
        return mBound;
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {

        // If the sprite is on-screen draw it
        if (GraphicsHelper.getSourceAndScreenRect(this, layerViewport, screenViewport, drawSourceRect, drawScreenRect)){
            graphics2D.drawBitmap(mBitmap, mSpriteRect,drawScreenRect,null);
        }
    }
}
