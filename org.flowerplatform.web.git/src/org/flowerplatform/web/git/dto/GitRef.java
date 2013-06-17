package org.flowerplatform.web.git.dto;

/**
 *	@author Cristina Constantinescu
 */
public class GitRef {

	private String name;
	
	private String shortName;

	private String label;
	
	private String upstream;
	
	private String image;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
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

	public GitRef() {		
	}

	public GitRef(String name, String shortName) {		
		this.name = name;
		this.shortName = shortName;
	}

	public String getUpstream() {
		return upstream;
	}

	public void setUpstream(String upstream) {
		this.upstream = upstream;
	}
		
}
