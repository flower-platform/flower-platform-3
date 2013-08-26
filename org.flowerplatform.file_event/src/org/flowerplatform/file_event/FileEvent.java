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
	
	private int event;
	
	private File file;
	
	/*
	 * event can be:
	 * FILE_CLOSED
	 * FILE_CREATED
	 * FILE_DELETED
	 * FILE_MODIFIED
	 * FILE_OPENED
	 */
	public FileEvent(File file,int event) {
		this.file = file;
		this.event = event;
	}

	public int getEvent() {
		return event;
	}
	
	public File getFile() {
		return file;
	}
}
