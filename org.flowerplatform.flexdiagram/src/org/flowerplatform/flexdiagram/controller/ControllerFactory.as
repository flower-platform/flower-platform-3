package org.flowerplatform.flexdiagram.controller {
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	public class ControllerFactory {
		
		public var generator:Class;
		public var properties:Object = null;
		
		public function ControllerFactory(generator:Class, properties:Object = null) {
			this.generator = generator;
			this.properties = properties;
		}
		
		public function newInstance(diagramShel:DiagramShell):* {
			var instance:Object = new generator(diagramShel);
			
			if (properties != null)
			{
				for (var p:String in properties)
				{
					instance[p] = properties[p];
				}
			}
			
			return instance;
		}
	}
}