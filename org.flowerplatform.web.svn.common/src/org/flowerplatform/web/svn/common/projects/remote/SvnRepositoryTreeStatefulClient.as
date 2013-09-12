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
package org.flowerplatform.web.svn.common.projects.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 * @author Gabriela Murgoci
	 */
	public class SvnRepositoryTreeStatefulClient extends GenericTreeStatefulClient {
		
		public static const WORKING_DIRECTORY_KEY:String = "workingDirectory";
		public static const ORGANIZATION_KEY:String = "organization";
		
		public function SvnRepositoryTreeStatefulClient() {
			super();
			
			clientIdPrefix = "Svn Repository";
			requestDataOnSubscribe = false;			
			statefulServiceId = "svnRepositoryTreeStatefulService";
		}		
	}
}