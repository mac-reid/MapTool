package UserInterface;

import java.io.File;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class PopupWindow extends Thread {

	public void run() {}

	public File[] importFiles() {

		Display display = new Display ();
		Shell shell = new Shell (display); 
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		String[] filterNames = new String[] {"All Files (*)"};
		String[] filterExtensions = new String[] {"*"};

		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		
		dialog.open();

		String filter = dialog.getFilterPath();
		
		System.out.println("Selected files: ");
		String[] selectedFileNames = dialog.getFileNames();
		
		File[] selectedFiles = new File[selectedFileNames.length];
		int i = 0;

		for(String fileName : selectedFileNames) {
			
			if(filter != null && filter.trim().length() > 0) 
				selectedFiles[i] = new File(filter, selectedFileNames[i]);
			else
				selectedFiles[i] = new File(selectedFileNames[i]);

			System.out.println(selectedFiles[i].getAbsolutePath());
			i++;
		}
		
		shell.close();
		while (!shell.isDisposed()) 
			if (!display.readAndDispatch()) 
				display.sleep();
		display.dispose();

		return selectedFiles;
	}
}
