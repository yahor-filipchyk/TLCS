
package by.bsuir.iba.tlcs.core.units;

import by.bsuir.iba.tlcs.core.configuration.Configuration;
import by.bsuir.iba.tlcs.core.control.Crossroad;
import java.util.HashMap;

/**
 * An abstract class that represents parent class of crossroad elements factories.
 * @author Yahor Filipchyk
 */
public abstract class ElementFactory {
    
    /**
     * This method create map of crossroad elements.
     * @param config Configuration object for certain crossroad. It's contains characteristics of elements to be created.
     * @param crossroad A crossroad elements are belong to. In the system each crossroad has to listen to state of each its elements
     * @return Hash map of crossroad elements.
     */
    public abstract HashMap createElements(Configuration config, Crossroad crossroad);
}
