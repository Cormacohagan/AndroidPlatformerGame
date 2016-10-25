package com.gamedev.techtronic.lunargame.gage.ui;

import com.gamedev.techtronic.lunargame.gage.engine.AssetStore;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.audio.Sound;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.engine.input.Input;
import com.gamedev.techtronic.lunargame.gage.engine.input.TouchEvent;
import com.gamedev.techtronic.lunargame.gage.engine.input.TouchHandler;
import com.gamedev.techtronic.lunargame.gage.util.BoundingBox;
import com.gamedev.techtronic.lunargame.gage.world.GameObject;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;
import android.graphics.Bitmap;

/**
 * Created by 2045516 on 13/08/2015.
 *
 * RENAME THIS CLASS TO PUSH TRIGGER BUTTON - AS OPPOSED TO TOGGLE BUTTON
 * OTHER BUTTON TYPES RELEASE TRIGGER BUTTON
 *                      TOGGLE BUTTON
 *
 *                      PushButton
 *                      ReleaseButton
 *                      ToggleButton
 *
 * IMPORTANT - THIS BUTTON IS ASSUMED TO BE IN SCREEN SPACE.... NOT WORLD SPACE
 *
 *
 */
public class ReleaseButton extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////




    ///////////////////////////////////////////////////////////////////////////
    // Class data: PushButton look and sound                                     //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Name of the graphical asset used to represent the default button state
     */
    protected Bitmap mDefaultBitmap;

    /**
     * Name of the graphical asset used to represent the pushed button state
     */
    protected Bitmap mPushBitmap;

    /**
     * Name of the sound asset to be played whenever the button is clicked
     */
    protected Sound mReleaseSound;




    ///////////////////////////////////////////////////////////////////////////
    // Constructors                                                          //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Create a new push button.
     *
     * @param x
     *            Centre y location of the button
     * @param y
     *            Centre x location of the button
     * @param width
     *            Width of the button
     * @param height
     *            Height of the button
     * @param defaultBitmap
     *            Bitmap used to represent this control
     * @param pushBitmap
     *            Bitmap used to represent this control
     * @param releaseSound
     *            Bitmap used to represent this control
     * @param gameScreen
     *            Gamescreen to which this control belongs
     */
    public ReleaseButton(float x, float y, float width, float height,
                         String defaultBitmap,
                         String pushBitmap,
                         String releaseSound,
                         GameScreen gameScreen) {
        super(x, y, width, height,
                gameScreen.getGame().getAssetManager().getBitmap(defaultBitmap), gameScreen);

        AssetStore assetStore = gameScreen.getGame().getAssetManager();

        mDefaultBitmap = assetStore.getBitmap(defaultBitmap);
        mPushBitmap = assetStore.getBitmap(pushBitmap);

        mReleaseSound = (releaseSound==null) ? null : assetStore.getSound(releaseSound);

    }


    public ReleaseButton(float x, float y, float width, float height,
                         String defaultBitmap,
                         String pushBitmap,
                         GameScreen gameScreen) {
        this(x, y, width, height, defaultBitmap, pushBitmap, null, gameScreen);
    }



    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////


    private boolean mPushTriggered;
    private boolean mIsPushed;

    /**
     * Update the button
     *
     * @param elapsedTime
     *            Elapsed time information
     */
    public void update(ElapsedTime elapsedTime) {
        // Consider any touch events occurring in this update
        Input input = mGameScreen.getGame().getInput();
        BoundingBox bound = getBound();


        // Check for a press release on this button
        for( TouchEvent touchEvent : input.getTouchEvents()) {
            if(touchEvent.type == TouchEvent.TOUCH_UP
                    && bound.contains(touchEvent.x, touchEvent.y)) {
                // A touch up has occured in this control
                mPushTriggered = true;
                // TODO: Also play sound here if it's available.
                return;
            }
        }

        // Check if any of the touch events were on this control
        for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
            if (input.existsTouch(idx)) {
                if (bound.contains(input.getTouchX(idx), input.getTouchY(idx))) {
                    if(!mIsPushed) {
                        mBitmap = mPushBitmap;
                        mIsPushed = true;
                    }

                    return;
                }
            }
        }

        // If we have not returned by this point, then there is no touch event on the button
        if(mIsPushed) {
            mBitmap = mDefaultBitmap;
            mIsPushed = false;
        }
    }


    // This will only be returned as true once per push -
    public boolean pushTriggered() {
        if(mPushTriggered) {
            mPushTriggered = false;
            return true;
        }
        return false;
    }

    public boolean isPushed() {
        return mIsPushed;
    }




    /*
     * (non-Javadoc)
     *
     * @see
     * com.gamedev.techtronic.lunargame.gage.world.GameObject#draw(com.gamedev.techtronic.lunargame.gage.engine
     * .ElapsedTime, com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D,
     * com.gamedev.techtronic.lunargame.gage.world.LayerViewport,
     * com.gamedev.techtronic.lunargame.gage.world.ScreenViewport)
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Assumed to be in screen space so just draw the whole thing
        drawScreenRect.set((int) (position.x - mBound.halfWidth),
                (int) (position.y - mBound.halfWidth),
                (int) (position.x + mBound.halfWidth),
                (int) (position.y + mBound.halfHeight));

        graphics2D.drawBitmap(mBitmap, null, drawScreenRect, null);
    }

}
