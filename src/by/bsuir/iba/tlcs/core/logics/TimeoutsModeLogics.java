/*
 * 
 */
package by.bsuir.iba.tlcs.core.logics;

import by.bsuir.iba.tlcs.core.control.Crossroad;
import by.bsuir.iba.tlcs.core.units.lights.LightState;
import by.bsuir.iba.tlcs.core.units.sensors.SensorState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author deer
 */
public class TimeoutsModeLogics extends CrossroadLogics {

    /**
     *
     * @see CrossroadLogics
     */
    public TimeoutsModeLogics(Crossroad crossroad) {
        super(crossroad);
    }

    @Override
    public void sensorEvent(SensorState state) {
        this.units.get(state.getID()).setSensorActive(state.isActive());
        this.calculatePriorities();
    }

    @Override
    public void lightEvent(LightState state) {
        this.units.get(state.getID()).setLightState(state.getState());
        this.calculatePriorities();
    }

    @Override
    public double getPriority(int index) {
        // (LS == R ? 1 : 0) * LT * TP + 500 * ME + 1000 * LL
        LogicalUnit unit = this.units.get(index);
        int lightStateComponent = (unit.getLightState() == LightState.RED) ? 1 : 0;
        double lightTimeComponent = (System.nanoTime() - unit.getLightStateTime()) / 1000000;
        double trafficComponent = 1;//(this.totalTraffic == 0) ? 0 : unit.getNumberOfActivations() / this.totalTraffic;
        int emergencyComponent = unit.getEmergency() ? 1 : 0;
        int lockedComponent = (unit.getLightState() == LightState.AMBER
                || unit.getLightState() == LightState.GREEN_TIMEOUT) ? 1 : 0;
        return lightStateComponent * lightTimeComponent * trafficComponent
                + 500000 * emergencyComponent + 1000000 * lockedComponent;
    }

    @Override
    protected void applySubmission(Set<Integer> redLights, Set<Integer> greenLights) {
        if (redLights.isEmpty() && greenLights.isEmpty()) {
            boolean allLightsAreRed = true;
            for (int index : this.units.keySet()) {
                if (this.units.get(index).getLightState() != LightState.RED) {
                    allLightsAreRed = false;
                    break;
                }
            }
            if (allLightsAreRed) {
                greenLights = this.minimalGreenSet();
            }
        }
        this.submitRedLights(redLights);
        this.submitGreenLights(greenLights);
    }

    /*
     * Select a random non-conflicting green lights set
     */
    private Set<Integer> minimalGreenSet() {
        Set<Integer> greenLights = new HashSet<>();
        ArrayList<Integer> keys = new ArrayList(this.units.keySet());
        Collections.shuffle(keys);
        for (int index : keys) {
            boolean canBeGreen = true;
            for (int conflict : this.crossroad.getConfig().getConflicts(index)) {
                if (greenLights.contains(conflict)) {
                    canBeGreen = false;
                    break;
                }
            }
            if (canBeGreen) {
                greenLights.add(index);
            }
        }
        return greenLights;
    }
}
