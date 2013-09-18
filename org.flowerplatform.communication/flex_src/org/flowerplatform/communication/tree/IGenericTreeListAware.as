package org.flowerplatform.communication.tree
{
	/**
	 * @author Tache Razvan Mihai 
	 */
	public interface IGenericTreeListAware
	{
		function get genericTreeList() : GenericTreeList;
		function set genericTreeList(genericTreeList: GenericTreeList) : void;
	}
}