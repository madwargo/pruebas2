package com.madgear.pruebasandengine;


import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;



public class ResourceManager {

	MainActivity activity;
	Engine engine;
	
	// ResourceManager Singleton instance
	private static ResourceManager INSTANCE;
	
	/* The variables listed should be kept public, allowing us easy access
	   to them when creating new Sprites, Text objects and to play sound files */
	
	
	public BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	public ITiledTextureRegion mTiledTextureRegion;
	//public ITextureRegion mSpriteTextureRegion;

	public static String fontName = "go3v2.ttf";
	
	public Sound mSound;

	public Font	mFont;

	
	// Inicializa el manejador
	public static void setup(MainActivity pActivity, Engine pEngine, Context pContext, 
			float pCameraWidth, float pCameraHeight, float pCameraScaleX, float pCameraScaleY){
		
		getInstance().activity = pActivity;
		getInstance().engine = pEngine; 
		
/*		
		getInstance().context = pContext;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;
		getInstance().cameraScaleFactorX = pCameraScaleX;
		getInstance().cameraScaleFactorY = pCameraScaleY;*/
	}
	
	
	
	
	
	
	
	
	ResourceManager(){
		// The constructor is of no use to us
	}

	public synchronized static ResourceManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ResourceManager();
		}
		return INSTANCE;
	}

	/* Each scene within a game should have a loadTextures method as well
	 * as an accompanying unloadTextures method. This way, we can display
	 * a loading image during scene swapping, unload the first scene's textures
	 * then load the next scenes textures.
	 */
	public synchronized void loadGameTextures(Engine pEngine, Context pContext){
		// Set our game assets folder in "assets/gfx/game/"
		//BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		
		/* Create the bitmap texture atlas for the sprite's texture
		region */
		BuildableBitmapTextureAtlas mBitmapTextureAtlas =
				new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1548, 332, TextureOptions.BILINEAR);
		
		/* Create the TiledTextureRegion object, passing in the usual
		parameters, as well as the number of rows and columns in our sprite sheet
		for the final two parameters */
		// 6 = nº de imágenes que tiene la animación :D
		mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, pContext, "sprite1.png", 6, 1);
		
		/* Build the bitmap texture atlas */
		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
		/* Load the bitmap texture atlas into the device's gpu memory
		*/
		mBitmapTextureAtlas.load();
		
		
	}
	
	/* All textures should have a method call for unloading once
	 * they're no longer needed; ie. a level transition. */
	public synchronized void unloadGameTextures(){
		// call unload to remove the corresponding texture atlas from memory
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mTiledTextureRegion.getTexture();
		mBitmapTextureAtlas.unload();
		
		// ... Continue to unload all textures related to the 'Game' scene
		
		// Once all textures have been unloaded, attempt to invoke the Garbage Collector
		System.gc();
	}
	
	
	
	// Se crea un método de load/unload para cada escena:
	
/*	 Similar to the loadGameTextures(...) method, except this method will be
	 * used to load a different scene's textures
	 
	public synchronized void loadMenuTextures(Engine pEngine, Context pContext){
		// Set our menu assets folder in "assets/gfx/menu/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(pEngine.getTextureManager() ,800 , 480);
		
		mMenuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "menu_background.png");
		
		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
	}
	
	// Once again, this method is similar to the 'Game' scene's for unloading
	public synchronized void unloadMenuTextures(){
		// call unload to remove the corresponding texture atlas from memory
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mMenuBackgroundTextureRegion.getTexture();
		mBitmapTextureAtlas.unload();
		
		// ... Continue to unload all textures related to the 'Game' scene
		
		// Once all textures have been unloaded, attempt to invoke the Garbage Collector
		System.gc();
	}*/
	
	
	
	
	/* As with textures, we can create methods to load sound/music objects
	 * for different scene's within our games.
	 */
	public synchronized void loadSounds(Engine pEngine, Context pContext){
		// Set the SoundFactory's base path
		SoundFactory.setAssetBasePath("sounds/");
		 try {
			 // Create mSound object via SoundFactory class
			 mSound	= SoundFactory.createSoundFromAsset(pEngine.getSoundManager(), pContext, "sound.mp3");			 
		 } catch (final IOException e) {
             Log.v("Sounds Load","Exception:" + e.getMessage());
		 }
	}	
	
	/* In some cases, we may only load one set of sounds throughout
	 * our entire game's life-cycle. If that's the case, we may not
	 * need to include an unloadSounds() method. Of course, this all
	 * depends on how much variance we have in terms of sound
	 */
	public synchronized void unloadSounds(){
		// we call the release() method on sounds to remove them from memory
		if(!mSound.isReleased())mSound.release();
	}
	
	
	/* Lastly, we've got the loadFonts method which, once again,
	 * tends to only need to be loaded once as Font's are generally 
	 * used across an entire game, from menu to shop to game-play.
	 */
	public synchronized void loadFonts(Engine pEngine){
		FontFactory.setAssetBasePath("fonts/");
		
		// Create mFont object via FontFactory class
		mFont = FontFactory.createFromAsset(pEngine.getFontManager(), pEngine.getTextureManager(), 256, 256, 
				activity.getAssets(), fontName, 32f, true, org.andengine.util.adt.color.Color.WHITE_ABGR_PACKED_INT);

		mFont.load();
	}
	
	/* If an unloadFonts() method is necessary, we can provide one
	 */
	public synchronized void unloadFonts(){
		// Similar to textures, we can call unload() to destroy font resources
		mFont.unload();
	}
}