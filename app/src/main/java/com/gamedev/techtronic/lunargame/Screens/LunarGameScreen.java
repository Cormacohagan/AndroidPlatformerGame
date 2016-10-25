package com.gamedev.techtronic.lunargame.Screens;


import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.gamedev.techtronic.lunargame.AI.Scheduler;
import com.gamedev.techtronic.lunargame.Character.Enemy;
import com.gamedev.techtronic.lunargame.HUD.HUDBar;
import com.gamedev.techtronic.lunargame.Character.NPC;
import com.gamedev.techtronic.lunargame.Character.Player;
import com.gamedev.techtronic.lunargame.Level.Entities.HealthPickUp;
import com.gamedev.techtronic.lunargame.Level.Entities.InvincibilityPickUp;
import com.gamedev.techtronic.lunargame.Level.Entities.LivesPickUp;
import com.gamedev.techtronic.lunargame.Level.Entities.Projectile;
import com.gamedev.techtronic.lunargame.Level.Triggers.DialogueBox;
import com.gamedev.techtronic.lunargame.Level.Triggers.DialogueTrigger;
import com.gamedev.techtronic.lunargame.HUD.HUD;
import com.gamedev.techtronic.lunargame.HUD.HUDBarResourceBundle;
import com.gamedev.techtronic.lunargame.HUD.HUDLabel;
import com.gamedev.techtronic.lunargame.HUD.HUDUnaryImage;
import com.gamedev.techtronic.lunargame.Level.Entities.JumpPad;
import com.gamedev.techtronic.lunargame.Level.Entities.Key;
import com.gamedev.techtronic.lunargame.Level.Entities.PlayerProjectile;
import com.gamedev.techtronic.lunargame.Level.Entities.ProjectileManager;
import com.gamedev.techtronic.lunargame.Level.Level;
import com.gamedev.techtronic.lunargame.Level.Triggers.FinishLevelTrigger;
import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.ui.PushButton;
import com.gamedev.techtronic.lunargame.gage.util.BoundingBox;
import com.gamedev.techtronic.lunargame.gage.util.CollisionDetector;
import com.gamedev.techtronic.lunargame.gage.util.Vector2;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;
import com.gamedev.techtronic.lunargame.gageExtension.FPSCounter;
import com.gamedev.techtronic.lunargame.gageExtension.ScrollSprite;




// The actual playable game. This game screen will be used regardless of what level is loaded in

public class LunarGameScreen extends GameScreen {

    public enum LevelChosen {LEVEL_INTRO, LEVEL_ONE, LEVEL_BOSS}
    public enum CharacterChosen {CHARACTER_ONE, CHARACTER_TWO}
    public enum ScreenState {PLAY, GAME_OVER, DEAD, PAUSED, DIALOGUE, LEVEL_FINISH}

    private static ScreenState screenState = ScreenState.PLAY;
    private static CharacterChosen characterChosen;
    private static LevelChosen levelChosen;
    //private Player player;
    public static Player player;
    public static Level level;
    private Scheduler AIscheduler;
    private NPC testNPC;
    //private Level level;
    private String levelID = "1", interactionButtonEvent = ""; // default value in case level is not set properly
    private NPC testNPCSeeker;
    private Enemy testEnemy;
    private ScrollSprite waterfall;
    private JumpPad testJumpPad;
    private Key testDoorKey;
    private int projectileCount = 0;
    //private FinishLevelTrigger finishLevelTrigger;
    private PushButton leftButton, rightButton, jumpButton, pauseButton, interactButton, shootButton, activeButton;
    private FPSCounter fpsCounter;
    private Vector2 playerSpawnPosition;
    private int xVel, yVel;
    public float dialogueBoxPositionX, dialogueBoxPositionY, viewportPosX = 240.0F, viewportPosY = 138.0F, viewportZoom;
    private ProjectileManager projectileManager;
    //private FinishLevelTrigger levelTransitionTrigger;
    private boolean playerStateSound = false, playerConfirmedDead = false,
            enemyStateSound = true, enemyConfirmedDead = false, dialogueIntroduced = false,
            levelFinishedSequencePlayedYet = false, finishLevelFade = false;
    private DialogueBox dialogueBox;
    private DialogueTrigger testDialogueTrigger, testDialogueTrigger2;
    private FinishLevelTrigger testFinishLevelTrigger;
    private HealthPickUp testHealthPickUp;
    private LivesPickUp testLivesPickUp;
    private InvincibilityPickUp testInvincibilityPickUp;
    private Paint textPaint, dialoguePaint, inputPaint;
    Thread dialogueThread;
    boolean dThreadStarted = false;
    public static ScreenViewport screenVP;
    public static LayerViewport layerVP;


    private HUD hud;
    private HUDLabel labelHealth;
    private HUDLabel labelLives;
    private HUDBar healthBar;
    private HUDUnaryImage livesLeft;

