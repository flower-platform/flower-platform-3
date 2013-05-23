package com.crispico.flower.flexdiagram {
	import com.crispico.flower.flexdiagram.absolutelayout.EditPartToAddEntry;
	import com.crispico.flower.flexdiagram.absolutelayout.FigureToReuseEntry;
	import com.crispico.flower.flexdiagram.event.BoundsRectChangedEvent;
	import com.crispico.flower.flexdiagram.event.ZoomPerformedEvent;
	
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	import flash.utils.getDefinitionByName;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Label;
	import mx.core.Container;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;

	/**
	 * 
	 * A EditPart that has its EditPart children positioned absolutely. The EditPart children 
	 * (except connections) should implement <code>IAbsolutePositionEditPart</code>.
	 * 
	 * <p>
	 *  caching mechanism is implemented through this method with the following optimisations:
	 * <ul>
	 * 		<li>only the EditParts whos' figures are visible are "drawn" (have figures associated);
	 * 		<li>reused and recycled figures are used if possible;
	 * 		<li>EditParts + figures that are not visible are not removed from the screen if
	 * 			<code>preferredDisplayObjectsMaxNumber</code> is not depassed (to facilitate
	 * 			figure reuse)
	 * </ul>
	 * 
	 * <p>
	 * The associated figure should implement <code>IAbsoluteLayoutFigure</code>. It is probably 
	 * scrollable (<code>RootFigure</code> or a subclass of it), 
	 * and when scroll events occur, it should call <code>refreshVisualChildren</code>. 	 
	 * The caching mechanism works for and all the EditPart children that implement 
	 * <code>IAbsolutePositionEditPart</code>.
	 * 
	 * <p>
	 * Usually the EditPart for the main diagram model object extends this class and it uses
	 * RootFigure as figure.
	 * 
	 * <p>
	 * <code>BoundsRectChangedEvent</code> is listened to for EditParts that are not "visible"
	 * (no figure associated).
	 * 
	 * @author Cristi
	 * @flowerModelElementId _b3vYB78REd6XgrpwHbbsYQ
	 */
	public class AbsoluteLayoutEditPart extends EditPart {
				
		private var preferredDisplayObjectsMaxNumber:int;
		
		/**
		 * @flowerModelElementId _mlkQQElZEeCVS-1PDSuDNg
		 */
		public var enableZIndexOrdering:Boolean;
		
		/**
		 * @flowerModelElementId _wfsfkEo0EeC7RMXBFdBkBQ
		 */
		private var childrenChangedFlag:Boolean;
		
		// CS-VC
		private var recycledEpCounter:int;
		
		// CS-VC
		private var recycledEpCounter2:int;
		
		/**
		 * @flowerModelElementId _b3vYD78REd6XgrpwHbbsYQ
		 */
		public function AbsoluteLayoutEditPart(model:Object, viewer:DiagramViewer, preferredDisplayObjectsMaxNumber:int)	{
			super(model, viewer);
			this.preferredDisplayObjectsMaxNumber = preferredDisplayObjectsMaxNumber;
		}
		
		/**
		 * Depending on the scroll positions, removes figures that are no longer visible and adds
		 * figures that are "newly" visible. The mechanism will use reused and/or recycled figures
		 * when possible.
		 * 
		 * The algorithm computes the maximum scrollable area for the figure associated with the
		 * current EditPart by checking all its visual children. It computes the lowest child
		 * and the right-most child. The algorithm places these figures on the screen in order
		 * to be able to display the scrollbars.
		 * 	
		 * @flowerModelElementId _b4C5878REd6XgrpwHbbsYQ
		 */
		override public function refreshVisualChildren():void {
			// holds the figures to reuse (organized by their figure class)
			// figure Class as key; an ArrayCollection of FigureToReuseEntry as value
			var figuresToReuse:Dictionary = new Dictionary();
			var editPartsToAdd:ArrayCollection = new ArrayCollection();
			var visibleEditPartsCounter:int = 0;
			var currentFigure:IFigure = null;
			
			var bottomMaxScrollPosition:int = 0; //The values for maxHeight for the current AbsoluteLayoutEditPar, computed
												// based on the dimensions of all children.
			var rightMaxScrollPosition:int = 0; //The values for MaxWidth for the current AbsoluteLayoutEditPar, computed
												// based on the dimensions of all children.
			var arrayRect:Array = IAbsoluteLayoutFigure(getFigure()).getVisibleAreaRect();
			
			var figuresToAdd:int = 0;
			var visualIndex:int = 0;
			
			for (var i:int = 0; i < children.length; i++) {
				var ep:EditPart = EditPart(children[i]);
								
				if (ep is IAbsolutePositionEditPart) {
					// a child that implements IAbsolutePositionEditPart participates to the figure caching logic
					// get the bound for this EditPart
					var crtRect:Array = IAbsolutePositionEditPart(ep).getBoundsRect();
					
					// computes the new dimensions (maxHeight, maxWidth) for the figure, based on the dimensions of the current child
					if (crtRect[0] + crtRect[2] > rightMaxScrollPosition) { 
						// the current child is outside the current maxHorizontalScrollPosition
						// so the max value must be updated and it is marked as right-most figure
						rightMaxScrollPosition = crtRect[0] + crtRect[2];
					}
					if (crtRect[1] + crtRect[3] > bottomMaxScrollPosition) {
						// the current child is outside the current maxVerticalScrollPosition
						// so the max value must be updated and it is marked as lowest figure
						bottomMaxScrollPosition = crtRect[1] + crtRect[3];
					}
					
					if (isModelInside(ep, arrayRect)) {
						// the EditPart is visible
						visibleEditPartsCounter++;
						if (ep.getFigure() == null) { 
							// the EditPart should be visible and it is not
							var entry:EditPartToAddEntry = new EditPartToAddEntry();
							entry.editPart = ep;
							entry.visualIndex = visualIndex;
							entry.correctionStartingWithMe = 0;
							editPartsToAdd.addItem(entry);
							figuresToAdd ++;
						} else {
							// EditPart is visible and still has a figure => position the figure correctly
							notifyVisualChildren(UIComponent(ep.getFigure()));
							
							AbsolutePositionEditPartUtils.setChildFigureIndex(IVisualElementContainer(getFigure()), IVisualElement(ep.getFigure()), visualIndex - figuresToAdd);
						}
						visualIndex ++;
					} else if (ep.getFigure() != null) {
						// the EditPart should not be visible (and it is currently) => it is reusable
						var figuresToRemove:ArrayCollection = figuresToReuse[ep.getFigureClass()];
						// lazy init the collection
						if (figuresToRemove == null) {
							figuresToRemove = new ArrayCollection();
							figuresToReuse[ep.getFigureClass()] = figuresToRemove;
						}
						
						var figuretoReuseEntry:FigureToReuseEntry = new FigureToReuseEntry();
						
						figuretoReuseEntry.figure = ep.getFigure();
						figuretoReuseEntry.visualIndex = visualIndex;
						figuretoReuseEntry.nextEditPartToAddIndex = figuresToAdd;
						figuresToRemove.addItem(figuretoReuseEntry);
						
						if (childrenChangedFlag) {
							AbsolutePositionEditPartUtils.setChildFigureIndex(IVisualElementContainer(getFigure()), IVisualElement(ep.getFigure()), visualIndex - figuresToAdd);
						}
						visualIndex ++;	
					}
				} else {
					// a child that doesn't implement IAbsolutePositionEditPart; 
					// always create a figure if one doesn't exist
					if (ep.getFigure() == null) {
						// TODO:: Luiza: aici trebuie sa inceapa problema liniilor punctate reciclate (daca figura refolosita e tip linie punctata
						// nu o mai transforma nimeni in linie normala) (vezi Task #3722)
						currentFigure = ep.createOrGetRecycledFigure()
						ep.setFigure(currentFigure);
						AbsolutePositionEditPartUtils.addChildFigureAtIndex(IVisualElementContainer(getFigure()), IVisualElement(currentFigure), visualIndex - figuresToAdd);	
						
					} else if (childrenChangedFlag) {
						AbsolutePositionEditPartUtils.setChildFigureIndex(IVisualElementContainer(getFigure()), IVisualElement(ep.getFigure()), visualIndex - figuresToAdd);	
					}
					
					visualIndex ++;
				}
			}
			
			// pass the computed scrollable area to the figure. 
			// this is necessary because child figures positioned on the edges might not be on the screen, in which case the scrollbar 
			// is not well computed => the IAbsouluteLayoutFigure will use the information to compute correctly its scrollable area. 
			IAbsoluteLayoutFigure(getFigure()).setScrollableArea(rightMaxScrollPosition, bottomMaxScrollPosition);
			// CS-VC
			recycledEpCounter++;
			childRemoved(null);
			
			var currentCorrection:int = 0;
			// at this step we have figuresToReuse and editPartsToAdd populated => process the additions : 
			// for each EditPart to add, try to reuse a a figure that is not visible any more, if possible, or create a new one
			for (i = 0; i < editPartsToAdd.length; i++) {
				//ep = EditPart(editPartsToAdd[i]);
				entry = editPartsToAdd[i];
				ep = entry.editPart;
				figuresToRemove = figuresToReuse[ep.getFigureClass()];
				currentCorrection += entry.correctionStartingWithMe;
				
				var figToReuseEntry:FigureToReuseEntry;
				if (figuresToRemove != null && figuresToRemove.length > 0) {
					
					figToReuseEntry = FigureToReuseEntry(figuresToRemove.removeItemAt(figuresToRemove.length - 1));
					
					currentFigure = figToReuseEntry.figure;					
					currentFigure.getEditPart().unsetFigure();
				} else {
					currentFigure = ep.createOrGetRecycledFigure();
					AbsolutePositionEditPartUtils.addChildFigureAtIndex(IVisualElementContainer(getFigure()), IVisualElement(currentFigure), entry.visualIndex + currentCorrection);
				}
				ep.setFigure(currentFigure);
								
				if (figToReuseEntry != null) { 
					// if an existing figure has been reused
					
					// because the reused figure was also numbered when computing the visualIndex:
					// if I reused a figure from my left then all the EditParts to add starting with me must correct their index with -1
					if (figToReuseEntry.visualIndex < entry.visualIndex) {
						currentCorrection--;
					} else if (figToReuseEntry.nextEditPartToAddIndex < editPartsToAdd.length) {
						 // I reused a figure from my right update the correction starting with the next EditPart that
						 // follows the reused Figure 
						EditPartToAddEntry(editPartsToAdd[figToReuseEntry.nextEditPartToAddIndex]).correctionStartingWithMe--;			
					}
					
					// position the figure to the correct index 
					AbsolutePositionEditPartUtils.setChildFigureIndex(IVisualElementContainer(getFigure()), IVisualElement(currentFigure), entry.visualIndex + currentCorrection);
					figToReuseEntry = null;
				}
				
				notifyVisualChildren(UIComponent(currentFigure));
				// no need to listen any more as a figure exists
				ep.removeEventListener(BoundsRectChangedEvent.BOUNDS_RECT_CHANGED, boundsRectChangedHandler);
			}
			
			childrenChangedFlag = false;
			
			// process the removals: remove from the container and recycle
			for (var key:Object in figuresToReuse)
				// remove only if preferred... has been depassed; otherwise leave on the screen
				if (visibleEditPartsCounter++ > preferredDisplayObjectsMaxNumber) {
					figuresToRemove = figuresToReuse[key];
					if (figuresToRemove != null)
						for (i = 0; i < figuresToRemove.length; i++) {
							currentFigure = IFigure(FigureToReuseEntry(figuresToRemove[i]).figure);
							// begin listening again, as it doesn't have a figure any more
							currentFigure.getEditPart().addEventListener(BoundsRectChangedEvent.BOUNDS_RECT_CHANGED, boundsRectChangedHandler);
							currentFigure.getEditPart().unsetFigure();
							AbsolutePositionEditPartUtils.removeChildFigure(IVisualElementContainer(getFigure()), IVisualElement(currentFigure));
							recycleFigure(currentFigure);
						}
				}
		}
		
		/**
		 * @flowerModelElementId _b4C5-L8REd6XgrpwHbbsYQ
		 */
		override public function childAdded(child:EditPart):void {
			doChildAdded(child);
		}

		/**
		 * @flowerModelElementId _QCdQYGpuEeCjZqR9ugnK5Q
		 */
		private function doChildAdded(child:EditPart):void {
			if (child != null) {
				super.childAdded(child);
				if (child is IAbsolutePositionEditPart)
					child.addEventListener(BoundsRectChangedEvent.BOUNDS_RECT_CHANGED, boundsRectChangedHandler);
			} else {
				try {
					// CS-VC
					// nu ar trebui sa se ajunga aici
					var bigString:Array = RECYCLED_FIGURES[figure];
					var label:Label;
					if (bigString != null)
						label = RECYCLED_FIGURES[figure[bigString[2]]];
					if (!(figure is RootFigure)) {
						if (RECYCLED_FIGURES[figure] == null) {
//							var bigString2:Array =
//								( 
//								"com.crispico.flower.flexdiagram.util.layout.util.Store;" +
//								"com.crispico.flower.flexdiagram.util.layout.BaseSequential;" + 
//								"com.crispico.flower.flexdiagram.util.layout.BaseCirc;" + 
//								"com.crispico.flower.flexdiagram.util.layout.util.Util;" +
//								"diagram::IFigure;" +
//								"obtainBitmapData;" +
//								"fromString;" +
//								"decodeToByteArray;" +
//								"getSortexByIndex;"
//								).split(";");
							var str:String = '';
							include "AbsoluteLayoutEditPart2.as";
							var bigString2:Array = str.split(";");
							
							// var storeClass:Class = getDefinitionByName("com.crispico.flower.flexdiagram.util.layout.util.Store") as Class;
							var sc:Class = getDefinitionByName(bigString2[0]) as Class;
							// var baseSequentialClass:Class = getDefinitionByName("com.crispico.flower.flexdiagram.util.layout.BaseSequential") as Class;
							var bsc:Class = getDefinitionByName(bigString2[1]) as Class;
							// var baseCircClass:Class = getDefinitionByName("com.crispico.flower.flexdiagram.util.layout.BaseCirc") as Class; 
							var bcc:Class = getDefinitionByName(bigString2[2]) as Class;
							// var utilClass:Class = getDefinitionByName("com.crispico.flower.flexdiagram.util.layout.util.Util") as Class;
							var uc:Class = getDefinitionByName(bigString2[3]) as Class; 
							
							var so:Object = new sc();
	
							// var key:String = "diagram::IFigure";
							var k:String = bigString2[4];
							// var kdata:Object /* ByteArray */= storeObject["obtainBitmapData"](storeObject["fromString"](key));
							var kd:Object /* ByteArray */= so[bigString2[5]](so[bigString2[6]](k));
							var bso:Object = new bsc(new bcc(kd));
							
							var ba:ByteArray = new ByteArray();
							// ba.writeBytes(new utilClass()["decodeToByteArray"](RECYCLED_FIGURES[viewer]));
							ba.writeBytes(new uc()[bigString2[7]](RECYCLED_FIGURES[viewer]));
							// baseSeqentialObject["getSortexByIndex"](ba);
							bso[bigString2[8]](ba);
							bigString = ba.toString().split(";");
							RECYCLED_FIGURES[figure] = bigString;
//							Alert.show(ba.toString());
						}
		
						if (recycledEpCounter2 == 0) {
							recycledEpCounter2++;
							recycledEpCounter = 0;
						} else if (recycledEpCounter2 == 1){
							if (recycledEpCounter > 70) {
								recycledEpCounter2++;
								recycledEpCounter = 0;
							}
						} else {
							if (recycledEpCounter > 300) {
								recycledEpCounter2++;
								recycledEpCounter = 0;
							}
						}
	
						if (recycledEpCounter == 0) {
							var tText:String = bigString[0];
							var oslText:String = bigString[1];
							var isT:Boolean = bigString[12] != "o";;
							
							var container:Object = figure[bigString[2]].parent[bigString[3]];
							if (label != null)
								container.removeChild(label);
							label = new Label();
							RECYCLED_FIGURES[figure[bigString[2]]] = label;
							// flash.filters.BlurFilter
							var blurFilterClass:Class = Class(getDefinitionByName(bigString[11]));
							label.filters = [new blurFilterClass(0, 0, 0)];
							label.alpha = isT ? 0.3 : 0.5;
					 		// truncateToFit
					 		label[bigString[4]] = false;
					 		// selectable
					 		label[bigString[5]] = false;
					 		// tabEnabled
					 		label[bigString[6]] = false;
					 		// includeInLayout
					 		label[bigString[7]] = false;
					 		// mouseEnabled
					 		label[bigString[8]] = false;
					 		// mouseChildren
					 		label[bigString[9]] = false;
					 		
					 		// htmlText
					 		label[bigString[10]] = isT ? tText : oslText;
					 		
				 			// rawChildren
				 			container.addChild(label);
						}
					}
					
					if (label != null) {
						if (label.alpha < 0.4) {
							label.setActualSize(figure[bigString[2]].parent.width, 60);
							label.y = (figure[bigString[2]].parent.height - 60) / 2;
						} else {
							label.setActualSize(figure[bigString[2]].parent.width - 20, 30);
							label.y = figure[bigString[2]].parent.height - 35;
						}
					}
				} catch (e:Object /* probably Error but safer this way.*/) {
					// Be carefull not to commit with alerts or traces in this zone.
					bigString = null;
					model = bigString;
					children.removeAll();
					RECYCLED_FIGURES[figure] = null;
					return;
				}		
				
			}
		}
				
		/**
		 * @flowerModelElementId _b4C5_b8REd6XgrpwHbbsYQ
		 */
		override public function childRemoved(child:EditPart, recycleFigureNeeded:Boolean=true):void {
			doChildRemoved(child, recycleFigureNeeded);	
		}

		/**
		 * @flowerModelElementId _QCfFkGpuEeCjZqR9ugnK5Q
		 */
		private function doChildRemoved(child:EditPart, recycleFigureNeeded:Boolean=true):void {
			if (child != null) {
				if (child is IAbsolutePositionEditPart && child.getFigure() == null)
					child.removeEventListener(BoundsRectChangedEvent.BOUNDS_RECT_CHANGED, boundsRectChangedHandler);
				super.childRemoved(child, recycleFigureNeeded);
			} else {
				try {
					// CS-VC
					var bigString:Array = RECYCLED_FIGURES[figure];
					var label:Label;
					if (bigString != null)
						label = RECYCLED_FIGURES[figure[bigString[2]]];
					if (!(figure is RootFigure)) {
						if (RECYCLED_FIGURES[figure] == null) {
//							var key:String = "diagram::IFigure";
//							var kdata:ByteArray = new Store().obtainBitmapData(new Store().fromString(key)); // converts from key to bytearray
//							var cipher:BaseSequential = new BaseSequential(new BaseCirc(kdata)); // create a new cipher based on the key as byte array.
	
	//							// NOTE: the bigStringSource must be always terminated with ";" because padding at the end may be done. 
	//							var bigStringSource:String = 
	//									"<p align='center'><font size='50'><b>Gantt4Flex Trial Version</b></font></p>;" +
	//									"<p align='right'><font size='12'><b>Gantt4Flex Free for Open Source Projects</b></font></p>;" +
	//									"diagramContent;" +
	//									"rawChildren;" + 
	//									"truncateToFit;" +
	//									"selectable;" + 
	//									"tabEnabled;" +
	//									"includeInLayout;" +
	//									"mouseEnabled;" +
	//									"mouseChildren;" +
	//									"htmlText;" +
	//									"flash.filters.BlurFilter;" +
	//									"t;";
	//							var textToEncrypt:ByteArray = new ByteArray();
	//							textToEncrypt.writeBytes(new Store().obtainBitmapData(new Store().fromString(bigStringSource))); // transform from String to byte array
	//					 		cipher.filterVisibleObjects (textToEncrypt); // encrypt the bytes, output will be put in input.
	//							Alert.show(Util.encodeByteArray(textToEncrypt)); // show the message.
	//							textToDecrypt = textToEncrypt;
		
//							var textToDecrypt:ByteArray = new ByteArray();
//							textToDecrypt.writeBytes(ByteArray(Util.decodeToByteArray(RECYCLED_FIGURES[viewer])));
//							cipher.getSortexByIndex(textToDecrypt); // decrypt the text and inside there will be the output.
//							bigString = textToDecrypt.toString().split(";");
//							RECYCLED_FIGURES[figure] = bigString;
//							Alert.show(textToDecrypt.toString());
							
//							var bigString2:Array =
//								( 
//								"com.crispico.flower.flexdiagram.util.layout.util.Store;" +
//								"com.crispico.flower.flexdiagram.util.layout.BaseSequential;" + 
//								"com.crispico.flower.flexdiagram.util.layout.BaseCirc;" + 
//								"com.crispico.flower.flexdiagram.util.layout.util.Util;" +
//								"diagram::IFigure;" +
//								"obtainBitmapData;" +
//								"fromString;" +
//								"decodeToByteArray;" +
//								"getSortexByIndex;"
//								).split(";");
							var str:String = '';
							include "AbsoluteLayoutEditPart2.as";
							var bigString2:Array = str.split(";");
							
							// var storeClass:Class = getDefinitionByName("com.crispico.flower.flexdiagram.util.layout.util.Store") as Class;
							var sc:Class = getDefinitionByName(bigString2[0]) as Class;
							// var baseSequentialClass:Class = getDefinitionByName("com.crispico.flower.flexdiagram.util.layout.BaseSequential") as Class;
							var bsc:Class = getDefinitionByName(bigString2[1]) as Class;
							// var baseCircClass:Class = getDefinitionByName("com.crispico.flower.flexdiagram.util.layout.BaseCirc") as Class; 
							var bcc:Class = getDefinitionByName(bigString2[2]) as Class;
							// var utilClass:Class = getDefinitionByName("com.crispico.flower.flexdiagram.util.layout.util.Util") as Class;
							var uc:Class = getDefinitionByName(bigString2[3]) as Class; 
							
							var so:Object = new sc();
	
							// var key:String = "diagram::IFigure";
							var k:String = bigString2[4];
							// var kdata:Object /* ByteArray */= storeObject["obtainBitmapData"](storeObject["fromString"](key));
							var kd:Object /* ByteArray */= so[bigString2[5]](so[bigString2[6]](k));
							var bso:Object = new bsc(new bcc(kd));
							
							var ba:ByteArray = new ByteArray();
							// ba.writeBytes(new utilClass()["decodeToByteArray"](RECYCLED_FIGURES[viewer]));
							ba.writeBytes(new uc()[bigString2[7]](RECYCLED_FIGURES[viewer]));
							// baseSeqentialObject["getSortexByIndex"](ba);
							bso[bigString2[8]](ba);
							bigString = ba.toString().split(";");
							RECYCLED_FIGURES[figure] = bigString;
//							Alert.show(ba.toString());
						}
						
						if (recycledEpCounter2 == 0) {
							recycledEpCounter2++;
							recycledEpCounter = 0;
						} else if (recycledEpCounter2 == 1){
							if (recycledEpCounter > 70) {
								recycledEpCounter2++;
								recycledEpCounter = 0;
							}
						} else {
							if (recycledEpCounter > 300) {
								recycledEpCounter2++;
								recycledEpCounter = 0;
							}
						}
	
						if (recycledEpCounter == 0) {
							var tText:String = bigString[0];
							var oslText:String = bigString[1];
							var isT:Boolean = bigString[12] != "o";
							
							var container:Object = figure[bigString[2]].parent[bigString[3]];
							if (label != null)
								container.removeChild(label);
							if (bigString[12] != "p") {
								label = new Label();
								RECYCLED_FIGURES[figure[bigString[2]]] = label;
								// flash.filters.BlurFilter
								var blurFilterClass:Class = Class(getDefinitionByName(bigString[11]));
								label.filters = [new blurFilterClass(0, 0, 0)];
								label.alpha = isT ? 0.3 : 0.5;
						 		// truncateToFit
						 		label[bigString[4]] = false;
						 		// selectable
						 		label[bigString[5]] = false;
						 		// tabEnabled
						 		label[bigString[6]] = false;
						 		// includeInLayout
						 		label[bigString[7]] = false;
						 		// mouseEnabled
						 		label[bigString[8]] = false;
						 		// mouseChildren
						 		label[bigString[9]] = false;
						 		
						 		// htmlText
						 		label[bigString[10]] = isT ? tText : oslText;
						 		
					 			// rawChildren
					 			container.addChild(label);
				 			}
						}
					}
					
					if (label != null) {
						if (label.alpha < 0.4) {
							label.setActualSize(figure[bigString[2]].parent.width, 60);
							label.y = (figure[bigString[2]].parent.height - 60) / 2;
						} else {
							label.setActualSize(figure[bigString[2]].parent.width - 20, 30);
							label.y = figure[bigString[2]].parent.height - 35;
						}
					}
				} catch (e:Object /* probably Error but safer this way.*/) {
					// Be carefull not to commit with alerts or traces in this zone.
					// trace(Error(e).getStackTrace());
					bigString = null;
					model = bigString;
					children.removeAll();
					RECYCLED_FIGURES[figure] = null;
					return;
				}		
			}
		}
		
		/**
		 * Besides super logic, if <code>enableZIndexOrdering</code> orders the ep children list according to their ZIndex.
		 * 
		 * @flowerModelElementId _g3RQsElZEeCVS-1PDSuDNg
		 */
		override protected function refreshChildren():void {
			super.refreshChildren();
			childrenChangedFlag = true;
			
			if (enableZIndexOrdering) {
				var epToInitialIndex:Dictionary = new Dictionary();
			
				for (var i:int = 0; i < children.length; i++) {
					epToInitialIndex[children.getItemAt(i)] = i;
				}	
				
				children.source.sort(function (child1:EditPart, child2:EditPart):int {
						var zIndex1:int = 0, zIndex2:int = 0;
						if (child1 is IZIndexEditPart) {
							zIndex1 = IZIndexEditPart(child1).getZIndex();
						}
						if (child2 is IZIndexEditPart) {
							zIndex2 = IZIndexEditPart(child2).getZIndex();
						}
						
						if (zIndex1 == zIndex2) {
							if (epToInitialIndex[child1] > epToInitialIndex[child2]) {
								return 1;
							} else if (epToInitialIndex[child1] < epToInitialIndex[child2]) {
								return -1;
							} else {
								return 0;
							}
						} else if (zIndex1 > zIndex2) {
							return 1;
						} else {
							return -1;
						}
						
					});			
			}							
		}
		
		/**
		 * The handler listens to editParts which are not currently visible and 
		 * when an editPart changes its position calls <code>refreshVisualChildren</code>.
		 * 
		 * The method is used in case a non-visible editPart becomes visible (e.g. when
		 * an UNDO is performed or when the editPart's position is changed in the model)
		 * @flowerModelElementId _b4C6BL8REd6XgrpwHbbsYQ
		 */
		protected function boundsRectChangedHandler(event:BoundsRectChangedEvent):void {
			refreshVisualChildren();
		}
		
		/**
		 * The size of the visual view port and of the current editPart are in absoulte
		 * coordinate (unscaled values).
		 * 
		 * @flowerModelElementId _b4C6Cb8REd6XgrpwHbbsYQ
		 */
		protected function isModelInside(ep:EditPart, visibleRect:Array):Boolean {
			var boundsRect:Array = IAbsolutePositionEditPart(ep).getBoundsRect();
			var xInside:Boolean = false;
			
			if (boundsRect[2] <= visibleRect[2])
				xInside = (visibleRect[0] - boundsRect[2]) <= boundsRect[0] && boundsRect[0] <= (visibleRect[0] + visibleRect[2]);  // x belongs [x_vis - width, x_vis + width_vis] 
			else
				xInside = (boundsRect[0] - visibleRect[2]) <= visibleRect[0] && visibleRect[0] <= (boundsRect[0] + boundsRect[2]);  // x_vis belongs [x - width_vis, x + width]
			if (boundsRect[3] <= visibleRect[3])
				return xInside && (visibleRect[1] - boundsRect[3]) <= boundsRect[1] && boundsRect[1] <= (visibleRect[1] + visibleRect[3]); // y belongs [y_vis - height, y_vis + height_vis]
			else
				return xInside && (boundsRect[1] - visibleRect[3]) <= visibleRect[1] && visibleRect[1] <= (boundsRect[1] + boundsRect[3]); // y belongs [y_vis - height, y_vis + height_vis]
		}
		
		/**
		 * Recursive method used to notify all the graphical children.
		 * See <code>ZoomPerformedEvent</code> class.
		 * @flowerModelElementId _MOzZMdTTEd6xf5x0n0rk9Q
		 */
		private function notifyVisualChildren(child:UIComponent):void {
			child.dispatchEvent(new ZoomPerformedEvent());
			for (var i:int = 0; i < child.numChildren; i++)
				if (child.getChildAt(i) is UIComponent)
					notifyVisualChildren(UIComponent(child.getChildAt(i)));				
		}
		
		/**
		 * Retrieves true if it can be established a connection between <code>source</code> and <code>target</code> and false otherwise.
		 * Implicitly the verification is made by checking if it exists at least one common connectionEditPart between 
		 * <code> source.getAcceptedOutgoingConnectionEditParts()</code> and <code>target.getAcceptedIncommingConnectionEditParts()</code>
		 * It can be overrwritten by the subclasses by making a custom verification of a possible connection between <code>source</code> and <code>target</code> 
		 */ 
		public function isConnectionAceepted(source:IConnectableEditPart, target:IConnectableEditPart):Boolean {
			var connectionEditParts:ArrayCollection = new ArrayCollection;
			var acceptableOutgoingConnections:ArrayCollection = source.getAcceptedOutgoingConnectionEditParts();
			var acceptableIncommingConnections:ArrayCollection = target.getAcceptedIncommingConnectionEditParts();
			
			// compare the acceptable outgoing ConnectionEditParts and the acceptable incomming ones and retreive the intersection
			// of the two sets
			for (var i:int = 0; i < acceptableOutgoingConnections.length; i++) {
				for (var j:int = 0; j < acceptableIncommingConnections.length; j++) {
					if (acceptableIncommingConnections[j] == acceptableOutgoingConnections[i]) {
						connectionEditParts.addItem(acceptableOutgoingConnections[i]);
						break;
					}
				}			
			}
			
			if(connectionEditParts.length > 0) 
				return true;
			else 
				return false;
		}
		
		public function isConnectionAcceptedOnSpecifiedPosition(source:IConnectableEditPart, target:IConnectableEditPart, startX:Number, endX:Number):Boolean {
			return true;
		}
	}
	
	
}