package by.bsuir.iba.tlcs.gui.intersection;

import by.bsuir.iba.tlcs.core.control.Crossroad;
import by.bsuir.iba.tlcs.core.units.lights.ObservableLight;
import by.bsuir.iba.tlcs.gui.units.lights.CrosswalkLight;
import by.bsuir.iba.tlcs.gui.units.lights.Light;
import by.bsuir.iba.tlcs.gui.units.lights.TrafficLight;
import by.bsuir.iba.tlcs.gui.units.sensors.Button;
import by.bsuir.iba.tlcs.gui.units.sensors.LaneSensor;
import by.bsuir.iba.tlcs.gui.units.sensors.Sensor;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 * A panel on which crossroad is represented.
 * @author Yahor Filipchyk
 */
public class Intersection implements StateObserver {

    private String id;         // string name of crossroad
    private Crossroad crossroad;    // crossroad to be represented
    private JPanel contentPane;   
    private Image background;       // background image
    private ArrayList<Light> lights;    // list of shown lights
    private HashMap<Integer, ObservableLight> coreLights;   // observable lights from crossroad
    private int x, y;   // variables to store mouse coordinates
    private Light chosenLight;  // the light chosen with mouse clicking
    private Light activeLight;  // the light that we can drag
    private RightPanel panel;   // panel with control buttons
    // control buttons
    private JButton prevLight;
    private JButton nextLight;
    private JButton rotateLeft;
    private JButton rotateRight;
    private JButton prevSensor;
    private JButton nextSensor;
    private JButton rotate;
    private JButton stopLights;
    private JButton startLights;
    // another control items
    private JToggleButton setEmergency;
    private JTextField lightNum;
    private JToggleButton enableBtn;
    private JComboBox algorithms;
    
    private int imageWidth;
    private int imageHeight;
    private boolean enabled = true; // is panel enabled for aditing
    private ArrayList<Sensor> sensors;  // list of shown sensors
    private Sensor chosenSensor;
    private Sensor activeSensor;
    // constants for shown units sizes
    private static final int LIGHT_WIDTH = 18;
    private static final int LIGHT_HEIGHT = 54;
    private static final int SENSOR_WIDTH = 28;
    private static final int SENSOR_HEIGHT = 16;
    private static final int BUTTON_WIDTH = 16;
    
    public Intersection() {        
    }

    /**
     * Creates new panel built on appropriate configuration loaded from Crossroad object
     * and with appropriate id name.
     * @param cross The crossroad built on configuration file.
     * @param id The intersection identifier.
     */
    public Intersection(Crossroad cross, String id) {
        this.crossroad = cross;
        this.id = id;
        coreLights = crossroad.getLights();
        if (coreLights != null) {
            init();
        } else {
            JOptionPane.showMessageDialog(contentPane, "The System Configuration hasn't created lights for this crossroad");
        }
    }

