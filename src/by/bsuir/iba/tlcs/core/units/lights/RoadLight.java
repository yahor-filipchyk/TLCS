
package by.bsuir.iba.tlcs.core.units.lights;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class extends abstract class {@link by.bsuir.iba.tlcs.core.units.lights.Light} and presents light which has three colors: red, yellow
 * (amber) and green. Object of this class works in separate thread.
 * @author Yahor Filipchyk
 */
public class RoadLight extends Light {
   
    private final String TYPE = "ROAD"; // defines the string name of light type
    private int state = LightState.RED; // current state of light
    private int prevState;              // previous state of light
    private int amberTime;
    private int minGreenTime;
    private int flashingTime;          
    private boolean stopped = false;
    
    /**
     * Creates new {@link by.bsuir.iba.tlcs.core.units.lights.RoadLight} and starts it in new thread.
     * @param id An integer value of the light id
     */
    public RoadLight(int id) {
        super(id);
    } 
    
    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.ObservableLight#addListener(by.bsuir.iba.tlcs.core.units.lights.LightObserver) 
     */
    @Override
    public void addListener(LightObserver lo) {
        getObservers().add(lo);
    }

    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.ObservableLight#removeListener(by.bsuir.iba.tlcs.core.units.lights.LightObserver) 
     */
    @Override
    public void removeListener(LightObserver lo) {
        getObservers().remove(lo);
    }

    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.ObservableLight#updateAll()
     */
    @Override
    public void updateAll() {
        for (LightObserver observer : getObservers()) {
            observer.lightEvent(new LightState(state, getID()));
        }
    }
    
    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.Light#changeState()
     */
    @Override
    protected synchronized void changeState() {
        if (!stopped) {
            // if minimum green time is setted (if light changes its state from red to green) and if state is changed not
            // from green to green and not from red to red
            if (((prevState == LightState.GREEN && minGreenTime == 0) || (prevState == LightState.RED && minGreenTime > 0))) {
                // setting light state to LightState.AMBER and updating all listeners
                this.state = LightState.AMBER;
                updateAll();
                try {
                    // autonomic working amberTime
                    Thread.sleep(amberTime * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RoadLight.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // if minimum green time is setted (if light changes its state from red to green)
            if (minGreenTime > 0) {
                // setting light state to LightState.GREEN_TIMEOUT and updating all listeners
                this.state = LightState.GREEN_TIMEOUT;
                updateAll();
                try {
                    // autonomic working minGreenTime
                    Thread.sleep(minGreenTime * 1000);
                    // changing state to LightState.GREEN and updating all listeners
                    this.state = LightState.GREEN;
                    updateAll();
                } catch (InterruptedException ex) {
                    Logger.getLogger(RoadLight.class.getName()).log(Level.SEVERE, null, ex);
                }
            // if minimum green time is not setted (if light changes its state from green to red)
            } else {
                // changing state to LightState.RED and updating all listeners
                this.state = LightState.RED;
                updateAll();
            }
            // suspend thread (for waiting commands from crossroad)
            suspend();
        // if light was stoped make light blink with yellow color
        } else {
            // changing state to LightState.AMBER and updating all listeners
            this.state = LightState.AMBER;
            updateAll();
            try {
                // autonomic working flashingTime
                getThread().sleep(flashingTime * 1000);
                // changing state to LightState.NONE and updating all listeners
                this.state = LightState.NONE;
                updateAll();
                // autonomic working flashingTime
                getThread().sleep(flashingTime * 1000);
            } catch (InterruptedException ex) {
                System.out.println("Sleeping stopped");
            }
        }
    }
    
    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.Light#makeRed(int) 
     */
    @Override
    public boolean makeRed(int amberTimeout) {
        // if light is in waiting state (suspended)
        if (isSuspended()) {
            // save current state
            prevState = state;
            // set amber time
            this.amberTime = amberTimeout;
            this.minGreenTime = 0;
            // let light do work
            resume();
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.Light#makeGreen(int, int)
     */
    @Override
    public boolean makeGreen(int amberTimeout, int minGreenTime) {
        // if light is in waiting state (suspended)
        if (isSuspended()) { 
            // save current state
            prevState = state;
            // set amber time
            this.amberTime = amberTimeout;
            // set min green time
            this.minGreenTime = minGreenTime;
            resume();
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.Light#stopLight(int)
     */
    @Override
    public boolean stopLight(int flashingTime) {
        if (isSuspended() && !stopped) {     
            this.stopped = true;
            this.flashingTime = flashingTime;
            resume();
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.Light#resumeLight() 
     */
    @Override
    public boolean resumeLight() {
        if (stopped) {
            this.stopped = false;
            if (!isSuspended()) {
                getThread().interrupt();
                suspend();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.ObservableLight#getType()
     */
    @Override
    public String getType() {
        return TYPE;
    }
}
