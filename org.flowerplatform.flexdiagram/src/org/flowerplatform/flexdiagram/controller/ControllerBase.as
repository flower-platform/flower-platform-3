package org.flowerplatform.flexdiagram.controller {
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ControllerBase {
		
		private var _diagramShell:DiagramShell;
		
		public function get diagramShell():DiagramShell {
			return _diagramShell;
		}
		
		public function ControllerBase(diagramShell:DiagramShell) {
			_diagramShell = diagramShell;
		}

	}
}