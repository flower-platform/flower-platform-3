package com.crispico.flower.flexdiagram.print {
	
	import com.crispico.flower.flexdiagram.FlexDiagramAssets;
	
	import mx.containers.Form;
	import mx.containers.FormItem;
	import mx.containers.VBox;
	import mx.controls.CheckBox;
	import mx.controls.HSlider;
	import mx.controls.RadioButton;
	import mx.controls.RadioButtonGroup;
	import mx.core.UIComponent;
		
	/**
	 * Dialog displaying options for PDF export.
	 * 
	 * @author Luiza
	 * @flowerModelElementId _DY18IJZuEeCxFYVWO2w7oA
	 */
	public class ExportOptionsDialog extends PrintOptionsDialog {
					
		/**
		 * @private
		 * @flowerModelElementId _k6KeQJaQEeCy8_KPvpsIEQ
		 */ 
		protected var qualitySlider:HSlider;
		
		/**
		 * @private
		 * @flowerModelElementId _k6OIoJaQEeCy8_KPvpsIEQ
		 */ 
		protected var pageOrientationGroup:RadioButtonGroup; 
		
		protected var removeIconsCheck:CheckBox;
				
		
		override public function initialize():void {
			super.initialize();
			title = FlexDiagramAssets.INSTANCE.getMessage('UI_PDF_Export_Dialog_Title');
			this.titleIcon = FlexDiagramAssets.INSTANCE.pdf_icon;
		}
		
		
		override protected function createOptions():void {
			// exclude number of horizontal pages (the gantt width must fit a single page when doing PDF export)		
			qualitySlider = new HSlider();
			qualitySlider.setStyle("dataTipPlacement", "top");
			qualitySlider.setStyle("tickColor", "black");
			qualitySlider.width = 120; 
			qualitySlider.minimum = ExportOptions.MIN_QUALITY;
			qualitySlider.maximum = ExportOptions.MAX_QUALITY;
			qualitySlider.value = 1.5;
			qualitySlider.snapInterval = 0.1;
			qualitySlider.tickInterval = 0.7;
			qualitySlider.allowTrackClick = true;
			qualitySlider.labels = ["Low", "Med", "High"];
						
			var form:Form = new Form();
			form.autoLayout = false;
			
			var formItem:FormItem = new FormItem();
						
			formItem = new FormItem();
			formItem.width = 260;
			formItem.setStyle("labelWidth", 110);
			formItem.setStyle("horizontalAlign", "left");
			formItem.label = FlexDiagramAssets.INSTANCE.getMessage('UI_PDF_Export_printLayotGroup_Label');
			formItem.addChild(createPrintLayoutGroup());
			
			form.addChild(formItem);
			
			formItem = new FormItem();
			formItem.width = 260;
			formItem.setStyle("labelWidth", 110);
			formItem.setStyle("horizontalAlign", "left");
			formItem.label = FlexDiagramAssets.INSTANCE.getMessage('UI_PDF_Export_qualitySlider_Label');
			formItem.addChild(qualitySlider);
			
			form.addChild(formItem);
			
			addChild(form);	
			
			removeIconsCheck  = new CheckBox;
			removeIconsCheck.label = FlexDiagramAssets.INSTANCE.getMessage('UI_PDF_Export_removeIconsCheck_Label');
			removeIconsCheck.selected = false;
			addChild(removeIconsCheck);
		}
		
		/**
		 * Creates the <code>RadioButtonGroup</code> for choosing the page orientation. Adds two RadioButtons to a container,
		 * sets their group to </code>pageOrientationGroup</code> and returns the container.
		 * Used in <code>createOptions</code>
		 * 
		 * @private
		 * @flowerModelElementId _JVmglJaREeCy8_KPvpsIEQ
		 */ 
		protected function createPrintLayoutGroup():UIComponent {
			pageOrientationGroup = new RadioButtonGroup();
			var landscapeRadioButton:RadioButton = new RadioButton();
			var portraitRadioButton:RadioButton = new RadioButton();
			
			landscapeRadioButton.label = "Landscape";
			portraitRadioButton.label = "Portrait";
			landscapeRadioButton.value = ExportOptions.LANDSCAPE;
			portraitRadioButton.value = ExportOptions.PORTRAIT; 
			
			portraitRadioButton.selected = true;			
			
			landscapeRadioButton.group = portraitRadioButton.group = pageOrientationGroup;
			
			var vBox:VBox = new VBox();
			vBox.addChild(landscapeRadioButton);
			vBox.addChild(portraitRadioButton);
			return vBox;
		}
		
		/**
		 * @flowerModelElementId _I7UtcJaTEeCy8_KPvpsIEQ
		 */
		protected override function getResult():Object {
			return new ExportOptions(String(pageOrientationGroup.selectedValue), qualitySlider.value, removeIconsCheck.selected);
		}

	}
}