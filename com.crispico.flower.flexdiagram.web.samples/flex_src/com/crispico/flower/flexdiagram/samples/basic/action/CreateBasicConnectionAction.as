package com.crispico.flower.flexdiagram.samples.basic.action {
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.flexdiagram.action.BaseMultipleSelectionAction;
	import com.crispico.flower.flexdiagram.action.CreateElementActionContext;
	import com.crispico.flower.flexdiagram.action.CreateRelationActionContext;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicConnection;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicModel;
	
	import mx.collections.ArrayCollection;
	
	public class CreateBasicConnectionAction extends BaseAction {
		
		public function CreateBasicConnectionAction()	{
			label = "New BasicConnection";
		}
		
		override public function run(selectedEditParts:ArrayCollection):void {
			var con:BasicConnection = new BasicConnection();
			con.source = BasicModel(EditPart(CreateRelationActionContext(context).sourceEditPart).getModel());
			con.target = BasicModel(EditPart(CreateRelationActionContext(context).targetEditPart).getModel());
			
			ArrayCollection(CreateRelationActionContext(context).diagramFigure.getEditPart().getModel()).addItem(con);
		}
		
	}
}