    // initialising fields
    private void init() {
        contentPane = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);
            }
        };
        contentPane.setLayout(null);
        // loding background image
        loadBackground();
        imageWidth = background.getWidth(contentPane);
        imageHeight = background.getHeight(contentPane);
        // initialasing rigth panel
        panel = new RightPanel(contentPane, imageWidth, 0, 200, imageHeight);
        contentPane.setDoubleBuffered(true);
        //adding listeners for mouse events;
        PaneMouseListener mouseListener = new PaneMouseListener();
        contentPane.addMouseListener(mouseListener);
        contentPane.addMouseMotionListener(mouseListener);

        generateLightsAndSensors();
        //crossroad.setSensors(observableSensors);
        //placedLights = new ArrayList<>(lights.size());

        // adding buttons
        prevLight = new JButton("<-Previous");
        panel.add(prevLight, 15, 20, 80, 25);
        prevLight.addActionListener(new PrevLightBtnListener());
        nextLight = new JButton("Next->");
        panel.add(nextLight, 105, 20, 80, 25);
        nextLight.addActionListener(new NextLightBtnListener());
        rotateLeft = new JButton("←Turn");
        panel.add(rotateLeft, 15, 150, 80, 25);
        rotateLeft.addActionListener(new TurnLeftBtnListener());
        rotateRight = new JButton("Turn→");
        panel.add(rotateRight, 105, 150, 80, 25);
        rotateRight.addActionListener(new TurnRightBtnListener());
        prevSensor = new JButton("<-Previous");
        panel.add(prevSensor, 15, 200, 80, 25);
        prevSensor.addActionListener(new PrevSensorBtnListener());
        nextSensor = new JButton("Next->");
        panel.add(nextSensor, 105, 200, 80, 25);
        nextSensor.addActionListener(new NextSensorBtnListener());
        rotate = new JButton("Turn");
        panel.add(rotate, panel.getWidth() / 2 - 40, 280, 80, 25);
        rotate.addActionListener(new RotateBtnListener());
        enableBtn = new JToggleButton("Disable editing");
        enableBtn.setSelected(true);
        panel.add(enableBtn, panel.getWidth() / 2 - 60, 320, 120, 25);
        enableBtn.addActionListener(new EnableBtnListener());
        startLights = new JButton("Start");
        panel.add(startLights, panel.getWidth() / 2 - 50, 360, 100, 25);
        startLights.addActionListener(new StartBtnListener());
        /**
         * Button is disabled, because it's needed to edit the function it has to do.
         */
        startLights.setEnabled(false);
        stopLights = new JButton("Stop");
        panel.add(stopLights, panel.getWidth() / 2 - 50, 400, 100, 25);
        stopLights.addActionListener(new StopBtnListener());
        /**
         * Button is disabled, because it's needed to edit the function it has to do.
         */
        stopLights.setEnabled(false);
        setEmergency = new JToggleButton("Set emergency:");
        panel.add(setEmergency, panel.getWidth() / 2 - 85, 440, 130, 25);
        setEmergency.addActionListener(new EmergencyBtnListener());
        lightNum = new JTextField(4);
        panel.add(lightNum, panel.getWidth() / 2 + 50, 440, 30, 25);
        //String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
        String algsNames[] = crossroad.getAlgorithms();
        algorithms = new JComboBox(algsNames);
        algorithms.setSelectedIndex(0);
        panel.add(algorithms, panel.getWidth() / 2 - 80, 480, 160, 25);
        algorithms.addActionListener(new ChooseAlgorithmListener());

        // setting of current active items
        activeLight = lights.get(0);
        activeSensor = sensors.get(0);
    }

    /**
     * Returns {@link Container} on which all crossroad items are drawn and all control items are situated.
     * @return The container with crossroad objects and control items.
     */
    public Container getContentPane() {
        return this.contentPane;
    }

    /**
     * Return name if intersection.
     * @return The identifier of this intersection.
     */
    public String getID() {
        return id;
    }
    
    /**
     * Returns graphics context.
     * @return The graphics context.
     */
    public Graphics getGraphics() {
        return contentPane.getGraphics();
    }

    // load background image from file
    private void loadBackground() {
        background = null;
        try {
            background = ImageIO.read(new File(crossroad.getImagePath()));
            contentPane.repaint();
        } catch (IOException e) {
            System.out.println("Can not to read background: " + e);
            JOptionPane.showMessageDialog(null, "Cannot to load background image. Check path and reload crossroad or connect with support.");
            System.exit(1);
        }
    }

    // draws all components on the contentPane
    public void drawBackground(Graphics g) {
        g.drawImage(background, 0, 0, imageWidth, imageHeight, contentPane);
        g.setColor(Color.GRAY);
        g.drawLine(0, imageHeight, imageWidth + panel.getWidth(), imageHeight);
        g.drawLine(imageWidth + panel.getWidth(), imageHeight, imageWidth + panel.getWidth(), 0);
        //panel.draw(g);
        for (Light light : lights) {
            // if light is in the right panel
            boolean inXRange = imageWidth <= light.getX() && light.getX() <= imageWidth + panel.getWidth();
            boolean inYRange = 0 <= light.getY() && light.getY() <= panel.getHeight();
            if (inXRange && inYRange) {
                //chosenLight = light;
                // if light is active
                if (light.getID() == activeLight.getID()) {
                    light.draw(g);
                    //break;
                }
            } else {
                if (light.getID() == activeLight.getID()) {
                    activeLight = lights.get((lights.indexOf(activeLight) + 1) % lights.size());
                }
                light.draw(g);
            }
        }
        for (Sensor sensor : sensors) {
            // if sensor is in the right panel
            boolean inXRange = imageWidth <= sensor.getX() && sensor.getX() <= imageWidth + panel.getWidth();
            boolean inYRange = 0 <= sensor.getY() && sensor.getY() <= panel.getHeight();
            if (inXRange && inYRange) {
                // if senor is active
                if (sensor.getID() == activeSensor.getID()) {
                    sensor.draw(g);
                }
            } else {
                if (sensor.getID() == activeSensor.getID()) {
                    activeSensor = sensors.get((sensors.indexOf(activeSensor) + 1) % sensors.size());
                }
                sensor.draw(g);
            }
        }
    }

    /**
     * Returns container dimensions.
     * @return The container dimensions.
     */
    public Dimension getContainerSize() {
        Dimension result = new Dimension(imageWidth + panel.getWidth(), imageHeight);
        return result;
    }

    /**
     * This method is called from outer class if it's need to repaint contentPane.
     */    
    @Override
    public void update() {
        contentPane.repaint();
    }

    // generates lights and sensors
    private void generateLightsAndSensors() {
        //initialising lights and sensors;
        lights = new ArrayList<>();
        Set<Integer> indices = coreLights.keySet();
        sensors = new ArrayList<>();
        //HashMap<Integer, ObservableSensor> observableSensors = new HashMap<>();        
        for (Integer ind : indices) {
            Light light = null;
            Sensor sensor = null;
            switch (coreLights.get(ind).getType()) {
                case "ROAD":
                    light = new TrafficLight(this, imageWidth + panel.getWidth() / 2 - LIGHT_WIDTH / 2, 72, LIGHT_WIDTH, LIGHT_HEIGHT, ind);
                    lights.add(light);
                    sensor = new LaneSensor(ind, imageWidth + panel.getWidth() / 2 - SENSOR_WIDTH / 2, 240, SENSOR_WIDTH, SENSOR_HEIGHT);
                    break;
                case "CROSSWALK":
                    light = new CrosswalkLight(this, imageWidth + panel.getWidth() / 2 - LIGHT_WIDTH / 2, 90, LIGHT_WIDTH, LIGHT_HEIGHT / 3 * 2, ind);
                    lights.add(light);
                    sensor = new Button(ind, imageWidth + panel.getWidth() / 2 - BUTTON_WIDTH / 2, 240, BUTTON_WIDTH, BUTTON_WIDTH);
                    break;
            }
            coreLights.get(ind).addListener(light);
            sensors.add(sensor);
            sensor.addListener(crossroad);
            //observableSensors.put(ind, sensor);
        }
    }    
    
    private void setButtonsEnable(boolean enabled) {
        prevLight.setEnabled(enabled);
        nextLight.setEnabled(enabled);
        rotateLeft.setEnabled(enabled);
        rotateRight.setEnabled(enabled);
        prevSensor.setEnabled(enabled);
        nextSensor.setEnabled(enabled);
        rotate.setEnabled(enabled);
    }

    /*
     * Inner classes that handle control items events and mouse events.
     */
    private class ChooseAlgorithmListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String algorithm = (String) algorithms.getSelectedItem();
            crossroad.setAlgorithm(algorithm);
        }
    }

    private class PrevLightBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (activeLight != lights.get(0)) {
                activeLight = lights.get(lights.indexOf(activeLight) - 1);
            } else {
                activeLight = lights.get(lights.size() - 1);
            }
            contentPane.repaint();
        }
    }

    private class NextLightBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeLight = lights.get(Math.abs(lights.indexOf(activeLight) + 1) % lights.size());
            contentPane.repaint();
        }
    }

    private class TurnLeftBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeLight.rotateLeft();
            contentPane.repaint();
        }
    }

    private class TurnRightBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeLight.rotateRight();
            contentPane.repaint();
        }
    }

    private class PrevSensorBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (activeSensor != sensors.get(0)) {
                activeSensor = sensors.get(sensors.indexOf(activeSensor) - 1);
            } else {
                activeSensor = sensors.get(sensors.size() - 1);
            }
            System.out.println(activeSensor.getID());
            contentPane.repaint();
        }
    }

    private class NextSensorBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeSensor = sensors.get(Math.abs(sensors.indexOf(activeSensor) + 1) % sensors.size());
            System.out.println(activeSensor.getID());
            contentPane.repaint();
        }
    }

    private class RotateBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            activeSensor.rotate();
            contentPane.repaint();
        }
    }

    private class EnableBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (enableBtn.isSelected()) {
                enableBtn.setText("Disable editing");
                enabled = true;
                setButtonsEnable(true);
            } else {
                enableBtn.setText("Enable editing");
                enabled = false;
                setButtonsEnable(false);
            }
        }
    }

    private class EmergencyBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String num = lightNum.getText();
            int lightNumber = -1;
            if (num != null && !"".equals(num)) {
                num = num.trim();
                lightNumber = Integer.parseInt(num);
            }
            if (setEmergency.isSelected()) {
                if (lightNumber != -1) {
                    setEmergency.setText("Stop emergency:");
                    lightNum.setEditable(false);
                    // set to true;
                    crossroad.setEmergency(lightNumber, true);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Enter number of lane (light) for emergency");
                    setEmergency.setSelected(false);
                    setEmergency.setText("Set emergency:");
                }
            } else {
                setEmergency.setText("Set emergency:");
                lightNum.setEditable(true);
                // set to false;
                crossroad.setEmergency(lightNumber, false);
            }
        }
    }

    private class StartBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            crossroad.resumeCrossroad();
        }
    }

    private class StopBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            crossroad.stopCrossroad();
        }
    }

    private class PaneMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                x = e.getX();
                y = e.getY();
                if (enabled) {
                    for (Sensor sensor : sensors) {
                        // if mouse is inside the sensor
                        boolean inXRange = sensor.getX() <= x && x <= sensor.getX() + sensor.getWidth();
                        boolean inYRange = sensor.getY() <= y && y <= sensor.getY() + sensor.getHeight();
                        if (inXRange && inYRange) {
                            // if sensor is inside the right panel
                            inXRange = imageWidth <= sensor.getX() && sensor.getX() <= imageWidth + panel.getWidth();
                            inYRange = 0 <= sensor.getY() && sensor.getY() <= panel.getHeight();
                            if (inXRange && inYRange) {
                                if (sensor.getID() == activeSensor.getID()) {
                                    chosenSensor = sensor;
                                    break;
                                }
                            } else {
                                chosenSensor = sensor;
                                String connectedLights = JOptionPane.showInputDialog(contentPane, "Input connected lights separated with comma in format \"crossroad.light\"");
                                System.out.println(connectedLights);
                                setConnectedLights(connectedLights);
                                break;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            if (enabled) {
                for (Light light : lights) {
                    // if mouse is inside the light
                    boolean inXRange = light.getX() <= x && x <= light.getX() + light.getWidth();
                    boolean inYRange = light.getY() <= y && y <= light.getY() + light.getHeight();
                    if (inXRange && inYRange) {
                        // if light is inside the right panel
                        inXRange = imageWidth <= light.getX() && light.getX() <= imageWidth + panel.getWidth();
                        inYRange = 0 <= light.getY() && light.getY() <= panel.getHeight();
                        if (inXRange && inYRange) {
                            if (light.getID() == activeLight.getID()) {
                                chosenLight = light;
                                break;
                            }
                        } else {
                            chosenLight = light;
                            break;
                        }
                    }
                }
            }
            for (Sensor sensor : sensors) {
                // if mouse is inside the sensor
                boolean inXRange = sensor.getX() <= x && x <= sensor.getX() + sensor.getWidth();
                boolean inYRange = sensor.getY() <= y && y <= sensor.getY() + sensor.getHeight();
                if (inXRange && inYRange) {
                    // if sensor is inside the right panel
                    inXRange = imageWidth <= sensor.getX() && sensor.getX() <= imageWidth + panel.getWidth();
                    inYRange = 0 <= sensor.getY() && sensor.getY() <= panel.getHeight();
                    if (inXRange && inYRange) {
                        if (sensor.getID() == activeSensor.getID()) {
                            chosenSensor = sensor;
                            break;
                        }
                    } else {
                        chosenSensor = sensor;
                        if (!enabled) {
                            sensor.setActive(true);
                            sensor.updateAll();
                            contentPane.repaint();
                        }
                        break;
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            chosenLight = null;
            if (!enabled && chosenSensor != null) {
                chosenSensor.setActive(false);
                chosenSensor.updateAll();
                contentPane.repaint();
            }
            chosenSensor = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            if (chosenLight != null && enabled) {
                chosenLight.setCoordinates(x, y, chosenLight.getWidth(), chosenLight.getHeight());
            }
            if (chosenSensor != null && enabled) {
                chosenSensor.setCoordinates(x, y, chosenSensor.getWidth(), chosenSensor.getHeight());
            }
            contentPane.repaint();
        }
    }

    public void setConnectedLights(String connected) {
    }
}
