/*

 */
package by.bsuir.iba.tlcs.core.units.lights;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An abstract class that represents crossroad unit such as light. Light works in separate thread and can work
 * in autonomic mode, when central control of crossroad is unable to interrupt working of the light. When
 * light isn't in autonomic mode (it's suspended), the crossroad control can affect the light and change its state.
 * This class implements the {@link by.bsuir.iba.tlcs.core.units.lights.ObservableLight} interface that means light can be listened by
 * any other object of class, which one implements an interface {@link by.bsuir.iba.tlcs.core.units.lights.LightObserver}.
 * Also the class implements interface {@link Runnable} to be able to run light in separate thread.
 * @author Yahor Filipchyk
 */
public abstract class Light implements ObservableLight, Runnable {
    
    private Thread thread;                      // a thread in wich light is working
    private boolean suspended;                  // suspended (not suspended) state of light
    private int id;            
    private ArrayList<LightObserver> observers; // list of observesrs
    private int state = LightState.RED;         // current state of the light
    private boolean alive;                      // alive or dead state of light
    
    /**
     * Constructor, that creates new light with some id in separate thread and starts the thread.
     * @param id identifier of light
     */
    public Light(int id) {
        this.id = id;
        observers = new ArrayList<>();
        thread = new Thread(this, "Light_" + id); 
        thread.setDaemon(true);     // setting thread type as daemon
        suspended = true;           // set initial state of thread as suspended
        alive = true;
        thread.start(); // starting thread
    }
    
    /**
     * Implementation of abstract method of the {@link Runnable} interface.
     * This method describes a behavior of thread. The current implementation allows the light
     * while its state set to alive wait when thread state set to suspended and do some action (change its state)
     * when light state set to not suspended.
     * @see by.bsuir.iba.tlcs.core.units.lights.Light#changeState() 
     */
    @Override
    public void run() {
        while (isAlive()) { 
            changeState();
            synchronized (this) {
                while (isSuspended()) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Light.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    /**
     * Returns true if light state set to alive, else returns false.
     * @return true if light state set to alive, else returns false
     */
    public boolean isAlive() {
        return alive;
    }
    
    /**
     * Returns true if light state set to suspended, else returns false.
     * @return true if light state set to suspended, else returns false
     */
    protected boolean isSuspended() {
        return suspended;
    }    
    
    /**
     * Sets the light state to suspended.
     */
    synchronized protected final void suspend() {
        suspended = true;
    }
    
    /**
     * Sets the light state to not suspended.
     */
    synchronized protected final void resume() {
        suspended = false;
        notify();
    }
    
    /**
     * Sets the light state to not alive.
     */
    public void stop() {
        alive = false;
    }
    
    /**
     * @see by.bsuir.iba.tlcs.core.units.lights.ObservableLight
     * @return the light identifier.
     */
    @Override
    public int getID() {
        return id;
    }
    
    /**
     * Returns a list of {@link by.bsuir.iba.tlcs.core.units.lights.LightObserver} objects that were subscribed to the light.
     * @return List of {@link by.bsuir.iba.tlcs.core.units.lights.LightObserver} objects.
     */
    protected ArrayList<LightObserver> getObservers() {
        return observers;
    }    
    
    /**
     * Returns current thread in which one the light is working.
     * @return Current thread.
     */
    protected Thread getThread() {
        return thread;
    }
    
    /**
     * An abstract method that describes how the light can change its state. 
     * Called from {@link by.bsuir.iba.tlcs.core.units.lights.Light#run()} method when light is not suspended.
     */
    protected abstract void changeState();
    
    /**
     * Changes the light state to red if it is possible (if light is not in autonomic mode).
     * Returns true if change was successful, otherwise returns false.
     * @param amberTimeout The time how long the light must be in amber state.
     * @return true if light state was changed to red.
     */
    public abstract boolean makeRed(int amberTimeout);  
    
    /**
     * Changes the light state to green if it is possible (if light is not in autonomic mode).
     * Returns true if change was successful, otherwise returns false.
     * @param amberTimeout The time how long the light must be in amber state.
     * @param minGreenTime The minimum time how long the light must be in green state.
     * @return true if light state was changed to green.
     */
    public abstract boolean makeGreen(int amberTimeout, int minGreenTime);
    
    /**
     * Switches the light off. The state will be set to NONE.
     * @param flashingTime The time in what interval the light will blink.
     * @return true if light was stopped, else returns false.
     */
    public abstract boolean stopLight(int flashingTime);
    
    /**
     * Switches the light on. Now the light state can be set to red or green.
     * @return true if light was resumed, else returns false.
     */
    public abstract boolean resumeLight();
}
