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
package  org.flowerplatform.editor.remote {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.editor.EditorPlugin;
	
	/**
	 * Received from Java. Has a list of <code>ContentTypeEntry</code>s. 
	 * 
	 * <p>
	 * Initializes the <code>EditorSupport</code> with the content type entries
	 * and ids. The <code>contentTypesToIds</code>map is also built here, and it
	 * will look exactly like the one in Java. We don't send it, to save some
	 * bandwidth.
	 * 
	 * @author Cristi
	 * @author Mariana
	 * 
	 */
	[RemoteClass] 
	public class InitializeEditorPluginClientCommand extends AbstractClientCommand  {
		
		/**
		 * 
		 */
		public var contentTypeDescriptors:IList;
		
		/**
		 * 
		 */
		public var lockLeaseSeconds:int;
		
		/**
		 * 
		 */
		public override function execute():void {	
			super.execute();
			EditorPlugin.getInstance().contentTypeDescriptors = contentTypeDescriptors;
			EditorPlugin.getInstance().lockLeaseSeconds = lockLeaseSeconds;
		}
	}
}