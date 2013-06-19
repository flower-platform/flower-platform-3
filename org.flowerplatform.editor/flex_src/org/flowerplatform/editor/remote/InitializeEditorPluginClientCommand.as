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
	 * @flowerModelElementId _C8JfQE1sEeGsUPSh9UfXpw
	 */
	[RemoteClass] 
	public class InitializeEditorPluginClientCommand extends AbstractClientCommand  {
		
		/**
		 * @flowerModelElementId _KcENcE1sEeGsUPSh9UfXpw
		 */
		public var contentTypeDescriptors:IList;
		
		/**
		 * @flowerModelElementId _akNHcLoREeGbWKUNv6VenQ
		 */
		public var lockLeaseSeconds:int;
		
		/**
		 * @flowerModelElementId _T-AdwE1sEeGsUPSh9UfXpw
		 */
		public override function execute():void {	
			super.execute();
			EditorPlugin.getInstance().contentTypeDescriptors = contentTypeDescriptors;
			EditorPlugin.getInstance().lockLeaseSeconds = lockLeaseSeconds;
		}
	}
}