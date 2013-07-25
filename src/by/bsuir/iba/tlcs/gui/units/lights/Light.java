
package by.bsuir.iba.tlcs.gui.units.lights;

import by.bsuir.iba.tlcs.core.units.lights.LightObserver;
import by.bsuir.iba.tlcs.core.units.lights.LightState;
import by.bsuir.iba.tlcs.gui.intersection.StateObserver;
import java.awt.Graphics;
import java.io.Serializable;

/**
 * An abstract class thar represents light that listens some logical light and changes its state
 * when the event from the logical light is arrives.
 * @author Yahor Filipchyk
 */
public abstract class Light implements Serializable, LightObserver {
    
    private int x, y, width, height;    // coordinates
    private int id;                     // identifier    
    private int rotation;   // current rotation position
    private int state = LightState.RED; // current state
    private StateObserver contentPane;  // the observer that is notified when state of the light is changed
    
    /**
     * Constants that describe the light rotation position.
     */
    public static final int ROTATION_LEFT = -1;
    public static final int ROTATION_RIGHT = 1;
    public static final int ROTATION_UP = 0;
    public static final int ROTATION_DOWN = 2;
    
    public Light() {
    }
    
    /**
     * Creates new Light.
     * @param observer An observer that is notified when state of the light is changed.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param width The light width.
     * @param heigth The light height.
     * @param id Identifier of the light.
     */
    public Light(StateObserver observer, int x, int y, int width, int heigth, int id) {
        this.contentPane = observer;
        this.id = id;
        this.rotation = ROTATION_UP;
        setCoordinates(x, y, width, heigth);
    }
    
    /**
     * Draws the light on the screen.
     * @param g Graphics context.
     */
    public void draw(Graphics g) { 
        updateCenters();
        switch (this.rotation) {
            case ROTATION_UP:
                drawUp(g);
                break;
            case ROTATION_LEFT:
                drawLeft(g);
                break;
            case ROTATION_RIGHT:
                drawRight(g);
                break;
            case ROTATION_DOWN:
                drawDown(g);
                break;
            default:
                break;
        }
    }   
    
    /**
     * Draws light when it is in ROTATION_LEFT position.
     * @param g Graphics context.
     */
    protected abstract void drawLeft(Graphics g);
    
    /**
     * Draws light when it is in ROTATION_RIGHT position.
     * @param g Graphics context.
     */
    protected abstract void drawRight(Graphics g);
    
    /**
     * Draws light when it is in ROTATION_UP position.
     * @param g Graphics context.
     */
    protected abstract void drawUp(Graphics g);
    
    /**
     * Draws light when it is in ROTATION_DOWN position.
     * @param g Graphics context.
     */
    protected abstract void drawDown(Graphics g);
    
    /**
     * Rotates the light to left side.
     */
    public void rotateLeft() {
        switch (rotation) {
            case ROTATION_UP:
                this.rotation = ROTATION_LEFT;
                break;
            case ROTATION_LEFT:
                this.rotation = ROTATION_DOWN;
                break;
            case ROTATION_RIGHT:
                this.rotation = ROTATION_UP;
                break;
            case ROTATION_DOWN:
                this.rotation = ROTATION_RIGHT;
                break;
            default:
                break;
        }
    }
    
    /**
     * Rotates the light to right side.
     */
    public void rotateRight() {
        switch (rotation) {
            case ROTATION_UP:
                this.rotation = ROTATION_RIGHT;
                break;
            case ROTATION_LEFT:
                this.rotation = ROTATION_UP;
                break;
            case ROTATION_RIGHT:
                this.rotation = ROTATION_DOWN;
                break;
            case ROTATION_DOWN:
                this.rotation = ROTATION_LEFT;
                break;
            default:
                break;
        }
    }
    
    /**
     * Returns current light rotation position.
     * @return Rotation position.
     */
    public int getRotation() {
        return rotation;
    }
    
    /**
     * Sets light coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param width Light width.
     * @param height Light height.
     */
    public final void setCoordinates(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        updateCenters();
    }
    
    /**
     * Updates centers of the light colors according to its rotation position.
     */
    protected abstract void updateCenters();
    
    /**
     * Returns light x coordinate.
     * @return x coordinate of up-left corner
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns light y coordinate.
     * @return y coordinate of up-left corner.
     */
    public int getY() {
        return y;
    }
    
    /**
     * Returns light width.
     * @return Light width.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Returns light height.
     * @return Light height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Returns the light identifier.
     * @return The light identifier.
     */
    public int getID() {
        return id;
    }
    
    /**
     * Sets the light identifier.
     * @param id The identifier to be set.
     */
    public void setID(int id) {
        this.id = id;
    }
    
    /**
     * Returns the light state.
     * @return The state of the light.
     */
    public int getState() {
        return state;
    }
    
    /**
     * Sets the light state.
     * @param state The state to be set. An integer constant from class {@link by.bsuir.iba.tlcs.core.units.lights.LightState}
     */
    public void setState(int state) {
        if (0 <= state && state <= 2) {
            this.state = state;
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Notifies the content pane about the light state changing.
     */
    protected void updateObserver() {
        this.contentPane.update();
    }
}