    //Default constructor for the game screen
    public LunarGameScreen(Game game, String levelID, String characterChosen) {
        super("LunarGameScreen", game);

        fadeFromColour("white", 1, 700, 0);
        disableInput = false;

        setLevelChosen(levelID);
        setCharacterChosen(characterChosen);
        this.initViewports(viewportPosX, viewportPosY, 240.0F);
        super.initAssets();
        this.addComponents();

        // Experimental!
        // Check the class for details

        screenVP = mScreenViewport;
        layerVP = mLayerViewport;

        AIscheduler = new Scheduler();
        // Planning to add to scheduler from level, like
        // for each level.getNPCs: scheduler.add
//        AIscheduler.addNPCBehaviors(testNPC, 30);
        AIscheduler.addNPCBehaviors(testNPCSeeker, 60);


    }

    //Initialise all graphical assets used in the game screen
    protected void initBitmaps() {

        if (characterChosen == CharacterChosen.CHARACTER_ONE) {
            mAssetManager.loadAndAddBitmap("player", "img/inGame/levels/shared/playerSheet.png");
            Log.d("game screen", "char 1 chosen & received by gamescreen");
        }
        else if (characterChosen == CharacterChosen.CHARACTER_TWO) {
            mAssetManager.loadAndAddBitmap("player", "img/inGame/levels/shared/playerSheet2.png");
            Log.d("game screen", "char 2 chosen & received by gamescreen");
        }
        else {
            mAssetManager.loadAndAddBitmap("player", "img/inGame/levels/shared/playerSheet.png");
            Log.d("game screen", "no char received by gamescreen, giving default value");
        }

        mAssetManager.loadAndAddBitmap("npc", "img/inGame/levels/shared/npcs.png");
        mAssetManager.loadAndAddBitmap("jumpPad", "img/inGame/levels/shared/lunarSpring.png");
        mAssetManager.loadAndAddBitmap("npc", "img/inGame/levels/shared/npcs.png");
        mAssetManager.loadAndAddBitmap("gameBackground", "img/inGame/levels/levelOne/levelBackground.png");
        mAssetManager.loadAndAddBitmap("tileSheet", "img/inGame/levels/shared/tileSheet.png");
        mAssetManager.loadAndAddBitmap("miscSheet", "img/inGame/levels/levelOne/miscSheet.png");
        mAssetManager.loadAndAddBitmap("waterfallSheet", "img/inGame/levels/levelOne/waterScroll.png");
        mAssetManager.loadAndAddBitmap("rightArrowButton", "img/inGame/hud/rightArrowButton.png");
        mAssetManager.loadAndAddBitmap("leftArrowButton", "img/inGame/hud/leftArrowButton.png");
        mAssetManager.loadAndAddBitmap("rightArrowButtonClick", "img/inGame/hud/rightArrowButtonTouched.png");
        mAssetManager.loadAndAddBitmap("shootButton", "img/inGame/hud/shootButton.png");
        mAssetManager.loadAndAddBitmap("playerBullet", "img/inGame/levels/shared/PlayerProjectileSprite.png");
        mAssetManager.loadAndAddBitmap("leftArrowButtonClick", "img/inGame/hud/leftArrowButtonTouched.png");
        mAssetManager.loadAndAddBitmap("upArrowButton", "img/inGame/hud/upArrowButton.png");
        mAssetManager.loadAndAddBitmap("upArrowButtonClick", "img/inGame/hud/upArrowButtonTouched.png");
        mAssetManager.loadAndAddBitmap("interactButton", "img/inGame/hud/interactButton.png");
        mAssetManager.loadAndAddBitmap("interactButtonClick", "img/inGame/hud/interactButtonTouched.png");
        mAssetManager.loadAndAddBitmap("pauseButton", "img/inGame/hud/pauseButton.png");
        mAssetManager.loadAndAddBitmap("pauseButtonClick", "img/inGame/hud/pauseButtonTouched.png");
        mAssetManager.loadAndAddBitmap("dialogueBox", "img/menus/shared/dialogueBox.png");
        mAssetManager.loadAndAddBitmap("testDialogueTrigger", "img/inGame/levels/shared/dialogueTrigger.png");
        mAssetManager.loadAndAddBitmap("hudHPBackground", "img/inGame/hud/hpBackground.png");
        mAssetManager.loadAndAddBitmap("hudHPStart", "img/inGame/hud/hpStart.png");
        mAssetManager.loadAndAddBitmap("hudHPMid", "img/inGame/hud/hpSegment.png");
        mAssetManager.loadAndAddBitmap("hudHPEnd", "img/inGame/hud/hpEnd.png");
        mAssetManager.loadAndAddBitmap("lifeIcon", "img/inGame/hud/lifeIcon.png");
        mAssetManager.loadAndAddBitmap("labelAlphabet", "img/inGame/hud/hudAlphabetSheet.png");
    }

