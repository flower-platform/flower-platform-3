package org.flowerplatform.common.link {
	
	/**
	 * @author Cristina Constatinescu
	 */
	public interface ILinkHandler {
	
		function handleLink(command:String, ...parameters):void;
		
	}
}