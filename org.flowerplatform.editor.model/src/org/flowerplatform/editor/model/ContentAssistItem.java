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
package org.flowerplatform.editor.model;

/**
 * @author Mariana Gheorghe
 */
public class ContentAssistItem {

	private Object item;
	private String mainString;
	private String extraString;
	private String iconUrl;
	
	public ContentAssistItem(Object item, String mainString,
			String extraString, String iconUrl) {
		super();
		this.item = item;
		this.mainString = mainString;
		this.extraString = extraString;
		this.iconUrl = iconUrl;
	}

	public Object getItem() {
		return item;
	}
	
	public void setItem(Object item) {
		this.item = item;
	}
	
	public String getMainString() {
		return mainString;
	}
	
	public void setMainString(String mainString) {
		this.mainString = mainString;
	}
	
	public String getExtraString() {
		return extraString;
	}
	
	public void setExtraString(String extraString) {
		this.extraString = extraString;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}
	
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
}
