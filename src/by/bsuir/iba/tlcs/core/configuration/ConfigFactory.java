
package by.bsuir.iba.tlcs.core.configuration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * This class provides factory for creating list of configurations in concrete directory
 * @author Yahor Filipchyk
 */
public class ConfigFactory {
    
    /**
     * This method creates the {@link by.bsuir.iba.tlcs.core.configuration.SystemConfiguration} objects that 
     * implement {@link by.bsuir.iba.tlcs.core.configuration.Configuration} interfaces and return list of 
     * the {@link by.bsuir.iba.tlcs.core.configuration.Configuration} interfaces.
     * @param configPath Path where configuration files are situated.
     * @return list of the {@link by.bsuir.iba.tlcs.core.configuration.Configuration} interfaces of type {@link ArrayList}.
     */
    public static ArrayList createConfigs(String configPath) {
        ArrayList<Configuration> configs = new ArrayList<>();
        File catalog = new File(configPath);
        // given path is directory? If is not, show error message and return empty list of configs
        if (!catalog.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Cannot to load configuration files. " +
                    "Path is must be directory, not the single file");
            return configs;
        }
        // getting list of files in directory with file filter
        // files must have extension .cfg
        File[] files = catalog.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".cfg")) {
                    return true;
                }
                return false;
            }
        });
        // for each configuration file create new SystemCofiguration object and add it to list
        for (File file : files) {
            configs.add(new SystemConfiguration(file.getAbsolutePath()));
        }
        return configs;
    }
}
