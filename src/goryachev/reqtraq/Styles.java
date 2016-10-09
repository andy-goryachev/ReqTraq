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
				
		sel(TreeTablePane.STYLE_DISABLED_HOR_SCROLL_BAR, "scroll-bar:horizontal");
		maxHeight(0);
		prefHeight(0);
		padding(0);
		opacity(0);
		backgroundColor(TRANSPARENT);
		backgroundInsets(0);
		backgroundImage(null);
		shape(null);
		sel(TreeTablePane.STYLE_DISABLED_HOR_SCROLL_BAR, "scroll-bar:horizontal *");
		maxHeight(0);
		prefHeight(0);
		padding(0);
		opacity(0);
		backgroundColor(TRANSPARENT);
		backgroundInsets(0);
		backgroundImage(null);
		shape(null);
	}
}
