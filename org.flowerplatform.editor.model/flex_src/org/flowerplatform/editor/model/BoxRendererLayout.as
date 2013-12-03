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
package org.flowerplatform.editor.model {
	
	import org.flowerplatform.editor.model.remote.command.MoveResizeServerCommand;
	import org.flowerplatform.editor.model.renderer.BoxRenderer;
	import org.flowerplatform.emf_model.notation.Node;
	
	import spark.components.supportClasses.GroupBase;
	import spark.layouts.HorizontalAlign;
	import spark.layouts.VerticalAlign;
	import spark.layouts.VerticalLayout;
	
	/**
	 * @author Cristina Constantinescu
	 */  
	public class BoxRendererLayout extends VerticalLayout {
		
		public function BoxRendererLayout()	{
			super();
			
			paddingBottom = paddingTop = paddingLeft = paddingRight = 2;
			horizontalAlign = HorizontalAlign.CENTER;			
		}
		
		/**
		 * VerticalLayout changes its size depending on children's content size.
		 * The BoxRenderer needs to do this only if current size
		 * smaller than children's content size.
		 */ 
		override public function measure():void	{
			var renderer:BoxRenderer = BoxRenderer(target);	
			if (!renderer)
				return;
			
			super.measure();
			
			var sizeChanged:Boolean = false;
			if (!renderer.setOnlyMinWidthHeight && renderer.measuredWidth > renderer.width) {
				// children calculated width greater than parent's width
				renderer.width = Math.ceil(renderer.measuredWidth);				
				sizeChanged = true;
			}
			renderer.minWidth = Math.ceil(renderer.measuredMinWidth);
			
			if (!renderer.setOnlyMinWidthHeight && renderer.measuredHeight > renderer.height) {
				// children calculated height greater than parent's height
				renderer.height = Math.ceil(renderer.measuredHeight);  				 
				sizeChanged = true;
			} 
			renderer.minHeight = Math.ceil(renderer.measuredMinHeight);
			
			if (sizeChanged) { // save size to server									
				var command:MoveResizeServerCommand = new MoveResizeServerCommand();
				command.id = Node(renderer.data).layoutConstraint_RH.referenceIdAsString;
				command.newWidth = renderer.width;
				command.newHeight = renderer.height;
				command.newX = renderer.x;
				command.newY = renderer.y;						
				NotationDiagramShell(renderer.diagramShell).editorStatefulClient.attemptUpdateContent(null, command);								
			}   
		}
		
	}
}
