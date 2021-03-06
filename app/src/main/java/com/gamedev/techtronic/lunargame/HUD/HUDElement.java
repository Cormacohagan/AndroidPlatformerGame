package com.gamedev.techtronic.lunargame.HUD;

import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.world.GameObject;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;

public abstract class HUDElement extends GameObject {
    protected String name;

    public HUDElement(String name, float x, float y, Bitmap bitmap, GameScreen gameScreen) {
        super(x, y, bitmap, gameScreen);

        this.name = name;
    }

    public void update(ElapsedTime elapsedtime) {}

    public abstract void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport);
}
