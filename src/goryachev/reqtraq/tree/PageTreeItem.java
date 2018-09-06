// Copyright © 2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.tree;
import goryachev.reqtraq.data.Page;
import goryachev.reqtraq.util.ListTransform;
import javafx.scene.control.TreeItem;


/**
 * Page TreeItem.
 */
public class PageTreeItem
	extends TreeItem<Page>
{
	public PageTreeItem(Page p)
	{
		super(p);
		
		new ListTransform<Page,TreeItem<Page>>(p.children, getChildren(), (s) -> toTreeNode(s));
	}
	
	
	public Page getPage()
	{
		return getValue();
	}
	
	
	protected static PageTreeItem toTreeNode(Page p)
	{
		return new PageTreeItem(p);
	}
}
