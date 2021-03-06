package org.flowerplatform.codesync.action {
	
	import com.crispico.flower.mp.codesync.base.editor.CodeSyncEditorDescriptor;
	
	import flash.geom.Rectangle;
	
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
	import org.flowerplatform.codesync.remote.RelationDescriptor;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.action.DiagramShellAwareActionBase;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.View;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AddNewRelationAction extends DiagramShellAwareActionBase {
		
		protected var relationDescriptor:RelationDescriptor;
		
		public function AddNewRelationAction(relationDescriptor:RelationDescriptor) {
			super();
			this.relationDescriptor = relationDescriptor;
			// TODO CS/JS: eticheta si iconita
//			label = EditorModelPlugin.getInstance().getMessage("action.addRelation");
			label = relationDescriptor.label;
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/obj16/Association_none.gif");
			orderIndex = 110;
			parentId = "new";
		}
		
		override public function get visible():Boolean {	
			// (the action must be visible when dragToCreateRelationTool active)
			if (context == null || !context.hasOwnProperty("sourceModel") || !context.hasOwnProperty("targetModel")) {
				return false;
			}
			var sourceCodeSyncType:String = CodeSyncPlugin.getInstance().getCodeSyncTypeFromView(context.sourceModel);
			if (sourceCodeSyncType == null) {
				return false;
			}
			var targetCodeSyncType:String = CodeSyncPlugin.getInstance().getCodeSyncTypeFromView(context.targetModel);
			if (targetCodeSyncType == null && !relationDescriptor.acceptTargetNullIfNoCodeSyncTypeDetected) {
				return false;
			}
			
			// match for codeSyncType?
			if (relationDescriptor.sourceCodeSyncTypes == null || !relationDescriptor.sourceCodeSyncTypes.contains(sourceCodeSyncType)) {
				// no match for codeSyncType; try categories
				if (relationDescriptor.sourceCodeSyncTypeCategories == null) {
					// no category configured in this descriptor 
					return false;
				}
				var sourceDescriptor:CodeSyncElementDescriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(sourceCodeSyncType);
				if (sourceDescriptor.codeSyncTypeCategories == null) {
					// type has no categories
					return false;
				}
				var foundMatch:Boolean = false;
				for each (var possibleSourceCategory:String in relationDescriptor.sourceCodeSyncTypeCategories) {
					if (sourceDescriptor.codeSyncTypeCategories.contains(possibleSourceCategory)) {
						foundMatch = true;
						break;
					}
				}
				if (!foundMatch) {
					return false;
				}
			}
			
			// match for codeSyncType?
			if (targetCodeSyncType != null && (relationDescriptor.targetCodeSyncTypes == null || !relationDescriptor.targetCodeSyncTypes.contains(targetCodeSyncType))) {
				// no match for codeSyncType; try categories
				if (relationDescriptor.targetCodeSyncTypeCategories == null) {
					// no category configured in this descriptor 
					return false;
				}
				var targetDescriptor:CodeSyncElementDescriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(targetCodeSyncType);
				if (targetDescriptor.codeSyncTypeCategories == null) {
					// type has no categories
					return false;
				}
				foundMatch = false;
				for each (var possibleTargetCategory:String in relationDescriptor.targetCodeSyncTypeCategories) {
					if (targetDescriptor.codeSyncTypeCategories.contains(possibleTargetCategory)) {
						foundMatch = true;
						break;
					}
				}
				if (!foundMatch) {
					return false;
				}
			}			
			return true;
		}
		
		override public function run():void {
			var rectangle:Rectangle = diagramShell.convertCoordinates(
				context.rectangle, 
				UIComponent(FlexGlobals.topLevelApplication), 
				UIComponent(diagramShell.diagramRenderer));
			
			var parameters:Object = new Object();
			parameters.x = rectangle.x;
			parameters.y = rectangle.y;
			
			NotationDiagramEditorStatefulClient(NotationDiagramShell(diagramShell).editorStatefulClient)
				.service_addNewRelation(relationDescriptor.type, View(context.sourceModel).id, context.targetModel != null ? View(context.targetModel).id : null, parameters);
		}
		
	}
}
