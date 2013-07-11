package com.crispico.flower.mp.codesync.base
{
	import com.crispico.flower.flexdiagram.action.ActionContext;
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.flexdiagram.action.IActionProvider2;
	import com.crispico.flower.flexdiagram.contextmenu.ActionEntry;
	import com.crispico.flower.flexdiagram.contextmenu.ClientNotifierData;
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuManager;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	import com.crispico.flower.flexdiagram.contextmenu.IContextMenuLogicProvider;
	import com.crispico.flower.flexdiagram.contextmenu.SubMenuEntry;
	import com.crispico.flower.flexdiagram.contextmenu.SubMenuEntryModel;
	import com.crispico.flower.mp.codesync.base.action.DiffAction;
	import com.crispico.flower.mp.codesync.base.action.DiffActionEntry;
	import com.crispico.flower.mp.codesync.base.action.DiffContextMenuEntry;
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeStatefulClient;
	import com.crispico.flower.mp.codesync.base.editor.CodeSyncEditorStatefulClient;
	
	import flash.display.DisplayObjectContainer;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.ClassFactory;
	import mx.core.UITextField;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	import mx.events.ToolTipEvent;
	
	import org.flowerplatform.communication.tree.GenericTreeItemRenderer;
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.communication.tree.TreeNodeHierarchicalModelAdapter;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.tree.TreeList;
	
	import spark.events.ListEvent;
	
	use namespace mx_internal;
	
	public class DiffTree extends GenericTreeList { 
	
		public static const TREE_TYPE_DIFF:int = 0;
		
		public static const TREE_TYPE_ANCESTOR:int = 1;
		
		public static const TREE_TYPE_LEFT:int = 2;
		
		public static const TREE_TYPE_RIGHT:int = 3;
		
		public var treeType:int = -1;
		
		public var projectPath:String;
		
		public var contextMenuContainer:FlowerContextMenu;
		
		public var codeSyncEditorStatefulClient:CodeSyncEditorStatefulClient;
		
		
		public function DiffTree() {
			super();
//			var clientNotifierData:ClientNotifierData = ContextMenuManager.INSTANCE.registerClient(this, true, this, this, null, null, null, this, 50);
//			clientNotifierData.isOverSpecialSelectedElementFunction = function (x:Number, y:Number):Boolean {
//				return true;
//			};
//			ContextMenuManager.INSTANCE.expandMenuDelay = 0;
//			ContextMenuManager.INSTANCE.accelerateExpandMenuDelay = 0;
			
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			
//			showRoot = true;
//			_contextMenuEnabled = true;
//			fillContextMenuFunction = _fillContextMenu;
		
			itemRenderer = new ClassFactory(DiffTreeNodeItemRenderer);
			hierarchicalModelAdapter = new TreeNodeHierarchicalModelAdapter();

			addEventListener(ListEvent.ITEM_ROLL_OVER, rollOverHandler);
			addEventListener(ListEvent.ITEM_ROLL_OUT, rollOutHandler);
			//addEventListener(ToolTipEvent.TOOL_TIP_CREATE, toolTipCreateHandler);
			addEventListener(MouseEvent.CLICK, itemClickHandler);
		}
		
		override public function expandCollapseNode(modelWrapper:HierarchicalModelWrapper):void	{
			if (!modelWrapper.expanded) {
				// i.e. state = collapsed				
				codeSyncEditorStatefulClient.openNode(modelWrapper.treeNode, getContext());			
			} else {
				// i.e. state = expanded; call server only if dispatch type tree
//				statefulClient.closeNode(modelWrapper.treeNode);
			}
			super.expandCollapseNode(modelWrapper);
		}
		
		/**
		 * Creates and registers the stateful client. Can't do this in the constructor because the 
		 * <code>treeType</code> is not yet set.
		 * 
		 * @author Mariana
		 */
		protected function creationCompleteHandler(event:FlexEvent):void {
			var root:DiffTreeNode = new DiffTreeNode();
			root.children = new ArrayCollection();
			var node:DiffTreeNode = new DiffTreeNode();
			node.label = projectPath;
			node.pathFragment = new PathFragment(projectPath, String(treeType));
			node.children = new ArrayCollection();
			node.hasChildren = true;
			root.children.addItem(node);
			rootNode = root;
			
//			clientIdPrefix = "Diff Tree";
//			serviceId = "DiffTreeStatefulService";
//			
//			requestInitialDataAutomatically = true;
			statefulClient = new GenericTreeStatefulClient();
			statefulClient.requestDataOnServer = false;
			statefulClient.treeList = this;
			
			codeSyncEditorStatefulClient.diffTreeStatefulClient[treeType] = statefulClient;
//			CommunicationPlugin.getInstance().statefulClientRegistry.register(statefulClient, null);
		}
		
//		protected override function createRootTreeNode():TreeNode {
//			return new DiffTreeNode();
//		}
		
		private function rollOverHandler(event:ListEvent):void {
			//event.target.toolTip = event.itemRenderer.data.toolTip;
		}
		
		private function rollOutHandler(event:ListEvent):void {
			//event.target.toolTip = null;
		}
		
		private function toolTipCreateHandler(event:ToolTipEvent):void {
			//event.toolTip = new DiffTreeToolTip();
		}
		
//		public function get displayAreaOfSelection():Rectangle {			
//			if (selectedItems.length == 0) 
//				return null;		
//			var selectedItemRenderer:DiffTreeNodeItemRenderer = DiffTreeNodeItemRenderer(itemToItemRenderer(selectedItems[0]));
//			if (selectedItemRenderer == null)
//				return null;
//			var itemRendererLabel:UITextField = UITextField(selectedItemRenderer.getLabel());
//			
//			var upperLeft:Point = itemRendererLabel.localToGlobal(new Point(0,0));
//			var downRight:Point = itemRendererLabel.localToGlobal(new Point(itemRendererLabel.measuredWidth, itemRendererLabel.measuredWidth));
//			return new Rectangle(upperLeft.x, upperLeft.y, downRight.x - upperLeft.x, downRight.y - upperLeft.y);  			
//		}
//
//		public function isOverSelection(event:MouseEvent):Boolean {
//			var point:Point = new Point(event.stageX, event.stageY);
//			if (selectedItems.length == 0) 
//				return false;
//			
//			var selectedItemRenderer:DiffTreeNodeItemRenderer = DiffTreeNodeItemRenderer(itemToItemRenderer(selectedItems[0]));
//			if (selectedItemRenderer == null)
//				return false;
//			var itemRendererLabel:UITextField = UITextField(selectedItemRenderer.getLabel());
//			
//			var upperLeft:Point = itemRendererLabel.localToGlobal(new Point(0,0));
//			var downRight:Point = itemRendererLabel.localToGlobal(new Point(itemRendererLabel.measuredWidth, itemRendererLabel.measuredWidth));
//			if (point.x >= upperLeft.x 
//						&& point.y >= upperLeft.y 
//						&& point.x <= downRight.x
//						&& point.y <= downRight.y) {
//					return true;
//			}
//			return false;
//		}
		
		public function fillContextMenu():void {
			contextMenuContainer.removeAllChildren();
			
			if (selectedItems.length == 0 || selectedItems.length > 1) 
				return;
			// Set the title of the context menu according to the selection.
//			if (selectedItems.length == 1) {
//				contextMenu.setTitle(selectedItems[0].label as String);
//			} else { 
//				contextMenu.setTitle("Multiple Selection");
//			}
		
//			var entries:DiffContextMenuEntries = new DiffContextMenuEntries(contextMenu);			
//			contextMenu.addChild(entries);	
//			entries.fill();
			var diffTreeNode:DiffTreeNode = DiffTreeNode(HierarchicalModelWrapper(selectedItems[0]).treeNode);
			if (diffTreeNode.contextMenuEntries == null)
				return;
			
			action = new DiffAction(null, this);
			action.label = "Synchronize";
			ae = new ActionEntry(action);
			ae.enabled = diffTreeNode.crossColor == 0xFFFFFF;
//			contextMenu.addChild(ae);
			contextMenuContainer.addChild(ae);
			
			var container:DiffContextMenuContainer = new DiffContextMenuContainer();	
//			contextMenu.addChild(container);		
			contextMenuContainer.addChild(container);
			for each (var cmEntry:DiffContextMenuEntry in diffTreeNode.contextMenuEntries) {
				if (cmEntry.actionEntries == null || cmEntry.actionEntries.length == 0) {
					var dummyAction:BaseAction = new BaseAction();
					dummyAction.label = cmEntry.label;
					var ae:ActionEntry = new ActionEntry(dummyAction);
					ae.setStyle("color", cmEntry.color);
					ae.setStyle("textRollOverColor", cmEntry.color);
					if (!cmEntry.right)
						container.leftPanel.addChild(ae);
					else
						container.rightPanel.addChild(ae);
				} else {
					var subMenu:SubMenuEntry = new SubMenuEntry(new SubMenuEntryModel(null, cmEntry.label), contextMenuContainer);
					
					subMenu.setStyle("color", cmEntry.color);
					subMenu.setStyle("textRollOverColor", cmEntry.color);
					

					for each (var actionEntry:DiffActionEntry in cmEntry.actionEntries) {
						var action:DiffAction = new DiffAction(actionEntry, this);
						action.label = actionEntry.label;
						ae = new ActionEntry(action);
						ae.enabled = actionEntry.enabled;
						ae.setStyle("textDecoration", actionEntry.default1 ? "underline" : "none");
						subMenu.getSubMenu().addChild(ae);
					}
					if (!cmEntry.right)
						container.leftPanel.addChild(subMenu);
					else
						container.rightPanel.addChild(subMenu);
				}
			}
		}
		
		private function itemClickHandler(event:MouseEvent):void {
//			ContextMenuManager.INSTANCE.refresh(this);
//			container = new DiffContextMenuContainer();
//			container.createComponentsFromDescriptors();
//			fillContextMenu(/*new FlowerContextMenu(contextMenuContainer)*/);
		}		
		
		public function getContext():ActionContext {
			var context:ActionContext = new ActionContext();
			context.treeType = treeType;
			context.projectPath = projectPath;
			return context;
		}
		
	}
}