/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.git;

import java.util.List;

import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;

/**
 * @author Cristina
 * @flowerModelElementId _6vfJ0HH1EeK0WoOyJtfqdw
 */
public class GitUsernamePasswordCredentialsProvider extends CredentialsProvider {
		
	public GitUsernamePasswordCredentialsProvider() {		
	}
		
	/**
	 * Gets the user/password information stored in {@link FlowerWebPrincipal}.
	 * 
	 * <p>
	 * This method is called each time a GIT operation (clone/fetch/push)
	 * needs authentication data.
	 *  
	 * @flowerModelElementId _GWb-0HH2EeK0WoOyJtfqdw
	 */
	@Override
	public boolean get(URIish uri, CredentialItem... items)	throws UnsupportedCredentialItem {	
		if (!supports(items)) {
			return false;
		}
		// stores the URI in order to be used when updating the authentication data
		GitService.tlURI.set(uri.toString());
		
		FlowerWebPrincipal principal = (FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get();
		
		if (principal.getUserGitRepositories().containsKey(uri.toString())) {
			CredentialItem.Username userItem = null;
			CredentialItem.Password passwordItem = null;
			
			// get the user/password items that needs to be filled
			for (CredentialItem item : items) {
				if (item instanceof CredentialItem.Username) {
					userItem = (CredentialItem.Username) item;
				} else if (item instanceof CredentialItem.Password) {
					passwordItem = (CredentialItem.Password) item;
				}
			}
			List<String> info = principal.getUserGitRepositories().get(uri.toString());
			if (userItem != null) { // fill the user item
				userItem.setValue(info.get(0));
			}
			if (passwordItem != null) { //fill the password item
				passwordItem.setValue(info.get(1).toCharArray());
			}
			return true;		
		}
		
		return false;		
	}

	@Override
	public boolean isInteractive() {		
		return false;
	}

	/**
	 * Only user/password items are supported for now.
	 */
	@Override
	public boolean supports(CredentialItem... items) {
		for (CredentialItem i : items) {
			if (i instanceof CredentialItem.Username)
				continue;
			else if (i instanceof CredentialItem.Password)
				continue;
			else
				return false;
		}
		return true;
	}
	
	@Override
	public void reset(URIish uri) {
		FlowerWebPrincipal principal = (FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get();
		if (principal.getUserGitRepositories().containsKey(uri.toString())) {
			principal.getUserGitRepositories().remove(uri.toString());
		}		
	}
	
}