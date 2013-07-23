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
package org.flowerplatform.editor.text.remote {
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.editor.text.TextEditorFrontend;

	/**
	 * @flowerModelElementId _YC7YMAJ4EeKGLqam5SXwYg
	 */
	public class TextEditorStatefulClient extends EditorStatefulClient {
	
		protected var statefulServiceId:String;
		
		/**
		 * @flowerModelElementId _VMeLoAJ8EeKGLqam5SXwYg
		 */
		public override function getStatefulServiceId():String {	
			return statefulServiceId;
		}
		
		public function TextEditorStatefulClient(statefulServiceId:String) {
			this.statefulServiceId = statefulServiceId;
		}
		
		/**
		 * @flowerModelElementId _XZRk8AJ9EeKGLqam5SXwYg
		 */
		override protected function copyLocalDataFromExistingEditorToNewEditor(existingEditor:EditorFrontend, newEditor:EditorFrontend):void	{
			super.copyLocalDataFromExistingEditorToNewEditor(existingEditor, newEditor);
			TextEditorFrontend(newEditor).setContent(
				TextEditorFrontend(existingEditor).syntaxTextEditor.doc.text);
		}
		
		/**
		 * @flowerModelElementId _NJ22IgcIEeK49485S7r3Vw
		 */
		override protected function areLocalUpdatesAppliedImmediately():Boolean	{
			return true;
		}
		
		/**
		 * Update the dirty state of the editors to ensure that text updates
		 * will be dispatched immediately if the resource is not dirty.
		 */ 
		public function updateDirtyState(editorInput:Object, dirtyState:Boolean):void {
			for each (var editor:TextEditorFrontend in editorFrontends) {
				editor.updateDirtyState(dirtyState);
			}
		}
		
		///////////////////////////////////////////////////////////////
		// Wrappers for service methods
		///////////////////////////////////////////////////////////////
		
		/**
		 * @flowerModelElementId _1PSw4DfPEeKY0qaVeMRK2Q
		 * @author Daniela
		 */
		public function service_selectRangeFor(category:String, searchString:String):void {
			invokeServiceMethod("selectRangeFor", [editableResourcePath, category, searchString]);
		}
		
		///////////////////////////////////////////////////////////////
		// @RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		
//		/**
//		 * @flowerModelElementId _aNND0khHEeKn-dlTSOkszw
//		 */
//		[RemoteInvocation]
//		public function selectRange(offset:int, length:int):void {
//			var activeView:UIComponent = WebPlugin.getInstance().workbench.activeViewList.getActiveView();
//			var activeTextEditorFrontend:TextEditorFrontend = null;
//			if (activeView is TextEditorFrontend && TextEditorFrontend(activeView).editorStatefulClient == this) {
//				// use the current view if it is the right one
//				activeTextEditorFrontend = TextEditorFrontend(activeView);
//			} else {
//				activeTextEditorFrontend = editorFrontends[0];
//				WebPlugin.getInstance().workbench.activeViewList.setActiveView(activeTextEditorFrontend);
//			}
//			
//			activeTextEditorFrontend.syntaxTextEditor.textDisplay.setFocus(); // Without it scroll and select are not processed well.
//			activeTextEditorFrontend.syntaxTextEditor.selectRange(offset, offset + length);
//			activeTextEditorFrontend.syntaxTextEditor.scrollToRange(offset, offset + length);
//		}
		
		
	}
}