    //Initialise all audio assets used in the game screen
    protected void initSounds() {
        mAssetManager.loadAndAddSound("playerDeath", "sfx/inGame/playerDeath.ogg");
        mAssetManager.loadAndAddSound("respawn", "sfx/inGame/playerRespawn.ogg");
        mAssetManager.loadAndAddSound("playerZeroLives", "sfx/inGame/playerDeathZeroLives.ogg");
        mAssetManager.loadAndAddSound("npcDeath", "sfx/inGame/enemyTestDeath.ogg");
        mAssetManager.loadAndAddSound("dialogueStart", "sfx/inGame/dialogueStart.ogg");
        mAssetManager.loadAndAddSound("dialogueEnd", "sfx/inGame/dialogueEnd.ogg");
        mAssetManager.loadAndAddSound("dialogueSpeech1", "sfx/inGame/dialogueSpeech1.ogg");
        mAssetManager.loadAndAddSound("dialogueSpeech2", "sfx/inGame/dialogueSpeech2.ogg");
        mAssetManager.loadAndAddSound("playerHurt", "sfx/inGame/playerHurt.ogg");
        mAssetManager.loadAndAddSound("pickUp1", "sfx/inGame/pickUp1.ogg");
        mAssetManager.getSound("playerDeath").setVolume(0.5f);
        mAssetManager.getSound("respawn").setVolume(0.5f);
        mAssetManager.getSound("playerZeroLives").setVolume(0.6f);
        mAssetManager.getSound("npcDeath").setVolume(0.7f);
    }

    //Add game objects to the game screen
    protected void addComponents() {

        // Level class wraps up a lot of loading, updating and drawing of game objects such as moving platforms
        // bitmap in constructor is just a a null placeholder, it isn't used
        level = new Level(0, 0, mAssetManager.getBitmap("tileSheet"), this);

        // set levelID here
        level.loadLevel(levelID, this.getGame().getActivity());
        //level.loadLevel("1", this.getGame().getActivity());

        playerSpawnPosition = new Vector2(level.getPlayerSpawnPosition());

        projectileManager = new ProjectileManager();

        player = new Player(playerSpawnPosition.x, playerSpawnPosition.y, mAssetManager.getBitmap("player"), this, "player_standing_right", true);
        testNPC = new NPC(300, 140, mAssetManager.getBitmap("npc"), this, "soldier_idle_right", true, 200, 20, "STAND_STILL");
        testNPCSeeker = new NPC(300, 140, mAssetManager.getBitmap("npc"), this, "floating_ball_spin", true, 190, 20, "STAND_STILL");
        testEnemy = new Enemy(300, 140, mAssetManager.getBitmap("npc"), this, "soldier_idle_left", true, 190, 20, "STAND_STILL");
        testJumpPad = new JumpPad(205, 97, mAssetManager.getBitmap("jumpPad"), this, "jump_pad_unactivated", true, 130);
        testDoorKey = new Key(105, 97, 20, 20, mAssetManager.getBitmap("lifeIcon"), this, "SHRINK_AND_GROW");
        testHealthPickUp = new HealthPickUp(205, 127, 20, 20, mAssetManager.getBitmap("lifeIcon"), this, "FLOAT", 40);
        testLivesPickUp = new LivesPickUp(245, 157, 20, 20, mAssetManager.getBitmap("lifeIcon"), this, "SHRINK_AND_GROW", 1);
        testInvincibilityPickUp = new InvincibilityPickUp(275, 157, 20, 20, mAssetManager.getBitmap("lifeIcon"), this, "SHRINK_AND_GROW", 400);

        yVel = (-20);
        player.velocity.set(0, yVel);

        //dialogueThread = new Thread(dialogueBox);
        dialogueBoxPositionX =  mLayerViewport.getWidth()/2;
        dialogueBoxPositionY =  mLayerViewport.getHeight()/3;

        dialogueBox = new DialogueBox(dialogueBoxPositionX, dialogueBoxPositionY, mAssetManager.getBitmap("dialogueBox"), this, "dialogue_box", true, mLayerViewport);
        testDialogueTrigger = new DialogueTrigger(150, 85, mAssetManager.getBitmap("testDialogueTrigger"), this, "dialogue_trigger", false, "test_dialogue_2");
        testDialogueTrigger2 = new DialogueTrigger(80, 85, mAssetManager.getBitmap("testDialogueTrigger"), this, "dialogue_trigger", false, "test_dialogue_1");
        testFinishLevelTrigger = new FinishLevelTrigger(280, 85, mAssetManager.getBitmap("testDialogueTrigger"), this, "dialogue_trigger", false);

        //levelTransitionTrigger = new FinishLevelTrigger("Boss Screen",150,150,50,50,mAssetManager.getBitmap("testDialogueTrigger"),this);

        // Uses a Scrolling Sprite, additional method of showing movement, most suited to flowing liquid
        waterfall = new ScrollSprite(400, 100, 49, 126, mAssetManager.getBitmap("waterfallSheet"), this);
        leftButton = new PushButton(mScreenViewport.left + mScreenViewport.width / 9.2f,
                mScreenViewport.top + mScreenViewport.height /1.1f, mScreenViewport.width / 10, mScreenViewport.width / 10,
                "leftArrowButton", "leftArrowButtonClick", this);
        rightButton = new PushButton(mScreenViewport.left + mScreenViewport.width / 4.6f,
                mScreenViewport.top + mScreenViewport.height /1.1f, mScreenViewport.width / 10, mScreenViewport.width / 10,
                "rightArrowButton", "rightArrowButtonClick", this);
        jumpButton = new PushButton (mScreenViewport.left + mScreenViewport.width * 0.89f,
                mScreenViewport.top + mScreenViewport.height /1.1f, mScreenViewport.width / 10, mScreenViewport.width / 10,
                "upArrowButton", "upArrowButtonClick", this);
        pauseButton = new PushButton(mScreenViewport.left + mScreenViewport.width / 10.0f * 9.2f,
                mScreenViewport.top + mScreenViewport.height /9.1f, mScreenViewport.width / 10, mScreenViewport.width / 10,
                "pauseButton", "pauseButtonClick", this);
        interactButton = new PushButton (mScreenViewport.left + mScreenViewport.width * 0.78f,
                mScreenViewport.top + mScreenViewport.height /1.1f, mScreenViewport.width / 10, mScreenViewport.width / 10,
                "interactButton", "interactButtonClick", this);
        shootButton = new PushButton (mScreenViewport.left + mScreenViewport.width * 0.78f,
                mScreenViewport.top + mScreenViewport.height /1.1f, mScreenViewport.width / 10, mScreenViewport.width / 10,
                "shootButton","upArrowButtonClick", this);
        activeButton = shootButton;



        // Paint object for putting text onscreen
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(30);

        // Paint object for input buttons
        inputPaint = new Paint();
        inputPaint.setAlpha(170);

        // Paint object for black background in dialogue state
        dialoguePaint = new Paint();
        dialoguePaint.setColor(Color.argb(220, 0, 0, 0));
        dialoguePaint.setStyle(Paint.Style.FILL);

        fpsCounter = new FPSCounter(mScreenViewport.width - 160, 35, 30, this.mGame);

        this.initHUD();
    }

