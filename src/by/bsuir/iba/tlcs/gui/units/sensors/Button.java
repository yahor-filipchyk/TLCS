
package by.bsuir.iba.tlcs.gui.units.sensors;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class extends an abstract class {@link by.bsuir.iba.tlcs.gui.units.sensors.Sensor} and represents sensor that placed
 * on the crosswalk and can be activated with mouse pressing and deactivated with mouse releasing.
 * @author Yahor Filipchyk
 */
public class Button extends Sensor {
    
    /**
     * Creates new Sensor with appropriate id and coordinates.
     * @see by.bsuir.iba.tlcs.gui.units.sensors.Sensor
     */
    public Button(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height);
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval(getX(), getY(), getWidth(), getHeight());
        if (isActive()) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.fillOval(getX() + 1, getY() + 1, getWidth() - 1, getHeight() - 1);
        g.setColor(Color.BLACK);
        g.drawString("" + getID(), getX() + getWidth() / 2 - 7, getY() + g.getFont().getSize() + 2);
    }
}
