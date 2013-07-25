
package by.bsuir.iba.tlcs.gui.intersection;

/**
 * Interface that allows to update state of the object that implements this interface. In our case it will be
 * a content pane that should be repainted when method update is called from outside (form the light).
 * @author Yahor Filipchyk
 */
public interface StateObserver {
    
    /**
     * Updates state of object.
     */
    public void update();
}
