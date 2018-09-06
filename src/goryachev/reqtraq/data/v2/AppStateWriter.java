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
			header("global");
			sep();
			nl();
			nl();
			
			header("pages");
			write("id|created|modified|parent|status|title|text|pic\n");
			
			for(Page p: pages)
			{
				String parentRef = p.getParent() == null ? "" : p.getParent().getID().toString();
				String pic = null; // TODO
					
				write(p.getID().toHexString());
				sep();
				write(p.getTimeCreated());
				sep();
				write(p.getTimeModified());
				sep();
				write(parentRef);
				sep();
				write(p.getStatus());
				sep();
				text(p.getTitle());
				sep();
				text(p.getText());
				sep();
				write(pic);
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
		case '|':
		case '[':
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
	
	
	protected String safe(Object x)
	{
		if(x == null)
		{
			return "";
		}
		
		String s = x.toString();
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
	
	
	protected void text(Object x) throws Exception
	{
		wr.write(safe(x));
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
	
	
	protected void sep() throws Exception
	{
		wr.write('|');
	}
	
	
	protected void header(String s) throws Exception
	{
		wr.write("[");
		text(s);
		wr.write("]\n");
	}
}
