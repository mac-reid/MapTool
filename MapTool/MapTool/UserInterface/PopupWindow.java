package UserInterface;

import java.io.File;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class PopupWindow {

	Shell shell;
	String filter;
	Display display;
	FileDialog dialog;
	File[] selectedFiles;
	String[] filterNames;
	String[] filterExtensions;
	String[] selectedFileNames;
	
	public File[] importTokens() {

		prepareYourself("All Files (*)", "*", SWT.OPEN | SWT.MULTI);	
		
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			dialog.setFilterPath("C:\\");
		else 
			dialog.setFilterPath("/");
		
		dialog.open();
		importFiles();
		
		// need to move the files to the proper dir
		kill();
		return selectedFiles;
	}
	
	public File[] importMaps() {
		
		prepareYourself("All Files (*)", "*", SWT.OPEN | SWT.MULTI);
		
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			dialog.setFilterPath("C:\\");
		else 
			dialog.setFilterPath("/");
		
		dialog.open();
		importFiles();
		
		// need to move the files to the proper dir
		kill();
		return null;
	}
	
	public File loadGame() {
		
		prepareYourself("MapTool Save Files (.sav)", "*", SWT.OPEN);
		
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			dialog.setFilterPath(System.getProperty("user.dir") + "\\saves\\");
		else 
			dialog.setFilterPath(System.getProperty("user.dir") + "/saves/");
		
		dialog.open();
		
		importFiles();
		
		File ret = null;
		if (selectedFiles != null && selectedFiles.length >= 1)
			ret = selectedFiles[0];
		
		kill();
		return ret;
	}
	
	public File saveGame() {
		
		prepareYourself("MapTool Save Files (.sav)", "*", SWT.SAVE);	
		
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			dialog.setFilterPath(System.getProperty("user.dir") + "\\saves\\");
		else 
			dialog.setFilterPath(System.getProperty("user.dir") + "/saves/");
		
		dialog.open();
		
		importFiles();
		
		File ret = null;
		if (selectedFiles != null && selectedFiles.length >= 1)
			ret = selectedFiles[0];
		
		kill();
		return ret;
	}	
	
	private void prepareYourself(String filter, String extensions, int type) {
		
		display = new Display();
		shell = new Shell(display); 
		dialog = new FileDialog(shell, type);
		filterNames = new String[] {filter};
		filterExtensions = new String[] {extensions};
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		filter = dialog.getFilterPath();
		selectedFileNames = dialog.getFileNames();
		selectedFiles = new File[selectedFileNames.length];
	}
	
	private void importFiles() {
		
		for (int i = 0; i < selectedFiles.length; i++) 
			if(filter != null && filter.trim().length() > 0) 
				selectedFiles[i] = new File(filter, selectedFileNames[i]);
			else
				selectedFiles[i] = new File(selectedFileNames[i]);
	}
	
	private void kill() {
		shell.close();
		while (!shell.isDisposed()) 
			if (!display.readAndDispatch()) 
				display.sleep();
		display.dispose();
	}
}
