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
package org.flowerplatform.web.git.history.remote.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class HistoryDrawingDto {
	
	public static final String DRAW_LINE = "drawLine";
	
	public static final String DRAW_DOT = "drawDot";
	
	private String type;
	
	private List<Object> params;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}
	
	public HistoryDrawingDto(){
		
	}

	public HistoryDrawingDto(String type, Object... params) {		
		this.type = type;
		
		this.params = new ArrayList<Object>();
		for (Object param : params) {
			this.params.add(param);
		}
	}	
	
}