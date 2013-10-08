package org.flowerplatform.web;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.flowerplatform.editor.model.IModelAccessController;

/**
 * @author Cristina Constantinescu
 */
public class WebModelAccessController implements IModelAccessController {

	@Override
	public URI getURIFromFile(Object file) {		
		return URI.createFileURI(((File) file).getAbsolutePath());
	}

}
