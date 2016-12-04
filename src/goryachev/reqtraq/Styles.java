// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CssGenerator;
import javafx.scene.paint.Color;
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
			new Selector(TreeTablePane.TREE).defines
			(
				new Selector(".cell").defines
				(					
					padding(0),
					borderWidth(0),
					cellSize("1.8em"), // TODO font size + necessary padding
					
					new Selector(".text-field").defines
					(
						padding(0),
						backgroundColor(null),
						backgroundInsets(null),
						borderWidth(0),
						
						new Selector(FOCUSED).defines
						(
							translateX(-1)
						)
					)
				)
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
				return new Styles().generate();
			}
		};
	}
}
