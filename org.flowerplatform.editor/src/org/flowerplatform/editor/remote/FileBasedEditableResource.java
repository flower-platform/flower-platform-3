/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.editor.remote;

import java.io.File;


/**
 * Represents a resource that has a corresponding file (e.g. text files, model files).
 * 
 * @author Mariana
 * 
 * @flowerModelElementId _I7PPwAisEeKf1tSwqjoWGA
 */
public abstract class FileBasedEditableResource extends EditableResource {
	
	/**
	 * @flowerModelElementId _tHePkCm1EeKzq6x6tgMJRw
	 */
	private File file;
	
	/**
	 * Keeps the last modification stamp when this resource was synchronized
	 * with the corresponding file.
	 * 
	 * @see IFile#getModificationStamp()
	 * 
	 * @flowerModelElementId _I7RsAgisEeKf1tSwqjoWGA
	 */
	private long synchronizationStamp = -1;
	
	/**
	 * @see Getter.
	 */
	private boolean ignoreResourceChangedNotification;
	
	/**
	 * @flowerModelElementId _tHpOsSm1EeKzq6x6tgMJRw
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @flowerModelElementId _tHuHMCm1EeKzq6x6tgMJRw
	 */
	public void setFile(File file) {
		this.file = file;
//		if (file != null)
//			synchronizationStamp = file.getModificationStamp();
	}

//	/**
//	 * Called after saving; this way, the resource changed listener will not reload the
//	 * file after detecting changes.
//	 * 
//	 * @see FileBasedEditorStatefulService#save()
//	 * @flowerModelElementId _tIAbEim1EeKzq6x6tgMJRw
//	 */
//	public void updateSynchronizationStamp() {
//		synchronizationStamp = file.getModificationStamp();
//	}
//	
//	/**
//	 * @flowerModelElementId _tIY1kSm1EeKzq6x6tgMJRw
//	 */
//	public boolean isSynchronized() {
//		return synchronizationStamp == file.getModificationStamp();
//	}

	/**
	 * Resource changed notifications for resources where the {@link #ignoreResourceChangedNotification} flag
	 * is set to <code>false</code> will be ignored, i.e. updates will not be sent to the clients. Used during
	 * saving, SVN revert. 
	 */
	public boolean isIgnoreResourceChangedNotification() {
		return ignoreResourceChangedNotification;
	}

	public void setIgnoreResourceChangedNotification(
			boolean ignoreResourceChangedNotification) {
		this.ignoreResourceChangedNotification = ignoreResourceChangedNotification;
	}
}