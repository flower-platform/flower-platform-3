package com.crispico.flower.flexdiagram.samples.basic.action {
	import com.crispico.flower.flexdiagram.action.BaseAction;
	
	import flash.filesystem.File;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	
	public class SampleAction extends BaseAction {
	
		[Embed(source = "..//assets//task16.png")]
		public var upCls:Class;
		
		public function SampleAction(title:String) {
			super();
			this.label = title;
			this.image = upCls;
		}
		
		override public function run(selectedEditParts:ArrayCollection):void {
			Alert.show("Hello!");
		}
	}
}