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
package org.flowerplatform.web.action {
	
	import com.crispico.flower.util.layout.Workbench;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.layout.Perspective;
	
	/**	
	 * @author Cristina	Constatinescu
	 */
	public class SwitchPerspectiveAction extends ActionBase {
		
		private var perspective:Perspective;
		
		public function SwitchPerspectiveAction(perspective:Perspective) {
			label = perspective.name;
			icon = perspective.iconUrl;
			this.perspective = perspective;
			parentId = "show_perspective";
		}
		
		override public function run():void	{
			perspective.resetPerspective(Workbench(FlexUtilGlobals.getInstance().workbench));
		}	
		
	}
}