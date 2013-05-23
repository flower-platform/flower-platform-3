package com.crispico.flower.flexdiagram {

	import com.crispico.flower.flexdiagram.event.UpdateConnectionEndsEvent;
	import com.crispico.flower.flexdiagram.ui.IDraggable;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;

	/**
	 * 
	 * An EditPart is a connection between the model and the views (figures) and acts as a controller 
	 * as well. It is the most important element in the library and it is involved in the 2 flows: model - EditPart
	 * and EditPart - figure.
	 * 
	 * <p><b>The model - EditPart flow (ref. <code>refreshChildren()</code>)</b>
	 * Each (graphical) model element has an EditPart associated (i.e. the model elements tree is "doubled"
	 * by an EditPart tree, where a 1 to 1 relation exists between model elements and EditParts). An EditPart
	 * listens to the associated model. The notifications can be of 2 types: either structural (a child model
	 * has been added or removed and the EditParts children need to be refreshed) or non-structural (changes
	 * need to be applied to the graphical figure only - e.g. changes can be: text, style, images, position, size, 
	 * etc). Please note that an EditPart without a model doesn't make sense.
	 *
	 * <p><b>The EditPart - figure flow (ref. <code>refreshVisualChildren()</code>)</b>
	 * For a given model we have an EditPart. For a given EditPart a figure <i>may exist or not</i>.<br>
	 * In an ideal world, each EditPart should always have a figure associated but when dealing with a large
	 * tree of objects, the Flex UI has performance issues when loading the figures (during startup) and afterwards
	 * (the processor is at 100% load when only moving the mouse). The optimization for the above problems is
	 * to have on the screen only figures that are visible (and eventually a small cache). When possible "reused" 
	 * figures are used (figures that are on the screen but they are no longer visible as we avoid the expensive
	 * call to Flex's addChild()). If this is not possible, "recycled" figures are used (figures that are not on the 
	 * screen, but they were at a given time. In this case, a call to addChild() is invitable, but as most of the 
	 * figures are composed, we spare costly calls to addChild() from the children. This mechanism optimizes 
	 * a little bit the memory consumption. When neither reused nor recycled figures are available, new ones
	 * are created of course.
	 *
	 * <p><b>If the EditPart supports connections:</b>
	 * EditParts are responsible of dispatching <code>UpdateConnectionEndsEvent</code> to notify the associated
	 * ConnectionEditParts that they need to recalculate the ends. If the EditPart doesn't have a figure
	 * (e.g. an <code>IAbsolutePositionEditPart</code> (or one of its children) that isn't 
	 * yet within the viewable area) the EditPart) it 
	 * would dispatch the notifications when the model changes. Otherwise (i.e. a figure exist), the EditPart
	 * should listen directly the figure for various events and should dispatch the notification accordingly.
	 * We need this latter behaviour to update the ConnectionEditParts in real-time when UI operations
	 * happen without model modification (e.g. move or resize effect, drag to move/resize).
	 *
	 * <p> The EditPart manages the drag operations (move and resize). To validate a move operation, the editpart
	 * of the target figure (the one which receives other figures) is asked wheather it can accept them or not.
	 * 
	 * <p>
	 * Subclasses would normally override the following methods and/or have the following responsabilities:
	 * <ul>
	 * 		<li><code>activate()</code>
	 * 		<li><code>deactivate()</code>
	 * 		<li><code>getModelHolderCollections()</code> - if the EditPart has children 
	 * 		<li><code>getModelFromModelHolder()</code> - if the EditPart has children
	 * 		<li><code>refreshVisualChildren()</code> - if the EditPart has children
	 * 		<li><code>refreshVisualDetails()</code> - if the appearance of EditPart's figure is changeable 
	 * 		<li><code>setFigure()</code> - if connections are supported
	 * 		<li><code>unsetFigure()</code> - if connections are supported
	 * 		<li><code>getConnectionAnchorRect()</code> - if connections are supported
	 * 		<li><code>isDraggable</code> - if the EditPart is draggable
	 * 		<li><code>isSelectable</code> - if the EditPart is selectable
	 * 		<li><code>beginDrag</code> - if the EditPart is draggable
	 * 		<li><code>drag()</code> - if the EditPart supports a drag operation
	 * 		<li><code>drop()</code> - if the EditPart supports a drag operation
	 * 		<li><code>isEditPartAccepted()</code> - if the EditPart can accept an EditPart array 
	 * 		<li><code>acceptEditPart()</code> - if the EditPart can accept an EditPart array
	 * 		<li><code>acceptNewEditPart()</code> - if the EditPart can accept a newly created EditPart
	 * 		<li><code>isSourceConnectionEditPartAccepted()</code> - if a Connection EditPart supports another editPart 
	 * 			as source EditPart
	 * 		<li><code>isTargetConnectionEditPartAccepted()</code> - if a Connection EditPart supports another editPart 
	 * 			as target EditPart
	 * 		<li><code>activateInplaceEditor()</code> - if the EditPart supports inplace editing
	 * 		<li><code>deactivateInplaceEditor()</code> - if the EditPart supports inplace editing
	 * 		<li>should listen to the underlying model; if structural changes ar detected, <code>refresh()</code>
	 * 			should be called; otherwise ("visual" changes) <code>refreshVisualDetails()</code> should be
	 * 			called
	 * 		<li>should use <code>dispatchUpdateConnectionEndsEvent()</code> to dispatch notifications to the 
	 * 			ConnectionEditParts when the absolute position changes (both during real-time UI operations and
	 * 			after model updates) 
	 * </ul>
	 * 
	 * @author cristi
	 * @flowerModelElementId _bxx54b8REd6XgrpwHbbsYQ
	 */
	public class EditPart extends EventDispatcher {
		
		/**
		 * Holds the figures to reuse (organized by their figure class)
		 * figure class as key; an ArrayCollection as value
		 * @flowerModelElementId _b47qxb8REd6XgrpwHbbsYQ
		 */ 
		public static var RECYCLED_FIGURES:Dictionary = new Dictionary();
		
		/**
		 * Boolean variable that tells if the edit part is having a custom function <code>isConnectionAcceptedBetweenModels</code> 
		 * that tells if we can have a connection between two model-objects given like arguments
		 * The default value is false. 
		 * The value of this variable is tested in <code>SelectMoveResizeTool</code> in order to see if we need to use the 
		 * <code>sourceConnections</code> and <code>targetConnections</code> to see if two editParts can be linked or if we use 
		 * the custom defined function <code>isConnectionAcceptedBetweenModels</code> .
		 */   
		public var hasCustomAcceptConnectionWithAnotherEditPart:Boolean = false;
		
		/**
		 * The model wrapped by this EditPart.
		 * @flowerModelElementId _b47qyr8REd6XgrpwHbbsYQ
		 */
		protected var model:Object;

		/**
		 * 
		 * Should be set using the setter
		 * @flowerModelElementId _b47qzr8REd6XgrpwHbbsYQ
		 */
		protected var figure:IFigure;
		
		/**
		 * Contains child EditParts.
		 * @flowerModelElementId _b47q0r8REd6XgrpwHbbsYQ
		 */
		protected var children:ArrayCollection = new ArrayCollection();

		/**
		 * @flowerModelElementId _b5Fbwb8REd6XgrpwHbbsYQ
		 */
		protected var parent:EditPart;

		/**
		 * @flowerModelElementId _b5FbxL8REd6XgrpwHbbsYQ
		 */
		protected var viewer:DiagramViewer;
		
		/**
		 * Lazily initialized because EditParts without connections shouldn't
		 * have 2 more object instances that they don't use.
		 * @flowerModelElementId _b5Fbx78REd6XgrpwHbbsYQ
		 */ 
		private var sourceConnections:ArrayCollection;

		/**
		 * Lazily initialized because EditParts without connections shouldn't
		 * have 2 more object instances that they don't use.
		 * @flowerModelElementId _b5Fby78REd6XgrpwHbbsYQ
		 */ 
		private var targetConnections:ArrayCollection;
		
		/**
		 * Selected state of the <code>figure</code> attached to this
		 * <code>EditPart</code>
		 * @flowerModelElementId _b5Fbz78REd6XgrpwHbbsYQ
		 */
		protected var isSelected:Boolean;
		
		/**
		 * If the <code>figure</code> attached to this <code>EditPart</code>
		 * is main selection
		 * @flowerModelElementId _b5Fb078REd6XgrpwHbbsYQ
		 */
		protected var isMainSelection:Boolean;
		
		/**
		 * The figure is added to the global RECYCLED_FIGURES repository. The parameter
		 * must not be associated to an EditPart (neither it or its children) and 
		 * it mustn't belong to a Flex container.
		 * 
		 * @flowerModelElementId _b5Fb178REd6XgrpwHbbsYQ
		 */ 
		public static function recycleFigure(currentFigure:IFigure):void {
			var col:ArrayCollection = RECYCLED_FIGURES[Object(currentFigure).constructor];
			// lazy init the collection
			if (col == null) {
				col = new ArrayCollection();
				RECYCLED_FIGURES[Object(currentFigure).constructor] = col;
			}
			col.addItem(currentFigure);
		}
		
		/**
		 * @flowerModelElementId _b5Fb3b8REd6XgrpwHbbsYQ
		 */
		public function EditPart(model:Object, viewer:DiagramViewer) {
			this.model = model;
			this.viewer = viewer;
		}
		
		/**
		 * Getter for isSelected property
		 * @flowerModelElementId _b5Fb4r8REd6XgrpwHbbsYQ
		 */
		public function getSelected():Boolean {
			return isSelected;
		}
		
		/**
		 * Setter for isSelected property
		 * @flowerModelElementId _b5Fb578REd6XgrpwHbbsYQ
		 */
		public function setSelected(val:Boolean):void {
			isSelected = val;
		}

		/**
		 * Getter from isMainSelection property
		 * @flowerModelElementId _b5Fb7b8REd6XgrpwHbbsYQ
		 */		
		public function getMainSelection():Boolean {
			return isMainSelection;
		}
				
		/**
		 * Getter for isMainSelection
		 * @flowerModelElementId _b5Fb8r8REd6XgrpwHbbsYQ
		 */
		public function setMainSelection(val:Boolean):void {
			isMainSelection = val;
		}
				
		/**
		 * Getter for model property.
		 * @flowerModelElementId _b5Fb-L8REd6XgrpwHbbsYQ
		 */
		public function getModel():Object {
			return model;
		}
				
		/**
		 * @flowerModelElementId _b5PMwL8REd6XgrpwHbbsYQ
		 */
		public function getFigure():IFigure {
		   return figure;
		}
		/**
		 * Associates a figure with the current EditPart. The following calls are made:
		 * 
		 * <ul>
		 * 		<li><code>refreshVisualChildren()</code>, 
		 * 		<li><code>refreshVisualDetails()</code>,
		 * 		<li><code>UpdateConnectionsEndsEvent</code> is dispatched.
		 * </ul>
		 * 
		 * The argument must not be null, it shouldn't be associated with an EditPart
		 * and the current figure should be null. Otherwise an exception is thrown.
		 * 
		 * <p>
		 * Subclasses should override this method to register the 
		 * <code>dispatchUpdateConnectionEndsEvent()</code> method as a listener for various 
		 * events (of the figure) that should also update the connections.
		 * @flowerModelElementId _b5PMxL8REd6XgrpwHbbsYQ
		 */
		public function setFigure(figure:IFigure):void {
			if (figure == null || this.figure != null || figure.getEditPart() != null)
				throw new Error("EditPart.setFigure() was called with non null arg, a figure already exists or the figure already has an EditPart.");
			// the order of the next 2 lines is important
			// as code invoked from setEditPart may want to access ep.getFigure()
			this.figure = figure;
			figure.setEditPart(this);
			refreshVisualChildren();
			refreshVisualDetails(true);
			dispatchUpdateConnectionEndsEvent(null);
		}
		/**
		 * Recursively unassociates the EditPart from the figure. 
		 * When this method is called it means that a figure will be 
		 * reused or recycled.
		 * 
		 * <p>
		 * If <code>setFigure()</code> was overridden to register listeners, 
		 * this method should be overridden as well to unregister them. 
		 * Super() should be called at the end in this case.
		 * @flowerModelElementId _b5PMyr8REd6XgrpwHbbsYQ
		 */
		public function unsetFigure():void {
			if (figure != null) {
				for (var i:int = 0; i < children.length; i++)
					EditPart(children[i]).unsetFigure();
				figure.setEditPart(null);
				figure = null;
			}
		}
		/**
		 * @flowerModelElementId _b5PMz78REd6XgrpwHbbsYQ
		 */
		public function getChildren():ArrayCollection {
			return children;
		}
		/**
		 * @flowerModelElementId _b5PM078REd6XgrpwHbbsYQ
		 */
		public function getParent():EditPart {
			return parent;
		}
		/**
		 * @flowerModelElementId _b5PM178REd6XgrpwHbbsYQ
		 */
		public function setParent(editPart:EditPart):void {
			parent = editPart;
		}
		/**
		 * Getter for the viewer.
		 * @flowerModelElementId _b5PM3L8REd6XgrpwHbbsYQ
		 */
		public function getViewer():DiagramViewer {
			return viewer;
		}
		/**
		 * @flowerModelElementId _b5PM4b8REd6XgrpwHbbsYQ
		 */
		public function getSourceConnections():ArrayCollection {
			if (sourceConnections == null)
				sourceConnections = new ArrayCollection();
			return sourceConnections;
		}
		/**
		 * @flowerModelElementId _b5PM5b8REd6XgrpwHbbsYQ
		 */
		public function getTargetConnections():ArrayCollection {
			if (targetConnections == null)
				targetConnections = new ArrayCollection();
			return targetConnections;
		}
		/**
		 * Instantiates a compatible figure for the EditPart. It is
		 * not associated with the current EditPart.The default
		 * implementation delegates to <code>getFigureClass()</code>.
		 * Subclasses could override this method (if they would want
		 * to pass some parameters to the constructor) although this
		 * is not recomended. Figures should be stateless to allow 
		 * their reusal.
		 * @flowerModelElementId _b5PM6b8REd6XgrpwHbbsYQ
		 */ 
		protected function createNewFigure(figureClass:Class):IFigure {
			return new figureClass();
		}
		/**
		 * Abstract method. Returns the class for the EditPart's figure.
		 * @flowerModelElementId _b5PM778REd6XgrpwHbbsYQ
		 */
		public function getFigureClass():Class {
			throw new Error("getFigureClass() should be implemented.");
		}
		/**
		 * If a recycled figure is found, it is returned; otherwise instantiates 
		 * a figure based on the figure class.
		 * @flowerModelElementId _b5PM9L8REd6XgrpwHbbsYQ
		 */
		public function createOrGetRecycledFigure():IFigure {
			var class_:Class = getFigureClass();
			var col:ArrayCollection = RECYCLED_FIGURES[class_];
			if (col != null && col.length > 0)
				return IFigure(col.removeItemAt(col.length - 1)); // found a recycled figure
			else
				return createNewFigure(class_);
		}
		/**
		 * Updates the children (call to <code>refreshChildren</code>) and the visuals 
		 * (call to <code>refreshVisualChildren()</code> and <code>refreshVisualDetails()</code>) 
		 * if a figure exist. This method is called from <code>activate</code> 
		 * and could be explicitly called from the event handlers that receive notifications from the model. 
		 * @flowerModelElementId _b5YWsL8REd6XgrpwHbbsYQ
		 */
		public function refresh():void {
			refreshChildren();
			if (figure != null) {
				refreshVisualChildren();
				refreshVisualDetails(false);
			}
		}
		/**
		 * Abstract method that should be implemented by EditParts
		 * that have children. It should return an Array that contains
		 * ArrayCollections that contain "model holders". 
		 * <code>getModelFromModelHolder()</code> will be called to 
		 * get the model. 
		 * 
		 * <p>E.g. to get the first child model the system would do
		 * the following: 
		 * <code>getModelFromModelHolder(getModelHolderCollections()[0][0])</code>
		 * @flowerModelElementId _b5YWtb8REd6XgrpwHbbsYQ
		 */ 
		protected function getModelHolderCollections():Array {
			return [];
		}
		/**
		 * Abstract method that should be implemented by EditParts
		 * that have children. It should return the model given the model
		 * holder. Depending on the implementation, this method could return
		 * the model itself or do some additionnal processing to get it.
		 * @flowerModelElementId _b5YWur8REd6XgrpwHbbsYQ
		 */ 
		public function getModelFromModelHolder(modelHolder:Object):Object {
			throw new Error("EditPart.getModelFromModelHolder() should be implemented.");
		}
		/**
		 * 
		 * This method is not intended to be overridden. It compares the model children
		 * (using <code>getModelHolderCollections</code> + <code>getModelFromModelHolder</code>)
		 * with the EditPart children.  
		 * 
		 * <ul>
		 *		<li>If there are new child models, <code>createChild</code> and <code>childAdded()</code> is be called;
		 *		<li>For child models that no longer exist, <code>childRemoved()</code> is called.
		 * </ul>
		 * 
		 * The algorithm ensures that the EditPart children and model children ar ordered
		 * in the same way. If some model elements don't have EditParts, there will not 
		 * be null values in the children list.
		 * 
		 * <p>The flow is generally the following:
		 * <ul>
		 *    <li>"root" EditPart.activate() that calls refresh() - only for boot strapping (when a diagram is
		 *          displayed
		 *    <li>refreshChildren()
		 *    <li>(createChild(); childAdded(child) - child.activate() is called) or (childRemoved(child) - child.deactivate() is called)
		 *    <li>child.refresh()
		 *    <li>child.refreshChildren()
		 *    <li>etc
		 * </ul
		 * @flowerModelElementId _b5YWwL8REd6XgrpwHbbsYQ
		 */
		protected function refreshChildren():void {
			var modelHolderCollections:Array = getModelHolderCollections();
			// map[model] = EditPart
			var map:Dictionary = new Dictionary(); 
			var i:int;
			// populate the map with the old values
			for (i = 0; i < children.length; i++) {
				var editPart:EditPart = EditPart(children[i]);
				map[editPart.getModel()] = editPart;
			}
				
			// calculate the new length = the sum of lengths
			// of the model collections
			var newLength:int = 0;
			for (var j:int = 0; j < modelHolderCollections.length; j++) 
				newLength += ListCollectionView(modelHolderCollections[j]).length;
			// and resize the array (to be sure that it has enough room)
			children.source.length = newLength;
			var hasNulls:Boolean = false;
				
			if (modelHolderCollections != null)
				// see how it is incremented to understand the meaning
				// we need it because multiple model lists are supported
				var newCurrentIndex:int = 0;
				// process each ArrayCollection that has children (e.g. nodes, edges)
				for (j = 0; j < modelHolderCollections.length; j++) {
					var modelHolderCollection:ListCollectionView = ListCollectionView(modelHolderCollections[j]);
					// iterate through new values
					for (i = 0; i < modelHolderCollection.length; i++) {
						// ask the edit part to give the model; it might be the parameter itself
						// or the EditPart could do some processing to get it
						var model:Object = getModelFromModelHolder(modelHolderCollection[i]);
						var ep:EditPart = map[model];
						if (ep != null) {
							// a value was found in the map => the value didn't change => remove from map
							delete map[model];
							// and put it in the new position
							children[newCurrentIndex] = ep;
						} else {
							// a value was not found in the map => the value is newly added 
							// so we need to create an EditPart for the model element
							ep = createChild(model);
							// add it to the children list; it may be null
							children[newCurrentIndex] = ep;
							if (ep != null)
								childAdded(ep); // if we have an EditPart for the model element, call add()
							else 
								hasNulls = true; // otherwise set the flag to true to eliminate nulls at the end 	
						}
						newCurrentIndex++; 
					}
				}
			// the elements that remain in the map after the iteration are the elements
			// that need to be removed
			for (var key:Object in map) 
				childRemoved(map[key]);
			// if there are nulls in the children list (i.e. not all the model elements have
			// a corresponding EditPart) we need to eliminate them
			if (hasNulls) {
				i = 0;
				while (i < children.length)
					if (children[i] != null)
						i++;
					else
						children.removeItemAt(i);
			}	
		}
		
		/**
		 * Abstract method. Should be implemented by EditParts that have children.
		 * An implementation should do the following:
		 * <ul>
		 * 		<li>Check the EditPart children against the IFigure children;
		 * 		<li>If new figures need to be added, obtain a figure and call 
		 * 			<code>EditPart.setFigure()</code>
		 * 		<li>If figures need to be removed, call <code>EditPart.unsetFigure()</code>
		 * 			and recycle the figure
		 * </ul>
		 * 
		 * Depending on its type, an EditPart could decide to associate figures only for
		 * some of the children EditParts (e.g. <code>AbsoluteLayoutEditPart</code>. 
		 * 
		 * <p>Figure reuse/recycle mechanisms should be implemented in this method.
		 * These mechanisms differ upon the different factors, e.g. layout used to 
		 * position the figures (absolute, sequential, etc).
		 * 
		 * <p>As stated in the comment of the class, there is a difference between reused and 
		 * recycled figures. A reused figure belongs to a container.
		 * A recycled figure is no longer present in any Flex container and we use only its instance. 
		 * But recycled figures can contain figures that can be reused. Recycled figures are held
		 * in the global RECYCLED_FIGURES hashmap.
		 * @flowerModelElementId _b5YWxb8REd6XgrpwHbbsYQ
		 */ 
		public function refreshVisualChildren():void {
			
		}
		/**
		 * Abstract method. Should be implemented by EditParts that modify the
		 * appearance of the figure (i.e. access data from the model and interract
		 * with the associated figure accordingly: labels, images, etc.).
		 * 
		 * @param calledDuringFigureSet true if the call comes from <code>setFigure()</code>.
		 * 		In theory, the figure can be set and unset at any time, and the subclasse may
		 * 		want to do some processing only during "normal " operations (not on figure set). 
		 * 		E.g. effects should be played when the param is false because otherwise the
		 * 		scroll would make the figures "dancing" on the screen (because of the optimization
		 * 		from <code>AbsoluteLayoutEditPart</code> 
		 * @flowerModelElementId _b5YWyr8REd6XgrpwHbbsYQ
		 */ 
		public function refreshVisualDetails(calledDuringFigureSet:Boolean):void {
			
		}
		/**
		 * This method is called only once, from <code>childAdded()</code> (when the lifecycle of
		 * the EditPart begins). By default it performs a refresh and updates the modelToEditPartMap
		 * in the viewer.
		 * 
		 * It should be overriden to register listeners for the model. Super should be called at the
		 * beginning.
		 * @flowerModelElementId _b5YW0L8REd6XgrpwHbbsYQ
		 */
		public function activate():void {
			viewer.addModelToEditPartMapping(model, this);
			refresh();
		}
		
		/**
		 * Called from <code>childRemoved()</code> (and it means that the lifecycle ends). By default
		 * the associated figure is unset (if existent) and all the children are removed
		 * (so they will be recursively deactivated as well).
		 * 
		 * <p>
		 * Should be overriden to unregister the listeners for the model (that have been 
		 * registered in <code>activate</code>). Super should be called at the end, as it 
		 * sets model to null (and the modelToEditPartMap from the viewer is updated).
		 * 
		 * @flowerModelElementId _b5iHsb8REd6XgrpwHbbsYQ
		 */
		public function deactivate():void {
			if (figure != null)
				unsetFigure();
			while (children.length > 0) {
				childRemoved(children[0], false);
				children.removeItemAt(0);
			}
			viewer.removeModelToEditPartMapping(model);
			model = null;
		}
		/**
		 * Create the child <code>EditPart</code> for the given model object. 
		 * This method is called from <code>refreshChildren()</code>.
		 * By default, the implementation will delegate to the EditPartFactory.
		 * @flowerModelElementId _b5iHtr8REd6XgrpwHbbsYQ
		 */
		public function createChild(model:Object):EditPart {
			return viewer.getEditPartFactory().createEditPart(viewer, this, model);
		}
		/**
		 * Called by <code>refreshChildren()</code> when a child 
		 * <code>EditPart</code> is added to this EditPart. 
		 * The following operations are performed:
		 * <ol>
		 *		<li>The child's parent is set to this,
		 * 	 	<li>The child's <code>activate</code> method is called.
		 * </ol>
		 * @flowerModelElementId _b5iHvL8REd6XgrpwHbbsYQ
		 */
		public function childAdded(child:EditPart):void {
			child.setParent(this);
			child.activate();
		}
		/**
		 * This method is called from <code>refreshChildren()</code> or from 
		 * <code>deactivate()</code> when a child <code>EditPart</code> is removed
		 * from this EditPart.
		 * The following operations are performed:
		 * 
		 * <ol>
		 * 		<li>The child's <code>deactivate</code> method is called,
		 * 		<li>The figure is recycled if specified,
		 * 		<li>The child's parent is set to null.
		 * 		<li>Remove from selection (if this <code>EditPart</code> is selected) 
		 * </ol>
		 * 
		 * @param recycleFigureNeede If the call comes from <code>refreshChildren()</code>
		 * 			this parameter is true, meaning that tha associated figure should
		 * 			be removed from the container and recycled.
		 * @flowerModelElementId _b5iHwr8REd6XgrpwHbbsYQ
		 */		
		public function childRemoved(child:EditPart, recycleFigureNeeded:Boolean=true):void {
			var currentFigure:IFigure = child.getFigure();
			if (child.getViewer().getSelectedElements().contains(child)) 
				child.getViewer().removeFromSelection(child);
			child.deactivate();
			if (recycleFigureNeeded && currentFigure != null) {
				IVisualElementContainer(IVisualElement(currentFigure).parent).removeElement(IVisualElement(currentFigure));
				recycleFigure(currentFigure);
			}
			child.setParent(null);
		}
		/**
		 * 
		 * Abstract method that should be implemented if the EditPart supports
		 * connections.
		 * 
		 * <p>
		 * Returns an array of 4 ints (x1, y1, width, height) representing the
		 * possible rect within which connections can be anchored. The coordinates
		 * are relative to the root figure.
		 * 
		 * <p>
		 * If the EditPart has a figure, it should use the visual components to 
		 * calculate the exact coordinates. Otherwise, if the EditPart knows
		 * its coordinates from the model (e.g. <code>IAbsolutePositionEditPart
		 * </code>) it should use them; otherwise, use a (direct or indirect)
		 * parent that knows the corrdinates and use them as an "estimation"
		 * @flowerModelElementId _b5iHyr8REd6XgrpwHbbsYQ
		 */
		public function getConnectionAnchorRect():Array {
			throw new Error("EditPart.getConnectionAnchorRect() should be implemented.");
		}
		/**
		 * 
		 * EditParts should register this method as a handler for various 
		 * UI events that should trigger the update of the connections (e.g. move, 
		 * resize, or more complex events). The parameter is ignored and it
		 * is there so that the method can be used as an event handler everywere.
		 * 
		 * <p>
		 * If no figure exists this method should be called directly as a result
		 * of model updates that should also update the connections.
		 * 
		 * @see UpdateConnectionEndsEvent
		 * @see ConnectionEditPart.updateConnectionsHandler()
		 * @flowerModelElementId _b5iHz78REd6XgrpwHbbsYQ
		 */
		public function dispatchUpdateConnectionEndsEvent(event:Event):void {
			dispatchEvent(new UpdateConnectionEndsEvent(this));
		}
		/**
		 * 
		 * Abstract method. Should be overriden if the EditPart is 
		 * selectable
		 * @flowerModelElementId _b5iH1b8REd6XgrpwHbbsYQ
		 */
		public function isSelectable():Boolean {
			return false;
		}
		/**
		 * 
		 * Abstract method. Should be overriden if the EditPart is 
		 * selectable. Following the provided arguments, the EditPart
		 * should update the figure to reflect the selected state
		 * @flowerModelElementId _b5iH2r8REd6XgrpwHbbsYQ
		 */
		public function updateSelectedState(isSelected:Boolean, isMainSelection:Boolean):void {
			setSelected(isSelected);
			setMainSelection(isMainSelection);
		}
		/**
		 * Abstract method. Should be overriden if the EditPart is
		 * draggable.
		 * @flowerModelElementId _b5iH4b8REd6XgrpwHbbsYQ
		 */
		public function isDraggable(): Boolean {
			return false;
		}
		/**
		 * <p>Function called from SelectMoveResizeTool when a drag operation is 
		 * started. If the EditPart is draggable then the subclasses could
		 * add a move/resize placeHolder for the current object. 
		 * 
		 * <p>If the EditPart is the initator than it can also modify selection. 
		 * 
		 * @param isInitiator is true if the IDraggable object received as
		 * parameter is the figure that fired the drag operation.
		 * 
		 * @return boolean:
		 * 		If returns true,<code>isEditPartAccepted()</code> will be
		 * 			called for targetEditPart for drag and
		 * 			<code>acceptEditPart</code> for drop.
		 * @flowerModelElementId _b5iH5r8REd6XgrpwHbbsYQ
		 */  
		public function beginDrag(draggable:IDraggable, x:int, y:int, isInitiator:Boolean):Boolean {
			return false;
		}
		
		/**
		 * Function called form SelectMoveResizeTool or other Tool when a drag-to-create operation is started.
		 * If the EditPart is draggable and supports creation by dragging then it should handle and create the moveResizePlaceHolder.
		 * <p>
		 * If a moveResizePlaceHolder is not already provided from outside (ex created in the tool), 
		 * this EditPart is responsible to create its default.
		 */ 
		public function beginDragToCreate(x:Number, y:Number, width:Number, height:Number):Boolean {
			// to implement in subclasses	
			return false;
		}
		
		/**
		  * Called from SelectMoveResizeTool. If draggable, subclasses could override this method.
		  * For instance, the function can update the position/dimension for the placeHolder. Also,
		  * make sure that the new values are positive values.
		  * 
		  * <p>
		  * The deltaX and deltaY parameters are the differences on x and y axis between the current
		  * mouse position and the initial mouse position (when <code>beginDrag()</code> is called).
		  * @flowerModelElementId _b5iH778REd6XgrpwHbbsYQ
		  */
		public function drag(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
			
		}
		
		/**
		 * Called form SelectMoveResizeTool or other Tool during a drag-to-create operation.
		 * This function should update the position and dimension of the placeHolder.
		 * 
		 * The deltaX and deltaY parameters are the differences on x and y axis between the current
		 * mouse position and the initial mouse position (when <code>beginDrag()</code> is called).
		 * 
		 * @return true if this EditPart will handle its moveResizePlaceHolder, otherwise, let the tool do the job.
		 */ 
		public function dragToCreate(deltaX:Number, deltaY:Number):Boolean {
			return false;
		}
		
		 /**
		 * Called from SelectMoveResizeTool. If draggable, subclasses could override this method. 
		 * E.g. the placeHolder should be removed (if one exists) and the model should by modified.
		 * Also make sure that the new values are positive values.
		 * 
		 * <p>
		 * The deltaX and deltaY parameters are the differences on x and y axis between the current
		 * mouse position and the initial mouse position (when <code>beginDrag()</code> is called).
		 * @flowerModelElementId _b5iH-L8REd6XgrpwHbbsYQ
		 */
		public function drop(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
		}
		
		/**
		 * Called form SelectMoveResizeTool or other Tool when a drag-to-create operation ends.
		 * This function should dispose the moveResizePlaceHolder and do the last updates on it.
		 * 
		 * <p>
		 * The deltaX and deltaY parameters are the differences on x and y axis between the current
		 * mouse position and the initial mouse position (when <code>beginDragToCreate()</code> is called).
		 * 
		 * @return true if this EditPart handles the drop or false if the Tool is resposible to do it instead
		 */ 
		public function dropToCreate(deltaX:Number, deltaY:Number):Boolean {
			return false
		}
		
		/**
		 * Called from SelectMoveResizeTool. If draggable, subclasses could override this method. 
		 * For instance, the function removes the placeHolder (if there is one) if the ESC 
		 * key was pressed during a move/resize action.
		 * 
		 * <p>
		 * The deltaX and deltaY parameters are the differences on x and y axis between the current
		 * mouse position and the initial mouse position (when <code>beginDrag()</code> is called).
		 * @flowerModelElementId _b5iIAb8REd6XgrpwHbbsYQ
		 */
		public function abortDrag(draggable:IDraggable, deltaX:int, deltaY:int, isInitiator:Boolean):void {
		}
		/**
		 * Overriden by all subclasses. Takes in consideration what types of
		 * children an editPart can have.
		 * @flowerModelElementId _b5rRpr8REd6XgrpwHbbsYQ
		 */
		public function isEditPartAccepted(editParts:ArrayCollection):Boolean {
			return false;
		}
		/**
		 * The function is called from SelectMoveResizeTool using the target's figure 
		 * edit part for a drag & drop operation. The function updates the model. Subclasses are
		 * responsible to check if the given edit part is accepted.
		 * @flowerModelElementId _b5rRrL8REd6XgrpwHbbsYQ
		 */
		public function acceptEditPart(editParts:ArrayCollection, deltaX:int, deltaY:int):void {
		}
		
		/**
		 * The function can be called in stead of calling the 
		 * <code>acceptEditPart</code>. The function updates the model taking 
		 * in account the <code>stratX</code> and <code>dropX</code> parameters.
		 * This is usefull for example if for accepting or not the editPart 
		 * it needs to take in acount the  side (left/right) of the taget figure.
		 * Subclasses are responsible to check if the given edit part is 
		 * accepted.
		 * 
		 */
		public function acceptEditPartOnSpecifiedXPossition(editParts:ArrayCollection, deltaX:int, deltaY:int, startX:Number, dropX:Number):void {
		}
		
		/**
		 * Subclasses must implement this method if this EditPart supports 
		 * new EditParts created by a Tool. 
		 * 
		 * <p>
		 * Subclasses must take into consideration that the recieved EditPart is
		 * a dummy one, and the only thing safe about it is its type.
		 * 
		 * <p>
		 * The creation tool can pass arbitrary additional information (using <code>
		 * additionalInfo</code> parameter).
		 * 
		 * @flowerModelElementId _b5rRtL8REd6XgrpwHbbsYQ
		 */		
		public function acceptNewEditPart(editPart:EditPart, x:int, y:int, width:int, height:int, additionalInfo:Object=null):void {
		}
		/**
		 * Overriden by all subclasses. Takes in consideration what type of
		 * connections can accept as source edit part.
		 * @flowerModelElementId _b5rRvr8REd6XgrpwHbbsYQ
		 */
		public function isSourceConnectionEditPartAccepted(editParts:ArrayCollection):Boolean {
			return false;
		}
		
		/**
		 * The function updates the model. Children are responsible to check if the 
		 * given edit part is accepted.
		 * @flowerModelElementId _b5rRxL8REd6XgrpwHbbsYQ
		 */
		public function acceptSourceConnectionEditPart(editParts:ArrayCollection):void {
		}
		/**
		 * Overriden by all subclasses. Takes in consideration what type of
		 * connections can accept as target edit part.
		 * @flowerModelElementId _b5rRyr8REd6XgrpwHbbsYQ
		 */
		public function isTargetConnectionEditPartAccepted(editParts:ArrayCollection):Boolean {
			return false;
		}
		/**
		 * The function updates the model. Children are responsible to check if the 
		 * given edit part is accepted.
		 * @flowerModelElementId _b5rR0L8REd6XgrpwHbbsYQ
		 */
		public function acceptTargetConnectionEditPart(editParts:ArrayCollection):void {
		}
		/**
		 * The function checks if targetEditPart can accept the editParts. The return values
		 * is used by <code>SelectMoveResizeTool</code> to update the mouse cursor.
		 * 
		 * <p>The draggable object is needed for the classes the subclasses that deals with
		 * connections.
		 * @flowerModelElementId _b5rR1r8REd6XgrpwHbbsYQ
		 */
		public function dragOverTargetEditPart(draggable:IDraggable, targetEditPart:EditPart, editParts:ArrayCollection):Boolean {
			return targetEditPart.isEditPartAccepted(editParts);
		}
		
		/**
		 * The function checks if targetEditPart can accept the editParts. 
		 * The return values is used by <code>SelectMoveResizeTool</code> 
		 * to update the mouse cursor.
		 * 
		 * <p>The draggable object is needed for the classes the subclasses that
		 *  deals with connections.
		 * 
		 * Takes in account the current position of the cursor 
		 * <code>dropX</code>
		 * 
		 */
		public function dragOverTargetEditPartOnSpecifiedXPosition(draggable:IDraggable, targetEditPart:EditPart, editParts:ArrayCollection, startX:Number, dropX:Number):Boolean {
			return true; 
		}
		
		/**
		 * The function accepts the editParts for the targetEditPart.
		 * @flowerModelElementId _b5rR3r8REd6XgrpwHbbsYQ
		 */
		public function dropOverEditPart(draggable:IDraggable, targetEditPart:EditPart, editParts:ArrayCollection, deltaX:int, deltaY:int):void {
			if (targetEditPart.isEditPartAccepted(editParts))
				targetEditPart.acceptEditPart(editParts, deltaX, deltaY);
		}
		
		/**
		 * The function accepts the editParts for the targetEditPart 
		 * Takes in account the position for the acceptance <code>dropX</code>
		 * Doesn't do nothing be default. Need to be implemented by the 
		 * subclasese.
		 * 
		 */
		public function dropOverEditPartOnSpecifiedXPossition(draggable:IDraggable, targetEditPart:EditPart, editParts:ArrayCollection, deltaX:int, deltaY:int, startX:Number, dropX:Number):void {
			throw new Error("EditPart.dropOverEditPartOnSpecifiedXPossition() should be implemented.");
		}
		
		/**
		 * Called when the inplace editor needs to be shown. If
		 * the subclass supports inplace editing than this method 
		 * must not return null.
		 * 
		 * The implementation is responsible in creating the editing
		 * component, adding it to the diagram, returning it and fill
		 * it with the correct text.
		 * 
		 * @param figure the IFigure where inplace editing has been invoked
		 * @flowerModelElementId _b5rR6L8REd6XgrpwHbbsYQ
		 */
		public function activateInplaceEditor(figure:IFigure):UIComponent {
			return null;
		}
		/**
		 * The implementation has to remove the InplaceEditor (editor).
		 * 
		 * @flowerModelElementId _b51Cor8REd6XgrpwHbbsYQ
		 */
		public function deactivateInplaceEditor(editor:UIComponent):void {
		}
		/**
		 * Called when the inplace editor needs to close and 
		 * the text should be updated.
		 * @flowerModelElementId _SWBa8NXXEd6axtupYmR2VA
		 */	
		public function updateInplaceEditorText(editor:UIComponent):void {
		}
		/**
		 * Has no effect on the given values. Returns an Array with <code>x</code> and <code>y</code> unchanged.
		 * Called from <code>SelectMoveResizeTool</code> during move and resize operations, passing new mouse coordinates
		 * for the moved/resized figures.
		 * 
		 * Subclasses may override to round the given coordinates as necessary.
		 * @flowerModelElementId _X8stYOwaEd-Mq65kNpXUPA
		 */ 
		public function snapToGrid(x:Number, y:Number):Array {
			return [x, y];
		}
		
		/**
		 * This function tells if to model-objects can be connected or not.
		 * It should be implemented by the subclases if the <code>hasCustomAcceptConnectionWithAnotherEditPart</code> variable is set to true
		 * used in <code>SelectMoveResizeTool</code>  to see if two editParts can be linked with a given link when 
		 * <code>hasCustomAcceptConnectionWithAnotherEditPart</code> is set to true.
		 */ 
		public function isConnectionAcceptedBetweenModels(sourceModel:Object, targetModel:Object, connectionModel:Object ):Boolean {
			throw new Error("isConnectionAcceptedBetweenModels() should be implemented.");
		} 
	}
	
}