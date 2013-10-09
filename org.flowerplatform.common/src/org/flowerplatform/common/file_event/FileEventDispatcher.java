package org.flowerplatform.common.file_event;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Tache Razvan Mihai
 */
public class FileEventDispatcher {
	
	private List<IFileEventListener> fileEventListeners = new ArrayList<>();
	
	public void addFileEventListener(IFileEventListener fileEventListener) {
		fileEventListeners.add(fileEventListener);
	}
	
	public void dispatch(FileEvent event) {
		for(IFileEventListener fileEventListener : fileEventListeners){
			fileEventListener.notify(event);
		}
	}
}
