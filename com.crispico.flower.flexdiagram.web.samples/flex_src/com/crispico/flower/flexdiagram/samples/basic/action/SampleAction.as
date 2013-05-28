package com.crispico.flower.flexdiagram.samples.basic.action {
	import com.crispico.flower.flexdiagram.action.BaseAction;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	
	public class SampleAction extends BaseAction {
	
		public function SampleAction(title:String) {
			super();
			this.label = title;
			this.image = "../assets/task16.png";
		}
		
		override public function run(selectedEditParts:ArrayCollection):void {
			Alert.show("Hello!");
		}
	}
}