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
	 * @flowerModelElementId _Bu3bMFr-EeG5zfjR769bxA
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
		 * @flowerModelElementId _E9FDoFr-EeG5zfjR769bxA
		 */
		[SecureSWF(rename="off")]
		public var editorInput:Object;
		
		/**
		 * @flowerModelElementId _458j8GnaEeGf2Ze1btT4ow
		 */ 
		[SecureSWF(rename="off")]
		public var label:String;
		
		/**
		 * @flowerModelElementId _45_nQ2naEeGf2Ze1btT4ow
		 */ 
		[SecureSWF(rename="off")]
		public var iconUrl:String;
		
		/**
		 * @flowerModelElementId _Nsu7YFr-EeG5zfjR769bxA
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
		 * @flowerModelElementId _CKcB8LurEeGAt-EVfEFJMA
		 */
		[Transient]
		public var slaveEditableResources:ArrayCollection;
		
		/**
		 * This property is Transient, but exists in Java as well. It is
		 * populated by the <code>EditorStatefulClient</code>.
		 * 
		 * @flowerModelElementId _fctSYLV6EeGHQ9UXq6mt1g
		 */
		[Transient]
		public var clients:ArrayCollection = new ArrayCollection();
		
		/**
		 * @flowerModelElementId _1F63EIn3EeGENqKo5G_OSw
		 */
		[SecureSWF(rename="off")]
		public var locked:Boolean;
		
		/**
		 * @flowerModelElementId _Q28G8In4EeGENqKo5G_OSw
		 */
		[SecureSWF(rename="off")]
		public var lockOwner:EditableResourceClient;
		
		/**
		 * @flowerModelElementId _C-Vi8In5EeGENqKo5G_OSw
		 */
		[SecureSWF(rename="off")]
		public var lockExpireTime:Date;
		
		/**
		 * @flowerModelElementId _EnerAIn5EeGENqKo5G_OSw
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