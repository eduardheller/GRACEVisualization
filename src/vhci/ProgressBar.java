/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vhci;

/**
 *
 * @author Eduard
 */

 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;
import org.lwjgl.system.Configuration;
import vhci.GRACE.GRACEFileLoader;
import vhci.GRACE.GRACELoader;
 
public class ProgressBar extends JPanel
                             implements ActionListener, 
                                        PropertyChangeListener {
 
    private JProgressBar progressBar;
    private JTextArea taskOutput;
    private Task task;
    private GRACEFileLoader GraceLoader;
    
    private static JFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            
            GraceLoader = new GRACELoader("grace");
   
            
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            
            float FileCount = GraceLoader.graceFileCount();

            for(int i = 0; i<FileCount; i++)
            {      
                GraceLoader.loadNextFile();
                setProgress((int)((i/FileCount)*100));                    
            }
            

            return null;
        }
 
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Configuration.DEBUG.set(true);
            MainWindow Window = new MainWindow(GraceLoader);
            Window.setVisible(true);
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
            disableProgressGUI();
        }
    }
 
    public ProgressBar() {
        super(new BorderLayout());
 
 
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
 
        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
 
        JPanel panel = new JPanel();
        panel.add(progressBar);
 
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
 
    }
    
 
    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(String.format(
                    "Loaded %d%% of GRACE Files.\n", task.getProgress()));

        } 

    }
 
 
    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("VHCI GRACE Earth Mass Change");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new ProgressBar();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
 
    private static void disableProgressGUI() {
        frame.setVisible(false);
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();                
            }
        });
    }
}
