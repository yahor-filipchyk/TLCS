
package by.bsuir.iba.tlcs.core.units.lights;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class extends abstract class {@link by.bsuir.iba.tlcs.core.units.lights.Light} and presents light which has two colors: red and green.
 * For this light an amber time equals zero. Object of this class works in separate thread.
 * @author Yahor Filipchyk
 */
public class CrosswalkLight extends Light {
    
    private final String TYPE = "CROSSWALK";    // defines the string name of light type
    private int state = LightState.RED;         // current state of light
    private int prevState;                      // previous state of light
    private int minGreenTime;   
    private boolean stoped = false;
    
    /**
     * Creates new {@link by.bsuir.iba.tlcs.core.units.lights.CrosswalkLight} and starts it in new thread.
     * @param id An integer value of the light id
     */
    public CrosswalkLight(int id) {
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
     * @see by.bsuir.iba.tlcs.core.units.lights.ObservableLight#getType()
     */
    @Override
    public String getType() {
        return TYPE;
    }
    
    /**
     * 
     * @see by.bsuir.iba.tlcs.core.units.lights.Light#changeState()
     */
    @Override
    protected synchronized void changeState() {   
        if (!stoped) {
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
                this.state = LightState.RED;
                updateAll();
            }
            // suspend thread (for waiting commands from crossroad)
            suspend();
        // if light was stoped
        } else {
            this.state = LightState.NONE;
            updateAll();
            suspend();
        }
    }

    /**
     * 
     * @param amberTimeout Here doesn't make sense, because crosswalk light hasn't amber state.
     * @see by.bsuir.iba.tlcs.core.units.lights.Light#makeRed(int) 
     */
    @Override
    public boolean makeRed(int amberTimeout) {
        // if light is in waiting state (suspended)
        if (isSuspended()) {
            // save current state
            prevState = state;
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
            // set min green time
            this.minGreenTime = minGreenTime;
            // let light do work
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
        if (isSuspended()) {                                    
            this.stoped = true;
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
        if (stoped) {
            this.stoped = false;
            return true;
        } else {
            return false;
        }
    }
    
}
