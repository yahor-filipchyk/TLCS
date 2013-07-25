/*
 * 
 */
package by.bsuir.iba.tlcs.core.logics;

import by.bsuir.iba.tlcs.core.control.Crossroad;
import by.bsuir.iba.tlcs.core.units.lights.LightState;
import by.bsuir.iba.tlcs.core.units.sensors.SensorState;

/**
 *
 * @author deer
 */
public class IntelligentModeLogics extends CrossroadLogics {

    /**
     *
     * @see CrossroadLogics
     */
    public IntelligentModeLogics(Crossroad crossroad) {
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
    }

    @Override
    public double getPriority(int index) {
        // (SS == on ? 1 : 0) * ST * TP + 500 * ME + 1000 * LL
        LogicalUnit unit = this.units.get(index);
        int sensorStateComponent = unit.getSensorActive() ? 1 : 0;
        double sensorTimeComponent = (System.nanoTime() - unit.getSensorActiveTime()) / 100000;
        double trafficComponent = (this.totalTraffic == 0) ? 0 : unit.getNumberOfActivations() / this.totalTraffic;
        if (trafficComponent < 0.001) {
            trafficComponent = 0.001;
        }
        int emergencyComponent = unit.getEmergency() ? 1 : 0;
        int lockedComponent = (unit.getLightState() == LightState.AMBER
                || unit.getLightState() == LightState.GREEN_TIMEOUT) ? 1 : 0;
        return sensorStateComponent * sensorTimeComponent * trafficComponent +
                500000 * emergencyComponent + 1000000 * lockedComponent;
    }
}
