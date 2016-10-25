package com.gamedev.techtronic.lunargame.gageExtension;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;

public class FPSCounter {
    private Game game;
    private Paint paint;
    private int targetFPS;
    private int displayFPS;
    private StringBuilder fpsString;
    private int x;
    private int y;
    private int count;
    private int cumulateFPS;

    //Default constructor for the FPS counter
    public FPSCounter(int x, int y, int size, Game game) {
        this.init(size, game.getTargetFramesPerSecond(), game);
        this.setPos(x, y);
    }

    //Initialise fields of the FPS counter
    private void init(int size, int targetFPS, Game game) {
        this.game = game;

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(Color.GREEN);
        this.paint.setTypeface(Typeface.DEFAULT_BOLD);
        this.paint.setTextSize(size);

        this.targetFPS = targetFPS;
        this.displayFPS = targetFPS;

        this.fpsString = new StringBuilder("");
    }

    //Set the position of the FPS counter on screen
    private void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //The update method of the FPS counter
    public void update(ElapsedTime elapsedTime) {

        count++;
        if (count >= targetFPS) {
            this.displayFPS = (int) this.game.getAverageFramesPerSecond();
            if (this.displayFPS >= (2/3.0F * targetFPS)) {
                this.paint.setColor(Color.GREEN);
            }
            else if (this.displayFPS >= (1/3.0F * targetFPS)) {
                this.paint.setColor(Color.YELLOW);
            }
            else {
                this.paint.setColor(Color.RED);
            }
            count = 0;
        }

    }


    //The draw method of the FPS counter
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {
        int fpsTempColor = this.paint.getColor();

        fpsString = new StringBuilder("FPS: ");
        fpsString.append(String.valueOf(this.displayFPS));

        this.paint.setColor(Color.BLACK);

        graphics2D.drawText(fpsString.toString(), this.x + 2, this.y + 2, this.paint);

        this.paint.setColor(fpsTempColor);
        graphics2D.drawText(fpsString.toString(), this.x, this.y, this.paint);
    }
}
