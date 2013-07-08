package com.madgear.pruebasandengine;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;

import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.align.HorizontalAlign;

import android.util.Log;



public class MainActivity extends BaseGameActivity implements IOnSceneTouchListener {

	// The following constants will be used to define the width and height
	// of our game's camera view
	private static final int WIDTH = 1920;		// Ouya res.
	private static final int HEIGHT = 1080;		// Ouya res.

	// Declare a Camera object for our activity
	private Camera mCamera;

	// Declare a Scene object for our activity
	private Scene mScene;

	AnimatedSprite animatedSpriteClon;
	int r = 0;



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
				new RatioResolutionPolicy(16/9f),   // Ratio 16:9
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

		// Setup the ResourceManager.
		ResourceManager.setup(this, this.getEngine(), this.getApplicationContext(),
				WIDTH, HEIGHT, 0,0);

		ResourceManager.getInstance().loadGameTextures(mEngine, this);
		ResourceManager.getInstance().loadFonts(mEngine);

		// Iniciamos la puntuación, fase, etc
		GameManager.getInstance().resetGame();
		
		// Se crea el fichero de datos del usuario si no existe:
		UserData.getInstance().init(ResourceManager.getInstance().context);

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


		
		// mEngine.registerUpdateHandler(new FPSLogger());  ??? no se para que sirve esto :/

		// Create the Scene object
		mScene = new Scene();
		mScene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);
		
		
		mScene.setOnSceneTouchListener(this);  // Para "escuchar" la pantalla.
		
		
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
				ResourceManager.getInstance().mTiledTextureRegion, mEngine.getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				Log.d("TIME", String.valueOf(pSecondsElapsed));
				// Calculate rotation offset based on time passed
				final float rotationOffset = pSecondsElapsed * 25;
				
				// Adjust this rectangle's rotation
				this.setRotation(this.getRotation() + rotationOffset);
				
				// Pass the seconds elapsed to our update thread
				super.onManagedUpdate(pSecondsElapsed);
			}
			
		};

		/* Length to play each frame before moving to the next */
		long frameDuration[] = { 50	, 100, 150 , 200, 250, 300 };

		/* We can define the indices of the animation to play between */
		int firstTileIndex = 0;
		int lastTileIndex = ResourceManager.getInstance().mTiledTextureRegion.getTileCount() - 1;

		/* Allow the animation to continuously loop? */
		boolean loopAnimation = true;

		/* Animate the sprite with the data as set defined above */
		animatedSprite.animate(frameDuration, firstTileIndex, lastTileIndex, loopAnimation);

		mScene.attachChild(animatedSprite);	

		
		// Añadimos un clon superápido :D   Es una variable global por el tema de r (cutre para probar)
		animatedSpriteClon = new AnimatedSprite(WIDTH * 0.5f + 400, HEIGHT * 0.5f,
				ResourceManager.getInstance().mTiledTextureRegion, mEngine.getVertexBufferObjectManager());
		long frameDurationClon[] = { 20	, 40, 60 , 80, 100, 120 };
		animatedSpriteClon.animate(frameDurationClon, firstTileIndex, lastTileIndex, loopAnimation);
		mScene.attachChild(animatedSpriteClon);		

		
		// Añadimos un clon que corre por la pantalla :D
		AnimatedSprite animatedSpriteCorre = new AnimatedSprite(WIDTH * 0.5f + 400, HEIGHT * 0.5f,
				ResourceManager.getInstance().mTiledTextureRegion, mEngine.getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				
				// Adjust our rectangles position
				if(this.getX() < (WIDTH)){
					// Increase the position by 5 pixels per update
					this.setPosition(this.getX() + 5f, this.getY());
				} else {
					// Reset the rectangles X position and slightly increase the Y position
					// If the rectangle exits camera view (width)
					this.setPosition(-20, this.getY() + (100));
				}
				
				// Reset to initial position if the rectangle exits camera view (height)
				if(this.getY() > HEIGHT){
					this.setPosition(0, 0);
				}
				
/*				// If our rectangle is colliding, set the color to green if it's
				// not already green
				if(this.collidesWith(rectangleOne) && this.getColor() != Color.GREEN){
					this.setColor(Color.GREEN);
					
				// If the shape is not colliding and not already red,
				// reset the rectangle to red
				} else if(this.getColor() != Color.RED){
					this.setColor(Color.RED);
				}*/
				
				// Pass the seconds elapsed to our update thread
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		animatedSpriteCorre.animate(frameDuration, firstTileIndex, lastTileIndex, loopAnimation);
		mScene.attachChild(animatedSpriteCorre);			
		
		

		// Añadimos algo de texto:
		final Text centerText = new Text(WIDTH * 0.5f, HEIGHT *0.3f, ResourceManager.getInstance().mFont,
				"Pruebas para las fuentes\nAqui la otra linea...    :D",
				new TextOptions(HorizontalAlign.CENTER), mEngine.getVertexBufferObjectManager());

		mScene.attachChild(centerText);


		// onPopulateSceneFinished(), similar to the resource and scene callback
		// methods, should be called once we are finished populating the scene.
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}


	// Liberamos la memoria:
	public void onUnloadResources () {
		ResourceManager.getInstance().unloadGameTextures();
		ResourceManager.getInstance().unloadFonts();
	}





	// Esto se ejecuta cuando se toca la pantalla:
	// El clon superápido se mueve a donde toquemos, mientras sigue corriendo claro.....  :D
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
		
		if(pSceneTouchEvent.isActionMove()){
			animatedSpriteClon.setPosition(x,y);
			animatedSpriteClon.setRotation(r);
			r = r-3;
			return true;
		}
		
		return false;
	}


// Esto finaliza el juego:
/*	// Some devices do not exit the game when the activity is destroyed.
	// This ensures that the game is closed.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}*/
}


