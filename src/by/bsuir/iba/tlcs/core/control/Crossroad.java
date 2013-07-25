package by.bsuir.iba.tlcs.core.control;

import by.bsuir.iba.tlcs.core.configuration.Configuration;
import by.bsuir.iba.tlcs.core.logics.CrossroadLogics;
import by.bsuir.iba.tlcs.core.logics.IntelligentModeLogics;
import by.bsuir.iba.tlcs.core.logics.TimeoutsModeLogics;
import by.bsuir.iba.tlcs.core.units.AbstractFactory;
import by.bsuir.iba.tlcs.core.units.ElementFactory;
import by.bsuir.iba.tlcs.core.units.lights.Light;
import by.bsuir.iba.tlcs.core.units.lights.LightObserver;
import by.bsuir.iba.tlcs.core.units.lights.LightState;
import by.bsuir.iba.tlcs.core.units.lights.ObservableLight;
import by.bsuir.iba.tlcs.core.units.sensors.SensorObserver;
import by.bsuir.iba.tlcs.core.units.sensors.SensorState;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author deer
 */
public class Crossroad implements SensorObserver, LightObserver {

    private CrossroadLogics algorithm;
    private Configuration config;
    private HashMap<Integer, Light> lights;
    private String imagePath;
    private String[] algorithms = {"Intelligent", "Timeouts"};

    /**
     *
     * @param config The crossroad's configuration object
     */
    public Crossroad(Configuration config) {
        ElementFactory lightsFactory = AbstractFactory.getElementFactory("light");
        this.lights = lightsFactory.createElements(config, this);
        this.imagePath = config.getImagePath();
        this.algorithm = this.loadAlgorithm(config.getAlgorithm());
        this.config = config;
    }

    /**
     *
     * @return A map of all the crossroad's lights
     */
    public HashMap<Integer, ObservableLight> getLights() {
        HashMap<Integer, ObservableLight> observableLigts = new HashMap<>(lights.size());
        Set<Integer> indices = lights.keySet();
        for (Integer ind : indices) {
            observableLigts.put(ind, lights.get(ind));
        }
        return observableLigts;
    }

    /**
     *
     * @return An array of all available algorithms
     */
    public String[] getAlgorithms() {
        return algorithms;
    }

    /**
     *
     * @return The path of the background image for graphical frontend
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     *
     * @param algorithm A new throughput algorithm object
     */
    public void setAlgorithm(CrossroadLogics algorithm) {
        this.algorithm = algorithm;
    }

    /**
     *
     * @param algorithm A new throughput algorithm name
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = this.loadAlgorithm(algorithm);
    }

    /**
     *
     * @return The current crossroad's config object
     */
    public Configuration getConfig() {
        return config;
    }

    @Override
    public void sensorEvent(SensorState state) {
        System.out.println(state.getID() + " " + state.isActive());
        this.algorithm.sensorEvent(state);
    }

    @Override
    public void lightEvent(LightState state) {
        System.out.println(state.getID() + " " + state.getState());
        if (this.algorithm != null) {
            this.algorithm.lightEvent(state);
        }
    }

    /**
     *
     * @param index The index of the lane where an emergency vehicle passing is
     * declared
     * @param emergency True to turn emergency on, False to turn emergency off
     */
    public void setEmergency(int index, boolean emergency) {
        this.algorithm.setEmergency(index, emergency);
    }

    /**
     *
     * @param greenLights A set of lights to be set to green
     */
    public void setGreenLights(Set<Integer> greenLights) {
        for (int i : greenLights) {
            this.lights.get(i).makeGreen(this.config.getAmberTimeout(i), this.config.getGreenTimeout(i));
        }
    }

    /**
     *
     * @param redLights A set of lights to be set to red
     */
    public void setRedLights(Set<Integer> redLights) {
        for (int i : redLights) {
            this.lights.get(i).makeRed(this.config.getAmberTimeout(i));
        }
    }

    /**
     * Stop the crossroad: set all light inactive
     */
    public void stopCrossroad() {
        int i = 0;
        Set<Integer> indices = lights.keySet();
        while (i != lights.size()) {
            for (Integer ind : indices) {
                if (lights.get(ind).stopLight(1)) {
                    i++;
                    break;
                }
            }
        }
    }

    /**
     * Stop the crossroad: set all light active
     */
    public void resumeCrossroad() {
        int i = 0;
        Set<Integer> indices = lights.keySet();
        while (i != lights.size()) {
            for (Integer ind : indices) {
                if (lights.get(ind).resumeLight()) {
                    i++;
                    break;
                }
            }
        }
    }

    /*
     * Load an algorithm by name
     */
    private CrossroadLogics loadAlgorithm(String algorithm) {
        CrossroadLogics logics;
        if ("intelligent".equalsIgnoreCase(algorithm)) {
            logics = new IntelligentModeLogics(this);
        } else if ("timeouts".equalsIgnoreCase(algorithm)) {
            logics = new TimeoutsModeLogics(this);
        } else {
            logics = new IntelligentModeLogics(this);
        }
        return logics;
    }
}
