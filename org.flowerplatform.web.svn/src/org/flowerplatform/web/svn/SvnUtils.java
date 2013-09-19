package org.flowerplatform.web.svn;

import java.io.File;


import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;

import org.apache.subversion.javahl.ClientException;
import org.tigris.subversion.subclipse.core.SVNException;

import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNInfoUnversioned;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapter;

/**
 * 
 * @author Victor Badila 
 */
public class SvnUtils implements ISvnVersionHandler{
	
	public Boolean isRepository(File f) {				 
		try {
			if (SVNProviderPlugin.getPlugin().getSVNClient().getInfo(f) instanceof SVNInfoUnversioned) {
				return false;
			}
			return true;
		} catch (SVNClientException | SVNException e) {
			return false;
		}			
	}

	@Override
	public boolean isAuthenticationClientException(Throwable exception) {
		if ((exception instanceof SVNException) &&
				(exception.getCause().getCause() instanceof ClientException)) {
			if ( ((ClientException)exception.getCause().getCause()).getAprError() == 170001) {
				return true;
			}
		}
		return false;
	}

}
