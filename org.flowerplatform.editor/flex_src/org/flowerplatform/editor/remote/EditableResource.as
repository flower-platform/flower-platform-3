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
	
	import org.flowerplatform.communication.CommunicationPlugin;
	
	// TODO CS/STFL de redenumit in EditableResourceStatus
	/**
	 * @see Corresponding Java class for doc, (class and fields). Some special fields contain doc here as well. 
	 * @author Cristi
	 * 
	 */
	[Bindable] 
	[RemoteClass]
	[SecureSWF(rename="off")]
	public class EditableResource {
		
		[SecureSWF(rename="off")]
		public var editorStatefulClientId:String;
		
		[SecureSWF(rename="off")]
		public var masterEditorStatefulClientId:String;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var editorInput:Object;
		
		/**
		 * 
		 */ 
		[SecureSWF(rename="off")]
		public var label:String;
		
		/**
		 * 
		 */ 
		[SecureSWF(rename="off")]
		public var iconUrl:String;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var dirty:Boolean;
		
		/**
		 * This property is Transient, but exists in Java as well. It is
		 * populated by the <code>EditorStatefulClient</code>.
		 * 
		 * <p>
		 * If this <code>EditableResources</code> is a master, then
		 * this field is not <code>null</code>. Otherwise, it's <code>null</code>.
		 * 
		 * 
		 */
		[Transient]
		public var slaveEditableResources:ArrayCollection;
		
		/**
		 * This property is Transient, but exists in Java as well. It is
		 * populated by the <code>EditorStatefulClient</code>.
		 * 
		 * 
		 */
		[Transient]
		public var clients:ArrayCollection = new ArrayCollection();
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var locked:Boolean;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var lockOwner:EditableResourceClient;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var lockExpireTime:Date;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var lockUpdateTime:Date;
		
		public function getEditorStatefulClient():EditorStatefulClient {
			return EditorStatefulClient(CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(editorStatefulClientId));
		}
		
		public function getMasterEditorStatefulClient():EditorStatefulClient {
			if (masterEditorStatefulClientId == null) {
				return null;
			} else {
				return EditorStatefulClient(CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(masterEditorStatefulClientId));
			}
		}
		
	}
}