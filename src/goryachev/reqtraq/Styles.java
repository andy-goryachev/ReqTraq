// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CssGenerator;


/**
 * Style sheet.
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
		};
	}
}
