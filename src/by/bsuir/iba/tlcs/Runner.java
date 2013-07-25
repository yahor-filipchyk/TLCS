
package by.bsuir.iba.tlcs;

import by.bsuir.iba.tlcs.core.control.Crossroad;
import by.bsuir.iba.tlcs.core.control.LightSystem;
import by.bsuir.iba.tlcs.gui.Frame;
import by.bsuir.iba.tlcs.gui.intersection.Intersection;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Main application class. Creates window with all controls and crossroads representations.
 * @author Yahor Filipchyk
 */
public class Runner {
    
    public static void saveIntersection(Intersection intersection) {
        String path = LightSystem.getConfigPath() + "saved_intersections";
        System.out.println(path);
        File folderToSave = new File(path);
        if (!folderToSave.exists()) {
            folderToSave.mkdir();
        }
        String fileName = intersection.getID();
        File fileToSave = new File(folderToSave, fileName);
        try {
            fileToSave.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave));
            oos.writeObject(intersection);
        } catch (IOException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void saveIntersections(ArrayList<Intersection> intersections) {
        
    }
    
    public static ArrayList<Intersection> loadIntersections() {
        return null;        
    }
    
    public static void main(String args[]) {
        LightSystem system = new LightSystem();
        Frame frame = new Frame();
        int crossroadsCount = 1;
        Dimension dimensions = new Dimension(500, 400);
        for (Crossroad crossroad : system.getCrossroads()) {
            Intersection intersection = new Intersection(crossroad, "Crossroad " + crossroadsCount);
            dimensions = intersection.getContainerSize();
            frame.addCrossroad(intersection, crossroadsCount - 1);
            crossroadsCount++;
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(dimensions.width + 50, dimensions.height + 120);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);        
        if (crossroadsCount == 1) {
            JOptionPane.showMessageDialog(frame, "No one crossroad is created. Check location of configuration " +
                    "files or connect with support!");
        }
    }
}
