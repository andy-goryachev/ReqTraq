// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


/**
 * Test D.
 */
public class TestD
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	@Test
	public void test()
	{
		D.print("yo");
		D.print("yo");
		D.print("yo");
	}
}
