package  com.crispico.flower.flexdiagram.util.imagefactory {
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.Loader;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.events.TimerEvent;
	import flash.net.URLRequest;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	 
	/**
	 * This class holds the <code>bitmapData</code> for the (composed) image identified 
	 * by <code>imageUrls</code>. For multiple urls, the resulting image is build by overlapping images.
	 * When the image becomes available (loading each image and overlapping process is finished), clients
	 * from <code>waitingList</code> receive the image.
	 * 
	 * @author Marius Arhire
	 * @author Florin Buzatu
	 * 
	 * @flowerModelElementId _qnUoQJ-hEd-Acq1dtkSbHA
	 */
	internal class ImageCacheEntry {
		
		/**
		 * Urls for one or more images.
		 */
		private var sourceImages:ArrayCollection;
		
		/**
		 * Number of images that have not yet been loaded.
		 */
		private var imageLeftToLoad:int;
		
		/**
		 * The bitmap data resulted after overlapping images found at <code>imageUrls</code>.
		 */
		private var bitmapData:BitmapData;
		
		/**
		 * Clients waiting to receive the image.
		 * 
		 * @flowerModelElementId _Hr3OIJ-lEd-Acq1dtkSbHA
		 */
		private var waitingList:ArrayCollection;
		
		private var clazz:Class;
		
		/**
		 * Recives request for a certain icon. If it has no decorators, it also begins
		 * image load; if it requires several images, it makes a request for each one. 
		 * @flowerModelElementId _22WpkJ-kEd-Acq1dtkSbHA
		 */
		 function ImageCacheEntry(sourceImages:ArrayCollection, initialWaitingListEntry:ImageCacheWaitingListEntry) {
			this.sourceImages = sourceImages;
			this.imageLeftToLoad = sourceImages.length;
			this.waitingList = new ArrayCollection([initialWaitingListEntry]);			
			
			if (sourceImages.length == 1) {
				if (sourceImages[0] is Class) {
					clazz = sourceImages[0];
					var timer:Timer = new Timer(10, 1);
					timer.addEventListener(TimerEvent.TIMER_COMPLETE, loadDataLater);
					timer.start();			 		
				} else {
					// This means the image has no decorators so there is no need for a composed image
					var request:URLRequest = new URLRequest(FlexUtilGlobals.getInstance().createAbsoluteUrl(sourceImages[0]));
					var loader:Loader = new Loader();
	
					loader.contentLoaderInfo.addEventListener(Event.COMPLETE, loaderCompleteHandler);
					loader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);	
					loader.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, showSecurityErrorMessage);
						
					try {
						loader.load(request);
					} catch (e:SecurityError) {
						showSecurityErrorMessage();
					}
				}
			} else {
				// This means the image has decorators and must be composed
				for each (var object:Object in sourceImages) {
					ImageFactory.INSTANCE.retrieveImage(new ArrayCollection([object]), this, "data", false);
				}
			}
		}
		
		/**
		 * Starts when an image is completly loaded, and for each decorator in 
		 * the icon, it calls the setter for the data. The Event.COMPLETE handler is called 
		 * when the (single) image is loaded.
		 */
		private function loaderCompleteHandler(event:Event):void {
			try {			
            	var image:Bitmap = Bitmap(event.target.content);
           		this.data = image.bitmapData;
            } catch(e:SecurityError) {
                showSecurityErrorMessage();
            }         
		}
		
		/**
		 * @flowerModelElementId _fO6pEN4VEeCfLfatVsLg4g
		 */
		private function showSecurityErrorMessage(e:SecurityErrorEvent=null):void {
			if (ImageFactory.showSecurityWorkaroundMessage) {
				Alert.show(ImageFactory.securityWorkaroundMessage);
			}
		}
				
		/**
		 * @flowerModelElementId _noxiZKuoEd-xvsA5HKaIYA
		 */
		private function errorHandler(e:IOErrorEvent):void {
			Alert.show("Error while loading image " + e.text);			
		}
		
		/**
		 * @flowerModelElementId _eIxMIMmXEeCjU6U15ZjMTA
		 */
		private function loadDataLater(event:TimerEvent):void {				
			var bd:BitmapData = new clazz().bitmapData;
			this.data = bd;
		}
		
		/**
		 * When there are no images left to load updates the icon to everyone 
		 * in the waiting list. The icon is stored in the data property, so future
		 * requests for this icon won't have to reload the images.
		 * 
		 * @flowerModelElementId _3i-pkJ-lEd-Acq1dtkSbHA
		 */
		internal function set data(data:BitmapData):void {
			imageLeftToLoad--;
			if (imageLeftToLoad == 0) {
				if (sourceImages.length == 1) {
					bitmapData = data;
				} else {
					// composing image
					for each (var object:Object in sourceImages) {
						var imageCacheEntry:ImageCacheEntry = ImageFactory.INSTANCE.getImageCacheEntry(object);						
						if (bitmapData == null) 
							//intialize bitmapData with the first image
							// a clone is created, otherwise the image from cache will be
							// decorated, so when it will be used again it will contain those
							// decorators and we don't want that.
							// @author Cristina
							bitmapData = imageCacheEntry.data.clone();
						else {
							bitmapData.draw(imageCacheEntry.data);							
							// trace("2:" + bitmapData.width);
						}
					}
				}
				updateImageToWaitingList();
			}		
		}
		
		/**
		 * Getter for the bitmapData of the (composed) image.
		 * 
		 * @return bitmapData of the image. Null if the image has not yet been loaded/composed.
		 * @flowerModelElementId _noywgauoEd-xvsA5HKaIYA
		 */
		internal function get data():BitmapData {
			return bitmapData;
		}
		
		/**
		 * Adds to <code>waitingList<code> a <code>newWaitingListEntry</code> that needs
		 * to recieve the bitmap data when image loading and composing process is finished.
		 * 
		 * @param newWaitingListEntry the client to receive the bitmap data of the image.
		 * @flowerModelElementId _noywg6uoEd-xvsA5HKaIYA
		 */
		internal function addToWaitingList(newWaitingListEntry:ImageCacheWaitingListEntry):void {
			waitingList.addItem(newWaitingListEntry);
		}
		
		/**
		 * When all the images are loaded, this method should be called to give 
		 * the composed image to all waiting clients.
		 * @flowerModelElementId _nozXkquoEd-xvsA5HKaIYA
		 */
		internal function updateImageToWaitingList():void {
			for each (var waitingListEntry:ImageCacheWaitingListEntry in waitingList) {
				waitingListEntry.callbackObject[waitingListEntry.callbackProperty] = bitmapData; 
			}
			waitingList = null;
		}
	}
}