package com.crispico.flower.flexdiagram.samples.basic.old_impl {
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IEditPartFactory;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.action.IActionProvider2;
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuUtils;
	import com.crispico.flower.flexdiagram.contextmenu.DelegatingActionProvider;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	import com.crispico.flower.flexdiagram.samples.basic.action.CreateBasicConnectionAction;
	import com.crispico.flower.flexdiagram.samples.basic.action.CreateBasicModelAction;
	import com.crispico.flower.flexdiagram.samples.basic.action.SampleAction;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicConnection;
	import com.crispico.flower.flexdiagram.samples.basic.model.BasicModel;
	import com.crispico.flower.flexdiagram.tool.SelectMoveResizeTool;
	
	import flash.events.MouseEvent;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayCollection;
	
	public class Viewer extends DiagramViewer implements IEditPartFactory, IActionProvider2 {
		
		private var createBaseModelAction:CreateBasicModelAction = new CreateBasicModelAction();
		private var createBasicConnectionAction:CreateBasicConnectionAction = new CreateBasicConnectionAction();
		private var sampleAction1:SampleAction = new SampleAction("Sample Action 1");
		private var sampleAction2:SampleAction = new SampleAction("Sample Action 2");
		
		public function Viewer(rootFigure:IFigure, model:Object=null) {
			super(rootFigure, this, model);
			activate(true);
			selectMoveResizeTool = new SelectMoveResizeTool();
			setCreateElementActionProvider(new DelegatingActionProvider(getCreateElementActionContext, fillCreateElementContextMenu));
			setCreateRelationActionProvider(new DelegatingActionProvider(getCreateRelationActionContext, fillCreateRelationContextMenu));
			rightClickEnabled = true;
		}
		
		public function createEditPart(viewer:DiagramViewer, context:EditPart, model:Object):EditPart {
			if (model is ArrayCollection) {
				return new DiagramEditPart(model, this);
			} else if (model is BasicModel) {
				return new BasicModelEditPart(model, this); 
			} else if (model is BasicConnection) {
				return new BasicConnectionEditPart(model, this);
			} else {
				return null;
			}
		}
		
		override public function getEditPartFactory():IEditPartFactory {
			return this;
		}

		public function fillCreateElementContextMenu(menu:FlowerContextMenu):void {
			menu.addActionEntryIfVisible(createBaseModelAction);
		}
		
		public function fillCreateRelationContextMenu(menu:FlowerContextMenu):void {
			menu.addActionEntryIfVisible(createBasicConnectionAction);
		}
		
		public function fillContextMenu(contextMenu:FlowerContextMenu):void {
			contextMenu.addActionEntryIfVisible(sampleAction1);
			contextMenu.addActionEntryIfVisible(sampleAction2);
		}

		override public function isOverSelection(event:MouseEvent):Boolean {
			return ContextMenuUtils.diagramIsOverSelection(this, event);
		}
		
		override public function get displayAreaOfSelection():Rectangle {
			return ContextMenuUtils.diagramGetDisplayAreaOfSelection(this);
		}
		
	}
}