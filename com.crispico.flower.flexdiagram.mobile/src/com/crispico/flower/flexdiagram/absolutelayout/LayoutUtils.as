package  com.crispico.flower.flexdiagram.absolutelayout {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina
	 * @flowerModelElementId _VhaxAOUQEeC9N93_tZqx9g
	 */
	public class LayoutUtils {
		
		/**
		 * Based on a list of objects bounds and monitor's width, the method calculates
		 * and returns new bounds for them so that they can be displayed as a basic rectangular layout. 
		 * <p>
		 * The basic rectangular layout is formed in the following way :
		 * <ul>
		 * 	<li> objects are diplayed as a grid with some padding between rows and columns.
		 * 	<li> the grid max width is considered to be the monitor's width.
		 * 	<li> for each object the following behavior is done :
		 * 	- if the object fits in the right, it will be added there
		 * 	- otherwise it will be added in the next row, having the x coord the same as the first element added
		 * and the y coord the max height of the objects added on the last row.
		 * 	<li> each line will be vertical centered based on the higest height.
		 * </ul>
		 * The objects are considdered to have the same (x,y) before calling this method.
		 * The coordonates from where the new bounds are calculated represents the (initialX, initialY) point.
		 * @flowerModelElementId _RRVUgOUTEeC9N93_tZqx9g
		 */
		public static function getBasicLayoutBoundsForObjects(initialX:Number, initialY:Number, maxWidth:Number, bounds:ArrayCollection, padding:Number=30):ArrayCollection {
			var newBounds:ArrayCollection = new ArrayCollection();
			
			// represents the x coord where the current object must be added
			var x:Number;
			// represents the y coord where the current object must be added
			var y:Number = initialY;
			// represents the x coord where the next object must be added
			var nextX:Number = initialX;
			// stores the max height value for a row based on the objects added
			var maxY:Number = 0; 
			// stores new bounds per line
			var newBoundsPerLine:ArrayCollection = new ArrayCollection();
			var b:Array;
			
			for each (var bound:Array in bounds) {
				// stores the x coord for current object
				x = nextX;
				// verify if the current object can be added in right
				if (bound[2] + nextX < maxWidth) {
					// stores the next x coord adding the current object's width
					nextX += bound[2];
					// if the current object has a higher height, store it
					if (bound[3] > maxY) {
						maxY = bound[3];
					}
				} else { // cannot be added, move to next row
					// add new bounds per line to global new bounds
					for each (b in newBoundsPerLine) {
						// the y coord is recalculated so that the object will be displayed centered to vertical position
						// init y + the difference between the highest object and the current one / 2
						newBounds.addItem([b[0], b[1]+(maxY - b[3])/2, b[2], b[3]]);
					}
					// remove them for new line
					newBoundsPerLine.removeAll();
					// set the initial x coord for current object
					// this way the we'll have a nice grouped layout
					x = initialX;
					// stores the next object's x coord by adding the next object's width
					nextX = x + bound[2];	
					// stores the current object's y coord by adding the max height and a padding only if maxY > 0
					y += (maxY > 0) ? (maxY + padding) : 0;
					// new max height is initialized with current object's height	
					maxY = bound[3];								
				}
				// add a padding to row
				nextX += padding;
				// save new coords
				newBoundsPerLine.addItem([x, y, bound[2], bound[3]]);
			}
			// add the elements for the last line in the same way as above
			for each (b in newBoundsPerLine) {
				newBounds.addItem([b[0], b[1]+maxY/2-b[3]/2, b[2], b[3]]);
			}
			
			return newBounds;
		}
	}
	
}