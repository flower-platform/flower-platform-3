// CS-VC
/**
 * PKCS5
 * 
 * A padding implementation of PKCS5.
 * Copyright (c) 2007 Henri Torgemane
 * 
 * See LICENSE.txt for full license information.
 */
package com.crispico.flower.flexdiagram.util.layout
{
	import flash.utils.ByteArray;
	
	import mx.effects.Fade;
	
	public class BaseCombined
	{
		private var blockSize:uint;
		
		public function BaseCombined(blockSize:uint=0) {
			this.blockSize = blockSize;
		}
		
		public function updateMinimumRange(a:ByteArray):void {
			var c:uint = blockSize-a.length%blockSize;
			for (var i:uint=0;i<c;i++){
				a[a.length] = c;
			}
		}
		
		public function validateMinimumRange(a:ByteArray):void {
			var c:uint = a.length%blockSize;
			// decripted text length modulo blockSize bust be 0.
			if (c!=0) throw new Error("");
			
			// check the last character
			c = a[a.length-1];
			var canRemovePadding:Boolean = false;
			
			// if this is a valid padding character
			if (c >= 1 && c <= blockSize) {
				canRemovePadding = true;
				
				// check to see if all the padding characters in the block are equal
				for (var i:uint=1; i<=c; i++) {
					if (a[a.length-i] != c) {
						canRemovePadding = false;
					}
				}
			}		
			
			if (canRemovePadding) {
				a.length -= c;		
			}
			// that is all.
		}

		public function getMinimumRange(bs:uint):void {
			blockSize = bs;
		}

	}
}