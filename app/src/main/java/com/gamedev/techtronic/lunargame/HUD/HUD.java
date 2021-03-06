package com.gamedev.techtronic.lunargame.HUD;

import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;

import java.util.ArrayList;

public class HUD {
    private float x;
    private float y;
    private boolean debug;
    private ArrayList<HUDElement> componentList = null;

    public HUD(float x, float y, boolean debugOn) {
        this.x = x;
        this.y = y;
        this.debug = debugOn;
        this.componentList = new ArrayList<>();
    }

    public HUD(float x, float y) {
        this(x, y, false);
    }

    public void update(ElapsedTime elapsedTime) {
        for (int i = 0; i <= componentList.size(); i++) {
            this.componentList.get(i).update(elapsedTime);
        }
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        for (int i = 0; i < componentList.size(); i++) {
            this.componentList.get(i).draw(elapsedTime, graphics2D, layerViewport, screenViewport);
//            System.out.println("Test");
        }
    }

    public void addHUDComponent(HUDElement comp) {
        this.componentList.add(comp);
    }

    public void enableDebug(boolean debugOn) {
        this.debug = debugOn;
    }
}
