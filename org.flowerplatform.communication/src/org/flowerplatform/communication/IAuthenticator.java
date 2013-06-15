package org.flowerplatform.communication;

/**
 * @author Mariana
 */
public interface IAuthenticator {

	public enum AuthenticationResult {
		
		OK,
		INCORRECT_CREDENTIALS,
		NOT_ACTIVATED,
		ALREADY_ACTIVATED;
		
		private long id;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}
	}

	AuthenticationResult authenticate(String login, String password, String activationCode);
	
	IPrincipal getPrincipal(long id);
	
}
