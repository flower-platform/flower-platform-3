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
package org.flowerplatform.editor.model.renderer {
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CenteredBoxChildIconItemRenderer extends BoxChildIconItemRenderer {
		
		public function CenteredBoxChildIconItemRenderer() {
			super();
			
			setStyle("horizontalAlign", "center");
			setStyle("fontWeight", "bold");
		}
		
		override protected function set down(value:Boolean):void {
			super.down = false;
		}
		
		override protected function set hovered(value:Boolean):void {
			super.hovered = false;
		}
	}
}