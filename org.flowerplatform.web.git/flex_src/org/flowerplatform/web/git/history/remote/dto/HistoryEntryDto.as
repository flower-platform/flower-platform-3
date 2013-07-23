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
package org.flowerplatform.web.git.history.remote.dto {
	import mx.collections.ArrayCollection;

	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class HistoryEntryDto {
		
		public var id:String;
		public var shortId:String;
		
		public var message:String;
		
		public var author:String;		
		public var authorEmail:String;		
		public var authoredDate:Date;
		
		public var committer:String;
		public var committerEmail:String;
		public var committeredDate:Date;
		
		public var specialMessage:String;
				
		public var drawings:ArrayCollection;
		
		public var fileDiffs:ArrayCollection;
		
		public var commitMessage:HistoryCommitMessageDto;
		
	}
}