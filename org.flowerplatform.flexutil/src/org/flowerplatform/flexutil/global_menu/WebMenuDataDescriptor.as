package org.flowerplatform.flexutil.global_menu {
	import mx.collections.ICollectionView;
	import mx.collections.IList;
	import mx.collections.XMLListCollection;
	import mx.controls.menuClasses.IMenuDataDescriptor;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionUtil;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.action.IComposedAction;
	
	/**
	 * Atention: If an action is a ComposedAction it means it has children.
	 * (ActionUtil.processAndIterateActions will have filtered it out, if none of 
	 * its children were visible)
	 * 
	 * @author Mircea Negreanu
	 */
	public class WebMenuDataDescriptor implements IMenuDataDescriptor {
		
		protected var _actionProvider:IActionProvider;
		
		protected var _selection:IList;
		
		public function WebMenuDataDescriptor(ap:IActionProvider = null, sel:IList = null) {
			super();
			
			_actionProvider = ap;
			_selection = sel;
		}
		
		public function set actionProvider(ap:IActionProvider):void {
			_actionProvider = ap;	
		}
		
		public function get actionProvider():IActionProvider {
			return _actionProvider;
		}
		
		public function set selection(sel:IList):void {
			_selection = sel;
		}
		
		public function get selection():IList {
			return _selection;
		}
		
		/**
		 * Gets the id from the node and returns the list of children as am XMLList
		 */
		public function getChildren(node:Object, model:Object=null):ICollectionView {
			var id:String = null;
			if (node is XML) {
				id = node.@id;
			}

			var children:XMLList  = new XMLList();
			
			ActionUtil.processAndIterateActions(id, actionProvider.getActions(selection), selection, this, function(action:IAction):void {
				var xml:XML = <menuitem/>;
				xml.@id = action.id;
				xml.@label = action.label;
				xml.@enabled = action.enabled;
				xml.@icon = action.icon;
				
				children += xml;
			});
				
			return new XMLListCollection(children);
		}
		
		/**
		 * Gets the id from node and checks if the associated action is IComposedActions
		 * 
		 * @return true if the action is IComposedAction, false otherwise
		 */
		public function hasChildren(node:Object, model:Object=null):Boolean {
			if (node is XML) {
				var id:String = node.@id;
				
				for each (var action:IAction in actionProvider.getActions(selection)) {
					if (action.id == id) {
						if (action is IComposedAction) {
							return true;
						} else {
							return false;
						}
					}
				}
				
				return false;
			}
			return false;
		}
		
		public function getData(node:Object, model:Object=null):Object {
			return Object(node);
		}
		
		/**
		 * Check the associated action to the node.@id. 
		 * 
		 * @returns true if the action is IComposedAction, false otherwise
		 */
		public function isBranch(node:Object, model:Object=null):Boolean {
			if (node is XML) {
				var id:String = node.@id;
				
				for each (var action:IAction in actionProvider.getActions(selection)) {
					if (action.id == id) {
						return (action is IComposedAction);
					}
				}
			}
			return false;
		}
		
		/**
		 * Normal menu (other possible types: separator, check or radio)
		 */
		public function getType(node:Object):String {
			return "normal";
		}
		
		public function addChildAt(parent:Object, newChild:Object, index:int, model:Object=null):Boolean {
			return false;
		}
		
		public function removeChildAt(parent:Object, child:Object, index:int, model:Object=null):Boolean {
			return false;
		}
		
		public function isEnabled(node:Object):Boolean {
			return true;
		}
		
		public function setEnabled(node:Object, value:Boolean):void {
		}
		
		public function isToggled(node:Object):Boolean {
			return false;
		}
		
		public function setToggled(node:Object, value:Boolean):void {
		}
		
		public function getGroupName(node:Object):String {
			return null;
		}
	}
}