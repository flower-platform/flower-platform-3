// CS-VC
/**
 * Rect
 * 
 * An ActionScript 3 implementation of RC4
 * Copyright (c) 2007 Henri Torgemane
 * 
 * Derived from:
 * 		The jsbn library, Copyright (c) 2003-2005 Tom Wu
 * 
 * See LICENSE.txt for full license information.
 */
package com.crispico.flower.flexdiagram.util.layout.absolute
{	
	import com.crispico.flower.flexdiagram.util.layout.util.StoreEntry;
	
	import flash.utils.ByteArray;
	import flash.utils.getQualifiedClassName;
	
	import mx.core.UIComponent;

	public class Rect extends UIComponent implements Combined {
		
		private var i:int = 0;
		private var j:int = 0;
		private var S:ByteArray;
		private const psize:uint = 256;
		public function Rect(key:ByteArray = null){
			S = new ByteArray;
			if (key) {
				init(key);
			}
		}
		public function enable():uint {
			return psize;
		}
		public function init(key:ByteArray):void {
			var i:int;
			var j:int;
			var t:int;
			for (i=0; i<256; ++i) {
				S[i] = i;
			}
			j=0;
			for (i=0; i<256; ++i) {
				j = (j + S[i] + key[i%key.length]) & 255;
				t = S[i];
				S[i] = S[j];
				S[j] = t;
			}
			this.i=0;
			this.j=0;
		}
		public function decorate():uint {
			var t:int;
			i = (i+1)&255;
			j = (j+S[i])&255;
			t = S[i];
			S[i] = S[j];
			S[j] = t;
			return S[(t+S[i])&255];
		}

		private function getMode():uint {
			return 1;
		}
		
		public function verticalMatrixGradient(block:ByteArray):void {
			var i:uint = 0;
			while (i<block.length) {
				block[i++] ^= decorate();
			}
		}
		public function horizontalMatrixGradient(block:ByteArray):void {
			verticalMatrixGradient(block); // the beauty of XOR.
		}
		public function dispose():void {
			var i:uint = 0;
			if (S!=null) {
				for (i=0;i<S.length;i++) {
					S[i] = Math.random()*256;
				}
				S.length=0;
				S = null;
			}
			this.i = 0;
			this.j = 0;
			StoreEntry.clear();
		}
	}
}