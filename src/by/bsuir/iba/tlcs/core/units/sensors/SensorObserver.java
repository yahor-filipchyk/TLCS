
package by.bsuir.iba.tlcs.core.units.sensors;


/**
 *
 * @author deer
 */
public interface SensorObserver {

    /**
     *
     * @param state A new sensor's state object.
     */
    public void sensorEvent(SensorState state);
}