package org.flowerplatform.web.entity.dto;

/**
 * Named basic dto.
 * 
 * @author Cristi
 * @author Cristina
 * 
 * @flowerModelElementId _b64OgFcAEeGL3vi-zPhopA
 */
public class NamedDto extends Dto {

	/**
	 * @flowerModelElementId _efaAwFcAEeGL3vi-zPhopA
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NamedDto(){
	}
	
	public NamedDto(long id, String name) {
		super(id);
		this.name = name;
	}	
	
}