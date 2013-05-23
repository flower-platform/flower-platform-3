package com.crispico.flower.flexdiagram.util.treegrid {

  import com.crispico.flower.flexdiagram.util.common.URLIconStyles;
  
  import flash.events.Event;
  import flash.events.KeyboardEvent;
  import flash.ui.Keyboard;
  import flash.xml.XMLNode;
  
  import mx.collections.ArrayCollection;
  import mx.collections.ICollectionView;
  import mx.collections.IViewCursor;
  import mx.collections.ListCollectionView;
  import mx.collections.Sort;
  import mx.collections.SortField;
  import mx.collections.XMLListCollection;
  import mx.controls.DataGrid;
  import mx.controls.dataGridClasses.DataGridColumn;
  import mx.controls.listClasses.BaseListData;
  import mx.controls.listClasses.IListItemRenderer;
  import mx.controls.treeClasses.DefaultDataDescriptor;
  import mx.controls.treeClasses.ITreeDataDescriptor;
  import mx.events.CollectionEvent;
  import mx.events.CollectionEventKind;
  import mx.events.DataGridEvent;
  import mx.events.ListEvent;
  import mx.utils.UIDUtil;

/**
 * OBSERVATION:
 * This class (and others) have been copied to com.crispico.flower.mp.propertieseditor
 * due to following reasons:
 * <ul>
 * 	<li>To avoid adding com.crispico.flower.flexdiagram as a dependancy for  com.crispico.flower.mp.propertieseditor</li>
 *  <li>To avoid copying flexlib 2.5 (1.2 MB) to  com.crispico.flower.mp.propertieseditor</li>
 *  <li>
 * 	Using flexlib 2.4 as until now, would have implied investigation about how to dynamically add/remove rows,
 * 	and when finally migrating to flex lib 2.5 change again the solution found.
 * 	</li>
 * </ul>
 * 
 * List of classes copied: FlowerTreeGrid, FlowerTreeGridColumn, FlowerTreeGridItemRenderer,
 * FlowerTreeGridListData, BitmapContainer, URLIconStyles, ImageCacheEntry, ImageCacheWaitingListEntry,
 * ImageFactory
 */

  /**
   * The class provides functionality for a tree grid. 
   * 
   * The treegrid icons can be set in two ways :
   * <ul>
   * 	<li> using embedded icons.
   * 	<li> providing icons based on their URLs.
   * </ul>
   * 
   * NOTE :
   * This class was created by making a copy/paste from flexLib <code>mx.controls.TreeGrid</code>.
   * The action was done in order to provide functionality for icons to work with URLs, 
   * not only with Classes.
   * @see URLIconStyles
   * @flowerModelElementId _X66ksOwaEd-Mq65kNpXUPA
   */

  [Style(name="disclosureOpenIcon", type="Class", format="EmbeddedFile", inherit="no")]

  [Style(name="disclosureClosedIcon", type="Class", format="EmbeddedFile", inherit="no")]

  [Style(name="folderOpenIcon", type="Class", format="EmbeddedFile", inherit="no")]

  [Style(name="folderClosedIcon", type="Class", format="EmbeddedFile", inherit="no")]

  [Style(name="defaultLeafIcon", type="Class", format="EmbeddedFile", inherit="no")]

  public class FlowerTreeGrid extends DataGrid {

    /**
     *  Used to hold a list of items that are opened or set opened.
     */
    private var _openItems:Object = {};
    
    /**
     *  An object that specifies the icons for the items.
     *  Each entry in the object has a field name that is the item UID
     *  and a value that is an an object with the following format:
     *  <pre>
     *  {iconID: <i>Class</i>, iconID2: <i>Class</i>}
     *  </pre>
     *  The <code>iconID</code> field value is the class of the icon for
     *  a closed or leaf item and the <code>iconID2</code> is the class
     *  of the icon for an open item.
     *
     *  <p>This property is intended to allow initialization of item icons.
     *  Changes to this array after initialization are not detected
     *  automatically.
     *  Use the <code>setItemIcon()</code> method to change icons dynamically.</p>
     *
     *  @see #setItemIcon()
     *  @default undefined
     */
    public var itemIcons:Object;
    
    /**
     *  Storage variable for showRoot flag.
     */
    private var _showRoot:Boolean = true;
    
    /**
     *  Storage variable for changes to showRoot.
     */
    private var showRootChanged:Boolean = false;
    
    /**
     *  Flag to indicate if the model has a root
     */
    private var _hasRoot:Boolean = false;
    
    /**
     *  Storage variable for the original dataProvider
     */
    private var _rootModel:ICollectionView;
    
    /**
     *  Storage variable for the displayed dataProvider
     */
    private var _displayedModel:ArrayCollection = new ArrayCollection();
    
    /**
     * The last sort that was set by clicking on the column headers.
     */
    private var lastSort:Sort;
    
    private var dataProviderChanged:Boolean = false;
    
    /**
     * The TreeGrid can take a number of different kinds of data providers, such as collections,
     * xml, string, etc. These dataproviders all get converted to some form of collection class
     * in the setter for dataProvider. This means we may lose the actual reference to the dataProvider
     * object that the user passed in. So we store a reference to this object so we know if the user
     * is setting a completely new dataProvider or just updating the old dataProvider that was
     * previously set.
     */
    private var _dataProviderAsSet:Object;

    private var _dataDescriptor:ITreeDataDescriptor = new DefaultDataDescriptor();
 
    private var openItemsChanged:Boolean = false;
    
    /**
     * Used in <code>initListData</code> to fill a treeListData with specific icons URLs.
     * @author Cristina
     * @flowerModelElementId _rySg8O5NEd-AG91j4TO4_w
     */
    private var _urlIconStyles:URLIconStyles=new URLIconStyles();

    public function FlowerTreeGrid() {
      super();

      setStyle("indentation", 18);

      doubleClickEnabled = true;

      addEventListener(KeyboardEvent.KEY_UP, keyboardHandler);

      addEventListener(DataGridEvent.HEADER_RELEASE,
                       headerReleaseHandler,
                       false, 999);

      addEventListener(ListEvent.ITEM_DOUBLE_CLICK, doubleClickHandler);
    }
    
    /**
     * It is used to set the treegrid style by setting the URLs for icons.
     * @author Cristina
     * @flowerModelElementId _v3h4AO5NEd-AG91j4TO4_w
     */
    public function get urlIconStyles():URLIconStyles {
    	return _urlIconStyles;
    }
    
    private function doubleClickHandler(event:ListEvent):void {
      if (isItemOpen(event.itemRenderer.data)) {
        closeItemAt(event.rowIndex, event.itemRenderer.data);
      } else {
        openItemAt(event.rowIndex, event.itemRenderer.data);
      }
    }
    
    /**
     * We want to prevent the defeault behavior, which would be the sort functionality
     * of the DataGrid class. The default sort behavior will not take the hierarchical
     * nature of the data into account and will make the rows lose their hierarchy.
     */
    private function headerReleaseHandler(event:DataGridEvent):void {
      event.preventDefault();
      sortByColumn(event.columnIndex);
    }
    
    /**
     * This is essentailly the same sortByColumn function that is in DataGrid, but
     * modified only slightly. The main difference is the call to hierarchicalSort
     * at the end, which will sort the dataProvider and all the child items hierarchically.
     */
    private function sortByColumn(index:int):void {
      var c:DataGridColumn = columns[index];
      var desc:Boolean = c.sortDescending;

      // do the sort if we're allowed to
      if (c.sortable) {
        var s:Sort = lastSort;
        var f:SortField;
        if (s) {
          s.compareFunction = null;
          // analyze the current sort to see what we've been given
          var sf:Array = s.fields;
          if (sf) {
            for (var i:int = 0; i < sf.length; i++) {
              if (sf[i].name == c.dataField) {
                // we're part of the current sort
                f = sf[i]
                // flip the logic so desc is new desired order
                desc = !f.descending;
                break;
              }
            }
          }
        } else
          s = new Sort;

        if (!f) {
          f = new SortField(c.dataField);
        }

        c.sortDescending = desc;

        // if you have a labelFunction you must supply a sortCompareFunction
        f.name = c.dataField;
        if (c.sortCompareFunction != null) {
          f.compareFunction = c.sortCompareFunction;
        } else {
          f.compareFunction = null;
        }
        f.descending = desc;
        s.fields = [f];

        lastSort = s;
      }

      hierarchicalSort(_rootModel, s);
    }
    
    /**
     * Performs a sort of the ICollectionView and then gets all the children of
     * the node (if there are any) and performs the same sort on the children recursively.
     */
    private function hierarchicalSort(items:ICollectionView, sort:Sort):void {
      items.sort = sort;
      items.refresh();

      var n:int = items.length;
      for (var i:int = 0; i < n; i++) {
        var item:Object = items[i];

        var children:ICollectionView = _dataDescriptor.getChildren(item);

        if (children) {
          hierarchicalSort(children, sort);
        }
      }
    }
    
    public function dispatchTreeEvent(type:String,
                                      listData:FlowerTreeGridListData,
                                      renderer:IListItemRenderer,
                                      trigger:Event=null,
                                      opening:Boolean=true,
                                      dispatch:Boolean=true):void {
      if (opening) {
        openItemAt(getItemIndex(listData.item), listData.item); //listData.rowIndex - 1
      } else {
        closeItemAt(getItemIndex(listData.item), listData.item); //listData.rowIndex - 1
      }
    }
    
    [Inspectable(category="Data", defaultValue="undefined")]
    override public function set dataProvider(value:Object):void {
      if (_rootModel)
        _rootModel.removeEventListener(
          CollectionEvent.COLLECTION_CHANGE,
          collectionChangeHandler);

      if (_dataProviderAsSet != null && UIDUtil.getUID(value) == UIDUtil.getUID(_dataProviderAsSet)) {
        openItemsChanged = true;
      } else {
        _dataProviderAsSet = value;
      }

      // handle strings and xml
      if (typeof(value) == "string")
        value = new XML(value);
      else if (value is XMLNode)
        value = new XML(XMLNode(value).toString());
      else if (value is XMLList)
        value = new XMLListCollection(value as XMLList);

      if (value is XML) {
        _hasRoot = true;
        var xl:XMLList = new XMLList();
        xl += value;
        _rootModel = new XMLListCollection(xl);
      }
      //if already a collection dont make new one
      else if (value is ICollectionView) {
        _rootModel = ICollectionView(value);
      } else if (value is Array) {
        _rootModel = new ArrayCollection(value as Array);
      }
      //all other types get wrapped in an ArrayCollection
      else if (value is Object) {
        _hasRoot = true;
        // convert to an array containing this one item
        var tmp:Array = [];
        tmp.push(value);
        _rootModel = new ArrayCollection(tmp);
      } else {
        _rootModel = new ArrayCollection();
      }

      _rootModel.addEventListener(CollectionEvent.COLLECTION_CHANGE, rootModelChangeHandler);
      //flag for processing in commitProps
      dataProviderChanged = true;
      invalidateProperties();
    }
    
    protected function rootModelChangeHandler(event:CollectionEvent):void {
      dataProviderChanged = true;
      openItemsChanged = true;
      invalidateProperties();
    }
    
    override public function get dataProvider():Object {
      return _rootModel;
    }
    
    [Inspectable(category="Data")]
    public function set dataDescriptor(value:ITreeDataDescriptor):void {
      _dataDescriptor = value;
    }
    
    public function get dataDescriptor():ITreeDataDescriptor {
      return ITreeDataDescriptor(_dataDescriptor);
    }
    
    /**
     *  Sets the visibility of the root item.
     *
     *  If the dataProvider data has a root node, and this is set to
     *  <code>false</code>, the Tree control does not display the root item.
     *  Only the decendants of the root item are displayed.
     *
     *  This flag has no effect on non-rooted dataProviders, such as List and Array.
     *
     *  @default true
     *  @see #hasRoot
     */
    public function get showRoot():Boolean {
      return _showRoot;
    }
    
    public function set showRoot(value:Boolean):void {
      if (_showRoot != value) {
        _showRoot = value;
        showRootChanged = true;
        invalidateProperties();
      }
    }
    
    /**
     *  Indicates that the current dataProvider has a root item; for example,
     *  a single top node in a hierarchical structure. XML and Object
     *  are examples of types that have a root. Lists and arrays do not.
     *
     *  @see #showRoot
     */
    public function get hasRoot():Boolean {
      return _hasRoot;
    }
    
    private function keyboardHandler(event:KeyboardEvent):void {
      switch (event.keyCode) {
        case Keyboard.LEFT:
          expandSelectedNode();
          event.preventDefault();
          break;
        case Keyboard.RIGHT:
          closeSelectedNode();
          event.preventDefault();
          break;
      }
    }
    private function expandSelectedNode():void {
      if (_dataDescriptor.hasChildren(selectedItem) && isItemOpen(selectedItem)) {
        closeItemAt(selectedIndex, selectedItem);
      }
    }
    private function closeSelectedNode():void {
      if (_dataDescriptor.hasChildren(selectedItem) && !isItemOpen(selectedItem)) {
        openItemAt(selectedIndex, selectedItem);
      }
    }
    override protected function commitProperties():void {
      if (showRootChanged) {
        if (!_hasRoot)
          showRootChanged = false;
      }

      if (dataProviderChanged || showRootChanged) {
        var tmpCollection:ICollectionView;
        var row:Object;

        //we always reset the displayed rows on a dataprovider assignment or when the showRoot property change
        _displayedModel = new ArrayCollection();

        //reset flags 
        dataProviderChanged = false;
        showRootChanged = false;

        //we always reset the open and selected items on a dataprovider assignment
        if (!openItemsChanged)
          _openItems = {};

        // are we swallowing the root?
        if (_rootModel && !_showRoot && _hasRoot) {
          var rootItem:* = _rootModel.createCursor().current;
          if (rootItem != null &&
            _dataDescriptor.isBranch(rootItem, _rootModel) &&
            _dataDescriptor.hasChildren(rootItem, _rootModel)) {
            // then get rootItem children
            tmpCollection = getChildren(rootItem, _rootModel);

            for each (row in tmpCollection) {
              _displayedModel.addItem(row);
            }
          }
        } else {
          if (_hasRoot) {
            if (_rootModel != null && _rootModel.length > 0) {
              _displayedModel.addItem(_rootModel[0]);
            }
          } else {
            for each (row in _rootModel) {
              _displayedModel.addItem(row);
            }
          }
        }

        /* Before calling super.dataProvider we want to store the vertical and horizontal
         * scroll positions, as well as the list of selected items. After we set super.dataProvider
         * we need to always rest the selcted items, and if the open items have changed then we
         * need to reset the scroll positions.
         */
        var vScrollPos:int = verticalScrollPosition;
        var hScrollPos:int = horizontalScrollPosition;
        var savedSelection:Array = selectedItems;

        super.dataProvider = _displayedModel;

         //TODO: maybe we can use HierarchicalCollectionView?

        /*// at this point _rootModel may be null so we dont need to continue
           if ( _rootModel )
           {
           //wrap userdata in a TreeCollection and pass that collection to the List
           super.dataProvider = wrappedCollection = new HierarchicalCollectionView(
           tmpCollection != null ? tmpCollection : _rootModel,
           _dataDescriptor,
           _openItems);


           // not really a default handler, but we need to be later than the wrapper
           wrappedCollection.addEventListener(CollectionEvent.COLLECTION_CHANGE,
           collectionChangeHandler,
           false,
           EventPriority.DEFAULT_HANDLER, true);
           }
           else
           {
           super.dataProvider = null;
         }*/
	  }

      if (openItemsChanged) {
        verticalScrollPosition = vScrollPos;
        horizontalScrollPosition = hScrollPos;


        for each (var item:* in openItems) {
		// only expand an item iff it's a direct child from the root.  openItemAt is recursive so don't worry about those here.
          /*
             if( _showRoot && _rootModel.contains( item )) {
             var i : int = getItemIndex(item);
             if( i >= 0 && i < collection.length ) {
             openItemAt(i,item);
             }
             }
             else if( !_showRoot) {
             for(var j:int=0; j<_rootModel.length; j++) {
             var rootChild:Object = _rootModel[j];
             var children:ICollectionView = dataDescriptor.getChildren(rootChild, _rootModel);

             if(children.contains(item)) {
             var i : int = getItemIndex(item);
             if( i >= 0 && i < collection.length ) {
             openItemAt(i,item);
             }
             }
             }
             }
           */

          var rootNodes:Object;
          if (_showRoot || _hasRoot == false) {
            rootNodes = _rootModel;
          }
          else {
            rootNodes = [_rootModel[0]];
          }

          for (var i:int = 0; i < rootNodes.length; i++) {
            var rootChild:Object = rootNodes[i];
            var itemIndex:int;

            if (rootChild == item) {
              itemIndex = getItemIndex(item);
              if (itemIndex >= 0 && itemIndex < collection.length) {
                openItemAt(itemIndex, item);
              }
            } else {
              var children:ICollectionView = dataDescriptor.getChildren(rootChild, _rootModel);

              if (children && children.contains(item)) {
                itemIndex = getItemIndex(item);
                if (itemIndex >= 0 && itemIndex < collection.length) {
                  openItemAt(itemIndex, item);
                }
              }
            }
          }
        }

        openItemsChanged = false;
        //setting open items resets the collection
        var event:CollectionEvent =
          new CollectionEvent(CollectionEvent.COLLECTION_CHANGE);
        event.kind = CollectionEventKind.RESET;
        collection.dispatchEvent(event);
      }

      if (savedSelection != null) {
        super.selectedItems = savedSelection;
      }

      super.commitProperties();
    }
    /**
     * @flowerModelElementId _X7NfruwaEd-Mq65kNpXUPA
     */
    override protected function makeListData(data:Object, uid:String, rowNum:int, columnNum:int, column:DataGridColumn):BaseListData {
      var treeGridListData:FlowerTreeGridListData;

      if (data is DataGridColumn) {
        treeGridListData = new FlowerTreeGridListData(
          (column.headerText != null) ? column.headerText : column.dataField,
          column.dataField,
          columnNum,
          uid,
          this,
          rowNum);
      } else {
        treeGridListData = new FlowerTreeGridListData(
          column.itemToLabel(data),
          column.dataField,
          columnNum, uid,
          this,
          rowNum);

        initListData(data, treeGridListData);
      }

      return treeGridListData;
    }
    
    /**
     * Given an item, the method initializes a TreeGridListData with informations that will be send
     * later to a TreeGridItemRenderer like : icon, disclosureIcon, iconURL, disclosureIconURL etc. 
     * 
     * @author Cristina
     * @flowerModelElementId _X7NftewaEd-Mq65kNpXUPA
     */
    protected function initListData(item:Object, treeListData:FlowerTreeGridListData):void {
    	if (item == null)
        	return;

		var open:Boolean = isItemOpen(item);
    	var branch:Boolean = isBranch(item);
        var uid:String = itemToUID(item);
		
		// fills the treeListData disclosureIconURL and iconURL 
		// with the correct style stored in <code>_urlIconStyles</code>
		_urlIconStyles.fillListDataWithIconURLs(open, branch, item, treeListData);
		
	    // this is hidden by non-branches but kept so we know how wide it is so things align
        treeListData.disclosureIcon = getStyle(open ? "disclosureOpenIcon" :
                                             "disclosureClosedIcon");
  
      	treeListData.open = open;
      	treeListData.hasChildren = branch;
      	treeListData.depth = getItemDepth(item, treeListData.rowIndex);
      	treeListData.indent = (treeListData.depth - (_showRoot ? 1 : 2)) * getStyle("indentation");
      	treeListData.indentationGap = getStyle("indentation");
      	treeListData.item = item;
      	treeListData.icon = itemToIcon(item);

     	 treeListData.trunk = getStyle("verticalTrunks");
      	if (treeListData.trunk) {
       		treeListData.trunkOffsetTop = getStyle("paddingTop");
        	treeListData.trunkOffsetBottom = getStyle("paddingBottom");
        	treeListData.hasSibling = !isLastItem(treeListData);

        	var trunkColor:Object = getStyle("trunkColor");
        	treeListData.trunkColor = trunkColor ? uint(trunkColor) : 0xffffff;
      	}
    }
    
    private function getChildren(item:Object, view:Object):ICollectionView {
      //get the collection of children
      var children:ICollectionView = _dataDescriptor.getChildren(item, view);

      return children;
    }
    
    /**
     * This method find if the current node is the last displayed sibling
     *
     * Used to draw the vertical trunk lines,
     * if it is the last child then the vertical trunk line should stop in the middle of the row
     */
    protected function isLastItem(listData:FlowerTreeGridListData):Boolean {
    	//TODO: find a way to optimize this method, it's SLOOOWWWW maybe by using HierarchicalCollectionView? ...

      /*
         var rowIndex : int = getItemIndex( listData.item );

         var data : Object = IList( dataProvider ).getItemAt( rowIndex );

         if( IList( dataProvider ).length > rowIndex + 1 )
         {
         for(var i:int = rowIndex + 1; i < IList( dataProvider ).length; i++)
         {
         var nextData : Object = IList( dataProvider ).getItemAt( i );

         var nextDataDepth : int = getItemDepth( nextData, i );

         if( nextDataDepth == listData.depth )
         return false;
         }
         }
       */
		return false;
    }
    
    protected function getItemDepth(item:Object, offset:int):int {
      var depth:int;

      //if the dataprovider have a root, we begin from the root.
      if (hasRoot) {
        depth = searchForCurrentDepth(dataProvider[0], item);
      }
      //If the dataprovider don't have a root, we need to parse all items from the first level.
      else {
        var i:int = 0;
        do {
          depth = searchForCurrentDepth(dataProvider[i++], item);
        } while (depth == -1)
      }

      if (depth == -1)
        throw new Error("item not found");

      return depth;
    }
    
    private function searchForCurrentDepth(
      value:Object,
      item:Object,
      depth:int=1):int {
      if (value == item)
        return depth;

      if (value == null || _dataDescriptor.getChildren(value) == null)
        return -1;

      depth++;
      for (var i:int = 0; i < _dataDescriptor.getChildren(value).length; i++) {
        var result:int = searchForCurrentDepth(_dataDescriptor.getChildren(value)[i], item, depth);

        if (result != -1)
          return result;
      }

      return -1;
    }
    
    private function getItemIndex(item:Object):int {
      var cursor:IViewCursor = collection.createCursor();
      var i:int = 0;

      do {
        if (cursor.current === item)
          break;
        i++;
      } while (cursor.moveNext());

      return i;
    }
    
    /**
     *  The items that have been opened or set opened.
     *
     *  @default null
     */
    public function get openItems():Object {
      var openItemsArray:Array = [];
      for each (var item:* in _openItems) {
        openItemsArray.push(item);
      }
      return openItemsArray;
    }
    
    public function set openItems(value:Object):void {
      if (value != null) {
        for each (var item:* in value) {
          _openItems[itemToUID(item)] = item;
        }

        openItemsChanged = true;
        invalidateProperties();
      }
    }
    
    public function isBranch(item:Object):Boolean {
      if (item != null)
        return _dataDescriptor.isBranch(item, iterator.view);

      return false;
    }
    
    public function isItemOpen(item:Object):Boolean {
      var uid:String = itemToUID(item);

      return _openItems[uid] != null;
    }
    
    public function closeAllItems():void {
      for (var i:int = 0; i < ICollectionView(_displayedModel).length; i++) {
        this.closeItemAt(i);
      }
    }
    
    public function closeItemAt(rowNum:Number, item:Object=null, closeItem:Boolean=true):void {
      if (item == null)
        item = ListCollectionView(_displayedModel).getItemAt(rowNum);

      if (closeItem) {
        var uid:String = itemToUID(item);
        delete _openItems[uid];
      }

      if (_dataDescriptor.getChildren(item)) {
        // recursively remove the rows that were added for child records
        // but don't remove item from _openItems[] to keep opened items.
        for (var i:int = 0; i < _dataDescriptor.getChildren(item).length; i++) {
          if (isItemOpen(_dataDescriptor.getChildren(item)[i])) {
            closeItemAt(rowNum, _dataDescriptor.getChildren(item)[i], false);
          }

          ListCollectionView(_displayedModel).removeItemAt(rowNum + 1);
        }
      }
    }
    
    /**
     * @return the number of rows added to the display model
     */
    public function openItemAt(rowNum:Number, item:Object=null):Number {
      //if there was no item passed in then retrieve it via row number
      if (item == null)
        item = ListCollectionView(_displayedModel).getItemAt(rowNum);

      //add the item to the list of open items
      var uid:String = itemToUID(item);
      _openItems[uid] = item;
	
      var savedSelection:Array = this.selectedItems;
      this.clearSelected();
	
      var children:ICollectionView = _dataDescriptor.getChildren(item);

      //if there are children then go and open them up, otherwise skip to the bottom and return 0
      if (children) {
        // add the rows for the children at this level
        for (var i:int = 0; i < children.length; i++) {
          var child:Object = children[i];
          if (ListCollectionView(_displayedModel).contains(child) == false) {
            ListCollectionView(_displayedModel).addItemAt(child, rowNum + i + 1);
          }
        }
	
        var offset:Number = 0;

        for (i = 0; i < children.length; i++) {
          var vChild:Object = children[i];

          if (isItemOpen(vChild)) {
            offset += openItemAt(rowNum + i + 1 + offset, vChild);
          }
        }
      }
    
    // commented because it takes too long time 
	// @author Cristina
    // super.selectedItems = savedSelection;
	
      //return the number of children that were opened
      return _dataDescriptor.getChildren(item).length + offset;
    }  
    
	override public function itemToIcon(item:Object):Class {
    	if (item == null) {
        	return null;
      	}

      	var icon:*;
      	var open:Boolean = isItemOpen(item);
      	var branch:Boolean = isBranch(item);
      	var uid:String = itemToUID(item);

      	//first lets check the component
      	var iconClass:Class =
        	itemIcons && itemIcons[uid] ?
        	itemIcons[uid][open ? "iconID2" : "iconID"] :
        	null;

      	if (iconClass) {
        	return iconClass;
      	} else if (iconFunction != null) {
        	return iconFunction(item);
      	} else if (branch) {
        	return getStyle(open ? "folderOpenIcon" : "folderClosedIcon");
      	} else {
        //let's check the item itself
	        if (item is XML) {
	          try {
	            if (item[iconField].length() != 0)
	              icon = String(item[iconField]);
	          } catch (e:Error) {
          		}
	        } else if (item is Object) {
	          try {
	            if (iconField && item[iconField])
	              icon = item[iconField];
	            else if (item.icon)
	              icon = item.icon;
	          }
	          catch (e:Error) {
	          }
	        }
      	}
      	//set default leaf icon if nothing else was found
      	if (icon == null)
        	icon = getStyle("defaultLeafIcon");

     	//convert to the correct type and class
      	if (icon is Class) {
        	return icon;
      	} else if (icon is String) {
        	iconClass = Class(systemManager.getDefinitionByName(String(icon)));
        	if (iconClass)
          		return iconClass;

        	return document[icon];
      } else {
        return Class(icon);
      }
	}
	
	/**
     *  Sets the associated icon for the item.  Calling this method overrides the
     *  <code>iconField</code> and <code>iconFunction</code> properties for
     *  this item if it is a leaf item. Branch items don't use the
     *  <code>iconField</code> and <code>iconFunction</code> properties.
     *  They use the <code>folderOpenIcon</code> and <code>folderClosedIcon</code> properties.
     *
     *  @param item Item to affect.
     *  @param iconID Linkage ID for the closed (or leaf) icon.
     *  @param iconID2 Linkage ID for the open icon.
     *
     *  @tiptext Sets the icons for the specified item
     *  @helpid 3201
     */
    public function setItemIcon(item:Object, iconID:Class, iconID2:Class):void {
      if (!itemIcons)
        itemIcons = {};

      if (!iconID2)
        iconID2 = iconID;

      itemIcons[itemToUID(item)] = {iconID: iconID, iconID2: iconID2};

      itemsSizeChanged = true;
      invalidateDisplayList();
    }
  } 
  
} 