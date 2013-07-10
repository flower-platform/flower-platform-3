package org.flowerplatform.flexdiagram.controller.model_extra_info {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DynamicModelExtraInfoController extends ControllerBase implements IModelExtraInfoController {
	
		protected static const NO_RENDERER_ASSOCIATED_MARKER:Object = new Object();
		
		public function DynamicModelExtraInfoController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getRenderer(extraInfo:Object):IVisualElement {
			if (extraInfo == null || extraInfo.renderer == NO_RENDERER_ASSOCIATED_MARKER) {
				return null;
			} else {
				return IVisualElement(extraInfo.renderer);
			}
		}
		
		public function setRenderer(model:Object, extraInfo:Object, renderer:IVisualElement):void {
			if (extraInfo == null) {
				diagramShell.addInModelMapIfNecesssary(model, diagramShell.getControllerProvider(model));
			}
			if (renderer == null) {
				diagramShell.modelToExtraInfoMap[model].renderer = NO_RENDERER_ASSOCIATED_MARKER;	
			} else {
				diagramShell.modelToExtraInfoMap[model].renderer = renderer;
			}
		}
		
		public function createExtraInfo(model:Object):Object {
			return new Object();
		}	
		
		public function isSelected(model:Object):Boolean {
			return diagramShell.modelToExtraInfoMap[model].selected;
		}
		
		public function setSelected(model:Object, value:Boolean):void {		
			diagramShell.modelToExtraInfoMap[model].selected = value;
		}
		
		/**
		 * pt ca sa optimizam un pic; i.e. la pornire sa nu populam intreg mapul cu toate modelele; oricum cele vizibile vor fi ca e nevoie de renderer
		 */ 
		public function getDynamicObject(model:Object):Object {
			// lazy in map, ca la setRenderer
			// setRenderer foloseste asta; idem isSelected
		}
										 
		// DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getExtraInfoController(model)).getDynamicObject(model);
		// x, y, width, height
		// expandedY, expandedHeight
		
		/*
		alg: refreshNodePositions(node)
		expandez un nod
		1: parcurg recursiv copii, populez expHeight; => stiu expanded height al nodului parametru; stiu si diferenta dintre vechiul expHeight si noul expHeight = d
		2: parcurg copii, si actualizez y-ul lor, pornind de la y_nod - 1/2 expHeight
		3: fratii nodului expandat de sus: toti -d; de jos: +d; recursiv pe fratii parintelui, etc pana la root
		
		MindMapModelController: getChild(); getW, getH, getExp, set...
		Mecanismul de notificare a modificarii dimensiunii: trimitem pe BD; posibil sa tr. recalculat
		Tool care la selectie sa afiseze +
		
		
		*/
		
	}	
}