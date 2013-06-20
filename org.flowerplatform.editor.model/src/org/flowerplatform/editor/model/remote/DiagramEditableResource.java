package org.flowerplatform.editor.model.remote;

import org.flowerplatform.editor.remote.FileBasedEditableResource;

public class DiagramEditableResource extends FileBasedEditableResource {

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "hey, I'm a diagram!";
	}

	@Override
	public String getIconUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

}
