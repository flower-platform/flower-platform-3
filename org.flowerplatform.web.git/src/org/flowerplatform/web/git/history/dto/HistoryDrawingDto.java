package org.flowerplatform.web.git.history.dto;

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
