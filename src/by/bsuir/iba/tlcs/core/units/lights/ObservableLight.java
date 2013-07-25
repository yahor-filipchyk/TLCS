
package by.bsuir.iba.tlcs.core.units.lights;

/**
 * This interface describes a behavior of object which state can be listened to from outside.
 * @author Yahor Filipchyk
 */
public interface ObservableLight {
    
    /**
     * Adds new listener of the object state.
     * @param lo The objects that implements {@link by.bsuir.iba.tlcs.core.units.lights.LightObserver} interface.
     */
    public void addListener(LightObserver lo);
    
    /**
     * Removes listener of the object state.by.bsuir.iba.tlcs.core.units.lights.LightObserver} interface and which
     * one has to be removed.
     */
    public void removeListener(LightObserver lo);
    
    /**
     * Returns an identifier of the object that implements this interface.
     * @return An identifier of the object.
     */
    public int getID();
    
    /**
     * Returns type of the light.
     * @return Type of the light represented as String value.
     */
    public String getType();
    
    /**
     * Notifies all listeners about the state changing.
     */
    public void updateAll();   
}
