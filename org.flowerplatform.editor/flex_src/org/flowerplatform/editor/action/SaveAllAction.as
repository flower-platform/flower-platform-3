package org.flowerplatform.editor.action {

	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.flextojava.FlexToJavaCompoundCommand;
	import org.flowerplatform.communication.stateful_service.StatefulClientRegistry;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.GlobalEditorOperationsManager;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	use namespace mx_internal;
	
	// TODO CS/STFL de redenumit
	
	
	public class SaveAllAction extends ActionBase {
		/**
		 * @author Sebastian Solomon
		 */
		override public function run():void {
			
			var statefulClientsList:ArrayCollection = CommunicationPlugin.getInstance().statefulClientRegistry.mx_internal::statefulClientsList;
			
			var entriesToSave:ArrayCollection = EditorPlugin.getInstance().globalEditorOperationsManager.createEntriesToSave(statefulClientsList);
			for(var i:Number=0; i < entriesToSave.length; i++){
				if (entriesToSave[i].hasOwnProperty("editorStatefulClient")){
					EditorStatefulClient(entriesToSave[i].editorStatefulClient).save();
				}
			}
//			var entriesToSave:ArrayCollection = GlobalEditorOperationsManager.INSTANCE.createEntriesToSave(StatefulClientRegistry.mx_internal::statefulClientsList);
//			var commandToSend:FlexToJavaCompoundCommand = EditorPlugin.getInstance().globalEditorOperationsManager.getSaveCommandForSelectedEntries(entriesToSave);			
//			if (commandToSend.commandsList.length > 0) {
//				CommunicationPlugin.getInstance().bridge.sendObject(commandToSend);
//			}
		}
	}
}