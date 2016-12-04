// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CssGenerator;
import research.fx.css.CommonStyles;
import research.fx.css.FxStyleSheet;


/**
 * ReqTraq style sheet.
 */
public class Styles
	extends FxStyleSheet
{
	public Styles()
	{
		add
		(
			// tree
			new Selector(TreeTablePane.TREE, ".cell").defines
			(
				padding(0),
				cellSize("1.5em") // TODO font size + necessary padding
			),
			
			new Selector(TreeTablePane.TREE, ".cell", ".text-field").defines
			(
				padding(0)
			),
			
			// editor
			new Selector(EditorPane.ID_TITLE).defines
			(
				fontSize("150%")
			),
			
			// common fx styles
			new CommonStyles()
		);
	}

	
	@Deprecated // FIX
	public CssGenerator createOldStyleGenerator()
	{
		return new CssGenerator()
		{
			protected void generate()
			{
			}
			
			
			public String generateStyleSheet()
			{
				return Styles.this.generate();
			}
		};
	}
}
