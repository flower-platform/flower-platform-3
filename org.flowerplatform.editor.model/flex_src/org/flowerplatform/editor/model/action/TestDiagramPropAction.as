package org.flowerplatform.editor.model.action {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
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
			var diagramEditableResourcePath:String = "/crispico/ws_trunk/wd1/NewDiagram1.notation";
			var xmiID:String = "_wUgVYPnAEeKpg94yU-UoAw";
			var serviceID:String = "diagramEditorStatefulService";
			var selectedItems:ArrayCollection = new ArrayCollection();
			selectedItems.addItem(new DiagramSelectedItem(xmiID, diagramEditableResourcePath, serviceID, "javaClass"));
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