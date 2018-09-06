// Copyright © 2013-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data.v1;
import goryachev.common.io.CReader;
import goryachev.common.util.BKey;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.json.JsonDecoder;
import goryachev.reqtraq.data.Page;
import java.io.File;
import java.io.Reader;


/** Reads ReqDoc JSON file */
public class ReqDocJsonReader
	extends JsonDecoder
{
	public ReqDocJsonReader(Reader rd)
	{
		super(rd);
	}
	
	
	public static ReqDoc readJSON(File f) throws Exception
	{
		ReqDocJsonReader rd = new ReqDocJsonReader(new CReader(f));
		try
		{
			return rd.parse();
		}
		finally
		{
			CKit.close(rd);
		}
	}
	
	
	public ReqDoc parse() throws Exception
	{
		CList<Page> pages = null;
		BKey id = null;
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
				id = BKey.parse(nextString());
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
		BKey id = null;
		long created = 0;
		long modified = 0;
		String title = null;
		String text = null;
		String status = Page.STATUS_OPEN;
		int level = 1;
		
		beginObject();
		while(inObject())
		{
			String s = nextName();
			switch(s)
			{
			case Schema.KEY_ID:
				id = BKey.parse(nextString());
				break;
			case Schema.KEY_PAGE_TIME_CREATED:
				created = nextLong();
				break;
			case Schema.KEY_PAGE_TIME_MODIFIED:
				modified = nextLong();
				break;
			case Schema.KEY_PAGE_LEVEL:
				level = nextInt();
				break;
			case Schema.KEY_PAGE_STATUS:
				status = nextString();
				break;
			case Schema.KEY_PAGE_TEXT:
				text = nextString();
				break;
			case Schema.KEY_PAGE_TITLE:
				title = nextString();
				break;
			}
		}
		endObject();
		
		return new Page(id, created, modified, level, title, text, status);
    }
}
