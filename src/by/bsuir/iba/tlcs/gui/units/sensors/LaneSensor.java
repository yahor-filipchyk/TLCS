
package by.bsuir.iba.tlcs.gui.units.sensors;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class extends an abstract class {@link by.bsuir.iba.tlcs.gui.units.sensors.Sensor} and represents sensor that placed
 * on the road lane and can be activated with mouse pressing and deactivated with mouse releasing.
 * @author Yahor Filipchyk
 */
public class LaneSensor extends Sensor {
    
    private boolean horizontal = true;  // sensor position
    
    /**
     * Creates new Sensor with appropriate id and coordinates.
     * @see by.bsuir.iba.tlcs.gui.units.sensors.Sensor
     */
    public LaneSensor(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height);
    }
    
    @Override
    public void rotate() {
        horizontal = !horizontal;
    }
    
    @Override
    public void draw(Graphics g) {
        if (horizontal) {
            drawHorizontal(g);
        } else {
            drawVertical(g);
        }
    }
        
    private void drawVertical(Graphics g) {
        g.setColor(Color.BLACK);
        int vertX = getX() + getWidth() / 2 - getHeight() / 2;
        int vertY = getY() + getHeight() / 2 - getWidth() / 2;
        int vertWidth = getHeight();
        int vertHeight = getWidth();
        g.drawRect(vertX, vertY, vertWidth, vertHeight);
        if (isActive()) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.fillRect(vertX + 1, vertY + 1, vertWidth - 2, vertHeight - 2);
        g.setColor(Color.BLACK);
        g.drawString("" + getID(), getX() + getWidth() / 2 - 4, getY() + g.getFont().getSize() + 2);
    }
    
    private void drawHorizontal(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        if (isActive()) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.fillRect(getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 2);
        g.setColor(Color.BLACK);
        g.drawString("" + getID(), getX() + getWidth() / 2 - 10, getY() + g.getFont().getSize() + 2);
    }
}
