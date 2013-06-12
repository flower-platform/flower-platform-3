package org.flowerplatform.flexutil.tree {
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	
	import spark.components.DataGroup;
	
	public class TreeListDataGroup extends DataGroup {
		
//		private var _dataProviderChanged:Boolean;
//		
//		private var _lastScrollPosition:Number = 0;
//		
//		public function TreeListDataGroup()	{
//			super();
//		}
//		
//		override public function set dataProvider(value:IList):void {
//			if (dataProvider != null && value != dataProvider) {
//				dataProvider.removeEventListener(CollectionEvent.COLLECTION_CHANGE, onDataProviderChanged);
//			}
//			super.dataProvider = value;
//			
//			if (value != null) {
//				value.addEventListener(CollectionEvent.COLLECTION_CHANGE, onDataProviderChanged);
//			}
//		}
		
		override protected function commitProperties():void {
			// sometimes, the original mehtod, sets the scroll to 0, 0
			// we prevent this here
			var lastVerticalScrollPosition:Number = verticalScrollPosition;
			var lastHorizontalScrollPosition:Number = horizontalScrollPosition;
			
			super.commitProperties();
			
//			if (_dataProviderChanged) {
				verticalScrollPosition = lastVerticalScrollPosition;
				horizontalScrollPosition = lastHorizontalScrollPosition;
//			}
		}
		
//		private function onDataProviderChanged(e:CollectionEvent):void {
//			_dataProviderChanged = true;
//			invalidateProperties();
//		}
//		
//		
//		override public function set verticalScrollPosition(value:Number):void {
//			super.verticalScrollPosition = value;
//			_lastScrollPosition = value;
//		}
	}

}