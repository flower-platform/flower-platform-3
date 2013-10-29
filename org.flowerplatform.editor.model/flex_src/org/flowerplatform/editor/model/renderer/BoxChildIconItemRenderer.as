/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.editor.model.renderer {
	import flash.events.Event;
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.event.ZoomPerformedEvent;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.components.IconItemRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BoxChildIconItemRenderer extends IconItemRenderer {
		
		protected var effectsController:BoxEffectsController;
		
		protected static const GLOW_STRENGTH:Number = 3;
		
		protected static const GLOW_BLUR_TO:Number = 15;

		public function BoxChildIconItemRenderer() {
			super();
			iconFunction = getImage;
			
			minHeight = 0;
			percentWidth = 100;
			
			setStyle("verticalAlign", "middle");		
			cacheAsBitmap = true;
			
			addEventListener(ZoomPerformedEvent.ZOOM_PERFORMED, zoomPerformedHandler);
			
			// activate glow on mouse over
			createBoxEffects();
		}
		
		private function getImage(object:Object):Object {
			if (data != null && View(data).viewDetails != null) {
				var iconUrls:String = View(data).viewDetails.iconUrls;
				return FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(EditorModelPlugin.getInstance().getComposedImageUrl(iconUrls));
			}
			return null;
		}
		
		protected function createBoxEffects():void {
			effectsController = new BoxEffectsController(this, true);
			effectsController.glowEffect.strength = GLOW_STRENGTH;
			effectsController.glowEffect.blurXTo = effectsController.glowEffect.blurYTo = GLOW_BLUR_TO;
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {
		}
		
		override public function set data(value:Object):void {
			if (data != null) {
				View(data).removeEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
			}
			super.data = value;
			if (data != null) {
				View(data).addEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
				
				if (View(data).viewDetails != null) {
					viewDetailsUpdatedHandler(null);
				}
			}		
		}
		
		protected function viewDetailsUpdatedHandler(event:Event):void {
			label = View(data).viewDetails.label;
			if (iconDisplay) {
				iconDisplay.source = getImage(null);
			}
		}
		
		protected function zoomPerformedHandler(event:ZoomPerformedEvent):void {
			invalidateSize();
			invalidateDisplayList();
		}
		
		override protected function createLabelDisplay():void	{
			super.createLabelDisplay();
			labelDisplay.multiline = true;
		}
		
		override protected function drawBackground(unscaledWidth:Number, unscaledHeight:Number):void {
			// replace the code from LabelItemRenderer with ours
			// since we are gonna do our own selection (down, select, ..) and hover
			
			// transparent background for hit detection
			graphics.beginFill(0xFFFFFF, 0);
			graphics.lineStyle();
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			graphics.endFill();
			
			//turn of opaqueBackground since the renderer has some transparency
			opaqueBackground = null;
			
			drawBorder(unscaledWidth, unscaledHeight);
		}
		
	}
}
