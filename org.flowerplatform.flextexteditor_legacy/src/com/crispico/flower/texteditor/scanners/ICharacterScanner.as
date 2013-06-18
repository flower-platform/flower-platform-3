package com.crispico.flower.texteditor.scanners {
	
	import com.crispico.flower.texteditor.Document;
	
	/**
	 * @flowerModelElementId _MtzrsN3QEeCGOND4c9bKyA
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