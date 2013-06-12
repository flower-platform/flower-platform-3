package com.crispico.flower.util.layout.persistence {
	import org.flowerplatform.flexutil.layout.LayoutData;
	
	/**
	 * Interface that needs to be implemented in order to provide serialization/deserialization support for windows layout.
	 * Objects that implements this interface must provide functionality for the following methods:
	 * <ul>
	 * 	<li> serialize() - serializes a given <code>LayoutData</code> and returns corresponding string.
	 * 	<li> deserialize() - deserialize a given string and returns corresponding <code>LayoutData</codE>
	 * </ul>
	 */
	public interface ILayoutSerializer {
		
		   function serialize(layoutData:LayoutData):String;
		
		   function deserialize(data:String):LayoutData;
	}
	
}