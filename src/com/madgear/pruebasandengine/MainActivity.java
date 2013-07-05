package com.madgear.pruebasandengine;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;


public class MainActivity extends BaseGameActivity {

	// The following constants will be used to define the width and height
	// of our game's camera view
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 600;

	// Declare a Camera object for our activity
	private Camera mCamera;
	
	// Declare a Scene object for our activity
	private Scene mScene;
	
	
	///
	BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	ITextureRegion mSpriteTextureRegion;
	ITiledTextureRegion mTiledTextureRegion;
	
	
	
	
	
	
	
	
	/*
	 * The onCreateEngineOptions method is responsible for creating the options to be
	 * applied to the Engine object once it is created. The options include,
	 * but are not limited to enabling/disable sounds and music, defining multitouch
	 * options, changing rendering options and more.
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {

		// Define our mCamera object
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		// Declare & Define our engine options to be applied to our Engine object
		EngineOptions engineOptions = new EngineOptions(
				true,
				ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(),
				mCamera);

		// It is necessary in a lot of applications to define the following
		// wake lock options in order to disable the device's display
		// from turning off during gameplay due to inactivity
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		// Return the engineOptions object, passing it to the engine
		return engineOptions;
	}
	
	
	
	
	
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	
	
	
	
	
	/*
	 * The onCreateResources method is in place for resource loading, including textures,
	 * sounds, and fonts for the most part. 
	 */
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Create the bitmap texture atlas for the sprite's texture
		region */
		BuildableBitmapTextureAtlas mBitmapTextureAtlas =
				new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1548, 332, TextureOptions.BILINEAR);
		
		/* Create the TiledTextureRegion object, passing in the usual
		parameters, as well as the number of rows and columns in our sprite sheet
		for the final two parameters */
		// 6 = nº de imágenes que tiene la animación :D
		mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, this, "sprite1.png", 6, 1);
		
		/* Build the bitmap texture atlas */
		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
		/* Load the bitmap texture atlas into the device's gpu memory
		*/
		mBitmapTextureAtlas.load();
		
		
		/* We should notify the pOnCreateResourcesCallback that we've finished
		 * loading all of the necessary resources in our game AFTER they are loaded.
		 * onCreateResourcesFinished() should be the last method called.  */
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	
	
	
	
	
	/* The onCreateScene method is in place to handle the scene initialization and setup.
	 * In this method, we must at least *return our mScene object* which will then 
	 * be set as our main scene within our Engine object (handled "behind the scenes").
	 * This method might also setup touch listeners, update handlers, or more events directly
	 * related to the scene.
	 */
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		
		mEngine.registerUpdateHandler(new FPSLogger());
		
		// Create the Scene object
		mScene = new Scene();
		mScene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);
				
		// Notify the callback that we're finished creating the scene, returning
		// mScene to the mEngine object (handled automatically)
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	
	
	
	
	
	/* The onPopulateScene method was introduced to AndEngine as a way of separating
	 * scene-creation from scene population. This method is in place for attaching 
	 * child entities to the scene once it has already been returned to the engine and
	 * set as our main scene.
	 */
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		/* Create a new animated sprite in the center of the scene */
		AnimatedSprite animatedSprite = new AnimatedSprite(WIDTH * 0.5f, HEIGHT * 0.5f,
				mTiledTextureRegion, mEngine.getVertexBufferObjectManager());
		
		/* Length to play each frame before moving to the next */
		long frameDuration[] = { 50	, 100, 150 , 200, 250, 300 };
		
		/* We can define the indices of the animation to play between */
		int firstTileIndex = 0;
		int lastTileIndex = mTiledTextureRegion.getTileCount() - 1;
		
		/* Allow the animation to continuously loop? */
		boolean loopAnimation = true;

		/* Animate the sprite with the data as set defined above */
		animatedSprite.animate(frameDuration, firstTileIndex, lastTileIndex, loopAnimation);
		
		mScene.attachChild(animatedSprite);	
		
		
		// onPopulateSceneFinished(), similar to the resource and scene callback
		// methods, should be called once we are finished populating the scene.
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}


