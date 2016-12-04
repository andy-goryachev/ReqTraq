// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.fx.CssStyle;


/**
 * Common Styles.
 */
public class CommonStyles
	extends FxStyleSheet
{
	/** bold type face */
	public static final CssStyle BOLD = new CssStyle("CommonStyles_BOLD");

	/** disables horizontal scroll bar */
	public static final CssStyle NO_HORIZONTAL_SCROLL_BAR = new CssStyle("CommonStyles_NO_HORIZONTAL_SCROLL_BAR");

	
	public CommonStyles()
	{
		// bold
		selector(BOLD).defines
		(
			fontWeight("bold")
		);
		
		// disables horizontal scroll bar
		selector(NO_HORIZONTAL_SCROLL_BAR).defines
		(
			selector(".scroll-bar:horizontal").defines
			(
				maxHeight(0),
				padding(0),
				opacity(0)
			),
			selector(".scroll-bar:horizontal *").defines
			(
				maxHeight(0),
				padding(0),
				opacity(0)
			)
		);
	}
}
