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
package org.flowerplatform.web.svn.remote.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Badila
 */

@SuppressWarnings("unused")
public class FileDto {
	
		private String pathFromRoot;
		
		private String label;

		private ArrayList<String> imageUrls;

		private String status;
		
		public String getPathFromRoot() {
			return pathFromRoot;
		}

		public void setPathFromRoot(String path) {
			this.pathFromRoot = path;
		}
		
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
		
		public ArrayList<String> getImageUrls() {
			return imageUrls;
		}

		public void setImageUrls(ArrayList<String> list) {
			this.imageUrls = list;
		}
		
		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
}