package org.flowerplatform.flexdiagram.renderer {
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import org.flowerplatform.flexdiagram.DiagramShell;

	/**
	 * @author Cristian Spiescu
	 */
	public interface IDiagramShellAware {
		function get diagramShell():DiagramShell;
		function set diagramShell(value:DiagramShell):void;
	}
}