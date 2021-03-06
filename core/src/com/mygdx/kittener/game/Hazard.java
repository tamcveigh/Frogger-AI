package com.mygdx.kittener.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class which represents a hazard that could appear in the game. The hazards textures and the
 * speed it travels.
 * @author Brandon Townsend
 * @version 18 January 2020
 */
public class Hazard extends Rectangle {
    /** The texture this hazard should be currently displayed as. */
    private Texture texture;

    /** The speed at which this hazard should be currently traveling. */
    private float speed;

    /** The start position on the x,y plane and the start speed. */
    private int startXPos;
    private int startYPos;
    private float startSpeed;

    /**
     * Constructs a new hazard with the supplied information.
     * @param width The width (pixels) of the hazard.
     * @param height The height (pixels) of the hazard.
     * @param xPos The x-coordinate position (pixels) of the hazard.
     * @param yPos The y-coordinate position (pixels) of the hazard.
     * @param texture The texture to be applied to the hazard.
     * @param speed The speed at which the hazard should initially travel.
     */
    public Hazard(int width, int height, int xPos, int yPos, Texture texture, float speed) {
        super(xPos, yPos, width, height);
        this.texture = texture;
        this.speed = speed;
        this.startXPos = xPos;
        this.startYPos = yPos;
        this.startSpeed = speed;
    }

    /**
     * Returns the texture currently applied to this hazard.
     * @return The texture currently applied to this hazard.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Applies the supplied texture to this hazard.
     * @param texture The newly supplied texture.
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Returns the speed at which this hazard is currently traveling.
     * @return The speed at which this hazard is currently traveling.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the new traveling speed for this hazard.
     * @param speed The new speed to travel at.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Resets this hazard back to its starting position and speed.
     */
    public void reset() {
        setX(startXPos);
        setY(startYPos);
        setSpeed(startSpeed);
    }
}
