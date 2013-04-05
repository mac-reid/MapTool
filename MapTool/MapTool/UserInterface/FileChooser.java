package UserInterface;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


/*public class FileChooser extends JPanel{
    JFileChooser fc;
    String filename = "";
    String fileLocation = "";
    public FileChooser() {
 
        //Create a file chooser
        fc = new JFileChooser();
        
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(new JFrame());
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            	filename = file.getName();
            	fileLocation = file.getAbsolutePath();
        } else {
        }
        createAndShowGUI();
    }
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FileChooserDemo2");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        //Add content to the window.
        frame.add(new FileChooser());
 
        //Display the window.
        frame.pack();
        frame.setVisible(false);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
 
    public String getAbsLocation(){
    	return fileLocation;
    }
    
    public String getFilename(){
    	return filename;
    }
}*/




public class FileChooser {
    JFileChooser fc;
    String filename = "";
    String fileLocation = "";
    File selectedFile = null;
    public FileChooser() {
    	
    	JFrame frame = new JFrame();
    	frame.setAlwaysOnTop(true);
 
        //Create a file chooser
        fc = new JFileChooser();
        
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(new JFrame());
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = fc.getSelectedFile();
            	filename = selectedFile.getName();
            	fileLocation = selectedFile.getAbsolutePath();
        } else {
        }
 
    }
 
    public String getAbsLocation(){
    	return fileLocation;
    }
    
    public String getFilename(){
    	return filename;
    }
    
    public File getSelectedFile(){
    	return selectedFile;
    }
}
