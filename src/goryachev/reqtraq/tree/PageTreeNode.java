// Copyright © 2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.tree;
import goryachev.reqtraq.data.Page;
import goryachev.reqtraq.util.ListTransform;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;


/**
 * Page TreeNode.
 */
public class PageTreeNode
	extends TreeItem<Page>
{
	public PageTreeNode(Page p)
	{
		super(p);
		
		new ListTransform<Page,TreeItem<Page>>(p.children, getChildren(), (s) -> toTreeNode(s));
	}
	
	
	public Page getPage()
	{
		return getValue();
	}
	
	
	protected static PageTreeNode toTreeNode(Page p)
	{
		return new PageTreeNode(p);
	}
	
	
	public ObservableList<TreeItem<Page>> getChildren()
	{
		return super.getChildren();
	}
}
