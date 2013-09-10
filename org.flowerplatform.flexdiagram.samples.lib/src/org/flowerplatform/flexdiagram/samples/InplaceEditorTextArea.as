package org.flowerplatform.flexdiagram.samples {
	import flash.events.Event;
	
	import mx.events.FlexEvent;
	
	import spark.components.TextArea;
	
	public class InplaceEditorTextArea extends TextArea {
		
		protected var initialWidth:int = -1;
		protected var offsetWidth:int = 0;
		
		public function InplaceEditorTextArea() {
			super();			
			addEventListener(Event.CHANGE, resizeBorder);
			
			heightInLines = NaN;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			if (initialWidth == -1) {
				initialWidth = this.width;
				offsetWidth = 0;
			}
		}
		
		protected function resizeBorder(event:Event=null):void {
			// 12 represents borders
			var txtWidth:Number = measureText(this.text).width + 10;
			if (txtWidth + 90 > initialWidth + offsetWidth) {
				this.width = txtWidth + 90;
				offsetWidth += 100;
				this.invalidateSize();
			}
		}

	}
}