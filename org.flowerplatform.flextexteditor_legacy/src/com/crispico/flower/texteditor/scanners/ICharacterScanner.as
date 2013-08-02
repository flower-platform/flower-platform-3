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
package com.crispico.flower.texteditor.scanners {
	
	import com.crispico.flower.texteditor.Document;
	
	/**
	 * 
	 */
	public interface ICharacterScanner {
		
		function setRange(document:Document, offset:int, length:int):void;
		function unread():int;
		function read():int;
		function getOffset():int;
		function setOffset(value:int):void;
		function getEnd():int;
		function setEnd(value:int):void;
		
		function getDocument():Document;
		function setDocument(value:Document):void;
		function getLineStart(position:int):int;
		function getLineEnd(position:int):int;
	}
}