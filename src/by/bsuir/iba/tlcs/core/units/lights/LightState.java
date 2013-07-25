
package by.bsuir.iba.tlcs.core.units.lights;

/**
 * This class encapsulates state of object {@link by.bsuir.iba.tlcs.core.units.lights.Light} and provides constants that
 * describe different states of lights. Objects of this class will be created directly by lights.
 * @author Yahor Filipchyk
 */
public class LightState {
    
    /**
     * Constants that describe different state of lights.
     */
    public static int NONE = 0;
    public static final int RED = 1;
    public static final int AMBER = 2;
    public static int GREEN_TIMEOUT = 3;
    public static final int GREEN = 4;
    
    private int state;  // current state
    private int id; 
    
    /**
     * Constructor which creates new LightState object with appropriate state and identifier.
     * @param state Current state of the light.
     * @param id Identifier of the light which has called a constructor.
     */
    public LightState(int state, int id) {
        if (0 <= state && state <= 4) {
            this.state = state;
            this.id = id;
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Returns the state of light created this object.
     * @return State of the light created this object.
     */
    public int getState() {
        return state;
    }
    
    /**
     * Returns an identifier of the light created this object.
     * @return An identifier of the light created this object.
     */
    public int getID() {
        return id;
    }
}
