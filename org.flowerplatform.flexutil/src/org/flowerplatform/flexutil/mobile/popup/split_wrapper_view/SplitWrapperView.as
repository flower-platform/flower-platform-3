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
		
		private var _leftActiveComponent:IVisualElement;
		
		private var _rightActiveComponent:IVisualElement;
		
		protected var toggleOneViewModeAction:ToggleOneViewModeAction;
		
		protected var toggleOneViewModeLeftViewActive:ToggleOneViewModeLeftViewActiveAction;
		
		public var switchActionsVisibleOnNonEmptySelection:Boolean;
		
		protected var childrenCreated1:Boolean;
		
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
			
			if (childrenCreated1) {
				// we have this flag to avoid calling this method (and dispatching selection) when the
				// component is being created (i.e. in createChildren())
				var activeComponent:IVisualElement = oneViewModeLeftViewActive ? leftActiveComponent : rightActiveComponent;
				setActivePopupContent(activeComponent is IPopupContent ? IPopupContent(activeComponent) : null);
			}
		}
		
		protected function leftOrRightComponentFocusInHandler(event:FocusEvent):void {
			// .target is the element that actually triggered the event
			setActivePopupContent(event.currentTarget is IPopupContent ? IPopupContent(event.currentTarget) : null, true);
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			if (data != null) {
				if (data.leftActiveComponent) {
					leftActiveComponent = IVisualElement(data.leftActiveComponent);
				}
				
				if (data.rightActiveComponent) {
					rightActiveComponent = IVisualElement(data.rightActiveComponent);
				}
				
				if (data.switchActionsVisibleOnNonEmptySelection ) {
					switchActionsVisibleOnNonEmptySelection = data.switchActionsVisibleOnNonEmptySelection;
				}
				
				if (data.destructionPolicy) {
					destructionPolicy = data.destructionPolicy;
				}
			}

			childrenCreated1 = true;
			setActivePopupContent(leftActiveComponent is IPopupContent ? IPopupContent(leftActiveComponent) : null);
		}
		
		override protected function getActionsFromPopupContent(popupContent:IPopupContent, selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = super.getActionsFromPopupContent(popupContent, selection);
			result.push(toggleOneViewModeAction);
			result.push(toggleOneViewModeLeftViewActive);
			return result;
		}
		
	}
}