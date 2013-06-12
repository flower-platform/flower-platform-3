package com.crispico.flower.util.panel
{
	import com.crispico.flower.flexdiagram.util.common.BitmapContainer;
	import com.crispico.flower.util.spinner.ModalSpinner;
	import com.crispico.flower.util.spinner.ModalSpinnerSupport;
	
	import flash.display.DisplayObject;
	
	import mx.containers.TitleWindow;
	import mx.core.IFlexDisplayObject;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Panel that allows the use of custom icons, mainly
	 * meant to be used with images retrieved from <code>ImageFactory</code>.	
	 * 
	 * @author Cristina 
	 * @flowerModelElementId _a4O5kWEjEeGrW-vIIQ_SdA
	 */  
	public class CustomTitlePanel extends TitleWindow implements ModalSpinnerSupport {
		
		private var _modalSpinner:ModalSpinner;
		
		/**
		 * The icon URL displayed in the title bar.
		 */ 
		public var titleIconURL:String;
		
		/**
		 * If <code>titleIconURL</code> is set, replaces the image class
		 * with a <code>BitmapContainer</code> object.
		 */ 
		override protected function commitProperties():void {			
			var newIcon:IFlexDisplayObject;
			if (titleIconURL != null) {			
				newIcon = new BitmapContainer(titleIconURL);
				titleIcon = null;
			}			
			super.commitProperties();
			
			if (newIcon != null) {
				mx_internal::titleIconObject = newIcon;
				titleBar.addChild(DisplayObject(mx_internal::titleIconObject));  				
				if (initialized)
                	layoutChrome(unscaledWidth, unscaledHeight);                    
	 		}
		}
		
		public function get modalSpinner():ModalSpinner	{
			return _modalSpinner;
		}
		
		public function set modalSpinner(value:ModalSpinner):void {
			_modalSpinner = value;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (modalSpinner != null) {
				// reposition the modal spinner so that we leave the title bar uncovered
				modalSpinner.y = titleBar.height;
				modalSpinner.setActualSize(unscaledWidth, unscaledHeight - titleBar.height);
			}
		}

	}
}