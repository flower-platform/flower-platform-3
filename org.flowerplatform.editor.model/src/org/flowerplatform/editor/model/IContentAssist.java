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

import java.util.List;
import java.util.Map;

/**
 * @author Mariana Gheorghe
 */
public interface IContentAssist {
	
	public static String TYPE = "type";
	
	public static String RESOURCE = "project";

	/**
	 * @param context used to set the search scope
	 * @param pattern used as a prefix for the search
	 * @return list of matches as fully qualified names
	 */
	List<ContentAssistItem> findMatches(Map<String, Object> context, String pattern);
	
}
