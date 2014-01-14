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
package org.flowerplatform.editor.model.remote {
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.action.AddNewElementAction;
	import org.flowerplatform.editor.model.remote.scenario.ScenarioTreeStatefulClient;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class NotationDiagramEditorStatefulClient extends DiagramEditorStatefulClient {
		
		public var scenarioTreeStatefulClient:ScenarioTreeStatefulClient;
		
		public const codeSyncOperationsServiceId:String = "codeSyncOperationsService";
		public const codeSyncDiagramOperationsServiceId:String = "codeSyncDiagramOperationsService";
		
		[RemoteInvocation]
		public function updateNode(path:ArrayCollection, newNode:TreeNode, expandNode:Boolean = false, collapseNode:Boolean = false, selectNode:Boolean = false):void {			
			scenarioTreeStatefulClient.updateNode(path, newNode, expandNode, collapseNode, selectNode);
		}
		
		///////////////////////////////////////////////////////////////
		// Proxies to service methods
		///////////////////////////////////////////////////////////////
		
		public function service_addNew(viewIdOfParent:Object, codeSyncType:String, parameters:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncDiagramOperationsServiceId, 
				"addNew",
				[diagramId, viewIdOfParent, codeSyncType, parameters]));
		}
		
		public function service_synchronize(path:String, technology:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"synchronize",
				[path, technology]));
		}
		
		public function service_collapseCompartment(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncDiagramOperationsServiceId,
				"collapseCompartment", 
				[viewId]));
		}
		
		public function service_expandCompartment(viewId:Object, category:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncDiagramOperationsServiceId,
				"expandCompartment",
				[viewId, category]));
		}
		
		public function service_handleDragOnDiagram(pathsWithRoot:IList):void {
			attemptUpdateContent(null, invokeServiceMethod("handleDragOnDiagram", [pathsWithRoot, diagramId], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_setInplaceEditorText(viewId:Object, text:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncDiagramOperationsServiceId, 
				"setInplaceEditorText",
				[viewId, text]));
		}
		
		public function service_getInplaceEditorText(viewId:Object, callbackFunction:Function):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncDiagramOperationsServiceId, 
				"getInplaceEditorText",
				[viewId], null, callbackFunction));
		}
		
		public function service_removeView(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncDiagramOperationsServiceId,
				"removeView",
				[viewId]));
		}
		
		public function service_displayMissingRelations(viewId:Object, addMissingElements:Boolean):void {
			attemptUpdateContent(null,  new InvokeServiceMethodServerCommand(
				codeSyncDiagramOperationsServiceId,
				"displayMissingRelations",
				[viewId, addMissingElements]));
		}
		
		public function service_contentAssist(viewId:Object, pattern:String, callbackFunction:Function):void {
			var options:ServiceInvocationOptions = new ServiceInvocationOptions().setReturnCommandWithoutSending(true).setResultCallbackFunction(callbackFunction);
			attemptUpdateContent(null, invokeServiceMethod("contentAssist", [viewId, pattern], options));
		}
		
		public function service_addNewRelation(type:String, sourceViewId:Object, targetViewId:Object, parameters:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(codeSyncDiagramOperationsServiceId, "addNewRelation", [type, sourceViewId, targetViewId, parameters]));
		}

		public function service_addNewScenario(name:String):void {
			attemptUpdateContent(null, invokeServiceMethod("addNewScenario", [editableResourcePath, name], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_openScenarioNode(path:ArrayCollection, context:Object):void {
			attemptUpdateContent(null, invokeServiceMethod("openNode", [path, context], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_addNewComment(path:ArrayCollection, comment:String):void {
			attemptUpdateContent(null, invokeServiceMethod("addNewComment", [path, editableResourcePath, comment, scenarioTreeStatefulClient.context], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_deleteScenarioElement(path:ArrayCollection):void {
			attemptUpdateContent(null, invokeServiceMethod("deleteScenarioElement", [path, editableResourcePath, scenarioTreeStatefulClient.context], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_expandCollapseCompartment(viewId:Object, expand:Boolean):void {
//			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("jsClassDiagramOperationsDispatcher", "expandCollapseCompartment", [viewId, expand]));
		}
		
		public function service_addElement(type:String, keyParameter:String, isCategory:Boolean, parameters:Object, template:String, childType:String, nextSiblingSeparator:String, parentViewId:Object, parentCategory:String):void {
//			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("jsClassDiagramOperationsDispatcher", "addElement", [type, keyParameter, isCategory, parameters, template, childType, nextSiblingSeparator, parentViewId, parentCategory], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_deleteElement(viewId:Object):void {
//			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("jsClassDiagramOperationsDispatcher", "deleteElement", [viewId]));
		}
		
		public function service_getWizardElements(callbackObject:Object, callbackFunction:Function):void {
			CommunicationPlugin.getInstance().bridge.sendObject(new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"getWizardElements",
				[editableResourcePath], 
				callbackObject, callbackFunction));
		}	
		
		public function service_getWizardDependencies(path:ArrayCollection, callbackObject:Object, callbackFunction:Function):void {
			CommunicationPlugin.getInstance().bridge.sendObject(new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"getWizardDependencies",
				[editableResourcePath, path],  
				callbackObject, callbackFunction));			
		}	
		
		public function service_generateWizardDependencies(dependencies:ArrayCollection, path:ArrayCollection, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"generateWizardDependencies",
				[editableResourcePath, dependencies, path],
				callbackObject, callbackFunction));		
		}	
		
		public function service_dragOnDiagramWizardDependenciesTargets(dependencies:ArrayCollection, path:ArrayCollection, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"dragOnDiagramWizardDependenciesTargets",
				[editableResourcePath, dependencies, path],
				callbackObject, callbackFunction));		
		}	
		
		public function service_dragOnDiagramWizardElements(paths:ArrayCollection, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"dragOnDiagramWizardElements",
				[editableResourcePath, paths],
				callbackObject, callbackFunction));		
		}	
		
		public function service_addWizardElement(addWizardAttribute:Boolean, parentPath:ArrayCollection, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"addWizardElement",
				[editableResourcePath, addWizardAttribute, parentPath],
				callbackObject, callbackFunction));		
		}	
	
		public function service_removeWizardElement(path:ArrayCollection, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"removeWizardElement",
				[editableResourcePath, path],
				callbackObject, callbackFunction));		
		}	
		
		public function service_selectWizardDependenciesTargetsFromDiagram(dependencies:ArrayCollection, path:ArrayCollection):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"selectWizardDependenciesTargetsFromDiagram",
				[editableResourcePath, dependencies, path]));		
		}
		
		public function service_selectWizardElementsFromDiagram(paths:ArrayCollection):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(
				codeSyncOperationsServiceId,
				"selectWizardElementsFromDiagram",
				[editableResourcePath, paths]));		
		}
	}
}
