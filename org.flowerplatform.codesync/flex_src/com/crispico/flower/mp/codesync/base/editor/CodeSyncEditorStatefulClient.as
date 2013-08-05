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
package com.crispico.flower.mp.codesync.base.editor {
	import com.crispico.flower.flexdiagram.action.ActionContext;
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;
	
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	
	/**
	 * @author Mariana
	 */
	public class CodeSyncEditorStatefulClient extends EditorStatefulClient {
	
		public var diffTreeStatefulClient:Object = new Object();
		
		override public function getStatefulServiceId():String {
			return "codeSyncEditorStatefulService";
		}
		
		[RemoteInvocation]
		public function updateNode(treeType:int, path:ArrayCollection, newNode:TreeNode, expandNode:Boolean = false, collapseNode:Boolean = false, selectNode:Boolean = false):void {			
			diffTreeStatefulClient[treeType].updateNode(path, newNode, collapseNode, selectNode);
		}
		
		///////////////////////////////////////////////////////////////
		// Wrappers for service methods
		///////////////////////////////////////////////////////////////
		
		public function openNode(node:IEventDispatcher, context:ActionContext):void {
			invokeServiceMethod("openNode", [node, context]); 
		}
		
		public function executeDiffAction(actionType:int, diffIndex:int, node:DiffTreeNode, context:ActionContext):void {
			invokeServiceMethod("executeDiffAction", [editableResourcePath, actionType, diffIndex, node, context]);
		}
		
		public function synchronize():void {
			invokeServiceMethod("synchronize", [editableResourcePath]);
		}
		
		public function applySelectedActions():void {
			invokeServiceMethod("applySelectedActions", [editableResourcePath, true]);
		}
		
		public function cancelSelectedActions():void {
			invokeServiceMethod("cancelSelectedActions", [editableResourcePath, true]);
		}
		
	}
}