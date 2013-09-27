package org.flowerplatform.flexutil.samples.context_menu {
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.samples.renderer.MultipleIconItemRendererSample;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleAction1 extends ActionBase {
		public function SampleAction1() {
			super();
			label = "Sample Action 1";
			icon = MultipleIconItemRendererSample.infoImage;
			preferShowOnActionBar = true;
		}
	}
}