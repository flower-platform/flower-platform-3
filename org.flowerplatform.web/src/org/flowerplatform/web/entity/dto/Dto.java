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
 * 
 */
public class Dto {

	/**
	 * 
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