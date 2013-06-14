package org.flowerplatform.web.mobile.popup {
	import flash.display.Bitmap;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.components.Button;
	import spark.components.Label;
	import spark.components.View;
	import spark.primitives.BitmapImage;
	
	public class WrapperView extends View implements IPopupHost {
		
		protected var iconComponent:BitmapImage;
		
		protected var labelComponent:Label;
		
		override protected function createChildren():void {
			super.createChildren();
			
			iconComponent = new BitmapImage();
			labelComponent = new Label();
			titleContent = [iconComponent, labelComponent]; 
			
			var visualElementClass:Class = data.popupContentClass;
			var e:IVisualElement = IVisualElement(new visualElementClass());
			e.percentHeight = 100;
			e.percentWidth = 100;
			addElement(e);
			
			if (data.modalOverAllApplication) {
				navigationContent = [];
			}
			
			WrapperViewPopupHandler(data.popupHandler).popupContent = e;
			
			if (e is IPopupContent) {
				var popupContent:IPopupContent = IPopupContent(e);
				popupContent.popupHost = this;
				
				actionContent = null;
				var newActionContent:Array = new Array();
				var actions:Vector.<IAction> = popupContent.getActions(); 
				for (var i:int = 0; i < actions.length; i++) {
					var action:IAction = actions[i];
					
					var button:Button = new Button();
					button.label = action.label;
					newActionContent.push(button);
				}
				actionContent = newActionContent;
			}
		}
		
		public function refreshActions(popupContent:IPopupContent):void {
			// TODO Auto Generated method stub
			
		}
		
		public function setIcon(value:Object):void {
			iconComponent.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(value);
		}
		
		public function setLabel(value:String):void {
			labelComponent.text = value;			
		}
		
	}
}