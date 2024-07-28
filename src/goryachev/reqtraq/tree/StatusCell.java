// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.tree;
import goryachev.common.util.CKit;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.reqtraq.data.Page;
import goryachev.reqtraq.data.Status;
import javafx.scene.control.TreeTableCell;


/**
 * Status Cell.
 */
public class StatusCell
	extends TreeTableCell<Page,Object>
{
	public static final CssStyle DONE = new CssStyle("StatusCell_DONE");
	public static final CssStyle OPEN = new CssStyle("StatusCell_OPEN");
	public static final CssStyle TBD = new CssStyle("StatusCell_TBD");
	
	
	public StatusCell()
	{
	}


	@Override
	public void updateItem(Object item, boolean empty)
	{
		super.updateItem(item, empty);
		
		String s = CKit.toStringOrNull(item);
		setText(s);
		
		FX.style(this, getStyle(item));
	}
	
	
	private CssStyle getStyle(Object item)
	{
		if(item instanceof Status s)
		{
			switch(s)
			{
			case DONE:
				return DONE;
			case OPEN:
				return OPEN;
			case TBD:
				return TBD;
			}
		}
		return null;
	}
}
