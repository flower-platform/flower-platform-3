package com.crispico.flower.flexdiagram.print {
	
	import com.crispico.flower.flexdiagram.FlexDiagramAssets;
	import com.crispico.flower.flexdiagram.dialog.IDialog;
	import com.crispico.flower.flexdiagram.dialog.IDialogResultHandler;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.containers.Form;
	import mx.containers.FormItem;
	import mx.containers.HBox;
	import mx.containers.Panel;
	import mx.controls.Button;
	import mx.controls.CheckBox;
	import mx.controls.Image;
	import mx.controls.NumericStepper;
	import mx.managers.PopUpManager;

	/**
	 * Dialog displaying printing options.
	 * 
	 * <p>The <code>IDialogResultHandler</code> attached will receive the result of this dialog when it closes.
	 * The options will be sent as an Array with three elements. First two indicating the number of pages
	 * on horizontal and vertical directions and a Boolean indicating if scaling up is allowed.
	 * Note that if "Cancel" is selected the result will be <code>null</code>.
	 * 
	 * @see PrintManager for other details about printing.
	 * 
	 * @author Luiza
	 * @flowerModelElementId _k0U4ADUUEeCTrKdImkKvZg
	 */ 
	public class PrintOptionsDialog extends Panel implements IDialog {
		
		/**
		 * @flowerModelElementId _k0VfETUUEeCTrKdImkKvZg
		 */
		private static var BUTTON_WIDTH:int = 80;	
		
		
		/**
		 * The Dialog Result Handler that will receive the printing options when the dialog closes.
		 * @flowerModelElementId _k0VfEzUUEeCTrKdImkKvZg
		 */ 
		private var resultHandler:IDialogResultHandler;

		/**
		 * The button used to start the printing process.
		 * @flowerModelElementId _k0VfFDUUEeCTrKdImkKvZg
		 */
		protected var okButton:Button = new Button();
		
		/**
		 * The button used to cancel the printing process.
		 * @flowerModelElementId _k0WGIjUUEeCTrKdImkKvZg
		 */
		protected var cancelButton:Button = new Button();	
		
		/**
		 * Holds number of pages for horizontal direction
		 * @flowerModelElementId _k0WGJTUUEeCTrKdImkKvZg
		 */ 
		protected var hStepper:NumericStepper;
		
		/**
		 * Holds number of pages for vertical direction
		 * @flowerModelElementId _k0WtMTUUEeCTrKdImkKvZg
		 */ 
		protected var vStepper:NumericStepper;				
		
		/**
		 * By default this is checked, indicating that the diagram will be scaled up if necessary
		 * to cover as much space as possible vertically or/and horizonally depending on the diagram original size.
		 * <p>
		 * When unchecked, the diagram be printed at maximum size if the space allows it but will not become bigger 
		 * to fit extra pages.
		 * This is useful for smaller diagrams that fit less then one page to avoid 
		 * scaling up - in which case classes would be enlarged to cover the page(s).  
		 * @flowerModelElementId _k0WtMzUUEeCTrKdImkKvZg
		 */ 
		protected var fitPagesCheck:CheckBox; 
		
		/**
		 * By default this is not checked, indicating theat tha diagram will be printed in bitmap mode.
		 * To print in vectorial mode this CheckBox needs to be checked.
		 * <p>
		 * Printing in vectorial mode creates a much better quality image and works faster but transparency 
		 * is not supported and images don't look nice unless theya are vectorial images.
		 * <p>
		 * The printing algorithm will remove all icons when doing vectorial print.
		 */ 
		protected var printVectorialCheck:CheckBox;
		
		/**
		 * @flowerModelElementId _k0WtNDUUEeCTrKdImkKvZg
		 */
		public function PrintOptionsDialog() {
			super();
		}
		
		public function setResultHandler(resultHandler:IDialogResultHandler):void {
			this.resultHandler = resultHandler;
		}
		
		
		/**
		 * @flowerModelElementId _k0XUQjUUEeCTrKdImkKvZg
		 */
		override public function initialize():void {
			super.initialize();
			title = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_Dialog_Title');
			this.titleIcon = FlexDiagramAssets.INSTANCE.print_icon;
			this.setStyle("paddingTop", 15);
			this.setStyle("paddingBottom", 15);
			this.setStyle("paddingLeft", 15);
			this.setStyle("paddingRight", 15);
			this.setStyle("verticalAlign", "middle");
			this.minWidth = 280;
			this.minHeight = 150;
			
			createOptions();
			createButtons();
			
			okButton.addEventListener(MouseEvent.CLICK, buttonClickedHandler);
			cancelButton.addEventListener(MouseEvent.CLICK, buttonClickedHandler);
			stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDown);
		}
				
		/**
		 * The method creates the printing options. Two Nummeric Steppers (values from 1 to 10) and a Check Box 
		 * will give these options. By default they start from 1, so the diagram is scaled to fit one page.
		 * @flowerModelElementId _k0X7UjUUEeCTrKdImkKvZg
		 */
		protected function createOptions():void {
			hStepper = new NumericStepper();
			vStepper = new NumericStepper();
			hStepper.minimum = vStepper.minimum = 1;
			hStepper.maximum = vStepper.maximum = 10;
			hStepper.value = vStepper.value = 1;
			
			var form:Form = new Form();
			form.label = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_optionsForm_Label');
			form.minWidth = 250;
			
			var formItem:FormItem = new FormItem();
			formItem.direction = "horizontal";
			formItem.label = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_hPagesStepper_Label');
			formItem.addChild(hStepper);
			
			var image:Image = new Image();
			image.source = FlexDiagramAssets.INSTANCE.info_icon;
			image.toolTip = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_hPagesStepper_Tooltip');
			formItem.addChild(image);
			
			form.addChild(formItem);
			
			formItem = new FormItem();
			formItem.direction = "horizontal"
			formItem.label = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_vPagesStepper_Label');
			formItem.addChild(vStepper);
			
			image = new Image();
			image.source = FlexDiagramAssets.INSTANCE.info_icon;
			image.toolTip = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_vPagesStepper_Tooltip');
			formItem.addChild(image);
			
			form.addChild(formItem);
			
			addChild(form);
			
			var hBox:HBox = new HBox();
			fitPagesCheck = new CheckBox();
			fitPagesCheck.label = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_fitPagesCheck_Label');
			fitPagesCheck.selected = true;
			hBox.addChild(fitPagesCheck);
			
			image = new Image();
			image.source =  FlexDiagramAssets.INSTANCE.info_icon;
			image.toolTip = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_featPagesCheck_Tooltip');
			hBox.addChild(image);
			
			addChild(hBox);
			
			hBox = new HBox()
			printVectorialCheck = new CheckBox();
			printVectorialCheck.label =  FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_vectorialPrintCheck_Label');
			printVectorialCheck.selected = false;
			hBox.addChild(printVectorialCheck);
			
			image = new Image();
			image.source =  FlexDiagramAssets.INSTANCE.info_icon;
			image.toolTip = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_vectorialPrintCheck_Tooltip');
			hBox.addChild(image);
			
			addChild(hBox);
		}	
			
		/**
		 * The method creates the two buttons:
		 * <ul>
		 * 		<li> the 'OK' button, which will trigger the printing process;
		 * 		<li> the 'Cancel' button, which will cancel the printing process;
		 * </ul>
		 * @flowerModelElementId _k0YiYDUUEeCTrKdImkKvZg
		 */
		protected function createButtons():void {
			var hbox:HBox = new HBox();
			hbox.setStyle("paddingTop", 10);
			hbox.setStyle("horizontalAlign", "right");
			hbox.percentWidth = 100;
			
			okButton.label = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_okButton');
			cancelButton.label = FlexDiagramAssets.INSTANCE.getMessage('UI_Printing_cancelButton');
			hbox.addChild(okButton);
			hbox.addChild(cancelButton);
			addChild(hbox);
			okButton.minWidth = BUTTON_WIDTH;
			cancelButton.minWidth = BUTTON_WIDTH;
		
		}	   
		
		/**
		 * Handles "OK" and "Cancel" button. If "Cancel" button is pressed the result will be <code>null</code>. 
		 * @flowerModelElementId _k0YiYzUUEeCTrKdImkKvZg
		 */ 
		private function buttonClickedHandler(event:MouseEvent):void {
			var result:Object = null;
			
			if (event.target == okButton) {
				result = getResult();
			}
			
			PopUpManager.removePopUp(this);
			
			if (resultHandler != null) {
				resultHandler.handleDialogResult(result);
			}
		}
		
		/**
		 * Keyboard event handler. If the 'ESCAPE' key is pressed,
		 * the popup closes returning <code>null</code>. Similar to "Cancel" being pressed.
		 * @flowerModelElementId _k0XURDUUEeCTrKdImkKvZg
		 */
		private function keyDown(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.ESCAPE) {
				
				PopUpManager.removePopUp(this);
				
				if (resultHandler != null) {
					resultHandler.handleDialogResult(null);
				}
				
			}
		}
		
		/**
		 * Returns the result of this dialog. By default this is an Array containing the
		 * values of the dialog default compnents: <code>[hStepper.value, vStepper.value, fitPagesCheck.selected]</code>
		 * @flowerModelElementId _BTDKgFn4EeC81OqkEmfPJg
		 */ 
		protected function getResult():Object {
			return [hStepper.value, vStepper.value, fitPagesCheck.selected, printVectorialCheck.selected];
		}
	}
}