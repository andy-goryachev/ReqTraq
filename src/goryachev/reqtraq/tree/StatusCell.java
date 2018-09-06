// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.tree;
import goryachev.common.util.CKit;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.reqtraq.data.Page;
import javafx.scene.control.TreeTableCell;


/**
 * Status Cell.
 */
public class StatusCell
	extends TreeTableCell<Page,Object>
{
	public static final CssStyle DONE = new CssStyle();
	public static final CssStyle OPEN = new CssStyle();
	public static final CssStyle TBD = new CssStyle();
	
	
	public StatusCell()
	{
	}


	public void updateItem(Object item, boolean empty)
	{
		super.updateItem(item, empty);
		
		String s = CKit.toString(item);
		setText(s);
		
		FX.setStyle(this, DONE, Page.STATUS_DONE.equals(s));
		FX.setStyle(this, OPEN, Page.STATUS_OPEN.equals(s));
		FX.setStyle(this, TBD, Page.STATUS_TBD.equals(s));
	}
}
