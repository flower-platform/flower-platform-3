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
			var node:Node = Node(selection.getItemAt(0));//.id / 
			//var diagramEditableResourcePath:String = "/crispico/ws_trunk/wd1/NewDiagram1.notation";
			var diagramEditableResourcePath:String = NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).editableResourcePath;
			var xmiID:String = node.idAsString;
			var serviceID:String = "diagramEditorStatefulService";
			var diagramViewType = node.viewType;
			var selectedItems:ArrayCollection = new ArrayCollection();
			selectedItems.addItem(new DiagramSelectedItem(xmiID, diagramEditableResourcePath, serviceID, diagramViewType));
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