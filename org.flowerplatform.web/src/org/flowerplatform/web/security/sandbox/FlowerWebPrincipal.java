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
package org.flowerplatform.web.security.sandbox;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;

import org.flowerplatform.web.entity.User;

/**
 * A FlowerWebPrincipal encapsulates the user associated with the client
 * application and the subject instance used to execute privileged code.
 * 
 * @author Florin
 * 
 * 
 */
public class FlowerWebPrincipal implements Principal, IPrincipal {
	
	/**
	 * 
	 */
	private long userId;
	
	/**
	 * 
	 */
	private Subject subject;
	
	/**
	 * 
	 */
	private User cachedUser;

	/**
	 * @see Getter doc.
	 * 
	 */
	private Map<String, List<String>> userRepositories;
	
	/**
	 * 
	 */
	private Map<String, List<String>> userGitRepositories;
	
	private Map<String, List<String>> userSvnRepositories;
	
	/**
	 * @see #getWikiClientConfigurations()
	 * 
	 * @author Mariana
	 */
	private Map<String, Object> wikiClientConfigurations;
	
	/**
	 * 
	 */
	public FlowerWebPrincipal(long userId) {
		this.userId = userId;
		subject = new Subject();		
		subject.getPrincipals().add(this);
		subject.setReadOnly();
		
		userRepositories = new HashMap<String, List<String>>();
		userGitRepositories = new HashMap<String, List<String>>();
		userSvnRepositories = new HashMap<String, List<String>>();
		wikiClientConfigurations = new HashMap<String, Object>();
	}
	
	/**
	 * 
	 */
	public synchronized User getUser() {
		if (cachedUser == null) {
			new DatabaseOperationWrapper(new DatabaseOperation() {
				
				@Override
				public void run() {
					cachedUser = wrapper.find(User.class, userId);
				}
			});
			
		}
		return cachedUser;
	}

	/**
	 * 
	 */
	public long getUserId() {
		return userId;
	}
	
	/**
	 * 
	 */
	@Override
	public Subject getSubject() {
		return subject;
	}
	
	/**
	 * 
	 */
	public synchronized void clearCachedUser() {
		cachedUser = null;
	}

	/**
	 * 
	 */
	@Override
	public String getName() {
		return getUser().getName();
	}

	/**
	 * Holds information about user's repositories, like user name and password
	 * for authentication mechanism.  
	 * <ul>
	 * 	<li> key: repository realm (returned by SVN native libraries);
	 * 		by default, represents repository's UUID
	 * 	<li> value: list containing 2 strings -> user name, password
	 * </ul> 
	 * 
	 */
	public Map<String, List<String>> getUserRepositories() {
		return userRepositories;
	}

	/**
	 * 
	 */
	public Map<String, List<String>> getUserGitRepositories() {
		return userGitRepositories;
	}	
	
	public Map<String, List<String>> getUserSvnRepositories(){
		return userSvnRepositories;
	}
	
	/**
	 * Holds information about client configurations (i.e. username, password) for wiki. Mapped by
	 * wiki technology.
	 * 
	 * @author Mariana
	 */
	public Map<String, Object> getWikiClientConfigurations() {
		return wikiClientConfigurations;
	}

	@Override
	public String toString() {
		return "User with login=" + getUser().getLogin();
	}
	
}