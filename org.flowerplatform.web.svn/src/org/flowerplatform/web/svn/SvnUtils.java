package org.flowerplatform.web.svn;

import java.io.File;

import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapter;

/**
 * 
 * @author Victor Badila 
 */
public class SvnUtils {
	
	public Boolean isRepository(File f) {				 
		try {
			SVNProviderPlugin.getPlugin().getSVNClient().getInfo(f);
			return true;
		} catch (SVNClientException | SVNException e) {
			return false;
		}			
	}

}
