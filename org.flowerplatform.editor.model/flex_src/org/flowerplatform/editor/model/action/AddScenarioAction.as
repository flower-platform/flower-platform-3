/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.emf_model.notation.Diagram;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AddScenarioAction extends TextInputAction {
		
		public function AddScenarioAction() {
			super();
			
			label = "Add Scenario";
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/chart_line_add.png");
			preferShowOnActionBar = true;
			orderIndex = 700;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Diagram) {
				// visible only on diagram
				return true;
			}
			return false;
		}
		
		override public function run():void {
			askForTextInput("Scenario 1", "Add Scenario", "Add", 
				function(name:String):void {
					notationDiagramEditorStatefulClient.service_addNewScenario(name);
				});
		}
	}
}