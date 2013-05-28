package com.crispico.flower.flexdiagram.samples.basic.action
{
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.flexdiagram.action.CreateRelationActionContext;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicConnection;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicModel;
	
	import mx.collections.ArrayCollection;

	public class CreateBasicConnectionWithLabelAction extends BaseAction {
		
		public function CreateBasicConnectionWithLabelAction()	{
			label = "New BasicConnection With Label";
		}
		
		override public function run(selectedEditParts:ArrayCollection):void {
			var con:BasicConnection = new BasicConnection();
			con.source = BasicModel(EditPart(CreateRelationActionContext(context).sourceEditPart).getModel());
			con.target = BasicModel(EditPart(CreateRelationActionContext(context).targetEditPart).getModel());
			
			var connLabel:BasicModel = new BasicModel();
			connLabel.type = BasicModel.CONNECTION_LABEL;
			connLabel.name = "Default Name";
			con.children.addItem(connLabel);
			
			ArrayCollection(CreateRelationActionContext(context).diagramFigure.getEditPart().getModel()).addItem(con);
		}
	}
}