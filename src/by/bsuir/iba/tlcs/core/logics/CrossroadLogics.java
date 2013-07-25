
package by.bsuir.iba.tlcs.core.logics;

import by.bsuir.iba.tlcs.core.control.Crossroad;
import by.bsuir.iba.tlcs.core.units.lights.LightObserver;
import by.bsuir.iba.tlcs.core.units.lights.LightState;
import by.bsuir.iba.tlcs.core.units.sensors.SensorObserver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author deer
 */
public abstract class CrossroadLogics implements SensorObserver, LightObserver {

    protected Crossroad crossroad;
    protected HashMap<Integer, LogicalUnit> units;
    protected int totalTraffic;

    /**
     *
     * @param crossroad The corresponding crossroad
     */
    public CrossroadLogics(Crossroad crossroad) {
        this.crossroad = crossroad;
        this.units = new HashMap<>();
        for (int i : crossroad.getLights().keySet()) {
            this.units.put(i, new LogicalUnit());
        }
    }

    /**
     *
     * @param index Some sensor-light unit index
     * @return An abstract value of sensor-light unit's relative priority
     */
    public abstract double getPriority(int index);

    /**
     *
     * @see CrossroadLogics
     */
    public void setCrossroad(Crossroad crossroad) {
        this.crossroad = crossroad;
    }

    /**
     *
     * @see Crossroad#setEmergency(int, boolean)
     */
    public void setEmergency(int index, boolean emergency) {
        System.out.println("Index " + index + " emergency " + emergency);
        this.units.get(index).setEmergency(emergency);
        calculatePriorities();
    }

    /**
     * Recalculate all units' priorities and submit new red and green lights
     * sets
     */
    public void calculatePriorities() {
        this.totalTraffic = 0;
        for (LogicalUnit unit : this.units.values()) {
            this.totalTraffic += unit.getNumberOfActivations();
        }
        Set<Integer> greenLights = new HashSet<>();
        for (int index : this.units.keySet()) {
            double priority = this.getPriority(index);
            double conflictingPriority = 0;
            for (int conflictingIndex : this.crossroad.getConfig().getConflicts(index)) {
                conflictingPriority += this.getPriority(conflictingIndex);
            }
            if (priority > conflictingPriority) {
                greenLights.add(index);
            }
        }
        Set<Integer> redLights = new HashSet<>();
        for (int index : this.units.keySet()) {
            if (this.units.get(index).getLightState() == LightState.GREEN
                    && !greenLights.contains(index)) {
                redLights.add(index);
            }
        }
        this.applySubmission(redLights, greenLights);
    }

    /**
     *
     * @param redLights A set of red lights to be submitted
     * @param greenLights A set of green lights to be submitted
     */
    protected void applySubmission(Set<Integer> redLights, Set<Integer> greenLights) {
        this.submitRedLights(redLights);
        this.submitGreenLights(greenLights);
    }

    /**
     *
     * @param greenLights A set of green lights to be submitted
     */
    public void submitGreenLights(Set<Integer> greenLights) {
        this.crossroad.setGreenLights(greenLights);
    }

    /**
     *
     * @param redLights A set of red lights to be submitted
     */
    public void submitRedLights(Set<Integer> redLights) {
        this.crossroad.setRedLights(redLights);
    }
}
