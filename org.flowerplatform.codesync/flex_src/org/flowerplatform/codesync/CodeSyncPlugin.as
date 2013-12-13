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
	import org.flowerplatform.codesync.action.AddNewRelationAction;
	import org.flowerplatform.codesync.action.ExpandCompartmentActionProvider;
	import org.flowerplatform.codesync.properties.remote.StringSelectedItem;
	import org.flowerplatform.codesync.regex.RegexUtils;
	import org.flowerplatform.codesync.regex.remote.MacroRegexDto;
	import org.flowerplatform.codesync.regex.remote.ParserRegexDto;
	import org.flowerplatform.codesync.regex.remote.RegexActionDto;
	import org.flowerplatform.codesync.regex.remote.RegexIndexDto;
	import org.flowerplatform.codesync.regex.remote.RegexMatchDto;
	import org.flowerplatform.codesync.regex.remote.RegexSelectedItem;
	import org.flowerplatform.codesync.regex.remote.RegexSubMatchDto;
	import org.flowerplatform.codesync.regex.remote.command.RegexCommand;
	import org.flowerplatform.codesync.remote.CodeSyncAction;
	import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
	import org.flowerplatform.codesync.remote.RelationDescriptor;
	import org.flowerplatform.codesync.views.loaded_descriptors.LoadedDescriptorsView;
	import org.flowerplatform.codesync.views.loaded_descriptors.LoadedDescriptorsViewProvider;
	import org.flowerplatform.codesync.wizard.WizardUtils;
	import org.flowerplatform.codesync.wizard.remote.MDADependency;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.CompoundServerCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.controller.ComposedControllerProviderFactory;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.VectorActionProvider;
	import org.flowerplatform.properties.PropertiesPlugin;
	import org.flowerplatform.properties.property_renderer.DropDownListPropertyRenderer;
	
	/**
	 * @author Cristian Spiescu
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncPlugin extends AbstractFlowerFlexPlugin {
		
		public static const CODE_SYNC_TYPE_CATEGORY_TOP_LEVEL:String = "topLevel";
		public static const CODE_SYNC_TYPE_CATEGORY_DONT_NEED_LOCATION:String = "dontNeedLocation";
		
		public static const CONTEXT_INITIALIZATION_TYPE:String = "initializationType";
		
		public var codeSyncTreeActionProvider:CodeSyncTreeActionProvider = new CodeSyncTreeActionProvider();
		
		protected var codeSyncElementDescriptors:ArrayCollection;
		
		protected var relationDescriptors:ArrayCollection;
		
		/**
		 * String (CodeSync type) to ArrayCollection of CodeSyncElementDescriptor.
		 * "" -> top level elements.
		 * 
		 * @see computeAvailableChildrenForCodeSyncType()
		 */
		public var availableChildrenForCodeSyncType:Dictionary;
		
		public var regexUtils:RegexUtils = new RegexUtils();
		public var wizardUtils:WizardUtils = new WizardUtils();
		
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
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new LoadedDescriptorsViewProvider());
			
			// register PropertiesPlugin Renderer
			PropertiesPlugin.getInstance().propertyRendererClasses["DropDownListWithRegexActions"] = new FactoryWithInitialization
				(DropDownListPropertyRenderer, {
					requestDataProviderHandler: function (callbackObject:Object, callbackFunction:Function):void {
						CommunicationPlugin.getInstance().bridge.sendObject(
							new InvokeServiceMethodServerCommand("regexService", "getRegexActions", 
								null, callbackObject, callbackFunction));						
					},
					
					labelFunction: function (object:Object):String {
						return RegexActionDto(object).name + " - " + RegexActionDto(object).description;
					},
					
					getItemIndexFromList: function (item:Object, list:ArrayCollection):int {
						if (item != null) {
							for (var i:int = 0; i < list.length; i++) {
								var dto:RegexActionDto = RegexActionDto(list.getItemAt(i));
								if (dto.name == RegexActionDto(item).name) {
									return i;
								}
							}
						}
						return -1;
					}
				});
		}
		
		override public function start():void {
			super.start();
			
			EditorModelPlugin.getInstance().notationDiagramActionProviders.push(new AddNewCodeSyncElementActionProvider());
			EditorModelPlugin.getInstance().notationDiagramActionProviders.push(codeSyncTreeActionProvider);
			EditorModelPlugin.getInstance().notationDiagramActionProviders.push(new ExpandCompartmentActionProvider());
		}
		
		/**
		 * @author Cristian Spiescu
		 * @author Mircea Negreanu
		 */
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(CodeSyncElementDescriptor);
			registerClassAliasFromAnnotation(RelationDescriptor);
			
			registerClassAliasFromAnnotation(CodeSyncAction);
			registerClassAliasFromAnnotation(DiffTreeNode);
			registerClassAliasFromAnnotation(DiffContextMenuEntry);
			registerClassAliasFromAnnotation(DiffActionEntry);
			
			// add the Descriptor too
			registerClassAliasFromAnnotation(StringSelectedItem);
						
			registerClassAliasFromAnnotation(RegexCommand);
			registerClassAliasFromAnnotation(MacroRegexDto);
			registerClassAliasFromAnnotation(ParserRegexDto);
			registerClassAliasFromAnnotation(RegexActionDto);
			registerClassAliasFromAnnotation(RegexSubMatchDto);			
			registerClassAliasFromAnnotation(RegexIndexDto);			
			registerClassAliasFromAnnotation(RegexMatchDto);
			registerClassAliasFromAnnotation(RegexSelectedItem);

			registerClassAliasFromAnnotation(MDADependency);
		}
		
		/**
		 * @author Mariana Gheorghe
		 * @author Mircea Negreanu
		 */
		override public function handleConnectedToServer():void {
			if (CommunicationPlugin.getInstance().firstWelcomeWithInitializationsReceived) {
				return;
			}
			
			// split out the loading of the descriptors from server
			// so that it can also be called from other parts
			loadDescriptorsFromServer();
		}
		
		/**
		 * @author Mariana Gheorge
		 * @author Mircea Negreanu
		 */
		public function loadDescriptorsFromServer():void {
			var command:CompoundServerCommand = new CompoundServerCommand();
			command.append(new InvokeServiceMethodServerCommand(
				"codeSyncDiagramOperationsService",
				"getCodeSyncElementDescriptors", 
				null,
				this,
				setCodeSyncElementDescriptorsCallback));
			command.append(new InvokeServiceMethodServerCommand(
				"codeSyncDiagramOperationsService",
				"getRelationDescriptors", 
				null,
				this,
				setRelationDescriptorsCallback));
			CommunicationPlugin.getInstance().bridge.sendObject(command);
		}
		
		/**
		 * @author Cristian Spiescu
		 * @author Mariana Gheorghe
		 * @author Mircea Negreanu
		 */
		protected function setCodeSyncElementDescriptorsCallback(codeSyncElementDescriptors:ArrayCollection):void {
			this.codeSyncElementDescriptors = codeSyncElementDescriptors;
			
			computeAvailableChildrenForCodeSyncType();
			// TODO CS/JS: we could add a new field in the descriptor (to get rid of the map)? or do the processing in the
			// constructor?
			var topLevelDescriptors:ArrayCollection = availableChildrenForCodeSyncType[""];
			if (topLevelDescriptors != null) {
				// TODO CS/JS: hardcoded diagram type
				registerControllerProviderFactories("classDiagram", topLevelDescriptors);				
			}
			
			// TEMPOARARY
			// Notification to the LoadedDescriptorsView that new descriptors have been loaded
			if (LoadedDescriptorsView.descriptorsView != null) {
				LoadedDescriptorsView.descriptorsView.notificationDescriptorsLoaded();
			}
		}
		
		/**
		 * Iterates the list of <code>CodeSyncElementDescriptor</code>s and computes a map from
		 * codeSyncType -> available children codeSyncTypes.
		 */
		private function computeAvailableChildrenForCodeSyncType():void {
			availableChildrenForCodeSyncType = new Dictionary();
			for each (var descriptor:CodeSyncElementDescriptor in codeSyncElementDescriptors) {
				for each (var codeSyncTypeCategory:String in descriptor.codeSyncTypeCategories) {
					if (codeSyncTypeCategory == CODE_SYNC_TYPE_CATEGORY_TOP_LEVEL) {
						// top level elements
						addChildCodeSyncTypeCategoryForParent("", descriptor);
					} else {
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
		
		public function getCodeSyncElementDescriptor(codeSyncType:String):CodeSyncElementDescriptor {
			for each (var descriptor:CodeSyncElementDescriptor in codeSyncElementDescriptors) {
				if (descriptor.codeSyncType == codeSyncType) {
					return descriptor;
				}
			}
			return null;
		}
		
		// TODO CS/JS in web mode: would it make sens that users can have their own?
		private function registerControllerProviderFactories(viewTypePrefix:String, childDescriptorsForCurrentLevel:ArrayCollection):void  {
			for each (var childDescriptor:CodeSyncElementDescriptor in childDescriptorsForCurrentLevel) {
				var standardControllerProviderFactory:ComposedControllerProviderFactory = EditorModelPlugin.getInstance().standardControllerProviderFactories[childDescriptor.standardDiagramControllerProviderFactory];
				var viewType:String = viewTypePrefix + "." + childDescriptor.codeSyncType;
				if (standardControllerProviderFactory != null) {
					// the current descriptor wants a standard ContrProvFact
					EditorModelPlugin.getInstance().composedControllerProviderFactories[viewType] = standardControllerProviderFactory;
				}
				var children:ArrayCollection = availableChildrenForCodeSyncType[childDescriptor.codeSyncType];
				if (children != null) {
					registerControllerProviderFactories(viewType, children);
				}
			}
		}
		
		/**
		 * @author Cristian Spiescu
		 * @author Mariana Gheorghe
		 * @author Mircea Negreanu
		 */
		protected function setRelationDescriptorsCallback(result:ArrayCollection):void {
			relationDescriptors = result;
			var addNewRelationActionProvider:VectorActionProvider = new VectorActionProvider();
			for each (var d:RelationDescriptor in relationDescriptors) {
				var a:AddNewRelationAction = new AddNewRelationAction(d);
				addNewRelationActionProvider.getActions(null).push(a);
			}
			EditorModelPlugin.getInstance().notationDiagramActionProviders.push(addNewRelationActionProvider);
			
			// TEMPORARY
			// notification to the LoadDescriptorsView that new descriptors were loaded from db
			if (LoadedDescriptorsView.descriptorsView != null) {
				LoadedDescriptorsView.descriptorsView.notificationDescriptorsLoaded();
			}
		}	
		
		public function getCodeSyncTypeFromView(potentialView:Object):String {
			if (!(potentialView is View)) {
				return null;
			}
			var view:View = View(potentialView);
			var lastIndexOfDot:int = view.viewType.lastIndexOf(".");
			if (lastIndexOfDot <= 0) {
				// a view without . => not coming from a codesync element
				return null;
			}
			return view.viewType.substr(lastIndexOfDot + 1);
		}
		
		/**
		 * Returns all the codeSyncElementDescriptors.
		 * <p>Needed by the Loaded Descriptors view</p>
		 * 
		 * @author Mircea Negreanu
		 */
		public function getCodeSyncElementDescriptors():ArrayCollection {
			return codeSyncElementDescriptors;
		}

		/**
		 * Returns all the relationDescriptors
		 * <p>Needed by the Loaded Descriptors view</p>
		 * 
		 * @author Mircea Negreanu
		 */
		public function getRelationDescriptors():ArrayCollection {
			return relationDescriptors;
		}
	}
}
