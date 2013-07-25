
package by.bsuir.iba.tlcs.core.units;

import by.bsuir.iba.tlcs.core.units.lights.LightsFactory;
import by.bsuir.iba.tlcs.core.units.sensors.SensorsFactory;

/**
 * This class provides factories for creating of elements of crossroad (Lights, Sensors).
 * 
 * @author Yahor Filipchyk
 */
public class AbstractFactory {
    
    private enum Element {
        LIGHT, 
        SENSOR
    }
    
    /**
     * 
     * @param elem String that determines the type of factory to be returned.
     * @return factory of type {@link by.bsuir.iba.tlcs.core.units.ElementFactory}.
     */
    public static ElementFactory getElementFactory(String elem) {
        Element el = Element.valueOf(elem.toUpperCase());
        switch (el) {
            case LIGHT : 
                return new LightsFactory();
            case SENSOR :
                return new SensorsFactory();
            default :
                throw new EnumConstantNotPresentException(Element.class, elem.toUpperCase());
        }
    }
}
