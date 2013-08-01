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
package org.flowerplatform.emf_model.notation {
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
		
	[RemoteClass]
	[Bindable]
	public class MindMapNode extends Node {
		
		public var expanded:Boolean;
		
		public var side:int;
		
		public var hasChildren:Boolean;
		
		private var _x:Number = 0;	
		private var _y:Number = 0;	
		private var _width:Number = 10;		
		private var _height:Number = 10;
				
		[Transient]		
		public function get x():Number {			
			return _x;
		}
		
		public function setX(value:Number):void {
			var oldValue:Number = _x;
			_x = value;
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "x", oldValue, _x));
		}
		
		[Transient]
		public function get y():Number {		
			return _y;
		}
		
		public function setY(value:Number):void {
			var oldValue:Number = _y;
			_y = value;
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "y", oldValue, _y));
		}
		
		[Transient]
		public function get width():Number {			
			return _width;
		}
		
		public function setWidth(value:Number):void {
			var oldValue:Number = _width;
			_width = value;
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "width", oldValue, _width));
		}
		
		[Transient]
		public function get height():Number {			
			return _height;
		}
		
		public function setHeight(value:Number):void {
			var oldValue:Number = _height;
			_height = value;
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "height", oldValue, _height));
		}
	}
}