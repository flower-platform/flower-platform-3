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
package org.flowerplatform.editor.text.remote;

import org.flowerplatform.editor.remote.FileBasedEditableResource;
import org.flowerplatform.editor.text.EditorTextPlugin;


/**
 * A specific editable resource representing a text file. It
 * also keeps the file content as the {@link #fileContent}
 * property, and all the changes received from the clients
 * are merged into this property, to keep this resource in sync
 * with the clients.
 * 
 * @author Cristi
 * @author Mariana
 * @author Sorin
 * 
 */
public class TextEditableResource extends FileBasedEditableResource {
	
	public static final String EOLN_RN_DELIMITER = "\r\n";
	public static final String EOLN_R_DELIMITER = "\r";
	public static final String EOLN_N_DELIMITER = "\n";

	private boolean dirty;
	
	/**
	 * 
	 */
	private StringBuffer fileContent;
	/**
	 * 
	 */
	private String replacedEolnDelimiter;
	
	@Override
	public String getIconUrl() {
		return EditorTextPlugin.getInstance().getResourceUrl("images/file.gif");
	}

	/**
	 * The content of this text file. It is kept in 
	 * sync with the text displayed in the editors
	 * opened by the clients.
	 */
	public StringBuffer getFileContent() {
		return fileContent;
	}
	
	public void setFileContent(StringBuffer fileContent) {
		this.fileContent = fileContent;
	}

	/**
	 * Keeps the eoln delimiter to be reverted when disposing this resource.
	 * 
	 * @see TextEditorBackend
	 * @return null if the the eoln delimiter was not replaces or {@link #EOLN_RN_DELIMITER} or {@link #EOLN_R_DELIMITER}.
	 */
	public String getReplacedEolnDelimiter() {
		return replacedEolnDelimiter;
	}
	
	public void setReplacedEolnDelimiter(String replacedEolnDelimiter) {
		this.replacedEolnDelimiter = replacedEolnDelimiter;
	}
	
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public String getLabel() {
		return getFile().getName();
	}

	/**
	 * Intended to be extended to provide helping behaviour. 
	 */
	public void replaceContent(int offset, int length, String toInsert) {
		getFileContent().replace(offset, offset + length, toInsert);
	}
}