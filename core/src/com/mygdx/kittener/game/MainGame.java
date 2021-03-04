package com.mygdx.kittener.game;

import AIinterfaces.AlgorithmName;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.tools.javac.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * Class which controls the screen we are viewing in our game and holds all sprites, fonts, and
 * the application width and height.
 * @author Brandon Townsend
 * @version 22nd November, 2020
 * @additions Brooke Kiser and Tyler McVeigh
 */
public class MainGame extends com.badlogic.gdx.Game {
    /** The width of the application window. */
    private final int width;

    /** The height of the application window. */
    private final int height;

    /** Sprite batch used to draw 2D shapes. */
    public SpriteBatch batch;

    /** Text for displaying score and any other information needed. */
    public BitmapFont font;

    /** The screen for the actual Kittener game. */
    public Screen gameScreen;

    /** The screen for the main menu of the game. */
    public Screen mainMenuScreen;

    /** The type of AI algorithm being used */
    AlgorithmName aiName;

    /** CSV file to hold statistics of the run*/
    public static File STAT_LOG;

    public static File NETWORK_LOG;

    /**
     * Passing the application window size to the game.
     * @param width The width of the window.
     * @param height The height of the window.
     */
    public MainGame(int width, int height, AlgorithmName aiName) {
        this.width  = width;
        this.height = height;
        this.aiName = aiName;

        try{
            Instant timestamp = Instant.now();
            MainGame.STAT_LOG = new File("logs",aiName + "-" + timestamp.getEpochSecond() + ".csv" );
            MainGame.STAT_LOG.createNewFile();
            FileWriter statSetup = new FileWriter(MainGame.STAT_LOG);
            statSetup.write("Generation,Average,Maximum,Color");
            statSetup.close();
            MainGame.NETWORK_LOG = new File("logs", aiName + "-Network-" + timestamp.getEpochSecond() + ".csv" );
            MainGame.STAT_LOG.createNewFile();
            FileWriter networkSetup = new FileWriter(MainGame.NETWORK_LOG);
            networkSetup.write("Generation,NetworkID,1,2,3,4,5,6,7,8,9,10");
            networkSetup.close();
        } catch (IOException e) {
            e.printStackTrace();
            //System.err.println("ERROR: Unable to create statistics log file");

        }

    }

    /**
     * Returns the width of the application window.
     * @return The width of the application window.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the application window.
     * @return The height of the application window.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Method called once the application is created. Assets are usually loaded here.
     */
    @Override
    public void create() {
        // Creation of the sprite batch.
        batch = new SpriteBatch();

        // Creation of the font.
        font = new BitmapFont();

        // Setting of screens.
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        setScreen(mainMenuScreen);
    }

    /**
     * Method called by the game loop from the application every time rendering should be
     * performed. Renders the screen we set in create().
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * On desktop this is called before the dispose() when exiting the application.
     * NOTE: Good place to save the game state.
     */
    @Override
    public void pause() {
        super.pause();
    }

    /**
     * Called when the application is destroyed. Preceded by a call to pause(). Disposes all
     * objects which are not otherwise handled by the Java garbage collector.
     */
    @Override
    public void dispose() {
        gameScreen.dispose();
        batch.dispose();
        font.dispose();
    }
}
