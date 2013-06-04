package com.crispico.flower.flexdiagram.tree
{
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	
	import flashx.textLayout.formats.VerticalAlign;
	
	import flexlib.controls.IconLoader;
	
	import mx.core.IDataRenderer;
	import mx.core.InteractionMode;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	
	import spark.components.Group;
	import spark.components.IItemRenderer;
	import spark.components.IconItemRenderer;
	import spark.components.LabelItemRenderer;
	import spark.components.List;
	import spark.components.supportClasses.InteractionState;
	import spark.components.supportClasses.StyleableTextField;
	import spark.core.DisplayObjectSharingMode;
	import spark.core.IGraphicElement;
	import spark.core.ISharedDisplayObject;
	import spark.layouts.HorizontalLayout;
	import spark.layouts.VerticalLayout;
	import spark.primitives.BitmapImage;
	import spark.primitives.Graphic;
	
	public class TreeListItemRenderer extends IconItemRenderer {
					
		private static const LEVEL_DEPTH:int = 15;
		
		[Embed(source="/../icons/plus.gif")]			
		public static const _iconCollapsed:Class;	
		
		[Embed(source="/../icons/minus.gif")]			
		public static const _iconExpanded:Class;
		
		protected var expandIconDisplay:BitmapImage;
				
		public function TreeListItemRenderer() {
			super();
			
			setStyle("verticalAlign", "middle");
			
			// TODO: temporary (use fields from TreeNode)
			iconField = "image";
			labelField = "name";
			
			// only for web
			//minHeight = 22;					
		}
		
		private function get treeListDataProvider():TreeListDataProvider {
			var ownerList:List = owner as List;
			return TreeListDataProvider(List(owner).dataProvider);
		}
		
		private function get treeNode():TreeNode {
			return TreeNode(data);
		}
		
		private function hasChildren():Boolean {			
			return treeNode.children != null && treeNode.children.length > 0;
		}
		
		private function get nestingLevel():int {			
			return treeListDataProvider.getItemDepth(treeNode);
		}
		
		protected override function createChildren():void {
			super.createChildren();
			
			expandIconDisplay = new BitmapImage();

			var imageContainer:Group = new Group();
			imageContainer.width = 16;
			imageContainer.height = 16;
			addChildAt(imageContainer, 0);
			
			imageContainer.addElement(expandIconDisplay);		
			
			imageContainer.addEventListener(MouseEvent.CLICK, expandedIconDisplayClickHandler);
		}
			
		protected function expandedIconDisplayClickHandler(event:MouseEvent):void {
			if (treeListDataProvider.isItemOpen(treeNode)) {
				treeListDataProvider.closeItem(treeNode);
			} else {
				treeListDataProvider.openItem(treeNode);
			}
			invalidateDisplayList();
		}
		
		override protected function layoutContents(unscaledWidth:Number, unscaledHeight:Number):void {	
			// no need to call super.layoutContents() since we're changing how it happens here
			
			if (!hasChildren()) {
				expandIconDisplay.source = null;
			} else if (treeListDataProvider.isItemOpen(treeNode)) {
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
			
			paddingLeft += LEVEL_DEPTH * nestingLevel;
			
			expandIconWidth = expandIconDisplay.parent.width;
			expandIconHeight = expandIconDisplay.parent.height;
			
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
				
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {			
		}
	}
	
}