    private void initHUD() {
        this.hud = new HUD(0, 0, false);

        HUDBarResourceBundle hpBarResources = new HUDBarResourceBundle(mAssetManager.getBitmap("hudHPBackground"), mAssetManager.getBitmap("hudHPStart"), mAssetManager.getBitmap("hudHPMid"),mAssetManager.getBitmap("hudHPEnd"));

        this.labelHealth = new HUDLabel("Health", mScreenViewport.left + 20, mScreenViewport.top + 20, this.mAssetManager.getBitmap("labelAlphabet"), this);
        this.healthBar = new HUDBar("Health", mScreenViewport.left + 220, mScreenViewport.top + 20, hpBarResources, 0, 100, 100, this);
        this.labelLives = new HUDLabel("Lives", mScreenViewport.left + 400, mScreenViewport.top + 20, this.mAssetManager.getBitmap("labelAlphabet"), this);
        this.livesLeft = new HUDUnaryImage("Lives", mScreenViewport.left + 570, mScreenViewport.top + 20, this.mAssetManager.getBitmap("lifeIcon"), player.livesLeft, this);

        this.hud.addHUDComponent(this.labelHealth);
        this.hud.addHUDComponent(this.healthBar);
        this.hud.addHUDComponent(this.labelLives);
        this.hud.addHUDComponent(this.livesLeft);

    }

