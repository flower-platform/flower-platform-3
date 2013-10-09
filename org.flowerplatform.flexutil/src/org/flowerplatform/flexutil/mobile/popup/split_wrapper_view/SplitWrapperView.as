package org.flowerplatform.flexutil.mobile.popup.split_wrapper_view {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import mx.collections.IList;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.mobile.popup.WrapperViewBase;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.layouts.HorizontalLayout;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SplitWrapperView extends WrapperViewBase {
		private var _oneViewMode:Boolean = true;
		
		private var _oneViewModeLeftViewActive:Boolean = true;
		
		public var leftAreaPercentWidth:int = 30;
		
//		public var leftComponents:ArrayList = new ArrayList();
//		
//		public var rightComponents:ArrayList = new ArrayList();
		
		private var _leftActiveComponent:IVisualElement;
		
		private var _rightActiveComponent:IVisualElement;
		
		private var _activePopupContent:IPopupContent;
		
		protected var toggleOneViewModeAction:ToggleOneViewModeAction;
		
		protected var toggleOneViewModeLeftViewActive:ToggleOneViewModeLeftViewActiveAction;
		
		public var visibleOnlyOnEmptySelection:Boolean = true;
		
		public function SplitWrapperView() {
			super();
			
			layout = new HorizontalLayout();
			HorizontalLayout(layout).gap = 0;
			
			toggleOneViewModeAction = new ToggleOneViewModeAction();
			toggleOneViewModeAction.splitWrapperView = this;
			
			toggleOneViewModeLeftViewActive = new ToggleOneViewModeLeftViewActiveAction();
			toggleOneViewModeLeftViewActive.splitWrapperView = this;
			
			destructionPolicy = "never";
		}
		
		public function get leftActiveComponent():IVisualElement {
			return _leftActiveComponent;
		}
		
		public function set leftActiveComponent(value:IVisualElement):void {
			if (_leftActiveComponent != null) {
				_leftActiveComponent.removeEventListener(FocusEvent.FOCUS_IN, leftOrRightComponentFocusInHandler);
			}
			_leftActiveComponent = value;
			if (_leftActiveComponent != null) {
				_leftActiveComponent.addEventListener(FocusEvent.FOCUS_IN, leftOrRightComponentFocusInHandler);
			}
			rearrangeLayout();
		}
		
		public function get rightActiveComponent():IVisualElement {
			return _rightActiveComponent;
		}
		
		public function set rightActiveComponent(value:IVisualElement):void {
//			if (value) {
//				oneViewMode = false;
//			}
			if (_rightActiveComponent != null) {
				_rightActiveComponent.removeEventListener(FocusEvent.FOCUS_IN, leftOrRightComponentFocusInHandler);
			}
			_rightActiveComponent = value;
			if (_rightActiveComponent != null) {
				_rightActiveComponent.addEventListener(FocusEvent.FOCUS_IN, leftOrRightComponentFocusInHandler);
			}
			rearrangeLayout();
		}
		
		[Bindable]
		public function get oneViewMode():Boolean {
			return _oneViewMode;
		}
		
		public function set oneViewMode(value:Boolean):void {
			_oneViewMode = value;
			rearrangeLayout();
		}
		
		[Bindable]
		public function get oneViewModeLeftViewActive():Boolean {
			return _oneViewModeLeftViewActive;
		}
		
		public function set oneViewModeLeftViewActive(value:Boolean):void {
			_oneViewModeLeftViewActive = value;
			rearrangeLayout();
			var activeComponent:IVisualElement = oneViewModeLeftViewActive ? leftActiveComponent : rightActiveComponent;
			activePopupContent = activeComponent is IPopupContent ? IPopupContent(activeComponent) : null;
		}
		
		protected function rearrangeLayout():void {
			this.removeAllElements();
			if (oneViewMode) {
				// one view
				if (oneViewModeLeftViewActive) {
					// one view / left
					if (leftActiveComponent != null) {
						addElement(leftActiveComponent);
						leftActiveComponent.percentHeight = 100;
						leftActiveComponent.percentWidth = 100;
					}
				} else {
					// one view / right
					if (rightActiveComponent != null) {
						addElement(rightActiveComponent);
						rightActiveComponent.percentHeight = 100;
						rightActiveComponent.percentWidth = 100;
					}
				}
			} else {
				// 2 views
				if (leftActiveComponent != null) {
					addElement(leftActiveComponent);
					leftActiveComponent.percentHeight = 100;
					leftActiveComponent.percentWidth = leftAreaPercentWidth;
				}
				if (rightActiveComponent != null) {
					addElement(rightActiveComponent);
					rightActiveComponent.percentHeight = 100;
					rightActiveComponent.percentWidth = 100;
				}					
			}
		}
		
		protected function leftOrRightComponentFocusInHandler(event:FocusEvent):void {
			// .target is the element that actually triggered the event
			activePopupContent = event.currentTarget is IPopupContent ? IPopupContent(event.currentTarget) : null;
		}
		
		override public function get activePopupContent():IPopupContent {	
			return _activePopupContent;
		}
		
		override public function set activePopupContent(value:IPopupContent):void {	
			_activePopupContent = value;
			if (activePopupContent != null) {
				activePopupContent.popupHost = this;
			}
			refreshActions(_activePopupContent);
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			if (data.leftActiveComponent) {
				leftActiveComponent = IVisualElement(data.leftActiveComponent);
			}
			
			if (data.rightActiveComponent) {
				rightActiveComponent = IVisualElement(data.rightActiveComponent);
			}
			
			if (data.visibleOnlyOnEmptySelection) {
				visibleOnlyOnEmptySelection = data.visibleOnlyOnEmptySelection;
			}
			
			if (data.destructionPolicy) {
				destructionPolicy = data.destructionPolicy;
			}

			activePopupContent = leftActiveComponent is IPopupContent ? IPopupContent(leftActiveComponent) : null;
		}
		
		override protected function getActionsFromPopupContent(popupContent:IPopupContent, selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = super.getActionsFromPopupContent(popupContent, selection);
			result.push(toggleOneViewModeAction);
			result.push(toggleOneViewModeLeftViewActive);
			return result;
		}
		
	}
}