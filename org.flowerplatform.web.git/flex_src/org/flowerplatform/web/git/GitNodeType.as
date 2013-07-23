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
package org.flowerplatform.web.git {
	
	/**
	 *	@author Cristina Constantinescu
	 */ 
	public class GitNodeType {
		
		public static const NODE_TYPE_GIT_REPOSITORIES:String = "gitrp";
		
		public static const NODE_TYPE_REPOSITORY:String = "rp";
		
		public static const NODE_TYPE_LOCAL_BRANCHES:String = "lbrs";
		public static const NODE_TYPE_REMOTE_BRANCHES:String = "rbrs";
		public static const NODE_TYPE_TAGS:String = "tgs";
		public static const NODE_TYPE_REMOTES:String = "rmts";
		public static const NODE_TYPE_WDIRS:String = "wdirs";
		
		public static const NODE_TYPE_LOCAL_BRANCH:String = "lbr";
		public static const NODE_TYPE_REMOTE_BRANCH:String = "rbr";
		public static const NODE_TYPE_TAG:String = "tg";
		
		public static const NODE_TYPE_REMOTE:String = "rmt";
		
		public static const NODE_TYPE_WDIR:String = "wdir";
		
		public static const NODE_TYPE_FILE:String = "fl";
		public static const NODE_TYPE_FOLDER:String = "fld";
		
	}
}