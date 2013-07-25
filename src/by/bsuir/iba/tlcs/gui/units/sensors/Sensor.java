
package by.bsuir.iba.tlcs.gui.units.sensors;

import by.bsuir.iba.tlcs.core.units.sensors.ObservableSensor;
import by.bsuir.iba.tlcs.core.units.sensors.SensorObserver;
import by.bsuir.iba.tlcs.core.units.sensors.SensorState;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * An abstract class that represents a physical sensor, that can send events to its
 * observers when its state is changed. In this case this is some clickable area on the screen
 * that can be activated with mouse clicks.
 * @author Yahor Filipchyk
 */
public abstract class Sensor implements ObservableSensor, Serializable {

    // sensor coordinates
    private int x;
    private int y;
    private int width;
    private int height;    
    
    private ArrayList<SensorObserver> observers;    // sensor observers
    private int id;                                 // sensor identifier
    private boolean active = false;                 // current sensor state
    
    /**
     * Creates new Sensor with appropriate identifier and coordinates.
     * @param id Identifier.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param width Sensor width.
     * @param height Sensor height.
     */
    public Sensor(int id, int x, int y, int width, int height) {
        this.id = id;
        this.setCoordinates(x, y, width, height);
        this.observers = new ArrayList<>();
    }
    
    /**
     * Sets sensor coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param width Sensor width.
     * @param height Sensor height.
     */
    public void setCoordinates(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;        
    }
    
    /**
     * Returns sensor x coordinate.
     * @return x coordinate of up-left corner.
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns sensor y coordinate.
     * @return y coordinate of up-left corner.
     */
    public int getY() {
        return y;
    }
    
    /**
     * Returns sensor width.
     * @return Sensor width.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Returns sensor height.
     * @return Sensor height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Draws the sensor on the screen.
     * @param g Graphics context.
     */
    public abstract void draw(Graphics g);
       
    /**
     * Sets sensor to appropriate state.
     * @param active The state the sensor to be set to.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Returns the sensor state.
     * @return true if sensor is active, otherwise returns false.
     */
    protected boolean isActive() {
        return active;
    }
    
    /**
     * Returns the sensor identifier.
     * @return The sensor identifier.
     */
    public int getID() {
        return id;
    }
    
    /**
     * Rotates sensor.
     */
    public void rotate() {        
    }    
    
    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.sensors.ObservableSensor#addListener(by.bsuir.iba.tlcs.core.units.sensors.SensorObserver) 
     */
    @Override
    public void addListener(SensorObserver so) {
        observers.add(so);
    }

    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.sensors.ObservableSensor#removeListener(by.bsuir.iba.tlcs.core.units.sensors.SensorObserver) 
     */
    @Override
    public void removeListener(SensorObserver so) {
        for (SensorObserver obserber : observers) {
            if (obserber.equals(so)) {
                observers.remove(obserber);
            }
        }
    }

    /**
     * @see by.bsuir.iba.tlcs.core.units.sensors.ObservableSensor#updateAll() 
     */
    @Override
    public void updateAll() {
        for (SensorObserver observer : observers) {
            observer.sensorEvent(new SensorState(id, active));
        }
    }    
}
