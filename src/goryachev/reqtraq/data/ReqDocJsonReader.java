// Copyright (c) 2013 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.util.CList;
import goryachev.json.JsonDecoder;
import goryachev.reqtraq.Page;
import java.io.Reader;


/** Reads ReqDoc JSON file */
public class ReqDocJsonReader
	extends JsonDecoder
{
	public ReqDocJsonReader(Reader rd)
	{
		super(rd);
	}
	
	
	public ReqDoc parse() throws Exception
	{
		CList<Page> pages = null;
		String id = null;
		String ver = null;
		
		beginObject();
		while(inObject())
		{
			String s = nextName();
			switch(s)
			{
			case Schema.KEY_PAGES:
				pages = parsePages();
				break;
			case Schema.KEY_ID:
				id = nextString();
				break;
			case Schema.KEY_VERSION:
				ver = nextString();
				break;
			}
		}
		endObject();
		
		return new ReqDoc(id, pages);
	}


	protected CList<Page> parsePages() throws Exception
    {
		CList<Page> pages = new CList();
		
		beginArray();
		while(inArray())
		{
			Page p = parsePage();
			pages.add(p);
		}
		endArray();
		
		return pages;
    }
	
	
	protected Page parsePage() throws Exception
    {
		String id = null;
		String title = null;
		String text = null;
		int level = -1;
		
		beginObject();
		while(inObject())
		{
			String s = nextName();
			switch(s)
			{
			case Schema.KEY_ID:
				id = nextString();
				break;
			case Schema.KEY_PAGE_LEVEL:
				level = nextInt();
				break;
			case Schema.KEY_PAGE_TITLE:
				title = nextString();
				break;
			case Schema.KEY_PAGE_TEXT:
				text = nextString();
				break;
			}
		}
		endObject();
		
		return new Page(id, title, text);
    }
}
