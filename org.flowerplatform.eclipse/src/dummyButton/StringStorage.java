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
package dummyButton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

class StringStorage implements IStorage {
	  private String string;
	 
	  StringStorage(String input) {
	    this.string = input;
	  }
	 
	  public InputStream getContents() throws CoreException {
	    return new ByteArrayInputStream(string.getBytes());
	  }
	 
	  public IPath getFullPath() {
	    return null;
	  }
	 
	  public Object getAdapter(Class adapter) {
	    return null;
	  }
	 
	  public String getName() {
	    return "Flower...";
	  }
	 
	  public boolean isReadOnly() {
	    return true;
	  }
	}
