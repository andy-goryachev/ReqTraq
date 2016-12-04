// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.OBSOLETE;
import goryachev.fx.internal.CssTools;


/**
 * Common Styles.
 */
@Deprecated // TODO replace with new style sheet 
public class CommonStyles
	extends CssGenerator
{

	
	protected void generate()
	{
		// bold
		sel(CssTools.BOLD);
		fontWeight("bold");
		
		// disables horizontal scroll bar
		sel(CssTools.NO_HORIZONTAL_SCROLL_BAR, ".scroll-bar:horizontal");
		maxHeight(0);
		padding(0);
		opacity(0);
		sel(CssTools.NO_HORIZONTAL_SCROLL_BAR, ".scroll-bar:horizontal *");
		maxHeight(0);
		padding(0);
		opacity(0);
	}
}
