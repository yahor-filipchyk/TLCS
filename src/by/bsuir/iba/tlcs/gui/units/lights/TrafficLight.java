
package by.bsuir.iba.tlcs.gui.units.lights;

import by.bsuir.iba.tlcs.core.units.lights.LightState;
import by.bsuir.iba.tlcs.gui.intersection.StateObserver;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * This class implements an abstract class {@link by.bsuir.iba.tlcs.gui.units.lights.Light} and 
 * represents a road (traffic) light that has three colors: red, yellow (amber) and green.
 * @author Yahor Filipchyk
 */
public class TrafficLight extends Light {
    
    /**
     * Light colors.
     */
    public static final Color RED = Color.RED;
    public static final Color YELLOW = Color.YELLOW;
    public static final Color GREEN = Color.GREEN;
    
    private Point redCenter;    // center of red color
    private Point yellowCenter; // center of yellow color
    private Point greenCenter;  // center of green color
    private int prevState = LightState.RED; // previous light state
    private int state = LightState.RED;     // current light state
    
    public TrafficLight() {        
    }
    
    /**
     * @see by.bsuir.iba.tlcs.gui.units.lights.Light 
     */
    public TrafficLight(StateObserver observer, int x, int y, int width, int height, int id) {
        super(observer, x, y, width, height, id);
        redCenter = new Point(x + 2, y + 2);
        yellowCenter = new Point(x + 2, y + height / 3 + 2);
        greenCenter = new Point(x + 2, y + height / 3 * 2 + 2);
    }   
    
    /**
     * @see by.bsuir.iba.tlcs.gui.units.lights.Light#drawUp(java.awt.Graphics) 
     */
    @Override
    protected void drawUp(Graphics g) {
        drawVertical(g);
        g.setColor(Color.BLACK);
        g.drawString("" + getID(), getX() + 2, getY() - g.getFont().getSize() / 2);
    }
    
    /**
     * @see by.bsuir.iba.tlcs.gui.units.lights.Light#drawLeft(java.awt.Graphics) 
     */
    @Override
    protected void drawLeft(Graphics g) {
        drawHorizontal(g);
        int x = getX() - getWidth() / 2;
        int y = getY() + getHeight() / 2 - getWidth() / 2;
        g.setColor(Color.BLACK);
        g.drawString("" + getID(), Math.round((float)(x - g.getFont().getSize() * 1.5)), y + g.getFont().getSize());
    }
    
    /**
     * @see by.bsuir.iba.tlcs.gui.units.lights.Light#drawRight(java.awt.Graphics)  
     */
    @Override
    protected void drawRight(Graphics g) {
        drawHorizontal(g);
        int x = getX() - getWidth() / 2;
        int y = getY() + getHeight() / 2 - getWidth() / 2;
        g.setColor(Color.BLACK);
        g.drawString("" + getID(), x + getHeight() + 2, y + g.getFont().getSize());
    }
    
    /**
     * @see by.bsuir.iba.tlcs.gui.units.lights.Light#drawDown(java.awt.Graphics) 
     */
    @Override
    protected void drawDown(Graphics g) {
        drawVertical(g);
        g.setColor(Color.BLACK);
        g.drawString("" + getID(), getX() + 2, getY() + getHeight() + g.getFont().getSize());
    }
    
    /**
     * Draws the light when it is in vertical position.
     * @param g Graphics context.
     */
    private void drawVertical(Graphics g) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
        setRed(g);
        g.fillOval(redCenter.x, redCenter.y, width - 4, height / 3 - 4);
        setYellow(g);
        g.fillOval(yellowCenter.x, yellowCenter.y, width - 4, height / 3 - 4);
        setGreen(g);
        g.fillOval(greenCenter.x, greenCenter.y, width - 4, height / 3 - 4);
    }
    
    /**
     * Draws the light when it is in horizontal position.
     * @param g Graphics context.
     */
    private void drawHorizontal(Graphics g) {
        int x = getX() - getWidth() / 2;
        int y = getY() + getHeight() / 2 - getWidth() / 2;
        int width = getHeight();
        int height = getWidth();
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
        setRed(g);
        g.fillOval(redCenter.x, redCenter.y, width / 3 - 4, height - 4);
        setYellow(g);
        g.fillOval(yellowCenter.x, yellowCenter.y, width / 3 - 4, height - 4);
        setGreen(g);
        g.fillOval(greenCenter.x, greenCenter.y, width / 3 - 4, height - 4);
    }
    
    /**
     * Sets red or grey color.
     * @param g Graphics context.
     */
    private void setRed(Graphics g) {
        if ((state == LightState.RED) || (state == LightState.AMBER && prevState == LightState.RED)) {
            g.setColor(RED);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * Sets yellow or grey color.
     * @param g Graphics context.
     */
    private void setYellow(Graphics g) {
        if (state == LightState.AMBER) {
            g.setColor(YELLOW);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * Sets green or grey color.
     * @param g Graphics context.
     */
    private void setGreen(Graphics g) {
        if (state == LightState.GREEN || state == LightState.GREEN_TIMEOUT) {
            g.setColor(GREEN);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
    }
    
    /**
     * @see by.bsuir.iba.tlcs.gui.units.lights.Light#updateCenters() 
     */
    @Override
    protected void updateCenters() {
        int x = getX();
        int y = getY();
        int height = getHeight();        
        int width = getWidth();
        switch (getRotation()) {
            case ROTATION_UP:
                redCenter = new Point(x + 2, y + 2);
                yellowCenter = new Point(x + 2, y + height / 3 + 2);
                greenCenter = new Point(x + 2, y + height / 3 * 2 + 2);
                break;
            case ROTATION_LEFT:
                redCenter = new Point(x - width / 2 + 2, y + height / 2 - width / 2 + 2);
                yellowCenter = new Point(x - width / 2 + height / 3 + 2, y + height / 2 - width / 2 + 2);
                greenCenter = new Point(x - width / 2 + height / 3 * 2 + 2, y + height / 2 - width / 2 + 2);
                break;
            case ROTATION_RIGHT:
                redCenter = new Point(x - width / 2 + height / 3 * 2 + 2, y + height / 2 - width / 2 + 2);
                yellowCenter = new Point(x - width / 2 + height / 3 + 2, y + height / 2 - width / 2 + 2);
                greenCenter = new Point(x - width / 2 + 2, y + height / 2 - width / 2 + 2);
                break;
            case ROTATION_DOWN:
                redCenter = new Point(x + 2, y + height / 3 * 2 + 2);
                yellowCenter = new Point(x + 2, y + height / 3 + 2);
                greenCenter = new Point(x + 2, y + 2);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @see by.bsuir.iba.tlcs.core.units.lights.LightObserver#lightEvent(by.bsuir.iba.tlcs.core.units.lights.LightState) 
     */
    @Override
    public void lightEvent(LightState state) {
        //System.out.println(state.getID() + " gui_light " + state.getState());
        this.prevState = this.state;
        this.state = state.getState();  
        updateObserver();
    }
}
