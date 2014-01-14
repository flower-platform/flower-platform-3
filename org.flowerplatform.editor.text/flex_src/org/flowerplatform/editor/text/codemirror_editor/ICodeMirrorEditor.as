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
		
	/**
	 * Web and mobile code mirror editors must implement this interface 
	 * to provide different behavior when communicating with javascript.
	 * 
	 * <p>
	 * Note:
	 * Mobile editor uses <code>StageWebView</code> and 
	 * doesn't support <code>ExternalInterface</code>.
	 * 
	 * @see CodeMirrorEditorFrontend
	 * @author Cristina Constantinescu
	 */
	public interface ICodeMirrorEditor {
		
		function load(url:String):void;		
		
		function callJavaScriptMethod(method:String, callback:Function = null, ... arguments):void;		
		function addCallbackHandler(name:String, callback:Function):void;		
		
		function addViewCompleteHandler(handler:Function):void;
		
		function dispose():void;
	}
}