package com.gamedev.techtronic.lunargame.gage.engine.graphics;

import android.content.Context;
import com.gamedev.techtronic.lunargame.gage.Game;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import android.graphics.Canvas;
import android.view.View;

/**
 * Canvas based implementation of the render surface interface.
 * 
 * The canvas based render will accept render requests that a game screen draw
 * itself to the surface. The actual draw will be conducted from the GUI thread
 * and as such benefit from hardware acceleration of the canvas draw methods.
 * The cost associated with this benefit is a slightly delayed render (a canvas
 * invalidate request will need to be picked up and proceeded by the GUI
 * thread).
 * 
 * @version 1.0
 */
public class CanvasRenderSurface extends View implements IRenderSurface {

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Properties
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Graphics2D implementation that will be issued to the gamescreen to enable
	 * drawing onto this canvas
	 */
	public CanvasGraphics2D mCanvasGraphics2D;

	/**
	 * Game instance to which this render surface belongs
	 */
	protected Game mGame;

	/**
	 * Android context within which this canvas belongs
	 */
	protected Context mContext;

	/**
	 * Gamescreen instance that will be asked to render itself onto this canvas
	 */
	protected GameScreen mScreenToRender;

	/**
	 * Elapsed time information that will be provided to the game screen to
	 * sequence its render
	 */
	protected ElapsedTime mElapsedTime;

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Constructor
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Create a new canvas render surface for the specified game and Android
	 * context
	 * 
	 * @param game
	 *            Game which will use this render surface
	 * @param context
	 *            Context onto which the surface will be rendered
	 */
	public CanvasRenderSurface(Game game, Context context) {
		super(context);

		// Store the game and context
		mContext = context;
		mGame = game;

		// Create a new Graphics2D instance for drawing on this surface
		mCanvasGraphics2D = new CanvasGraphics2D(context.getAssets());
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Interface Implementation
	// /////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gamedev.techtronic.lunargame.gage.interfaces.IRenderSurface#getAsView()
	 */
	@Override
	public View getAsView() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gamedev.techtronic.lunargame.gage.interfaces.IRenderSurface#render(com.gamedev.techtronic.lunargame
	 * .gage.engine.ElapsedTime, com.gamedev.techtronic.lunargame.gage.engine.GameScreen)
	 */
	@Override
	public void render(ElapsedTime elapsedTime, GameScreen screenToRender) {

		// Store render target + time info
		mElapsedTime = elapsedTime;
		mScreenToRender = screenToRender;

		// Post invalidate message to the UI thread - which will result in
		// the onDraw method being called by the UI thread
		postInvalidate();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// Request that the game screen render itself to this surface
		// using the available graphics 2D instance
		mCanvasGraphics2D.setCanvas(canvas);
		mScreenToRender.draw(mElapsedTime, mCanvasGraphics2D);

		// Notify the game that the render has been completed
		mGame.notifyDrawCompleted();
	}

	public CanvasGraphics2D getCanvasGraphics2D() {
		return mCanvasGraphics2D;
	}
}
