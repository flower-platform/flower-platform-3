package org.flowerplatform.web.entity.dto;

/**
 * Basic DTO (i.e. Data Transfer Object).
 * Dtos correspond to entities, and have a subset of the properties of the corresponding entity.
 * 
 * <p>
 * We need them, because serializing entities to Flex is problematic. Because of the bi-directional
 * associations, o whole tree of objects is sent when we want to send, for example, only one entity.
 * This whole tree can be even the entire database, depending on how the data model looks like. To avoid
 * these problems, either dtos are used, or smarter app servers, like LiveCycle DS or Granite DS.
 * 
 * @author Cristi
 * @author Cristina
 * 
 * @flowerModelElementId _jTr6AFb-EeGL3vi-zPhopA
 */
public class Dto {

	/**
	 * @flowerModelElementId _UaHy0FcAEeGL3vi-zPhopA
	 */
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Dto() {		
	}

	public Dto(long id) {
		super();
		this.id = id;
	}
	
}