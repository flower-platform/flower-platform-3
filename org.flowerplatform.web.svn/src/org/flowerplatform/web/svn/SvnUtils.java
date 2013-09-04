package org.flowerplatform.web.svn;

import java.io.File;

import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapter;

/**
 * 
 * @author Victor Badila 
 */
public class SvnUtils {
	
	public Boolean isRepository(File f) {
		JhlClientAdapter clientAdapter = new JhlClientAdapter();		 
		try {
			clientAdapter.getInfo(f);
			return true;
		} catch (SVNClientException e) {
			return false;
		}			
	}

}
