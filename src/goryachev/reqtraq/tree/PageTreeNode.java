// Copyright © 2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.tree;
import goryachev.reqtraq.data.Page;
import goryachev.reqtraq.util.ListTransform;
import javafx.scene.control.TreeItem;


/**
 * Page TreeNode.
 */
public class PageTreeNode
	extends TreeItem<Page>
{
	protected final Page page;


	public PageTreeNode(Page p)
	{
		this.page = p;
		
		new ListTransform<Page,TreeItem<Page>>(p.children, getChildren(), (s) -> toTreeNode(s));
	}
	
	
	protected static PageTreeNode toTreeNode(Page p)
	{
		return new PageTreeNode(p);
	}
}
