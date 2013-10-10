package org.flowerplatform.orion.server;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.flowerplatform.editor.model.IModelAccessController;

/**
 * @author Cristina Constantinescu
 */
public class OrionModelAccessController implements IModelAccessController {

	@Override
	public URI getURIFromFile(Object file) {		
		return URI.createFileURI(((File) file).getAbsolutePath());
	}

}
