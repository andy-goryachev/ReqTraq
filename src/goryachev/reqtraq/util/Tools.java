// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.util;
import goryachev.reqtraq.Page;
import javafx.scene.control.TreeItem;


/**
 * Tools.
 */
public class Tools
{
	public static TreeItem<Page> addPage(TreeItem<Page> parent, Page p)
	{
		TreeItem<Page> t = new TreeItem<Page>(p);
		parent.getChildren().add(t);
		return t;
	}
	
	
	public static TreeItem<Page> addPage(TreeItem<Page> parent, int index, Page p)
	{
		TreeItem<Page> t = new TreeItem<Page>(p);
		parent.getChildren().add(index, t);
		return t;
	}
}
