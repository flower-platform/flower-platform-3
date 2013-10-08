package org.flowerplatform.eclipse;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.flowerplatform.editor.model.IModelAccessController;

/**
 * @author Cristina Constantinescu
 */
public class EclipseModelAccessController implements IModelAccessController {

	@Override
	public URI getURIFromFile(Object file) {		
		return URI.createPlatformResourceURI(((IFile) file).getFullPath().toString(), false);
	}

}
