
package by.bsuir.iba.tlcs.core.control;

import by.bsuir.iba.tlcs.core.configuration.ConfigFactory;
import by.bsuir.iba.tlcs.core.configuration.Configuration;
import java.io.File;
import java.util.ArrayList;

/**
 * The main core class. It creates the {@link by.bsuir.iba.tlcs.core.configuration.Configuration} objects
 * and then creates list of {@link by.bsuir.iba.tlcs.core.control.Crossroad} objects.
 * @author Yahor Filipchyk
 */
public class LightSystem {
    
    private ArrayList<Crossroad> crossroads;
    
    public LightSystem() {
        // getting path of the project
        String configPath = getConfigPath();
        // create configs for each intersection
        ArrayList<Configuration> configs = ConfigFactory.createConfigs(configPath);
        crossroads = new ArrayList<>(configs.size());
        // create crossroads with created above configurations
        for (Configuration cfg : configs) {
            crossroads.add(new Crossroad(cfg));
        }
    }
    
    /**
     * @return list of the {@link by.bsuir.iba.tlcs.core.control.Crossroad} objects of type {@link ArrayList}
     */
    public ArrayList<Crossroad> getCrossroads() {
        return crossroads;
    }
    
    /**
     * Returns the path where project is situated.
     * @return The path where project is situated.
     */
    public static String getConfigPath() {
        String path = System.getProperty("java.class.path");
        String pattern = "TLCS";
        String[] peaces = path.split(pattern);
        path = peaces[0] + pattern + File.separator;
        System.out.println(path);
        return path;
    }
}
