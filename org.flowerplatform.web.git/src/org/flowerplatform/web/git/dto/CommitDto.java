package org.flowerplatform.web.git.dto;

/**
 *	@author Cristina Constantinescu
 */
public class CommitDto {

	private String id;
	
	private String shortId;
	
	private String label;

	private String image;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShortId() {
		return shortId;
	}

	public void setShortId(String shortId) {
		this.shortId = shortId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}		
	
}
