package org.flowerplatform.eclipse.part;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;

public class FlowerDiagramMatchingStrategy implements IEditorMatchingStrategy {
	
	
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		
		return false;
	}

}
