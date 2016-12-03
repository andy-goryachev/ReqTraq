// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.fx.CssPseudo;


/**
 * Css Generator.
 */
public class CssGen
	extends StandardFxProperties
{
	public static final CssPseudo DISABLED = new CssPseudo(":disabled");
	public static final CssPseudo FOCUSED = new CssPseudo(":focused");
	public static final CssPseudo HOVER = new CssPseudo(":hover");
	public static final CssPseudo PRESSED = new CssPseudo(":pressed");
	
	public static final String TRANSPARENT = "transparent";


	public CssGen()
	{
	}
	
	
//	public CssGen(Object ... xs)
//	{
//	}
	
	
	public void defines(Object ... xs)
	{
	}


	public static Selector selector(String name, Object ... xs)
	{
		return new Selector();
	}
	
	
	public String generate()
	{
		return "tbd"; // FIX
	}
	
	
	//


	public static class Selector
		extends StandardFxProperties
	{
		protected Selector(Object ... sel)
		{
		}


		public Selector defines(Object ... sel)
		{
			return this;
		}
	}
}
