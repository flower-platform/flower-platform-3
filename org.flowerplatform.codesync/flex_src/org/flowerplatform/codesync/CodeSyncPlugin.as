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
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.codesync.action.AddNewCodeSyncElementActionProvider;
	import org.flowerplatform.codesync.remote.CodeSyncAction;
	import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristian Spiescu
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncPlugin extends AbstractFlowerFlexPlugin {
		
		public var codeSyncTreeActionProvider:CodeSyncTreeActionProvider = new CodeSyncTreeActionProvider();
		
		protected var codeSyncElementDescriptors:ArrayCollection;
		
		/**
		 * String (CodeSync type) to CodeSyncElementDescriptor.
		 * 
		 * @see computeAvailableChildrenForCodeSyncType()
		 */
		public var availableChildrenForCodeSyncType:Dictionary;
		
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
		
		override public function start():void {
			super.start();
			
			EditorModelPlugin.getInstance().notationDiagramActionProviders.push(new AddNewCodeSyncElementActionProvider());
			EditorModelPlugin.getInstance().notationDiagramActionProviders.push(codeSyncTreeActionProvider);
		}
		
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(CodeSyncElementDescriptor);
			
			registerClassAliasFromAnnotation(CodeSyncAction);
			registerClassAliasFromAnnotation(DiffTreeNode);
			registerClassAliasFromAnnotation(DiffContextMenuEntry);
			registerClassAliasFromAnnotation(DiffActionEntry);
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		override public function handleConnectedToServer():void {
			if (CommunicationPlugin.getInstance().firstWelcomeWithInitializationsReceived) {
				return;
			}
			
			var command:InvokeServiceMethodServerCommand = new InvokeServiceMethodServerCommand(
				"codeSyncDiagramOperationsService",
				"getCodeSyncElementDescriptors", 
				null,
				this,
				setCodeSyncElementDescriptorsCallback);
			CommunicationPlugin.getInstance().bridge.sendObject(command);
		}
		
		protected function setCodeSyncElementDescriptorsCallback(codeSyncElementDescriptors:ArrayCollection):void {
			this.codeSyncElementDescriptors = codeSyncElementDescriptors;
			
			computeAvailableChildrenForCodeSyncType();
		}
		
		/**
		 * Iterates the list of <code>CodeSyncElementDescriptor</code>s and computes a map from
		 * codeSyncType -> available children codeSyncTypes.
		 */
		private function computeAvailableChildrenForCodeSyncType():void {
			availableChildrenForCodeSyncType = new Dictionary();
			for each (var descriptor:CodeSyncElementDescriptor in codeSyncElementDescriptors) {
				if (descriptor.codeSyncTypeCategories.length == 0) {
					// top level elements
					addChildCodeSyncTypeCategoryForParent("", descriptor);
				} else {
					for each (var codeSyncTypeCategory:String in descriptor.codeSyncTypeCategories) {
						for each (var parentCodeSyncType:String in getParentsForChildCodeSyncTypeCategory(codeSyncTypeCategory)) {
							addChildCodeSyncTypeCategoryForParent(parentCodeSyncType, descriptor);	
						}
					}
				}
			}
		}
		
		private function addChildCodeSyncTypeCategoryForParent(parent:String, child:CodeSyncElementDescriptor):void {
			if (availableChildrenForCodeSyncType[parent] == null) {
				availableChildrenForCodeSyncType[parent] = new ArrayCollection();
			}
			availableChildrenForCodeSyncType[parent].addItem(child);
		}
		
		private function getParentsForChildCodeSyncTypeCategory(childCodeSyncTypeCategory:String):ArrayCollection {
			var parents:ArrayCollection = new ArrayCollection();
			for each (var descriptor:CodeSyncElementDescriptor in codeSyncElementDescriptors) {
				if (descriptor.childrenCodeSyncTypeCategories.contains(childCodeSyncTypeCategory)) {
					parents.addItem(descriptor.codeSyncType);
				}
			}
			return parents;
		}
		
	}
}
