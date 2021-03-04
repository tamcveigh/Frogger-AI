package com.mygdx.kittener.desktop;

import AIinterfaces.AlgorithmName;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.kittener.game.MainGame;

/**
 * Launches the game of "Kittener", which is based upon the classic arcade game "Frogger".
 * @author Brooke Kiser and Tyler McVeigh
 * @version 24 September 2020
 */
public class SUNADesktopLauncher {
	/** The pixel height and height of a square block, representing a game tile. */
	private static final int BLOCK_SIZE = 32;

	/** The number of blocks our game will be wide. */
	private static final int WIDTH = 16;

	/** The number of blocks our game will be tall. */
	private static final int HEIGHT = 12;

	/**
	 * Driver for our application.
	 * @param args Arguments for our application (if any).
	 */
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.addIcon("core/assets/cat_front.png", Files.FileType.Internal);
		config.useGL30					= false;
		config.forceExit 				= true;
		config.foregroundFPS 			= 60;
		config.height 					= BLOCK_SIZE * HEIGHT;
		config.width 					= BLOCK_SIZE * WIDTH;
		config.initialBackgroundColor 	= Color.BLACK;
		config.resizable 				= true;
		config.title 					= "SUNA Kittener";
		config.x = 0;
		config.y = 0;

		ApplicationListener game = new MainGame(config.width, config.height, AlgorithmName.NEAT);
		Application app = new LwjglApplication(game, config);
	}
}
