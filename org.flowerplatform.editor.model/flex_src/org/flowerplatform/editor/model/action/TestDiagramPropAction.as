package org.flowerplatform.editor.model.action {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.editor.remote.EditableResource;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.properties.PropertiesPlugin;

	public class TestDiagramPropAction extends ActionBase {
		
		public function TestDiagramPropAction() {
			super();	
			label = "Test Diagram Prop";
			preferShowOnActionBar = true;
		}
		
		override public function get visible():Boolean {	
			return true;
		}
		
		override public function run():void {
			
			var selectedItems:ArrayCollection = new ArrayCollection();
			for (var i:int = 0; i < selection.length; i++) {
				var node:Node = Node(selection.getItemAt(i));//.id / 
				var diagramEditableResourcePath:String = NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).editableResourcePath;
				var xmiID:String = node.idAsString;
				var serviceID:String = NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).getStatefulServiceId();
				var diagramViewType:String = node.viewType;
				
				selectedItems.addItem(new DiagramSelectedItem(xmiID, diagramEditableResourcePath, serviceID, diagramViewType));
			}
			var myObject:Object;
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("propertiesProviderService",
					"getProperties",[selectedItems],
					myObject,
					function(object:Object):void {
						var x:Object = object;
						PropertiesPlugin.getInstance().propertyList.dataProvider = object as IList;
					}
				));		
		}
	}
}