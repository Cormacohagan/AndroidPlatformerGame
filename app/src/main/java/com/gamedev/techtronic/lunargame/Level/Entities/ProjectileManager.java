package com.gamedev.techtronic.lunargame.Level.Entities;

import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;

import java.util.ArrayList;

public class ProjectileManager {
    private ArrayList<Projectile> projectileList;
    Projectile currentBullet;

    public ProjectileManager() {
        this.projectileList = new ArrayList<Projectile>();
    }

    public void createProjectile(Projectile projectile) {
        this.projectileList.add(projectile);
    }

    public void update(ElapsedTime elapsedTime) {
        for (int i = 0; i < projectileList.size(); i++) {
            this.projectileList.get(i).update(elapsedTime);
        }
    }

    public void draw(ElapsedTime elapsedTime,IGraphics2D graphics2D, LayerViewport mLayerViewport, ScreenViewport mScreenViewport) {
        for (int i = 0; i < projectileList.size(); i++) {
            this.projectileList.get(i).draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }

    public void setBulletVelocity(int bulletVelocity, int bulletCount){
        for (int i = 0; i < projectileList.size(); i++) {
            this.projectileList.get(bulletCount-1).velocity.set(bulletVelocity, 0);
        }
    }

    public void removeBullet(int bulletCount){
        for(int i = 0; i< projectileList.size(); i++){
            this.projectileList.remove(bulletCount - 1);
        }
    }

    public Projectile returnCurrentBullet(){
        for (int i = 0; i < projectileList.size(); i++) {
            currentBullet = this.projectileList.get(i);
        }
        return currentBullet;
    }
}