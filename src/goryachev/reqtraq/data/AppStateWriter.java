// Copyright © 2018-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.io.CWriter;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import java.io.OutputStream;


/**
 * AppState Writer.
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
		CList<Page> pages = collectPages(AppState.getRootPage());
		
		wr = new CWriter(out);
		
		try
		{
			header(Schema.SECTION_GLOBAL);
			sep();
			nl();
			nl();
			
			header(Schema.SECTION_PAGES);
			write(Schema.ID);
			sep();
			write(Schema.TIME_CREATED);
			sep();
			write(Schema.TIME_MODIFIED);
			sep();
			write(Schema.PARENT);
			sep();
			write(Schema.STATUS);
			sep();
			write(Schema.TITLE);
			sep();
			write(Schema.TEXT);
			sep();
			write(Schema.IMAGE);
			nl();
			
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
			collectPages(pages, ch);
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
