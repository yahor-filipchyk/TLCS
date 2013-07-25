
package by.bsuir.iba.tlcs.core.units.sensors;

/**
 * This class encapsulates state of the Sensor object. 
 * Objects of this class will be created directly by sensors.
 * @author Yahor Filipchyk
 */
public class SensorState {
    
    private boolean active; // current state
    private int id;         // sensor identifier
    
    /**
     * Constructor that creates new SensorState object with appropriate state and identifier.
     * @param id Identifier of the sensor which has called a constructor.
     * @param active Current state of the sensor.
     */
    public SensorState(int id, boolean active) {
        this.id = id;
        this.active = active;
    }
    
    /**
     * Returns the sensor state.
     * @return true if sensor is active, otherwise returns false.
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Returns an identifier of the sensor created this object.
     * @return An identifier of the sensor created this object.
     */
    public int getID() {
        return id;
    }
}
