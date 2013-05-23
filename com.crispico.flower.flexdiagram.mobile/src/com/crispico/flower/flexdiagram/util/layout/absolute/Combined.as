// CS-VC
/**
 * IPRNG
 * 
 * An interface for classes that can be used a pseudo-random number generators
 * Copyright (c) 2007 Henri Torgemane
 * 
 * See LICENSE.txt for full license information.
 */
package com.crispico.flower.flexdiagram.util.layout.absolute
{
	import flash.utils.ByteArray;
		
	public interface Combined {
		function enable():uint;
		function init(param:ByteArray):void;
		function decorate():uint;
		function dispose():void;
	}
}