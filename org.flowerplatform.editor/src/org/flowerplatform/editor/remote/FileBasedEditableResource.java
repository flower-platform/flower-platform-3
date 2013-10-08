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



/**
 * Represents a resource that has a corresponding file (e.g. text files, model files).
 * 
 * @author Mariana
 * 
 * 
 */
public abstract class FileBasedEditableResource extends EditableResource {
	
	/**
	 * @see Getter.
	 */
	private boolean ignoreResourceChangedNotification;

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