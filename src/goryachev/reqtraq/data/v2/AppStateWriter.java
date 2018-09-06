// Copyright © 2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data.v2;
import goryachev.common.io.CWriter;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import goryachev.reqtraq.data.Page;
import java.io.OutputStream;


/**
 * AppState Writer V2.
 */
public class AppStateWriter
{
	private final OutputStream out;
	private CWriter wr;


	public AppStateWriter(OutputStream out) throws Exception
	{
		this.out = out;
	}
	
	
	public void write() throws Exception
	{
		CList<Page> pages = collectPages(AppState.root);
		
		wr = new CWriter(out);
		
		try
		{
//			header("version");
			
			header("pages");
			text("id|title");
			nl();
			
			for(Page p: pages)
			{
				text(p.getID().toHexString());
				write("|");
				text(p.getTitle());
				nl();
			}
		}
		finally
		{
			CKit.close(wr);
			wr = null;
		}
	}
	
	
	protected CList<Page> collectPages(Page root)
	{
		CList<Page> pages = new CList();
		collectPages(pages, root);
		return pages;
	}
	
	
	protected void collectPages(CList<Page> pages, Page p)
	{
		pages.add(p);
		
		for(Page ch: p.children)
		{
			pages.add(ch);
		}
	}


	protected static boolean isIllegalSymbol(char c)
	{
		switch(c)
		{
		case '%':
			return true;
		default:
			return (c < 0x20);
		}
	}
	
	
	protected boolean hasIllegalSymbols(String s)
	{
		for(int i=0; i<s.length(); i++)
		{
			char c = s.charAt(i);
			if(isIllegalSymbol(c))
			{
				return true;
			}
		}
		return false;
	}
	
	
	protected String safe(String s)
	{
		if(s == null)
		{
			return "";
		}
		
		if(hasIllegalSymbols(s))
		{
			SB sb = new SB(s.length() * 2);
			for(int i=0; i<s.length(); i++)
			{
				char c = s.charAt(i);
				if(isIllegalSymbol(c))
				{
					sb.a('%');
					sb.a(Hex.toHexChar(c >> 4));
					sb.a(Hex.toHexChar(c));
				}
				else
				{
					sb.a(c);
				}
			}
			return sb.toString();
		}
		return s;
	}
	
	
	protected void text(String s) throws Exception
	{
		wr.write(safe(s));
	}
	
	
	protected void nl() throws Exception
	{
		wr.write("\n");
	}
	
	
	protected void write(Object x) throws Exception
	{
		if(x != null)
		{
			wr.write(x.toString());
		}
	}
	
	
	protected void header(String s) throws Exception
	{
		wr.write("[");
		text(s);
		wr.write("]\n");
	}
}
