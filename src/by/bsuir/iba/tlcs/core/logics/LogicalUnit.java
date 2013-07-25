/*
 *
 */
package by.bsuir.iba.tlcs.core.logics;

import by.bsuir.iba.tlcs.core.units.lights.LightState;

/**
 *
 * @author deer
 */
public class LogicalUnit {

    private boolean sensorActive;
    private int lightState;
    private long sensorActiveTime;
    private long lightStateTime;
    private int numberOfActivations;
    private boolean emergency;

    /**
     * Initializes sensor and light states and times
     */
    public LogicalUnit() {
        this.sensorActive = false;
        this.lightState = LightState.RED;
        this.sensorActiveTime = System.nanoTime();
        this.lightStateTime = System.nanoTime();
        this.numberOfActivations = 0;
        this.emergency = false;
    }

    /**
     *
     * @return Logical sensor's state
     */
    public boolean getSensorActive() {
        return sensorActive;
    }

    /**
     *
     * @return Logical light's state
     */
    public int getLightState() {
        return lightState;
    }

    /**
     *
     * @return Logical sensor's state's time
     */
    public long getSensorActiveTime() {
        return sensorActiveTime;
    }

    /**
     *
     * @return Logical light's state's time
     */
    public long getLightStateTime() {
        return lightStateTime;
    }

    /**
     *
     * @return Times the sensor was set to active state
     */
    public int getNumberOfActivations() {
        return numberOfActivations;
    }

    /**
     *
     * @return Unit's emergency state
     */
    public boolean getEmergency() {
        return emergency;
    }

    /**
     *
     * @param sensorActive A new sensor's state
     */
    public void setSensorActive(boolean sensorActive) {
        this.sensorActive = sensorActive;
        this.sensorActiveTime = System.nanoTime() - 10000000;
        if (sensorActive) {
            this.numberOfActivations++;
        }
    }

    /**
     *
     * @param lightState A new light's state
     */
    public void setLightState(int lightState) {
        this.lightState = lightState;
        this.lightStateTime = System.nanoTime();
    }

    /**
     *
     * @param emergency A new emergency state
     */
    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }
}
