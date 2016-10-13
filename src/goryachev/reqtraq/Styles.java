// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CommonStyles;
import goryachev.fx.CssGenerator;


/**
 * Application style sheet.
 */
public class Styles
	extends CssGenerator
{
	protected void generate()
	{
		sel(EditorPane.ID_TITLE);
		fontSize("150%");
			
		CommonStyles.generate(this);
	}
}