    // updates state of the game screen & handles user input
    @Override
    public void update(ElapsedTime elapsedTime) {

        player.isJumping = true;

         /*
        Check if the player has run out of lives,
        if so, transition to the menu screen after a set time
         */

        if (exitScreen) {
            if (screenFadeTo == "LunarPauseScreen"){
                mGame.getScreenManager().addScreen(new LunarPauseScreen(mGame));
                mGame.getScreenManager().setAsCurrentScreen("LunarPauseScreen");
            }
            else if (screenFadeTo == "LunarScoreScreenPlayerDeath") {
                mGame.getScreenManager().addScreen(new LunarScoreScreen(mGame, "died", player.score, 192, player.livesLeft));
                mGame.getScreenManager().setAsCurrentScreen("LunarScoreScreen");
            }
            else if (screenFadeTo == "LunarScoreScreenLevelFinish") {
                mGame.getScreenManager().addScreen(new LunarScoreScreen(mGame, levelID, player.score, 192, player.livesLeft));
                mGame.getScreenManager().setAsCurrentScreen("LunarScoreScreen");
            }

            this.resetFade();
            exitScreen = false;
        }

        /*
        Check if the player has died and take appropriate action
         */
        if (!playerConfirmedDead) {
            if (player.isDead()) {
                player.livesLeft -= 1;
                if (player.livesLeft > 0) {
                    screenState = ScreenState.DEAD;
                }
                else {
                    screenState = ScreenState.GAME_OVER;
                }
                playerConfirmedDead = true;
                playerStateSound = true;
            }
        }

        /*
        Check if the enemy has died and take appropriate action
         */
        if (!enemyConfirmedDead) {
            if (testNPC.isDead()) {
                testNPC.setAnimation("npc_2_death", false);
                enemyConfirmedDead = true;
            }
        }

        if (screenState == ScreenState.GAME_OVER) { // if game is in GAME OVER state..

            resolveEndState();
        }
        else if (screenState == ScreenState.DEAD) {
            if (!disableInput) {
                resolveTouchEvents();
            }
        }
        else if (screenState == ScreenState.LEVEL_FINISH) {
            if (!screenCurrentlyFading){
                if (!levelFinishedSequencePlayedYet) {

                    // play victory sound
                    // display level complete
                    // wait a little bit
                    // fadeToColourAndSwitchScreen(level select screen)

                    //displayLevelCompleteText = true;
                    disableInput = false;
                    levelFinishedSequencePlayedYet = true;
                }


                resolveTouchEvents();
            }
        }
        else if (screenState == ScreenState.PLAY) { // If game is currently running (not paused, in a gameover state, in dead state or in dialogue)
            projectileManager.update(elapsedTime);
            if (player.isInvincible()) {
                player.decreaseInvincibilityTimeLeft(1);
            }
            if (testEnemy.isInvincible()) {
                testEnemy.decreaseInvincibilityTimeLeft(1);
            }

            //Collision with the enemy
            if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testEnemy.getBound())) {
                if (CollisionDetector.determineCollisionType(player.customCollisionBox.getBound(), testEnemy.getBound()) == CollisionDetector.CollisionType.Top) {
                    if(!testEnemy.isInvincible()) {
                        testEnemy.takeDamage(player.damage);
                        testEnemy.setInvincible(true);
                    }
                    Log.d("Test Enemy", "Test enemy was hurt by player for " + player.damage + " points of damage. Enemy's health is now: " + testEnemy.health);
                }
                else if (CollisionDetector.determineCollisionType(player.customCollisionBox.getBound(), testEnemy.getBound()) == CollisionDetector.CollisionType.Left) {
                    player.velocity.set(40, 50);
                    if (!player.isInvincible()){
                        player.takeDamage(testEnemy.damage);
                        player.setInvincible(true);
                        mAssetManager.getSound("playerHurt").play();
                    }
                }
                else if (CollisionDetector.determineCollisionType(player.customCollisionBox.getBound(), testEnemy.getBound()) == CollisionDetector.CollisionType.Right) {
                    player.velocity.set(-40, 50);
                    if (!player.isInvincible()){
                        player.takeDamage(testEnemy.damage);
                        player.setInvincible(true);
                        mAssetManager.getSound("playerHurt").play();
                    }
                }
            }

            if (playerStateSound) {
                mAssetManager.getSound("respawn").play();
                playerStateSound = false;
            }


            if (!disableInput) {
                resolveTouchEvents();
            }

            // Important to resolve any collisions with platform bounding box before checking if the player is on a platform
            CollisionDetector.determineAndResolveCollision(player.customCollisionBox, level.getMovingPlatformList().get(0));
            CollisionDetector.determineAndResolveCollision(testNPC.customCollisionBox, level.getMovingPlatformList().get(0));

