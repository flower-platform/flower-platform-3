package com.crispico.flower.mp.codesync.wiki.dokuwiki;

/**
 * @author Mariana
 */
public class DokuWikiClientConfiguration {

	private String url, user, password;

	public DokuWikiClientConfiguration(String url, String user, String password) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
