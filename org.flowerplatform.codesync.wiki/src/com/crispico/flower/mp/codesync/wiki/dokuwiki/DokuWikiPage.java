package com.crispico.flower.mp.codesync.wiki.dokuwiki;

/**
 * @author Mariana
 */
public class DokuWikiPage {

	private String id;
	private String content;
	
	public DokuWikiPage(String id, String content) {
		super();
		this.setId(id);
		this.setContent(content);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return String.format("DokuWikiPage [%s]", getId());
	}
	
}
