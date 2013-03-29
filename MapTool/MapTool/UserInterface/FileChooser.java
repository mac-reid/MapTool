
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class FileChooser {
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
 
    }
 
    public String getAbsLocation(){
    	return fileLocation;
    }
    
    public String getFilename(){
    	return filename;
    }
}
