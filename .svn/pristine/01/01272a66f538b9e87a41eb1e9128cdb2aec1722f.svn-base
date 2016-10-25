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
public class ToggleButton extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     */
    protected enum ButtonState { ON, OFF}

    /**
     * Current button state
     */
    protected ButtonState mButtonState = ButtonState.OFF;



    ///////////////////////////////////////////////////////////////////////////
    // Class data: PushButton look and sound                                     //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Name of the graphical asset used to represent the default button state
     */
    protected Bitmap mOffBitmap;

    protected Bitmap mOffHoverBitmap;


    protected Bitmap mOnBitmap;

    protected Bitmap mOnHoverBitmap;



    /**
     * Name of the sound asset to be played whenever the button is clicked
     */
    protected Sound mOnSound;

    protected Sound mOffSound;



    ///////////////////////////////////////////////////////////////////////////
    // Constructors                                                          //
    ///////////////////////////////////////////////////////////////////////////



    public ToggleButton(float x, float y, float width, float height,
                        String offBitmap,
                        String offHoverBitmap,
                        String onBitmap,
                        String onHoverBitmap,
                        String onSound,
                        String offSound,
                        GameScreen gameScreen) {
        super(x, y, width, height,
                gameScreen.getGame().getAssetManager().getBitmap(offBitmap), gameScreen);

        AssetStore assetStore = gameScreen.getGame().getAssetManager();

        mOffBitmap = assetStore.getBitmap(offBitmap);
        mOffHoverBitmap = (offHoverBitmap==null) ? null : assetStore.getBitmap(offHoverBitmap);

        mOnBitmap = assetStore.getBitmap(onBitmap);
        mOnHoverBitmap = (onHoverBitmap==null) ? null : assetStore.getBitmap(onHoverBitmap);

        mOnSound = (onSound==null) ? null : assetStore.getSound(onSound);
        mOffSound = (offSound==null) ? null : assetStore.getSound(offSound);


    }

    public ToggleButton(float x, float y, float width, float height,
                        String offBitmap,
                        String offHoverBitmap,
                        String onBitmap,
                        String onHoverBitmap,
                        GameScreen gameScreen) {
        this(x, y, width, height, offBitmap, offHoverBitmap, onBitmap, onHoverBitmap, null, null, gameScreen);
    }

    public ToggleButton(float x, float y, float width, float height,
                        String offBitmap,
                        String onBitmap,
                        GameScreen gameScreen) {
        this(x, y, width, height, offBitmap, null, onBitmap, null, null, null, gameScreen);
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
                if(mButtonState == ButtonState.OFF) {
                    setToggled(true);
                    //mGameScreen.playToggleSfx(true);

                } else {
                    setToggled(false);
                    //mGameScreen.playToggleSfx(false);
                }

                return;
            }
        }


        // If no press release, check if we are hovering over the control
        // Check if any of the touch events were on this control
        for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
            if (input.existsTouch(idx)) {
                if (bound.contains(input.getTouchX(idx), input.getTouchY(idx))) {
                    if(!mIsPushed) {
                        mPushTriggered = true;
                        if(mOnHoverBitmap != null && mOffHoverBitmap != null )
                            mBitmap = mButtonState== ButtonState.ON ? mOnHoverBitmap : mOffHoverBitmap;
                        mIsPushed = true;
                    }
                    return;
                }
            }
        }

        // Toggle if we get here then there is no touch event occur within the region
        if(mIsPushed) {
            mBitmap = mButtonState== ButtonState.ON ? mOnBitmap : mOffBitmap;
            mIsPushed = false;
            mPushTriggered = false;
        }
    }




    public boolean isToggledOn() {
        return mButtonState == ButtonState.ON;
    }

    public void setToggled(boolean on) {
        if(on) {
            mButtonState = ButtonState.ON;
            mBitmap = mOnBitmap;
        } else {
            mButtonState = ButtonState.OFF;
            mBitmap = mOffBitmap;
        }
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
