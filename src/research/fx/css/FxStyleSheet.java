// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.fx.CssPseudo;
import goryachev.fx.internal.CssTools;


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
	
	private final CList<Object> elements = new CList();


	public FxStyleSheet()
	{
	}
	
	
	/** use this method to add selectors or style sheets */
	public void add(Object ... sel)
	{
		elements.addAll(sel);
	}
	
	
	public void add(Object ss)
	{
		elements.add(ss);
	}

	
	public String generate()
	{
		SB sb = new SB();
		generate(sb);
		return sb.toString();
	}
	
	
	protected void generate(SB sb)
	{
		for(Object x: elements)
		{
			if(x instanceof Selector)
			{
				((Selector)x).write(sb, null);
			}
			else if(x instanceof FxStyleSheet)
			{
				((FxStyleSheet)x).generate(sb);
			}
			else if(x != null)
			{
				throw new Error("?" + x);
			}
		}
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
		
		
		/** use this method to add properties or cascaded selectors */
		public Selector defines(Object ... sel)
		{
			items.addAll(sel);
			return this;
		}
		
		
		protected static Selector[] chain(Selector[] parents, Selector sel)
		{
			if(parents == null)
			{
				return new Selector[] { sel };
			}
			else
			{
				int sz = parents.length;
				Selector[] rv = new Selector[sz + 1];
				System.arraycopy(parents, 0, rv, 0, sz);
				rv[sz] = sel;
				return rv;
			}
		}
		
		
		protected void writeSelector(SB sb, boolean first, Object selector)
		{
			String s;
			if(selector instanceof Selector)
			{
				s = ((Selector)selector).selector;
			}
			else
			{
				s = (String)selector;
			}
			
			if(!first)
			{
				if(!s.startsWith(":"))
				{
					sb.sp();
				}
			}
			
			sb.a(s);
		}
		
		
		protected void write(SB sb, Selector[] parentSelectors)
		{
			if(items.size() == 0)
			{
				return;
			}
						
			boolean epilogue = false;
			
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
					if(!epilogue)
					{
						// write selector prologue
						boolean first = true;
						if(parentSelectors != null)
						{
							for(Selector sel: parentSelectors)
							{
								writeSelector(sb, first, sel);
								first = false;
							}
						}

						writeSelector(sb, first, selector);
						sb.a("\n{\n");
						epilogue = true;
					}
					
					sb.a("\t");
					((FxCssProp)x).write(sb);
				}
				else if(x != null)
				{
					throw new Error("?" + x);
				}
			}

			if(epilogue)
			{
				sb.a("}\n\n");
			}

			if(selectors != null)
			{
				for(Selector sel: selectors)
				{
					sel.write(sb, chain(parentSelectors, this));
				}
			}			
		}
	}
}
