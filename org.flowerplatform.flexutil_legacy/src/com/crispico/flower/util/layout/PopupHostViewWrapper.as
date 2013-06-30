package com.crispico.flower.util.layout {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.containers.Canvas;
	import mx.containers.VBox;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.context_menu.FillContextMenuEvent;
	import org.flowerplatform.flexutil.popup.ActionUtil;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.components.Button;
	import spark.components.HGroup;
	
	/**
	 * There is an issue with NavigatorContent: https://github.com/flex-users/flexlib/issues/301
	 * so we use Canvas instead of a NavigatorContent that should wrap spark components.
	 */
	public class PopupHostViewWrapper extends VBox implements IPopupHost {
		
		public var allActions:Vector.<IAction>
		
		public var selection:IList;
		
		protected var rootActionsAlreadyCalculated:ArrayList;
		
		protected var buttonBar:HGroup;
		
		protected var popupContent:IPopupContent;
		
		public function PopupHostViewWrapper(popupContent:IPopupContent) {
			super();
			percentHeight = 100;
			percentWidth = 100;
			addEventListener(FillContextMenuEvent.FILL_CONTEXT_MENU,fillContextMenuHandler); 
			setStyle("verticalGap", 0);
			this.popupContent = popupContent;
			popupContent.percentHeight = 100;
			popupContent.percentWidth = 100;
			popupContent.popupHost = this;
		}
		
		override protected function createChildren():void {
			super.createChildren();
			buttonBar = new HGroup();
			buttonBar.percentWidth = 100;
			addChild(buttonBar)
			buttonBar.paddingTop = 2;
			buttonBar.paddingBottom = 2;
			buttonBar.paddingLeft = 2;
			buttonBar.paddingRight = 2;
			buttonBar.horizontalAlign = "right";
			
			addElement(popupContent);
		}
		
		protected function fillContextMenuHandler(event:FillContextMenuEvent):void {
			event.allActions = allActions;
			event.selection = selection;
			event.rootActionsAlreadyCalculated = rootActionsAlreadyCalculated;
		}
		
		public function refreshActions(popupContent:IPopupContent):void {
			selection = popupContent.getSelection();
			allActions = popupContent.getActions(selection);
			
			buttonBar.removeAllElements();
			rootActionsAlreadyCalculated = new ArrayList();
			ActionUtil.processAndIterateActions(null, allActions, selection, this, function (action:IAction):void {
				if (action.preferShowOnActionBar) {
					var actionButton:ActionButton = new ActionButton();
					actionButton.label = action.label;
					actionButton.setStyle("icon", FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(action.icon));
					actionButton.action = action;
					actionButton.viewWrapper = this;
					buttonBar.addElement(actionButton);
				} else {
					rootActionsAlreadyCalculated.addItem(action);
				}
			});
		}
		
		public function get activePopupContent():IPopupContent {
			return popupContent;
		}
		
		public function setLabel(value:String):void
		{
		}
		
		public function setIcon(value:Object):void
		{
		}
		
		/**
		 * @author Mariana
		 */
		public function displayCloseButton(value:Boolean):void {
		}
		
		/**
		 * @author Mariana
		 */
		public function addToControlBar(value:Object):void {
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function showSpinner(text:String):void {			
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function hideSpinner():void {			
		}
	}
}
