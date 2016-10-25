package com.gamedev.techtronic.lunargame.Screens;
import android.graphics.Color;

import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.util.Vector2;
import com.gamedev.techtronic.lunargame.gage.world.GameObject;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gageExtension.AnimatedSprite;


public class BossScreen extends GameScreen {
    private GameObject background;
    private AnimatedSprite bossRoom;
    private AnimatedSprite roboBoss;

    private Vector2 initialPosition;

    //The default constructor for the boss screen
    public BossScreen(Game game) {
        super("Boss Screen", game);

        super.initViewports(200.0F, 115.0F, 200.0F);
        super.initAssets();
        this.addComponents();
    }

    //Initialise all graphical assets used in the boss screen
    protected void initBitmaps() {
        mAssetManager.loadAndAddBitmap("bossRoom", "img/inGame/levels/levelBoss/bossSheet.png");
        mAssetManager.loadAndAddBitmap("background", "img/inGame/levels/levelOne/levelBackground.png");
    }

    //Initialise all audio assets used in the boss screen
    protected void initSounds() {

    }

    //Add game objects to the boss screen
    protected void addComponents() {
        background = new GameObject(0,0,mAssetManager.getBitmap("background"),this);
        bossRoom = new AnimatedSprite(156,110,mAssetManager.getBitmap("bossRoom"),this,"bossroom_intro",false);
        roboBoss = new AnimatedSprite(156,75,mAssetManager.getBitmap("bossRoom"),this,"boss_robo_intro",false);
        roboBoss.velocity.y=-5;

        initialPosition = new Vector2(156,75);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        bossRoom.update(elapsedTime);

        if (elapsedTime.totalTime>5){
            roboBoss.setAnimation("boss_robo_lookAround", true);
        }

        // move roboBoss around, make it more realistic
        if (elapsedTime.totalTime>3.5){
            roboBoss.update(elapsedTime);
            if (roboBoss.position.y>initialPosition.y+1){
                roboBoss.velocity.y=-2;
            }else  if (roboBoss.position.y<initialPosition.y-1){
                roboBoss.velocity.y=2;
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        background.draw(elapsedTime, graphics2D,mLayerViewport, mScreenViewport);

        if (elapsedTime.totalTime>3.5){
            roboBoss.draw(elapsedTime, graphics2D,mLayerViewport, mScreenViewport);
        }
        bossRoom.draw(elapsedTime, graphics2D,mLayerViewport, mScreenViewport);
    }
}
