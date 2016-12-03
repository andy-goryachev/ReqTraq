// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.D;
import goryachev.reqtraq.TreeTablePane;


/**
 * CssGen Test.
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
		defines
		(
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
			)
		);
	}
	
	
	@Test
	public void test()
	{
		D.print("\n[" + new TestCssGen().generate() + "]");
	}
}
