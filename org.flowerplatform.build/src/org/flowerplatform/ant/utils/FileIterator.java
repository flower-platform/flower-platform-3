package org.flowerplatform.ant.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author Florin
 * 
 */
public class FileIterator implements Iterator<File> {

	private Queue<File> queue = new LinkedList<File>();

	private FileFilter secondLevelFileFilter;

	private boolean isChildLevel = true;
	
	public FileIterator(File rootFolder) {
		queue.add(rootFolder);
	}

	public FileIterator(File workspaceFolder, FileFilter secondLevelFileFilter) {
		this.secondLevelFileFilter = secondLevelFileFilter;
		queue.add(workspaceFolder);
	}

	@Override
	public boolean hasNext() {
		return queue.size() > 0;
	}

	@Override
	public File next() {
		File file = queue.remove();
		if (file.isDirectory()) {
			if (secondLevelFileFilter != null && isChildLevel) {
				for (File childFile : file.listFiles()) {
					if (secondLevelFileFilter.accept(childFile)) {
						queue.add(childFile);
					}
				}
			} else {
				queue.addAll(Arrays.asList(file.listFiles()));
			}
			isChildLevel = false;
		}
		return file;
	}

	@Override
	public void remove() {
		throw new RuntimeException();
	}

}
