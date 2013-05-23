package com.crispico.flower.flexdiagram.print {
		
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.IComposedFigure;
	import com.crispico.flower.flexdiagram.dialog.IDialog;
	import com.crispico.flower.flexdiagram.dialog.IDialogResultHandler;
	
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	import mx.core.Container;
	import mx.core.UIComponent;
	import mx.core.UIComponentGlobals;
	import mx.core.mx_internal;
	import mx.managers.PopUpManager;
	import mx.printing.FlexPrintJob;
	import mx.printing.FlexPrintJobScaleType;
	
	/**
	 * Base object used for printing a diagram. Each DiagramViewer should offer a specialized PrintManager capable of printing the diagram.
	 * 
	 * @author Luiza
	 * 
	 * @flowerModelElementId _TXtCoFkPEeC-9dy778yP1Q
	 */
	public class PrintManager /*extends AbstractPrintExportManager*/ implements IDialogResultHandler {
		
		/**
		 * @flowerModelElementId _PlXN4FkZEeC-9dy778yP1Q
		 */
		protected var diagramViewer:DiagramViewer;
		
		protected var flexPrintJob:FlexPrintJob;
		
		protected var vectorialPrint:Boolean;
		
		/**
		 * Keeps the selected elements on the diagram when the printing starts to be able to restore the selection after print.
		 * 
		 * @see #prePrint()
		 * @see #postPrint()
		 */
		protected var selectedElements:Array;
		
		/**
		 * Function called when printing ends. Useful when one expects a notification about print status. 
		 * This is optional and it is mainly used by ToolbarButtons or other components that may trigger printing.
		 */ 
		public var notifyPrintEndedFunction:Function;
		
		/**
		 * Calls #createPrintingDialog(), #positionPrintingDialog() and sets this <code>PrintManager</code> as handler.
		 * The expected result is made of aditional parameters for printing - for instance the number of pages to use.
		 */ 
		public function doPrintWithDialog():void {
			var dialog:IDialog = createPrintingDialog();
			positionPrintingDialog(dialog);
			dialog.setResultHandler(this);
		}
		
		/**
		 * Create a new FlexPrintJob with printAsBitmap option on true and starts the print job. This will allow the user to choose 
		 * the printer, the page style and size.
		 * Calls #prePrint() to obtain the component to print. It is expected to be in the final print size.
		 * Calls #print() passing the component to print.
		 * Calls #postPrint() to cancel any modifications on the printed component that were made during #prePrint().
		 * 
		 * @param param - additional parameters for printing; they are usually resulted from <code>PrintOptionsDialog</code> calls.
		 */
		public function doPrint(param:*=null):void {
			if (flexPrintJob == null) {
				flexPrintJob = new FlexPrintJob();
				flexPrintJob.printAsBitmap = true;
			} else {
				throw new Error("Print job already in progress!");
			}
			
			if (flexPrintJob.start()) {
				var printView:Container = prePrint(param);
				print(printView);
				postPrint();
			}
			flexPrintJob = null;
			if (notifyPrintEndedFunction != null) {
				notifyPrintEndedFunction();
				notifyPrintEndedFunction = null;
			}
		}
		
		/**
		 * If the received <code>result</code> is not <code>null</code> then calls <code>doPrint(result)</code>.
		 */ 
		public function handleDialogResult(result:Object):void {
			if (result != null) { // null means cancel or aborted
				doPrint(result);
			} else if (notifyPrintEndedFunction != null) {
				notifyPrintEndedFunction();
				notifyPrintEndedFunction = null;
			}
		}
		
		/**
		 * Called after <code>flexPrintJob</code> has been started. As a result, any modifications on the diagram will no longer 
		 * be visible at this point because the screen is freezing. 
		 * 
		 * <p>Clears the selection on diagram and returns <code>null</code>.
		 * 
		 * <p>Users must override this function to prepare the diagram for printing and return the actual component that will be printed - 
		 * this may be the diagram itself, a part of the diagram or another <code>Container</code> containing parts of the diagram.
		 * 
		 * <p>The preparation may involve several actions like: changing the size of the diagram, changing the scale, moving some 
		 * components etc.
		 * @flowerModelElementId _0wGf0FkREeC-9dy778yP1Q
		 */ 
		protected function prePrint(param:*):Container {
			// copy the selected elements to restore them after print
			selectedElements = diagramViewer.getSelectedElements().source.slice();
			// remove selection
			diagramViewer.resetSelection();
			
			return null;
		}
		
		use namespace mx_internal;
		
		/**
		 * Force layout validation and calls <code>DiagramScaleListener.blockTextTruncation(printView)</code> to avoid labels 
		 * being truncated during scaling. 
		 * Sends <code>printView</code> to printer.
		 * Calls <code>DiagramScaleListener.INSTANCE.restoreOriginalFonts()</code> to restore the original look of the labels.
		 *  
		 * @flowerModelElementId _1J1OcFkREeC-9dy778yP1Q
		 */
		protected function print(printView:Container):void {
			// this is used to force layout validation for the content
			// so that DiagramScaleListener can notice text truncation
			UIComponentGlobals.layoutManager.usePhasedInstantiation = false;
       		UIComponentGlobals.layoutManager.validateNow();
			DiagramScaleListener.INSTANCE.blockTextTruncation(printView);		
			
			// this can't be done in preprint because not all the figures are on the diagram yet
			if (vectorialPrint) {
				flexPrintJob.printAsBitmap = false;
				DiagramIconsListener.INSTANCE.removeIcons(printView);				
			}		
			
			try {		
				flexPrintJob.addObject(printView, FlexPrintJobScaleType.NONE);
			} catch (anyErr:Error) {
				// swallow -  print job must end anyway
				// this can happen if printing with MS XPS or PDF Printer when choosing cancel instead of giving a name for the file
			}
			flexPrintJob.send(); // send and finish
			
			DiagramScaleListener.INSTANCE.restoreOriginalFonts();
			
			if (vectorialPrint) {
				DiagramIconsListener.INSTANCE.restoreIcons();
			}
		}
		
		/**
		 * Called after #print(), this function should cancel any modifications made by #prePrint().
		 * 
		 * <p>By default restores the selection.
		 * 
		 * @flowerModelElementId _1Xq2IFkREeC-9dy778yP1Q
		 */
		protected function postPrint():void {
			diagramViewer.addToSelection(new ArrayCollection(selectedElements), true);
			selectedElements = null;
		}
		
		/**
		 * Given a DisplayObject and a maximum target size - <code>targetWidth</code> and <code>targetHeight</code>, 
		 * returns the scale that must be applied in order to fit the target size.
		 * 
		 * <p>If <code>obj</code> is smaller then target size and <code>canScaleUp</code> is <code>false</code> the returned scale is 1.
		 * 
		 * @flowerModelElementId _FEQjcFkVEeC-9dy778yP1Q
		 */
		protected function getScale(obj:DisplayObject, targetWidth:Number, targetHeight:Number, canScaleUp:Boolean):Number {
			var increaseSize:Boolean = canScaleUp && obj.width < targetWidth && obj.height < targetHeight;
			
			if (obj.width > targetWidth || obj.height > targetHeight || increaseSize) {
		  		var sx:Number = targetWidth / obj.width;
		  		var sy:Number = targetHeight / obj.height;
		  		// round the scale with one decimal
		  		return Math.floor(Math.min(sx, sy) * 10) / 10;
		 	}
		 	return 1;
		}
		
		/**
		 * Returns a new <code>PrintOptionsDialog</code>. Users may overwrite this function and return custom Print Dialogs.
		 * This dialog will be shown before printing starts and should return additional parameters: for instance the number of pages
		 * to use, wheather the diagram will be scaled up to fit the number of pages, etc.
		 * 
		 * <p>This dialog will be used in collaboration with #doPrintWidthDialog() function.
		 * <b>If the dialog response is <code>null</code> then the printing is canceled. 
		 * @flowerModelElementId _zPZWcFnfEeCmRqQ0cEpkTA
		 */ 
		protected function createPrintingDialog():IDialog {
			return new PrintOptionsDialog();
		}
		
		/**
		 * Displays and positions the printing dialog centered in relation to the printed diagram.
		 * @flowerModelElementId _zPZ9gFnfEeCmRqQ0cEpkTA
		 */ 
		protected function positionPrintingDialog(dialog:IDialog):void {
			PopUpManager.addPopUp(UIComponent(dialog), DisplayObject(Application.application), true);
			PopUpManager.centerPopUp(UIComponent(dialog));
		}
	}
}