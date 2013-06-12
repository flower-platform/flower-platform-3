package  com.crispico.flower.flexdiagram.util.imagefactory {
	
	/**
	 * This class groups the following information:
	 * <ul>
	 * 	<li>callbackObject - the object that requested an image</li>
	 * 	<li>
	 * 	callbackProperty - property of the callbackObject that will be set 
	 * 	to the bitmap data of the image, as soon as the image is available
	 * 	</li>
	 * </ul>
	 * 
	 * @author Marius Arhire
	 * @author Florin Buzatu
	 * 
	 * @flowerModelElementId _YfGh0J-lEd-Acq1dtkSbHA
	 */
	internal class ImageCacheWaitingListEntry {
		
		internal var callbackObject:Object;
		
		internal var callbackProperty:String;
		
		function ImageCacheWaitingListEntry(callbackObject:Object, callbackProperty:String) {
			this.callbackObject = callbackObject;
			this.callbackProperty = callbackProperty;
		}
	} 
}