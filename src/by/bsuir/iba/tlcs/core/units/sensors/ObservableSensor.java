
package by.bsuir.iba.tlcs.core.units.sensors;

/**
 * This interface describes a behavior of object which state can be listened to from outside.
 * @author Yahor Filipchyk
 */
public interface ObservableSensor {
    
    /**
     * Adds new listener of the sensor state.
     * @param so The object that implements {@link by.bsuir.iba.tlcs.core.units.sensors.SensorObserver} interface.
     */
    public void addListener(SensorObserver so);
    
    /**
     * Removes a listener of the object state.
     * @param so The object that implements {@link by.bsuir.iba.tlcs.core.units.sensors.SensorObserver} interface and which one
     * has to be removed.
     */
    public void removeListener(SensorObserver so);
    
    /**
     * Notifies all listeners about the object state changing.
     */
    public void updateAll();
    
}
