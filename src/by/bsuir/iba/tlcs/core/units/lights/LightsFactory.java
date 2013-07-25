
package by.bsuir.iba.tlcs.core.units.lights;

import by.bsuir.iba.tlcs.core.configuration.Configuration;
import by.bsuir.iba.tlcs.core.control.Crossroad;
import by.bsuir.iba.tlcs.core.units.ElementFactory;
import java.util.HashMap;
import java.util.Set;

/**
 * This class extends an abstract class {@link by.bsuir.iba.tlcs.core.units.ElementFactory} and represents
 * a factory that creates map of {@link by.bsuir.iba.tlcs.core.units.lights.Light} objects.
 * @author Yahor Filipchyk
 * @see by.bsuir.iba.tlcs.core.units.ElementFactory
 */
public class LightsFactory extends ElementFactory {

    /**
     * 
     * @param config Configuration object for certain crossroad. It's contains characteristics of elements to be created.
     * @param crossroad A crossroad lights are belong to. In the system each crossroad has to listen to state of each its elements
     * @return Hash map of crossroad elements.
     * @see by.bsuir.iba.tlcs.core.units.ElementFactory
     */
    @Override
    public HashMap createElements(Configuration config, Crossroad crossroad) {
        Set<Integer> indices = config.getLightsIndices();
        HashMap<Integer, Light> lights = new HashMap<>(indices.size());
        for (Integer ind : indices) {
            Light light;
            if (config.getAmberTimeout(ind) == 0) {
                light = new CrosswalkLight(ind);
            } else {
                light = new RoadLight(ind);
            }
            lights.put(ind, light);
            light.addListener(crossroad);
        }
        return lights;
    }    
}
