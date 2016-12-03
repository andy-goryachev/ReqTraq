// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.fx.CssPseudo;
import goryachev.fx.CssTools;


/**
 * Css Style Sheet Generator.
 */
public class FxStyleSheet
	extends StandardFxProperties
{
	public static final CssPseudo DISABLED = new CssPseudo(":disabled");
	public static final CssPseudo FOCUSED = new CssPseudo(":focused");
	public static final CssPseudo HOVER = new CssPseudo(":hover");
	public static final CssPseudo PRESSED = new CssPseudo(":pressed");
	
	public static final String TRANSPARENT = "transparent";
	
	private final CList<Selector> selectors = new CList();


	public FxStyleSheet()
	{
	}
	
	
	public void defines(Selector ... xs)
	{
		selectors.addAll(xs);
	}


	public static Selector selector(Object ... sel)
	{
		return new Selector(sel);
	}
	
	
	public String generate()
	{
		SB sb = new SB();
		for(Selector sel: selectors)
		{
			sel.write(sb, null);
		}
		return sb.toString();
	}
	
	
	//


	public static class Selector
		extends StandardFxProperties
	{
		protected final String selector;
		protected final CList<Object> items = new CList();
		
		
		public Selector(Object ... sel)
		{
			selector = CssTools.selector(sel);
		}


		public Selector defines(Object ... sel)
		{
			items.addAll(sel);
			return this;
		}
		
		
		public void write(SB sb, Object parentSelectors)
		{
			if(items.size() == 0)
			{
				return;
			}
			
			sb.a(selector);
			sb.a("\n{\n");
			
			CList<Selector> selectors = null;
			for(Object x: items)
			{
				if(x instanceof Selector)
				{
					if(selectors == null)
					{
						selectors = new CList<>();
					}
					selectors.add((Selector)x);
				}
				else if(x instanceof FxCssProp)
				{
					sb.a("\t");
					((FxCssProp)x).write(sb);
				}
				else
				{
					sb.a(x);
				}
			}

			sb.a("}\n\n");

			if(selectors != null)
			{
				for(Selector sel: selectors)
				{
					sel.write(sb, this);
				}
			}			
		}
	}
}
