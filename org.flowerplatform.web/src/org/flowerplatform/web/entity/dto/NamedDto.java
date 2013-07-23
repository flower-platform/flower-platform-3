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