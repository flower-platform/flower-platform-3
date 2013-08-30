package org.flowerplatform.file_event;

import java.io.File;
/**
 * 
 * @author Tache Razvan Mihai
 *
 */
public class FileEvent {
	
	public static final int FILE_CLOSED = 1;
	
	public static final int FILE_CREATED = 2;

	public static final int FILE_DELETED = 3;

	public static final int FILE_MODIFIED = 4;

	public static final int FILE_OPENED = 5;
	
	public static final int FILE_RENAMED = 6;
	
	private int event;
	
	private File file;
	
	private File oldFile = null;

	public FileEvent(File file,int event) {
		this.file = file;
		this.event = event;
	}

	public FileEvent(File file,int event, File oldFile) {
		this.file = file;
		this.event = event;
		this.oldFile = oldFile;
	}
	
	public int getEvent() {
		return event;
	}
	
	public File getFile() {
		return file;
	}
	
	public File getOldFile() {
		return oldFile;
	}
}