            if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testDoorKey.getBound())) {
                player.setHoldingKey(true);
                testDoorKey.setCollected(true);
            }

            player.update(elapsedTime);
            testNPC.update(elapsedTime);
            testNPCSeeker.update(elapsedTime);
            testEnemy.update(elapsedTime);
            level.update(elapsedTime);
            waterfall.update(elapsedTime);
            leftButton.update(elapsedTime);
            rightButton.update(elapsedTime);
            jumpButton.update(elapsedTime);
            pauseButton.update(elapsedTime);
            activeButton.update(elapsedTime);
            testDialogueTrigger.update(elapsedTime);
            testDialogueTrigger2.update(elapsedTime);
            testFinishLevelTrigger.update(elapsedTime);
            testHealthPickUp.update(elapsedTime);
            testLivesPickUp.update(elapsedTime);
            testInvincibilityPickUp.update(elapsedTime);
            testJumpPad.update(elapsedTime);
            testDoorKey.update(elapsedTime);

            /*if (projectileCount > 0){
                projectileManager.update(elapsedTime);
            }
            if (shootButton.pushTriggered()) {
                projectileManager.update(elapsedTime);
                projectileCount = projectileCount +1;
                System.out.println(projectileCount);
            }*/


            // Detect if player is on platform, if so match player velocity to platform
            if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), level.getMovingPlatformList().get(0).customCollisionBox.getBound())) {
                if (CollisionDetector.determineCollisionType(player.customCollisionBox.getBound(), level.getMovingPlatformList().get(0).customCollisionBox.getBound())== CollisionDetector.CollisionType.Top);
                {
                    player.velocity.set(level.getMovingPlatformList().get(0).velocity);
                }
                player.isJumping = false;
                player.canJump = true;
            }

            // same as above, but for npc
            if (CollisionDetector.isCollision(testNPC.customCollisionBox.getBound(), level.getMovingPlatformList().get(0).customCollisionBox.getBound())) {
                testNPC.velocity.set(level.getMovingPlatformList().get(0).velocity);
            } else {
//                testNPC.velocity.set(0, -30);
            }

            // collision detection for dialogue trigger with player
            if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testDialogueTrigger.getBound())) {
                activeButton = interactButton;
                dialogueBox.activateDialogueBox(testDialogueTrigger.getDialogueID());
                setEventForInteractionButtonTouch("dialogue");
            }
            else if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testDialogueTrigger2.getBound())) {
                activeButton = interactButton;
                dialogueBox.activateDialogueBox(testDialogueTrigger2.getDialogueID());
                setEventForInteractionButtonTouch("dialogue");
            }
            else if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testFinishLevelTrigger.getBound())) {
                activeButton = interactButton;
                setEventForInteractionButtonTouch("level_finish");
            }
            else{
                activeButton = shootButton;
            }

            if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testHealthPickUp.getBound())) {
                testHealthPickUp.setCollected(true);
                testHealthPickUp.setPosition(-200, -200);
                mAssetManager.getSound("pickUp1").play();
                player.addHealth(testHealthPickUp.getHealthIncreaseValue());
            }
            else if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testLivesPickUp.getBound())) {
                testLivesPickUp.setPosition(-100, -100);
                mAssetManager.getSound("pickUp1").play();
                player.addLives(testLivesPickUp.getLivesIncreaseValue());
            }
            else if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testInvincibilityPickUp.getBound())) {
                testInvincibilityPickUp.setPosition(-100, -100);
                player.setInvincible(true);
                mAssetManager.getSound("pickUp1").play();
                player.invincibilityTimeLeft = 600;
            }

            if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), testJumpPad.getBound())) {
                player.velocity.y = testJumpPad.getVelocityValue();
                testJumpPad.setAnimation("jump_pad_activated", true);
            }

            // Collision detection for the level ending trigger with player
            /*if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), levelTransitionTrigger.getBound())) {
                levelTransitionTrigger.setPosition(-500, -500);
                levelTransitionTrigger.execute();
            }*/

            // collision detection for score pickups
            for (int i = 0; i < level.getPickUpList().size(); i++ ) {
                if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), level.getPickUpList().get(i).getBound())) {
                    player.addScore(level.getPickUpList().get(i).getScoreValue());
                    mAssetManager.getSound("pickUp1").play();
                    Log.d("Pickup Debug", "Pickup number " + i + " in arraylist collected");
                    level.getPickUpList().remove(i);
                }
            }


            // Check if tiles which have collision are touching player, then resolve
            for (int i = 0; i < level.getCollidableTileList().size(); i++) {

                if (level.getCollidableTileList().get(i).isDamaging()) {
                    if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), level.getCollidableTileList().get(i).getBound())) {
                        if (!player.isInvincible()) {
                            player.takeDamage(player.health);
                            player.setInvincible(true);
                        }
                    } else if (CollisionDetector.isCollision(testNPC.getBound(), level.getCollidableTileList().get(i).getBound())) {
                        testNPC.takeDamage(testNPC.health);
                        if (enemyStateSound) {
                            mAssetManager.getSound("npcDeath").play();
                            enemyStateSound = false;
                        }

                    }
                }
                if (level.getCollidableTileList().get(i).isCollidable()) {
                    if (CollisionDetector.isCollision(player.customCollisionBox.getBound(), level.getCollidableTileList().get(i).getBound())) {
                        if (player.mCurrentAnimation.name.contains("right")) {
                            player.setAnimation("player_standing_right", true);
                        } else if (player.mCurrentAnimation.name.contains("left")) {
                            player.setAnimation("player_standing_left", true);
                        }
                        if (CollisionDetector.determineCollisionType(player.customCollisionBox.getBound(), level.getCollidableTileList().get(i).getBound()) == CollisionDetector.CollisionType.Top) {
                            player.velocity.y = -1;
                            player.isJumping = false;
                            player.canJump = true;
                        } else if (CollisionDetector.determineCollisionType(player.customCollisionBox.getBound(), level.getCollidableTileList().get(i).getBound()) == CollisionDetector.CollisionType.Bottom) {
                            player.velocity.y = -50;
                            player.isJumping = true;
                            player.canJump = false;
                        } else if (CollisionDetector.determineCollisionType(player.customCollisionBox.getBound(), level.getCollidableTileList().get(i).getBound()) == CollisionDetector.CollisionType.Left) {
                            player.velocity.y = -50;
                            player.isJumping = true;
                            player.canJump = false;
                        } else if (CollisionDetector.determineCollisionType(player.customCollisionBox.getBound(), level.getCollidableTileList().get(i).getBound()) == CollisionDetector.CollisionType.Right) {
                            player.velocity.y = -50;
                            player.isJumping = true;
                            player.canJump = false;
                        } else {
                            player.velocity.y = -50;
                            player.isJumping = true;
                            player.canJump = false;
                        }
                    }
                }

                //Absolute projectile collision spaghetti, ravioli ravioli give me the coderoni
               /* for (int j = 0; j < projectileCount; j++) {
                    Projectile currentBullet = projectileManager.returnCurrentBullet();
                    if (CollisionDetector.isCollision(currentBullet.customCollisionBox.getBound(), level.getCollidableTileList().get(i).getBound()))
                    {
                        projectileManager.removeBullet(projectileCount);
                        projectileCount = projectileCount - 1;
                    }
                }*/

                CollisionDetector.determineAndResolveCollision(player.customCollisionBox, level.getCollidableTileList().get(i));
                CollisionDetector.determineAndResolveCollision(testNPC.customCollisionBox, level.getCollidableTileList().get(i));
            }

            updateLayerViewport();
        }
        else if (screenState == ScreenState.DIALOGUE) {

            if (!dThreadStarted) {
                mAssetManager.getSound("dialogueStart").play();
                //dialogueBox.activateDialogueBox(testDialogueTrigger.getDialogueID());
                dialogueThread = new Thread(dialogueBox);
                dialogueThread.start();
                dThreadStarted = true;
            }

            dialogueBox.update(elapsedTime);

        }

        this.healthBar.update(elapsedTime, player.health);
        this.livesLeft.update(elapsedTime, player.livesLeft);

        AIscheduler.run();
        fpsCounter.update(elapsedTime);

    }

    protected void resolveTouchEvents() {

        if (screenState == ScreenState.PLAY) {
            if (leftButton.isPushed()) {
                xVel = (-50);
                player.velocity.x = xVel;
                player.setAnimation("player_walking_left", true);
                player.setFacingRight(false);
            } else if (rightButton.isPushed()) {
                xVel = (50);
                player.velocity.x = xVel;
                player.setAnimation("player_walking_right", true);
                player.setFacingRight(true);
            } else if (jumpButton.isPushed()) {
                if (player.canJump) {
                    if (player.mCurrentAnimation.name.contains("left")) {
                        player.setAnimation("player_jump_left", true);
                        player.setFacingRight(false);
                    } else if (player.mCurrentAnimation.name.contains("right")) {
                        player.setAnimation("player_jump_right", true);
                        player.setFacingRight(true);
                    }

                    player.canJump = false;
                    player.isJumping = true;
                    player.velocity.y = 90;
                }
            } else {
                player.xDecelerate(0.1F, 0.9F);
            }

            if (pauseButton.pushTriggered()) {
                fadeToColourAndSwitchScreen("black", 8, 0, 0, 0, "LunarPauseScreen");
            } else if (interactButton.pushTriggered()) {
                activateEventForInteractionButtonTouch();
            } else if (shootButton.pushTriggered()) {
                projectileManager.createProjectile(new PlayerProjectile(player.position.x, player.position.y, mAssetManager.getBitmap("playerBullet"), this, "bullet_shooting", true));
                projectileCount = projectileCount +1;
                if (player.mCurrentAnimation.name.contains("left")) {
                    projectileManager.setBulletVelocity(-100, projectileCount);
                }
                else{
                    projectileManager.setBulletVelocity(100, projectileCount);
                }
            } else if (screenState == ScreenState.DEAD) {

                if (mGame.getInput().existsTouch(0)) {
                    Log.d("test", "touch received on death screen");
                    spawnPlayer();
                }
            } else if (screenState == ScreenState.LEVEL_FINISH && !disableInput) {

                if (mGame.getInput().existsTouch(0)) {
                    disableInput = true;
                    finishLevelFade = true;

                    fadeToColourAndSwitchScreen("black", 1, 0, 1000, 230, "LunarScoreScreenLevelFinish");
                }
            }
        }
    }

    protected void resolveEndState() {
        if (playerStateSound) { // if death sound hasn't been played yet.. play death sound
            mAssetManager.getSound("playerZeroLives").play();
            playerStateSound = false; // (stops sound getting played each update() call)
            fadeToColourAndSwitchScreen("black", 2, 1200, 0, 0, "LunarScoreScreenPlayerDeath"); // cheeky, but allows the fade to be called once
        }

    }

    protected void spawnPlayer() {

        player.setPosition(playerSpawnPosition.x, playerSpawnPosition.y);
        player.health = player.getMaxHealthValue();
        playerConfirmedDead = false;
        screenState = ScreenState.PLAY;
        //playerStateSound = true;
    }

    protected void gameOverTransition(final int sleepTime) {
        Runnable gameOverTransition = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(sleepTime);
                    mGame.getScreenManager().removeScreen(LunarGameScreen.this.mName);
                    mGame.getScreenManager().addScreen(new LunarMenuScreen(mGame));
                    mGame.getScreenManager().setAsCurrentScreen("LunarMenuScreen");
                    System.gc();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        (new Thread(gameOverTransition)).run();
    }

    protected void setCharacterChosen(String charChosen) {
        if (charChosen == "CHARACTER_ONE") {
            characterChosen = CharacterChosen.CHARACTER_ONE;
        }
        else if (charChosen == "CHARACTER_TWO") {
            characterChosen = CharacterChosen.CHARACTER_TWO;
        }
        else {
            Log.d("error", "no character was chosen");
        }

    }

    protected void setLevelChosen(String levelChosen) {

        if (levelChosen == "LEVEL_INTRO") {
            levelID = "1";
        }
        else if (levelChosen == "LEVEL_ONE") {
            levelID = "0";
        }
        else if (levelChosen == "LEVEL_BOSS") {
            levelID = "2";
        }
        else {
            Log.d("error", "no level was chosen");
        }
    }

    // renders the most recent state of the game screen that's available
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());


        level.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testDialogueTrigger.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testDialogueTrigger2.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testFinishLevelTrigger.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testJumpPad.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testNPC.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testNPCSeeker.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testEnemy.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        waterfall.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        player.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        leftButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, inputPaint);
        rightButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, inputPaint);
        jumpButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, inputPaint);
        pauseButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, inputPaint);
        activeButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport, inputPaint);
        testDoorKey.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testHealthPickUp.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testLivesPickUp.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        testInvincibilityPickUp.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        //if (projectileCount > 0){
        //projectileManager.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        //}

        /*if (projectileCount > 0){
            projectileManager.draw(elapsedTime,graphics2D, mLayerViewport, mScreenViewport);
        }
        if (shootButton.pushTriggered()) {
            projectileManager.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            projectileCount = projectileCount +1;
        }*/

        projectileManager.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        if (screenState == ScreenState.DEAD) {
            textPaint.setColor(Color.argb(220, 0, 0, 0));
            textPaint.setStyle(Paint.Style.FILL);
            graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top, mScreenViewport.right, mScreenViewport.bottom, textPaint);

            textPaint.setColor(Color.argb(255, 0, 255, 0));
            textPaint.setTextAlign(Paint.Align.CENTER);
            graphics2D.drawText("YOU DIED. TOUCH SCREEN TO RESPAWN", mScreenViewport.centerX(), mScreenViewport.centerY(), textPaint);

        }
        else if (screenState == ScreenState.DIALOGUE) {

            graphics2D.drawRect(mScreenViewport.left, mScreenViewport.top, mScreenViewport.right, mScreenViewport.bottom, dialoguePaint);
            dialogueBox.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }

            // Draws FPS counter and a shadow to improve readability
        fpsCounter.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        graphics2D.drawText("Score: " + String.valueOf(player.score), 780, 45, textPaint);

        hud.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        super.draw(elapsedTime, graphics2D);

        if (screenState == ScreenState.LEVEL_FINISH) {
            player.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            if (!screenCurrentlyFading) {
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(50);
                graphics2D.drawText("LEVEL COMPLETED", 640, 300, textPaint);
            }
            if (finishLevelFade) {
                super.draw(elapsedTime, graphics2D);
            }
        }

    }

    public static void setScreenState(ScreenState state) {
        screenState = state;
    }

    public void setEventForInteractionButtonTouch(String typeOfEvent) {
        interactionButtonEvent = typeOfEvent;
    }

    public void activateEventForInteractionButtonTouch() {
        if (interactionButtonEvent == "dialogue") {
            //dialogueBox.set
            //testDialogueTrigger.setPosition(-500, -500);
            screenState = ScreenState.DIALOGUE;
        }
        else if (interactionButtonEvent == "level_finish") {
            if (player.getHoldingKey()) {
                screenState = ScreenState.LEVEL_FINISH;
                disableInput = true;
                fadeToColour("black", 1, 0, 2000, 230);
            }
            else {
                Log.d("t", "Level can't be finished, player doesn't have key");
            }
        }
    }

    // updates viewport position based on player position (so the player doesn't disappear off-screen
    private void updateLayerViewport() {
        BoundingBox playerBox = player.getBound();
        // if player's x pos is 66% or greater than layerviewport.right, shift layerviewport
        if (playerBox.getLeft() <= mLayerViewport.getLeft()+(mLayerViewport.getWidth()/2.3)) {
            viewportPosX-=1;
        }
        else if(playerBox.getRight() >= mLayerViewport.getRight() -(mLayerViewport.getWidth()/2.3)) {
            viewportPosX+=1;
        }

        if(playerBox.getTop() >= mLayerViewport.getTop() - (mLayerViewport.getHeight()/3)) {
            viewportPosY+=1;
        }
        else if(playerBox.getBottom() <= mLayerViewport.getBottom() + (mLayerViewport.getHeight()/3)) {
            viewportPosY-=1;

        }

        mLayerViewport.setX(viewportPosX);
        mLayerViewport.setY(viewportPosY);
    }
}
