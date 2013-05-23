// CS-VC
/**
 * SimpleIVMode
 * 
 * A convenience class that automatically places the IV
 * at the beginning of the encrypted stream, so it doesn't have to
 * be handled explicitely.
 * Copyright (c) 2007 Henri Torgemane
 * 
 * See LICENSE.txt for full license information.
 */
package com.crispico.flower.flexdiagram.util.layout
{
	import com.crispico.flower.flexdiagram.util.layout.util.StoreEntry;
	
	import flash.utils.ByteArray;
	import flash.utils.getQualifiedClassName;
	
	public class BaseSequential
	{
		protected var parentLayout:BaseRect;
		
		public function BaseSequential(composedLayout:BaseCirc) {
			parentLayout = new BaseRect(composedLayout);
		}
		
		public function getMaxRecyclableElements():uint {
			return parentLayout.getRecyclableElements();
		}
		
		public function dispose():void {
			parentLayout.dispose();
			parentLayout = null;
			StoreEntry.clear();
		}
		
		public function filterVisibleObjects(src:ByteArray):void {
			parentLayout.updateUpperLeftElement(src);
			var tmp:ByteArray = new ByteArray;
			tmp.writeBytes(parentLayout.overlapImage);
			tmp.writeBytes(src);
			src.position=0;
			src.writeBytes(tmp);
		}
		
		public function getSortexByIndex(src:ByteArray):void {
			var tmp:ByteArray = new ByteArray;
			tmp.writeBytes(src, 0, getMaxRecyclableElements());
			parentLayout.overlapImage = tmp;
			tmp = new ByteArray;
			tmp.writeBytes(src, getMaxRecyclableElements());
			parentLayout.updateBottomRightElement(tmp);
			src.length=0;
			src.writeBytes(tmp);
		}
	}
}