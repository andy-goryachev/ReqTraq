// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CssGenerator;


/**
 * Application style sheet.
 */
public class Styles
	extends CssGenerator
{
	protected Object[] generate()
	{
		return new Object[] 
		{
			EditorPane.ID_TITLE,
				fontSize("150%"),
				
			TreeTablePane.STYLE_DISABLED_HOR_SCROLL_BAR, "scroll-bar:horizontal",
				maxHeight(0),
				padding(0),
			TreeTablePane.STYLE_DISABLED_HOR_SCROLL_BAR, "scroll-bar:horizontal *",
				maxHeight(0),
				padding(0),
		};
	}
}
