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
			if(Schema.KEY_PAGES.equals(s))
			{
				pages = parsePages();
			}
			else if(Schema.KEY_ID.equals(s))
			{
				id = nextString();
			}
			else if(Schema.KEY_VERSION.equals(s))
			{
				ver = nextString();
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
		
		beginObject();
		while(inObject())
		{
			String s = nextName();
			if(Schema.KEY_ID.equals(s))
			{
				id = nextString();
			}
			else if(Schema.KEY_TITLE.equals(s))
			{
				title = nextString();
			}
			else if(Schema.KEY_TEXT.equals(s))
			{
				text = nextString();
			}
		}
		endObject();
		
		return new Page(id, title, text);
    }
}
