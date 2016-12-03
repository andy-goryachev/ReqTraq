// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.D;


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
			selector("a").defines
			(
				cellSize(0),
				selector("b").defines
				(
					cellSize(1)
				)
			)
		);
	}
	
	
	@Test
	public void test()
	{
		D.print("\n" + new TestCssGen().generate());
	}
}
