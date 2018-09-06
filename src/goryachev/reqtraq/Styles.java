// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CommonStyles;
import goryachev.fx.FX;
import goryachev.fx.FxStyleSheet;
import goryachev.reqtraq.tree.StatusCell;
import goryachev.reqtraq.tree.TreeTablePane;
import javafx.scene.paint.Color;


/**
 * ReqTraq style sheet.
 */
public class Styles
	extends FxStyleSheet
{
	public Styles()
	{
		// TODO themes
		
		add
		(
			// basic styles
			new Selector(".root").defines
			(
				prop("-fx-accent", Color.RED),
				prop("-fx-focus-color", Color.RED),
				prop("-fx-faint-focus-color", Color.BLACK)
			),
			
			new Selector(".text").defines
			(
				prop("-fx-font-smoothing-type", "gray")
			),
			
			// common fx styles
			new CommonStyles(),
			
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
			new Selector(StatusCell.DONE).defines
			(
				backgroundColor(FX.alpha(Color.LIGHTGREEN, 0.4))
			),
			new Selector(StatusCell.OPEN).defines
			(
				backgroundColor(FX.alpha(Color.LIGHTYELLOW, 0.4))
			),
			new Selector(StatusCell.TBD).defines
			(
				backgroundColor(FX.alpha(Color.GRAY, 0.1))
			),
			
			// editor
			new Selector(EditorPane.ID_TITLE).defines
			(
				fontSize("150%")
			)
		);
	}
}
