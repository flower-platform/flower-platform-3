package org.flowerplatform.editor.text.remote;

import org.flowerplatform.editor.remote.FileBasedEditableResource;


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
 * @flowerModelElementId _724PIGnaEeGf2Ze1btT4ow
 */
public class TextEditableResource extends FileBasedEditableResource {
	
	public static final String EOLN_RN_DELIMITER = "\r\n";
	public static final String EOLN_R_DELIMITER = "\r";
	public static final String EOLN_N_DELIMITER = "\n";

	private boolean dirty;
	
	/**
	 * @flowerModelElementId _725dQmnaEeGf2Ze1btT4ow
	 */
	private StringBuffer fileContent;
	/**
	 * @flowerModelElementId _NRLrBollEeGGgbhWYb3xSA
	 */
	private String replacedEolnDelimiter;
	
	@Override
	public String getIconUrl() {
		return "icons/Web/icons/file.gif";
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
