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
			var layoutTarget:GroupBase = target;
			if (!layoutTarget)
				return;
			
			super.measure();
			
			var sizeChanged:Boolean = false;
			if (layoutTarget.measuredWidth > layoutTarget.width) {
				// children calculated width greater than parent's width
				layoutTarget.width = Math.ceil(layoutTarget.measuredWidth);				
				sizeChanged = true;
			}
			layoutTarget.minWidth = Math.ceil(layoutTarget.measuredMinWidth);
			
			if (layoutTarget.measuredHeight > layoutTarget.height) {
				// children calculated height greater than parent's height
				layoutTarget.height = Math.ceil(layoutTarget.measuredHeight);  				 
				sizeChanged = true;
			} 
			layoutTarget.minHeight = Math.ceil(layoutTarget.measuredMinHeight);
			
			if (sizeChanged) { // save size to server
				var renderer:BoxRenderer = BoxRenderer(layoutTarget);						
				var command:MoveResizeServerCommand = new MoveResizeServerCommand();
				command.id = Node(renderer.data).layoutConstraint_RH.referenceIdAsString;
				command.newWidth = layoutTarget.width;
				command.newHeight = layoutTarget.height;
				command.newX = layoutTarget.x;
				command.newY = layoutTarget.y;						
				NotationDiagramShell(renderer.diagramShell).editorStatefulClient.attemptUpdateContent(null, command);								
			}   
		}
		
	}
}
