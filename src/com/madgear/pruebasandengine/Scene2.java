package com.madgear.pruebasandengine;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;

import android.util.Log;

public class Scene2 extends ManagedScene implements IOnSceneTouchListener {

	public final boolean hasLoadingScreen = false;
	
	AnimatedSprite animatedSpriteClon;
	int r = 0;
	
	/* Variable which will accumulate time passed to
	 * determine when to switch screens */
	float timeCounter = 0;
	
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadScene() {
		
		ResourceManager.getInstance().loadGameTextures(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadFonts(ResourceManager.getInstance().engine);
		
		getBackground().setColor(0.89804f, 0.0274f, 0.3784f);
	}

	@Override
	public void onShowScene() {		

		setOnSceneTouchListener(this);  // Para "escuchar" la pantalla.
		
		/* Create a new animated sprite in the center of the scene */
		AnimatedSprite animatedSprite = new AnimatedSprite(ResourceManager.getInstance().cameraWidth * 0.5f,
				ResourceManager.getInstance().cameraHeight * 0.5f,
				ResourceManager.getInstance().mTiledTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager()) {
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

		attachChild(animatedSprite);	

		
		// Añadimos un clon superápido :D   Es una variable global por el tema de r (cutre para probar)
		animatedSpriteClon = new AnimatedSprite(ResourceManager.getInstance().cameraWidth * 0.5f + 400, 
				ResourceManager.getInstance().cameraHeight * 0.5f,
				ResourceManager.getInstance().mTiledTextureRegion,
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		long frameDurationClon[] = { 20	, 40, 60 , 80, 100, 120 };
		animatedSpriteClon.animate(frameDurationClon, firstTileIndex, lastTileIndex, loopAnimation);
		attachChild(animatedSpriteClon);		

		
		// Añadimos un clon que corre por la pantalla :D
		AnimatedSprite animatedSpriteCorre = new AnimatedSprite(ResourceManager.getInstance().cameraWidth * 0.5f + 400,
				ResourceManager.getInstance().cameraHeight * 0.5f,
				ResourceManager.getInstance().mTiledTextureRegion, 
				ResourceManager.getInstance().engine.getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				
				// Adjust our rectangles position
				if(this.getX() < (ResourceManager.getInstance().cameraWidth)){
					// Increase the position by 5 pixels per update
					this.setPosition(this.getX() + 5f, this.getY());
				} else {
					// Reset the rectangles X position and slightly increase the Y position
					// If the rectangle exits camera view (width)
					this.setPosition(-20, this.getY() + (100));
				}
				
				// Reset to initial position if the rectangle exits camera view (height)
				if(this.getY() > ResourceManager.getInstance().cameraHeight){
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
		attachChild(animatedSpriteCorre);			
		
		

		// Añadimos algo de texto:
		final Text centerText = new Text(ResourceManager.getInstance().cameraWidth * 0.5f, 
				ResourceManager.getInstance().cameraHeight *0.3f, ResourceManager.getInstance().mFont,
				"Pruebas para las fuentes\nAqui la otra linea...    :D",
				new TextOptions(HorizontalAlign.CENTER), ResourceManager.getInstance().engine.getVertexBufferObjectManager());

		attachChild(centerText);

		
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
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

	// Si pasan más de 4 segundos, entonces cambiamos de escena :)
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (timeCounter >= 4) {
			timeCounter = 0;
			SceneManager.getInstance().showScene(new Scene1());
			
		}
		timeCounter  += pSecondsElapsed;
		
		// Pass the seconds elapsed to our update thread
		super.onManagedUpdate(pSecondsElapsed);
		
		
		}
	

}
