package org.flowerplatform.web.security.sandbox;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;

import org.flowerplatform.web.entity.User;

/**
 * A FlowerWebPrincipal encapsulates the user associated with the client
 * application and the subject instance used to execute privileged code.
 * 
 * @author Florin
 * 
 * @flowerModelElementId _Qo8c4G0dEeGBsfNm1ipRfw
 */
public class FlowerWebPrincipal implements Principal, IPrincipal {
	
	/**
	 * @flowerModelElementId _SMhvgG0dEeGBsfNm1ipRfw
	 */
	private long userId;
	
	/**
	 * @flowerModelElementId _bBoH8G0dEeGBsfNm1ipRfw
	 */
	private Subject subject;
	
	/**
	 * @flowerModelElementId _UxyFEG0eEeGBsfNm1ipRfw
	 */
	private User cachedUser;

	/**
	 * @see Getter doc.
	 * @flowerModelElementId _NOs-xIllEeGGgbhWYb3xSA
	 */
	private Map<String, List<String>> userRepositories;
	
	/**
	 * @flowerModelElementId _4l0SIHH2EeK0WoOyJtfqdw
	 */
	private Map<String, List<String>> userGitRepositories;
	
	/**
	 * @see #getWikiClientConfigurations()
	 * 
	 * @author Mariana
	 */
	private Map<String, Object> wikiClientConfigurations;
	
	/**
	 * @flowerModelElementId _puDdgHJqEeG32IfhnS7SDQ
	 */
	public FlowerWebPrincipal(long userId) {
		this.userId = userId;
		subject = new Subject();		
		subject.getPrincipals().add(this);
		subject.setReadOnly();
		
		userRepositories = new HashMap<String, List<String>>();
		userGitRepositories = new HashMap<String, List<String>>();
		wikiClientConfigurations = new HashMap<String, Object>();
	}
	
	/**
	 * @flowerModelElementId _ZQz1IG0eEeGBsfNm1ipRfw
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
	 * @flowerModelElementId _puZgYIMAEeG2LdcUX32kBA
	 */
	public long getUserId() {
		return userId;
	}
	
	/**
	 * @flowerModelElementId _YN3R8Hl1EeG7_fzMWZxiHA
	 */
	@Override
	public Subject getSubject() {
		return subject;
	}
	
	/**
	 * @flowerModelElementId _crrZgG0eEeGBsfNm1ipRfw
	 */
	public synchronized void clearCachedUser() {
		cachedUser = null;
	}

	/**
	 * @flowerModelElementId _puEEkXJqEeG32IfhnS7SDQ
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
	 * @flowerModelElementId _NOzFZYllEeGGgbhWYb3xSA
	 */
	public Map<String, List<String>> getUserRepositories() {
		return userRepositories;
	}

	/**
	 * @flowerModelElementId _IKA_0HH3EeK0WoOyJtfqdw
	 */
	public Map<String, List<String>> getUserGitRepositories() {
		return userGitRepositories;
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