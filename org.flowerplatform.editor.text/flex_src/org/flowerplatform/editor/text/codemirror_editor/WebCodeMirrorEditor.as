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
package org.flowerplatform.editor.text.codemirror_editor {
	
	import flash.external.ExternalInterface;
	
	import org.flowerplatform.flexutil.iframe.FlowerIFrame;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class WebCodeMirrorEditor extends FlowerIFrame implements ICodeMirrorEditor {
		
		public function WebCodeMirrorEditor(id:String = null) {
			super(id);
		}
		
		public function load(url:String):void {
			source = url;
		}
		
		public function addCallbackHandler(name:String, callback:Function):void {
			ExternalInterface.addCallback(name, callback);
		}
		
		public function callJavaScriptMethod(method:String, callback:Function = null, ... arguments):void {
			callIFrameFunction(method, arguments != null ? [].concat(arguments) : null, callback);
		}
		
		public function addViewCompleteHandler(handler:Function):void {
			addEventListener("frameLoad", handler);
		}
		
	}
}