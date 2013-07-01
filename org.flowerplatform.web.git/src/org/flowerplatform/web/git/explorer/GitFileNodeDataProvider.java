package org.flowerplatform.web.git.explorer;

import java.io.File;

import org.flowerplatform.web.explorer.AbstractFileWrapperNodeDataProvider;

/**
 * @author Cristina Constantienscu
 */
public class GitFileNodeDataProvider extends AbstractFileWrapperNodeDataProvider {

	protected File getFile(Object node) {
		if (node instanceof File) {			
			return (File) node;
		} 
		return super.getFile(node);		
	}
	
}
