// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.reqtraq.Page;
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
	}


	public PageTreeItem addPage(Page p)
	{
		PageTreeItem item = new PageTreeItem(p);
		getChildren().add(item);
		return item;
	}
}
