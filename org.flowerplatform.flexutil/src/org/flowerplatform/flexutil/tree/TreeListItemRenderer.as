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
package org.flowerplatform.flexutil.tree {
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.MouseEvent;
	import flash.system.Capabilities;
	
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.components.Group;
	import spark.components.IconItemRenderer;
	import spark.primitives.BitmapImage;
	
	public class TreeListItemRenderer extends IconItemRenderer {
					
		public var levelWidth:int = 15;
		
		[Embed(source="/plus.gif")]			
		public static const _iconCollapsed:Class;	
		
		[Embed(source="/minus.gif")]			
		public static const _iconExpanded:Class;
		
		protected var expandIconDisplay:BitmapImage;
				
		public function TreeListItemRenderer() {
			super();
			
			setStyle("verticalAlign", "middle");
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
			// only for web
				minHeight = 22;	
			}
			
			doubleClickEnabled = true;
			addEventListener(MouseEvent.DOUBLE_CLICK, expandedIconDisplayClickHandler);
		}
		
		override public function set data(value:Object):void {
			// hack: when the renderers are reused, on mobile, there is a flicker, because
			// the renderer under mouse (with state = down) is reused and displayed elsewhere
			// and is displayed for an instance in the down state, in the new location (probably
			// in a later displayUpdate. 
			// setting this to false here improves the issue. It still happens a little bit during
			// the first or second expand
			down = false;

			var initialData:Object = data;
			if (data != null && value != initialData) {
				HierarchicalModelWrapper(data).treeNode.removeEventListener(TreeList.UPDATE_TREE_RENDERER_EVENT, hasChildrenChangedHandler);	
			}
			super.data = value;
			if (data != null && value != initialData) {
				HierarchicalModelWrapper(data).treeNode.addEventListener(TreeList.UPDATE_TREE_RENDERER_EVENT, hasChildrenChangedHandler);	
			}
		}
		
		protected function hasChildrenChangedHandler(event:Event):void {
			// this includes invalidation for properties and display list. We need to do this
			// (instead of calling the invalidation methods directly), because
			// otherwise the "labelChanged" flag remain unchaged so the label won't be updated.
			data = data;
		}
		
		protected override function createChildren():void {
			super.createChildren();
			
			// create BitmapImage container
			var imageContainer:Group = new Group();
			imageContainer.width = 16;
			imageContainer.height = 16;
			addChildAt(imageContainer, 0);
			
			// create BitmapImage
			expandIconDisplay = new BitmapImage();
			// set to temporary emded icon in order to set width/height
			expandIconDisplay.source = _iconExpanded;
			imageContainer.addElement(expandIconDisplay);		
						
			// expandIcon position is set later because we must know its exact width/height
			imageContainer.callLater(setExpandIconDisplayPosition, [imageContainer.width, imageContainer.height]);
						
			imageContainer.addEventListener(MouseEvent.CLICK, expandedIconDisplayClickHandler);
		}
			
		private function setExpandIconDisplayPosition(width:Number, heigth:Number):void {
			setElementPosition(expandIconDisplay, width/2 - expandIconDisplay.measuredWidth/2, heigth/2 - expandIconDisplay.measuredHeight/2);
		}
		
		protected function expandedIconDisplayClickHandler(event:MouseEvent):void {
			var modelWrapper:HierarchicalModelWrapper = HierarchicalModelWrapper(data);
			if (!TreeList(owner).hierarchicalModelAdapter.hasChildren(modelWrapper.treeNode)) {
				return;
			}

			TreeList(owner).expandCollapseNode(modelWrapper);
		}
		
		override protected function layoutContents(unscaledWidth:Number, unscaledHeight:Number):void {	
			// no need to call super.layoutContents() since we're changing how it happens here
			
			if (!TreeList(owner).hierarchicalModelAdapter.hasChildren(HierarchicalModelWrapper(data).treeNode)) {
				expandIconDisplay.source = null;
			} else if (HierarchicalModelWrapper(data).expanded) {
				expandIconDisplay.source = _iconExpanded;
			} else {
				expandIconDisplay.source = _iconCollapsed;
			}
			
			// start laying out our children now
			var iconWidth:Number = 0;
			var iconHeight:Number = 0;
			var expandIconWidth:Number = 0;
			var expandIconHeight:Number = 0;
			
			var hasLabel:Boolean = labelDisplay && labelDisplay.text != "";
			var hasMessage:Boolean = messageDisplay && messageDisplay.text != "";
			
			var paddingLeft:Number   = getStyle("paddingLeft");
			var paddingRight:Number  = getStyle("paddingRight");
			var paddingTop:Number    = getStyle("paddingTop");
			var paddingBottom:Number = getStyle("paddingBottom");
			var horizontalGap:Number = getStyle("horizontalGap");
			var verticalAlign:String = getStyle("verticalAlign");
			var verticalGap:Number   = (hasLabel && hasMessage) ? getStyle("verticalGap") : 0;
			
			var vAlign:Number;
			if (verticalAlign == "top")
				vAlign = 0;
			else if (verticalAlign == "bottom")
				vAlign = 1;
			else // if (verticalAlign == "middle")
				vAlign = 0.5;
			// made "middle" last even though it's most likely so it is the default and if someone 
			// types "center", then it will still vertically center itself.
			
			var viewWidth:Number  = unscaledWidth  - paddingLeft - paddingRight;
			var viewHeight:Number = unscaledHeight - paddingTop  - paddingBottom;
			
			paddingLeft += levelWidth * HierarchicalModelWrapper(data).nestingLevel;
			
			expandIconWidth = getElementPreferredWidth(expandIconDisplay.parent);
			expandIconHeight = getElementPreferredHeight(expandIconDisplay.parent);
			
			// use vAlign to position the icon.
			var expandIconDisplayY:Number = Math.round(vAlign * (viewHeight - expandIconHeight)) + paddingTop;
			setElementPosition(expandIconDisplay.parent, paddingLeft, expandIconDisplayY);

			// icon is on the left
			if (iconDisplay) {
				// set the icon's position and size
				setElementSize(iconDisplay, this.iconWidth, this.iconHeight);
				
				iconWidth = iconDisplay.getLayoutBoundsWidth();
				iconHeight = iconDisplay.getLayoutBoundsHeight();
				
				// use vAlign to position the icon.
				var iconDisplayY:Number = Math.round(vAlign * (viewHeight - iconHeight)) + paddingTop;
				setElementPosition(iconDisplay, paddingLeft + expandIconWidth, iconDisplayY);
			}
						
			// Figure out how much space we have for label and message as well as the 
			// starting left position
			var labelComponentsViewWidth:Number = viewWidth - iconWidth - expandIconWidth;
			
			// don't forget the extra gap padding if these elements exist
			if (iconDisplay)
				labelComponentsViewWidth -= horizontalGap;
			if (decoratorDisplay)
				labelComponentsViewWidth -= horizontalGap;
			
			var labelComponentsX:Number = paddingLeft + expandIconWidth;
			if (iconDisplay)
				labelComponentsX += iconWidth + horizontalGap;
			
			// calculte the natural height for the label
			var labelTextHeight:Number = 0;
			
			if (hasLabel) {
				// reset text if it was truncated before.
				if (labelDisplay.isTruncated)
					labelDisplay.text = mx_internal::labelText;
				
				// commit styles to make sure it uses updated look
				labelDisplay.commitStyles();
				
				labelTextHeight = getElementPreferredHeight(labelDisplay);
			}
									
			var labelWidth:Number = 0;
			var labelHeight:Number = 0;
						
			if (hasLabel) {
				// handle labelDisplay.  it can only be 1 line
				
				// width of label takes up rest of space
				// height only takes up what it needs so we can properly place the message
				// and make sure verticalAlign is operating on a correct value.
				labelWidth = Math.max(labelComponentsViewWidth, 0);
				labelHeight = labelTextHeight;
				
				if (labelWidth == 0)
					setElementSize(labelDisplay, NaN, 0);
				else
					setElementSize(labelDisplay, labelWidth, labelHeight);
				
				// attempt to truncate text
				labelDisplay.truncateToFit();
			}
						
			// Position the text components now that we know all heights so we can respect verticalAlign style
			var totalHeight:Number = 0;
			var labelComponentsY:Number = 0; 
			
			// Heights used in our alignment calculations.  We only care about the "real" ascent 
			var labelAlignmentHeight:Number = 0; 
			
			if (hasLabel)
				labelAlignmentHeight = getElementPreferredHeight(labelDisplay);
			
			totalHeight = labelAlignmentHeight + verticalGap;          
			labelComponentsY = Math.round(vAlign * (viewHeight - totalHeight)) + paddingTop;
			
			if (labelDisplay)
				setElementPosition(labelDisplay, labelComponentsX, labelComponentsY);
			
		}
				
		override protected function measure():void {
			super.measure();
			// calculate expand icon width & left padding
			var expandIconParent:UIComponent = UIComponent(expandIconDisplay.parent);
			var expandIconParentLeftPadding:Number = getStyle("horizontalGap") + expandIconParent.x;
			var expandIconParentWidth:Number = getElementPreferredWidth(expandIconParent);
			var verticalScrollCompensation:Number = 15;
			
			measuredWidth += expandIconParentLeftPadding + expandIconParentWidth + verticalScrollCompensation;			
			measuredMinWidth += expandIconParentLeftPadding + expandIconParentWidth + verticalScrollCompensation;			
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {			
		}
	}
	
}