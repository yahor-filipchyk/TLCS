
package by.bsuir.iba.tlcs.core.configuration;

import java.util.Set;

/**
 *
 * @author deer
 */
public interface Configuration {

    /**
     *
     * @param lightIndex The index of a traffic light.
     * @return An array of indices of all conflicting traffic lights.
     */
    public int[] getConflicts(int lightIndex);

    /**
     *
     * @param lightIndex The index of a traffic light.
     * @return A minimal time of green light state.
     */
    public int getGreenTimeout(int lightIndex);

    /**
     *
     * @param lightIndex The index of a traffic light.
     * @return The time of amber light state.
     */
    public int getAmberTimeout(int lightIndex);

    /**
     *
     * @return A set of all valid traffic light indices.
     */
    public Set<Integer> getLightsIndices();

    /**
     *
     * @param filename The file with configuration to load.
     */
    public void reloadFromFile(String filename);

    /**
     *
     * @return The path of the background image for graphical frontend
     */
    public String getImagePath();

    /**
     *
     * @return The name of the default crossroad's algorithm
     */
    public String getAlgorithm();
}
