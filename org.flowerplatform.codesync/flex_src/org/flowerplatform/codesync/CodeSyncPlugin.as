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
package org.flowerplatform.codesync {
	import com.crispico.flower.mp.codesync.base.action.DiffActionEntry;
	import com.crispico.flower.mp.codesync.base.action.DiffContextMenuEntry;
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;
	import com.crispico.flower.mp.codesync.base.editor.CodeSyncEditorDescriptor;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.codesync.remote.CodeSyncAction;
	import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class CodeSyncPlugin extends AbstractFlowerFlexPlugin {
		
		public var codeSyncTreeActionProvider:CodeSyncTreeActionProvider = new CodeSyncTreeActionProvider();
		
		protected var codeSyncElementDescriptors:ArrayCollection;
		
		protected static var INSTANCE:CodeSyncPlugin;
		
		public static function getInstance():CodeSyncPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			var editorDescriptor:EditorDescriptor = new CodeSyncEditorDescriptor();
			EditorPlugin.getInstance().editorDescriptors.push(editorDescriptor);
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(editorDescriptor);
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(CodeSyncElementDescriptor);
			
			registerClassAliasFromAnnotation(CodeSyncAction);
			registerClassAliasFromAnnotation(DiffTreeNode);
			registerClassAliasFromAnnotation(DiffContextMenuEntry);
			registerClassAliasFromAnnotation(DiffActionEntry);
		}
		
		override protected function registerMessageBundle():void {
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		override public function handleConnectedToServer():void {
			if (!CommunicationPlugin.getInstance().firstWelcomeWithInitializationsReceived) {
				var command:InvokeServiceMethodServerCommand = new InvokeServiceMethodServerCommand(
					"codeSyncDiagramOperationsService",
					"getCodeSyncElementDescriptors", 
					null,
					this,
					setCodeSyncElementDescriptors);
				CommunicationPlugin.getInstance().bridge.sendObject(command);
			}
		}
		
		protected function setCodeSyncElementDescriptors(codeSyncElementDescriptors:ArrayCollection):void {
			this.codeSyncElementDescriptors = codeSyncElementDescriptors;	
		}
	}
}