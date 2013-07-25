/**
 * Class loads configuration from file and provides the information about lights and
 * detectors
 */
package by.bsuir.iba.tlcs.core.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author deer
 */
public class SystemConfiguration implements Configuration {

    private HashMap<Integer, ConfigurationElement> config;
    private String imagePath;
    private String algorithm;

    /**
     *
     * @param filename A file from which to load the configuration
     */
    public SystemConfiguration(String filename) {
        this.config = new HashMap<>();
        this.loadFromFile(filename);
    }

    @Override
    public int[] getConflicts(int lightIndex) {
        return this.config.get(lightIndex).getConflicts();
    }

    @Override
    public int getGreenTimeout(int lightIndex) {
        return this.config.get(lightIndex).getGreenTimeout();
    }

    @Override
    public int getAmberTimeout(int lightIndex) {
        return this.config.get(lightIndex).getAmberTimeout();
    }

    /*
     * Load the configuration from file according to its syntax
     */
    private void loadFromFile(String filename) {
        BufferedReader reader = null;
        try {
            /* file format is as follows
             * lightIndex: minGreenTime[, amberTime]; conflictingIndex[, ...]
             */
            File configFile = new File(filename);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            try {
                while (reader.ready()) {
                    String line = reader.readLine();
                    line = line.trim();
                    // specials start with a bang
                    if (line.startsWith("!")) {
                        line = line.substring(1);
                        String[] splittedLine = line.split(":");
                        if ("image".equalsIgnoreCase(splittedLine[0].trim())) {
                            imagePath = splittedLine[1].trim();
                            System.out.println("image = " + imagePath);
                        } else if ("algorithm".equalsIgnoreCase(splittedLine[0].trim())) {
                            algorithm = splittedLine[1].trim();
                            System.out.println("algorithm = " + algorithm);
                        }
                    } else {
                        // comments start with a hash symbol
                        if (!line.startsWith("#") && !"".equals(line)) {
                            String[] splittedLine = line.split(":");
                            Integer index = Integer.parseInt(splittedLine[0].trim());
                            splittedLine = splittedLine[1].split(";");
                            String[] timeouts = splittedLine[0].split(",");
                            Integer greenTimeout = Integer.parseInt(timeouts[0].trim());
                            Integer amberTimeout = new Integer(0);
                            if (timeouts.length > 1) {
                                amberTimeout = Integer.parseInt(timeouts[1].trim());
                            }
                            splittedLine = splittedLine[1].split(",");
                            int[] conflicts = new int[splittedLine.length];
                            for (int i = 0; i < splittedLine.length; i++) {
                                conflicts[i] = Integer.parseInt(splittedLine[i].trim());
                            }
                            this.config.put(index, new ConfigurationElement(conflicts, greenTimeout, amberTimeout));
//                            System.out.println("index = " + index
//                                    + "\nconflicts = " + conflicts
//                                    + "\ngreenTimeout = " + greenTimeout
//                                    + "\namberTimeout = " + amberTimeout);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(SystemConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SystemConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(SystemConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void reloadFromFile(String filename) {
        this.loadFromFile(filename);
    }

    @Override
    public Set<Integer> getLightsIndices() {
        return this.config.keySet();
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    private static class ConfigurationElement {

        private int[] conflicts;
        private int greenTimeout;
        private int amberTimeout;

        /**
         *
         * @param conflicts An array of indices of conflicting lights
         * @param greenTimeout A minimal green time of the light
         * @param amberTimeout An amber timeout of the light
         */
        public ConfigurationElement(int[] conflicts, int greenTimeout, int amberTimeout) {
            this.conflicts = conflicts;
            this.greenTimeout = greenTimeout;
            this.amberTimeout = amberTimeout;
        }

        /**
         *
         * @return An array of indices of conflicting lights
         */
        public int[] getConflicts() {
            return conflicts;
        }

        /**
         *
         * @param conflicts An array of indices of conflicting lights
         */
        public void setConflicts(int[] conflicts) {
            this.conflicts = conflicts;
        }

        /**
         *
         * @return A minimal green time of the light
         */
        public int getGreenTimeout() {
            return greenTimeout;
        }

        /**
         *
         * @param greenTimeout A new minimal green time of the light
         */
        public void setGreenTimeout(int greenTimeout) {
            this.greenTimeout = greenTimeout;
        }

        /**
         *
         * @return An amber timeout of the light
         */
        public int getAmberTimeout() {
            return amberTimeout;
        }

        /**
         *
         * @param amberTimeout A new amber timeout of the light
         */
        public void setAmberTimeout(int amberTimeout) {
            this.amberTimeout = amberTimeout;
        }
    }
}
