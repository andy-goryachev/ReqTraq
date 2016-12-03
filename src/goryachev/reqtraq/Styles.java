// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CommonStyles;
import goryachev.fx.CssGenerator;


/**
 * ReqTraq style sheet.
 * 
 * TODO I have to redesign the css style sheet:
 * instead of linear construct it should be hierarchical.
 * 
 * selector("tree_table")
 * {
 *     selector(".cell")
 *     {
 *         selector(":HOVER")
 *         {
 *         };
 *         selector(".text-field")
 *         {
 *         };
 *     }
 * }
 */
public class Styles
	extends CssGenerator
{
	protected void generate()
	{
		// tree
		sel(TreeTablePane.TREE, ".cell");
		padding(0);
		cellSize("1.5em"); // TODO font size + necessary padding
		
		sel(TreeTablePane.TREE, ".cell", ".text-field");
		padding(0);
		
		// editor
		sel(EditorPane.ID_TITLE);
		fontSize("150%");
			
		// common fx styles
		include(new CommonStyles());
	}
}
