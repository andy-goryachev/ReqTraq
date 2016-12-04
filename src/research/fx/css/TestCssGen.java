// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.D;
import goryachev.reqtraq.TreeTablePane;
import research.fx.css.FxStyleSheet.Selector;


/**
 * FxStyleSheet Test.
 */
public class TestCssGen
	extends FxStyleSheet
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	public TestCssGen()
	{
		add
		(
			new Selector(TreeTablePane.TREE, ".view").defines
			(
				cellSize("a1"),
				cellSize("a2"),
				
				new Selector("b").defines
				(
					cellSize("b1"),
					cellSize("b2"),
					
					// FIX extra space!
					new Selector(HOVER).defines
					(
						padding("0 0 0 0")
					)
				)
			),
			
			new Selector(".SHOULD_NOT_OUTPUT_ALONE").defines
			(
				new Selector(".ONLY_WITH_THIS").defines
				(
					padding("1px")
				)
			)
		);
	}
	
	
	@Test
	public void test()
	{
		D.print("\n[" + new TestCssGen().generate() + "]");
	}
}
