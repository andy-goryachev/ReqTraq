// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CssGenerator;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;


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
			
		// disables horizontal scroll bar
		sel(TreeTablePane.STYLE_NO_HORIZONTAL_SCROLL_BAR, ".scroll-bar:horizontal");
		maxHeight(0);
		padding(0);
		opacity(0);
		sel(TreeTablePane.STYLE_NO_HORIZONTAL_SCROLL_BAR, ".scroll-bar:horizontal *");
		maxHeight(0);
		padding(0);
		opacity(0);
	}
}
