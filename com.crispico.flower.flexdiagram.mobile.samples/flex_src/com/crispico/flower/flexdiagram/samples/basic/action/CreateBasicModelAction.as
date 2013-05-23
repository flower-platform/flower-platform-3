package com.crispico.flower.flexdiagram.samples.basic.action {
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.flexdiagram.action.BaseMultipleSelectionAction;
	import com.crispico.flower.flexdiagram.action.CreateElementActionContext;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicModel;
	
	import mx.collections.ArrayCollection;
	
	public class CreateBasicModelAction extends BaseAction {
		
		public function CreateBasicModelAction()	{
			label = "New BasicModel";
		}
		
		override public function run(selectedEditParts:ArrayCollection):void {
			var basicModel:BasicModel = new BasicModel();
			basicModel.x = CreateElementActionContext(context).x;
			basicModel.y = CreateElementActionContext(context).y;
			basicModel.height = CreateElementActionContext(context).height;
			basicModel.width = CreateElementActionContext(context).width;
			
			ArrayCollection(CreateElementActionContext(context).diagramFigure.getEditPart().getModel()).addItem(basicModel);
		}
		
	}
}