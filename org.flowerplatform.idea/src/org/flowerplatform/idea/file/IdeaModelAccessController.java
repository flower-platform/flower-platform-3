package org.flowerplatform.idea.file;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.flowerplatform.editor.model.IModelAccessController;

/**
 * @author Sebastian Solomon
 */
public class IdeaModelAccessController implements IModelAccessController {

	@Override
	public URI getURIFromFile(Object file) {		
		return URI.createFileURI(((File) file).getAbsolutePath()); //web
		//return URI.createPlatformResourceURI(((IFile) file).getFullPath().toString(), false); //eclipse
	}

}
