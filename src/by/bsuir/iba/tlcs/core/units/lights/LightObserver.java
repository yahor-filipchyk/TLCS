
package by.bsuir.iba.tlcs.core.units.lights;

/**
 * The interface to observe state of lights. Lights will be observable.
 * @author Yahor Filipchyk
 */
public interface LightObserver {
    
    /**
     * The method will be executed in an object of class which implements this interface and was subscribed as light observer.
     * @param state An object which encapsulates the light state.
     */
    public void lightEvent(LightState state);
}
