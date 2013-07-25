
package by.bsuir.iba.tlcs.gui.intersection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;

/**
 *
 * @author Yahor
 */
public class RightPanel {
    
    private int width;
    private int height;
    private int x;
    private int y;
    private Container container;
    private Color background;
    
    public RightPanel(Container container, int x, int y, int width, int height) {
        this.container = container;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        background = Color.WHITE;
        container.setBackground(background);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Color getBackground() {
        return background;
    }
    
    public void setBackground(Color color) {
        this.background = color;
    }
    
    public void setCoordinates(int x, int y, int width, int heigth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = heigth;
    }
    
    public void draw(Graphics g) {
        //Graphics g = container.getGraphics();
        g.setColor(background);
        g.fillRect(x, y + 80, width, height - 80);
    }
    
    public void add(Component comp, int x, int y, int width, int height) {
        comp.setBounds(this.x + x, this.y + y, width, height);
        container.add(comp);
    }
}
