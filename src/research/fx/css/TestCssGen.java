// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.D;
import goryachev.reqtraq.TreeTablePane;


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
		selector(TreeTablePane.TREE, ".view").defines
		(
			cellSize("a1"),
			cellSize("a2"),
			
			selector("b").defines
			(
				cellSize("b1"),
				cellSize("b2"),
				
				selector(HOVER).defines
				(
					padding("0 0 0 0")
				)
			)
		);
		
		selector("SHOULD_NOT_OUTPUT_ALONE").defines
		(
			selector("ONLY_WITH_THIS").defines
			(
				padding("1px")
			)
		);
	}
	
	
	@Test
	public void test()
	{
		D.print("\n[" + new TestCssGen().generate() + "]");
	}
}
