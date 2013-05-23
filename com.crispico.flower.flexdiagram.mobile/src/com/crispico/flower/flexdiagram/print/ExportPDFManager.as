package com.crispico.flower.flexdiagram.print {
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.dialog.IDialog;
	import com.crispico.flower.flexdiagram.dialog.IDialogResultHandler;
	
	import flash.display.DisplayObject;
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	import mx.core.Container;
	import mx.core.UIComponent;
	import mx.managers.PopUpManager;
	
	import org.alivepdf.display.Display;
	import org.alivepdf.layout.Layout;
	import org.alivepdf.layout.Mode;
	import org.alivepdf.layout.Orientation;
	import org.alivepdf.layout.Position;
	import org.alivepdf.layout.Resize;
	import org.alivepdf.layout.Size;
	import org.alivepdf.layout.Unit;
	import org.alivepdf.pdf.PDF;
	import org.alivepdf.saving.Method;
	
	/**
	 * Base manager for PDF export. This is an abstract class, it does not create the content to export and it does not provide saving.
	 * 
	 * <p>
	 * User should use <code>doExportWidthDialog()</code> to start a new <code>ExportOptionsDialog</code> or just call <code>doExport(param)</code>,
	 * passing as parameter an <code>ExportOptions</code> instance.
	 * 
	 * @see com.crispico.flower.flexdiagram.print.ExportOptions
	 * @see com.crispico.flower.flexdiagram.print.ExportOptionsDialog
	 * 
	 * @author Luiza
	 *  
	 * @flowerModelElementId _DY1VEJZuEeCxFYVWO2w7oA
	 */
	public class ExportPDFManager implements IDialogResultHandler {
		
		/**
		 * @flowerModelElementId _OeVWwJjHEeCdZMUSEY04IA
		 */
		protected var diagramViewer:DiagramViewer;
		
		
		/**
		 * @flowerModelElementId _EIe2MJdnEeCKA_g6f9YZjA
		 */
		protected var exportedContent:UIComponent;
		
		/**
		 * Holds the selected elements to restore them after the export.
		 * 
		 * @flowerModelElementId _hN9-oJdnEeCKA_g6f9YZjA
		 */
		protected var selection:Array;
		
		/**
		 * @flowerModelElementId _OeWk4JjHEeCdZMUSEY04IA
		 */
		protected var options:ExportOptions;
		
		/**
		 * Optional function called after the export is finished.
		 * 
		 * @flowerModelElementId _xOS8wJdpEeCKA_g6f9YZjA
		 */
		public var finishCallbackFunction:Function;
				
		/**
		 * @flowerModelElementId _OecrgZjHEeCdZMUSEY04IA
		 */
		protected function createExportDialog():IDialog {
			return new ExportOptionsDialog();
		}
		/**
		 * @flowerModelElementId _OedSkJjHEeCdZMUSEY04IA
		 */
		protected function positionExportDialog(dialog:IDialog):void {
			PopUpManager.addPopUp(UIComponent(dialog), DisplayObject(Application.application), true);
			PopUpManager.centerPopUp(UIComponent(dialog));
		}
		/**
		 * If the received <code>result</code> is not <code>null</code> then calls <code>doExport(result)</code>.
		 * @flowerModelElementId _OecEcJjHEeCdZMUSEY04IA
		 */ 
		public function handleDialogResult(result:Object):void {
			// null means cancel or aborted
			if (result != null) {
				doExport(ExportOptions(result));
			} else if (finishCallbackFunction != null) {
				// announce export finished
				finishCallbackFunction();
				// remove the reference
				finishCallbackFunction = null;
			}
		}
		
		/**
		 * @flowerModelElementId _OeYaEJjHEeCdZMUSEY04IA
		 */
		public function doExportWithDialog():void {
			var dialog:IDialog = createExportDialog();
			positionExportDialog(dialog);
			dialog.setResultHandler(this);
		}
		
		/**
		 * @flowerModelElementId _IOj6wJdnEeCKA_g6f9YZjA
		 */
		public function doExport(param:ExportOptions):void {
			this.options = param;
			prepareContents();
			prepareGraphics();
			var encodedContent:ByteArray = doEncoding();
			handleEncodedContent(encodedContent);
			restoreGraphics();
			restoreContents();
			
			exportedContent = null;
			options = null;
			if (finishCallbackFunction != null) {
				// announce export finished
				finishCallbackFunction();
				// remove the reference
				finishCallbackFunction = null;
			}
		}
		
		/**
		 * Removes and saves the selection on the diagram to restore it after the export.
		 * The selection must be handled at the begining because the content holding the figures
		 * might be moved from the diagram for export purpose.
		 * 
		 * This function should also prepare the <code>exportedContent</code> (resize, scale, move etc).
		 *  
		 * @flowerModelElementId _8wswUJdmEeCKA_g6f9YZjA
		 */
		protected function prepareContents():void {
			// TODO:Luiza this part is common with printing algorithm
			
			// copy the selected elements to restore them after print
			selection = diagramViewer.getSelectedElements().source.slice();
			// remove selection
			diagramViewer.resetSelection();
		}
		
		/**
		 * Restore selection. This function should do the inverse operations of the 
		 * #prepareContents().
		 *  
		 * @flowerModelElementId _DQuu0JdnEeCKA_g6f9YZjA
		 */
		protected function restoreContents():void {
			// restore the selected elements
			diagramViewer.addToSelection(new ArrayCollection(selection), true);
			selection = null;
		}
	
		/**
		 * Removes selection, apply font resize on labels to avoid text truncation, remove icons etc.
		 * Performs preliminary actions before the actual encoding.
		 * 
		 * @flowerModelElementId _Ila7kJdnEeCKA_g6f9YZjA
		 */
		protected function prepareGraphics():void {
			DiagramScaleListener.INSTANCE.blockTextTruncation(Container(exportedContent));
			if (options.removeIcons) {
				DiagramIconsListener.INSTANCE.removeIcons(exportedContent);
			}
		}
		
		/**
		 * Restore modifications done by the <code>prepareGraphics()</code>.
		 * 
		 * @flowerModelElementId _IyE3gJdnEeCKA_g6f9YZjA
		 */
		protected function restoreGraphics():void {
			DiagramScaleListener.INSTANCE.restoreOriginalFonts();
			if (options.removeIcons) {
				DiagramIconsListener.INSTANCE.restoreIcons();
			}
		}
				
		/**
		 * @flowerModelElementId _tCT58JdoEeCKA_g6f9YZjA
		 */
		protected function doEncoding():ByteArray {
			var orientation:String = (options.pageOrientation == ExportOptions.LANDSCAPE) ? Orientation.LANDSCAPE : Orientation.PORTRAIT;
			var printPDF:PDF = new PDF(orientation, Unit.MM, Size.A4);	
			printPDF.setDisplayMode(Display.FULL_PAGE, Layout.SINGLE_PAGE);
			printPDF.addPage();
			
			// resize the page to fit the component
			printPDF.addImage(exportedContent, new Resize(Mode.RESIZE_PAGE, Position.CENTERED));
			return printPDF.save(Method.LOCAL);
		}
		
		/**
		 * Save or publish to download the encoded content.
		 * This function should call the <code>finishCallbackFunction</code> when not <code>null</code>
		 * 
		 * @flowerModelElementId _tRT_QJdoEeCKA_g6f9YZjA
		 */
		protected function handleEncodedContent(content:ByteArray):void {
			throw new Error("handleEncodedContent() must be implemented")
		}
	}
}