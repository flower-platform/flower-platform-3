package com.crispico.flower.flexdiagram.tool {
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.IFigure;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	
	import mx.core.Application;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.logging.ILogger;
	import mx.logging.Log;
	
	
	/**
	 * If there is more than one diagram in the application, 
	 * the FD platform has a known limitation: the active tool(s) of
	 * each diagram are listening simultaneously and they interfer with
	 * each other.
	 * 
	 * <p>
	 * The reason of this singleton class (which is a pseudo-tool) is to
	 * provide a work-around for this issue. At each mouse-move it ensures
	 * that only the diagram under the cursor is active (and
	 * the other ones ar not).
	 * 
	 * @author Cristi
	 * 
	 */ 
	public class MultiDiagramWorkAroundTool	extends EventDispatcher {
		
		private static const LOGGER:ILogger = Log.getLogger("com.crispico.flower.flexdiagram.tool.MultiDiagramWorkAroundTool"); 
		
		/**
		 * @see Getter.
		 */
		private static var _INSTANCE:MultiDiagramWorkAroundTool;
		
		/**
		 * @see Getter.
		 */
		private var _currentViewer:DiagramViewer;
		
		// CS-VC
		private var wasInsideViewer:Boolean;

		// CS-VC
		private var activeViewersCounter:int;

		// CS-VC
		private var activeViewerCounter2:int;
		
		/**
		 * Read only property for singleton instance; lazy initialized.
		 */
		public static function get INSTANCE():MultiDiagramWorkAroundTool {
			if (_INSTANCE == null) {
				_INSTANCE = new MultiDiagramWorkAroundTool();
			}
			return _INSTANCE;
		}
		
		public function MultiDiagramWorkAroundTool() {
			if (Application.application.stage != null)
				addMouseMoveListener(null);
			else
				Application.application.addEventListener(Event.ADDED_TO_STAGE, addMouseMoveListener);	
		}
		
		private function addMouseMoveListener(event:Event):void {
			Application.application.removeEventListener(Event.ADDED_TO_STAGE, addMouseMoveListener);
			Application.application.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
		}
		
		/**
		 * The current viewer. This is the last viewer
		 * over which the mouse was during last movement. The viewer is usually
		 * active, unless someone has explicitly deactivated it.
		 */
		public function get currentViewer():DiagramViewer {
			return _currentViewer;
		}
		
		public function set currentViewer(value:DiagramViewer):void {
			LOGGER.debug("Set currentViewer to {0}", value);
			this._currentViewer = value;
		}
		
		/**
		 * Checks if the current viewer has changed (and if so, the old one is deactivated and
		 * the new one is activated).
		 */ 
		private function mouseMoveHandler(event:MouseEvent):void {
			// loop to determine the viewer under the mouse; stops at the first IFigure
			var viewerUnderMouse:DiagramViewer = null;
			var object:DisplayObject = DisplayObject(event.target);
			while (object != null) {
				if (object is IFigure && IFigure(object).getEditPart() != null) {
					viewerUnderMouse = IFigure(object).getEditPart().getViewer();
					break;
				}
				object = object.parent;
			}

			// CS-VC
			if (wasInsideViewer && viewerUnderMouse == null)
				dispatchEvent(new CollectionEvent(CollectionEventKind.RESET));
			else if (!wasInsideViewer && viewerUnderMouse != null) {
				var event1:CollectionEvent = new CollectionEvent(CollectionEventKind.UPDATE);
				if (activeViewerCounter2 == 0) {
					if (activeViewersCounter > 300) {
						activeViewerCounter2++;
						event1.location = activeViewersCounter;
						activeViewersCounter = 0;
					}
				} else {
					if (activeViewersCounter > 1500) {
						activeViewerCounter2++;
						event1.location = activeViewersCounter;
						activeViewersCounter = 0;
					}
				}
				dispatchEvent(event1);
			}
			if (viewerUnderMouse != null) {
				wasInsideViewer = true;
				activeViewersCounter++;
			} else {
				wasInsideViewer = false;
			}
			
			// check and activate/deactivate as needed
			if (viewerUnderMouse != null && viewerUnderMouse != _currentViewer) {
				if (_currentViewer != null) {
					LOGGER.debug("Deactivate viewer {0}", _currentViewer);
					// deactivate, by leaving the activateRequested flag to it's original state
					_currentViewer.deactivate(false);
				}
				LOGGER.debug("Change current viewer to {0}", viewerUnderMouse);
				_currentViewer = viewerUnderMouse;
				if (_currentViewer.activateRequested) {
					LOGGER.debug("Activate current viewer {0}", _currentViewer); 
					// activate only if a pending activation is in progress
					_currentViewer.activate();
				}
			}
		}

	}
}