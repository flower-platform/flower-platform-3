package org.flowerplatform.web.svn;

import java.io.File;

<<<<<<< HEAD
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
=======
import org.apache.subversion.javahl.ClientException;
import org.tigris.subversion.subclipse.core.SVNException;
>>>>>>> origin/GH78-Login
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapter;

/**
 * 
 * @author Victor Badila 
 */
public class SvnUtils implements ISvnVersionHandler{
	
	public Boolean isRepository(File f) {				 
		try {
			SVNProviderPlugin.getPlugin().getSVNClient().getInfo(f);
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
				
//		try {
//			Class<?> clazz = Class.forName("org.tigris.subversion.svnclientadapter.SVNClientException");
//			if (exception.getCause() instanceof SVNClientException){
//				Method getAprErrorMethod = clazz.getMethod("getAprError");
//				getAprErrorMethod.setAccessible(true);
//				Object result = getAprErrorMethod.invoke(exception.getCause());
//				if (Integer.valueOf(String.valueOf(result)).intValue() == -1) {
//					return true;
//				}
//			}			
//		} catch (Exception e) {	// swallow it -> consider not authentication exception			
//		}
		return false;
	}

}
