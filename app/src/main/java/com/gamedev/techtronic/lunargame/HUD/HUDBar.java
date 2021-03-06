package com.gamedev.techtronic.lunargame.HUD;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;

public class HUDBar extends HUDElement {
    private int minValue;
    private int maxValue;
    private int value;
    private Bitmap startSegment;
    private Bitmap middleSegment;
    private Bitmap endSegment;

    public HUDBar(String name, float x, float y, HUDBarResourceBundle bitmapBundle, int min, int max, int start, GameScreen gameScreen) {
        super(name, x, y, bitmapBundle.background, gameScreen);

        this.minValue = min;
        this.maxValue = max;
        this.value = start;

        this.extractResources(bitmapBundle);
    }

    private void extractResources(HUDBarResourceBundle bitmapBundle) {
        this.startSegment = bitmapBundle.firstSegment;
        this.middleSegment = bitmapBundle.middleSegment;
        this.endSegment = bitmapBundle.lastSegment;
    }

    public void update(ElapsedTime elapsedTime, int value) {
        this.value = value;
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
            float x = this.position.x;
            float y = this.position.y;

            //Draw background
            drawScreenRect.set((int) x, (int) y, (int) x + this.mBitmap.getWidth(), (int) y + this.mBitmap.getHeight());
            graphics2D.drawBitmap(mBitmap, null, drawScreenRect, null);

            //Draw left edge
            drawScreenRect.set((int) x, (int) y, (int) x + this.startSegment.getWidth(), (int) y + this.mBitmap.getHeight());
            graphics2D.drawBitmap(this.startSegment, null, drawScreenRect, null);

            x += this.startSegment.getWidth();

            int tenth = (this.maxValue - this.minValue) / 10;

            for (int i = value; i >= tenth ; i -= tenth) {
                drawScreenRect.set((int) x, (int) y, (int) x + this.middleSegment.getWidth(), (int) y + this.mBitmap.getHeight());
                graphics2D.drawBitmap(this.middleSegment, null, drawScreenRect, null);

                x += this.middleSegment.getWidth();
            }

            drawScreenRect.set((int) x, (int) y, (int) x + this.endSegment.getWidth(), (int) y + this.mBitmap.getHeight());
            graphics2D.drawBitmap(this.endSegment, null, drawScreenRect, null);
    }
}
