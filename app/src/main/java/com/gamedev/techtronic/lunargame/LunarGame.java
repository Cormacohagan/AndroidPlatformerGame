package com.gamedev.techtronic.lunargame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamedev.techtronic.lunargame.Screens.BossScreen;
import com.gamedev.techtronic.lunargame.Screens.LunarBestiaryScreen;
import com.gamedev.techtronic.lunargame.Screens.LunarCharacterSelectScreen;
import com.gamedev.techtronic.lunargame.Screens.LunarGameScreen;
import com.gamedev.techtronic.lunargame.Screens.LunarLevelSelectScreen;
import com.gamedev.techtronic.lunargame.Screens.LunarMenuScreen;
import com.gamedev.techtronic.lunargame.Screens.LunarPauseScreen;
import com.gamedev.techtronic.lunargame.Screens.LunarScoreScreen;
import com.gamedev.techtronic.lunargame.Screens.LunarSplashScreen;
import com.gamedev.techtronic.lunargame.gage.Game;


public class LunarGame extends Game {

    private final int targetFPS =60;
    public boolean isMusicOn;
    public boolean isSFXOn;
    public float textSpeed; // default value for text speed in dialogue boxes

    // boolean for toggling debug features e.g. draw Pathfinding paths to screen
    public static boolean debug = false;

    public LunarGame(){
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setTargetFramesPerSecond(targetFPS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.initSettings();

        //create gameScreen and add to screen manager
        /*BossScreen bossScreen = new BossScreen(this);
        mScreenManager.addScreen(bossScreen);
        mScreenManager.setAsCurrentScreen("bossScreen");*/

        /*LunarBestiaryScreen lunarBestiaryScreen = new LunarBestiaryScreen(this);
        mScreenManager.addScreen(lunarBestiaryScreen);
        mScreenManager.setAsCurrentScreen("LunarBestiaryScreen");*/

      /*  LunarPauseScreen lunarPauseScreen = new LunarPauseScreen(this);
        mScreenManager.addScreen(lunarPauseScreen);
        mScreenManager.setAsCurrentScreen("LunarPauseScreen");*/

     /*   LunarMenuScreen lunarMenuScreen = new LunarMenuScreen(this);
        mScreenManager.addScreen(lunarMenuScreen);
        mScreenManager.setAsCurrentScreen("LunarMenuScreen");*/

       /* LunarCharacterSelectScreen lunarCharacterSelectScreen = new LunarCharacterSelectScreen(this);
        mScreenManager.addScreen(lunarCharacterSelectScreen);
        mScreenManager.setAsCurrentScreen("LunarCharacterSelectScreen");*/

        /*LunarLevelSelectScreen lunarLevelSelectScreen = new LunarLevelSelectScreen(this);
        mScreenManager.addScreen(lunarLevelSelectScreen);
        mScreenManager.setAsCurrentScreen("LunarLevelSelectScreen");*/

        /*LunarScoreScreen lunarScoreScreen = new LunarScoreScreen(this, "player_died");
        mScreenManager.addScreen(lunarScoreScreen);
        mScreenManager.setAsCurrentScreen("LunarScoreScreen");*/

        /*LunarScoreScreen lunarScoreScreen = new LunarScoreScreen(this, "player_finished_level");
        mScreenManager.addScreen(lunarScoreScreen);
        mScreenManager.setAsCurrentScreen("LunarScoreScreen");*/

        LunarSplashScreen lunarSplashScreen = new LunarSplashScreen(this);
        mScreenManager.addScreen(lunarSplashScreen);
        mScreenManager.setAsCurrentScreen("LunarSplashScreen");

        return view;
    }

    @Override
    public boolean onBackPressed() {
        // music will not stop when back button is pressed to exit the app
        //mAssetManager.getMusic("mainmenuSong").stop();
        return true;
    }

    private void initSettings() {
        this.isMusicOn = true;
        this.isSFXOn = true;
        this.textSpeed = 70;
    }
}
