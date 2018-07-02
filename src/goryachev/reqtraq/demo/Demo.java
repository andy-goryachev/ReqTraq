// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.demo;
import goryachev.common.io.CReader;
import goryachev.common.util.BKey;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.reqtraq.Page;
import goryachev.reqtraq.data.GUID;
import goryachev.reqtraq.data.ReqDoc;
import java.util.Locale;


/**
 * Demo Data.
 */
public class Demo
{
	public static ReqDoc create()
	{
		String s = CKit.readStringQuiet(Demo.class, "requirements.txt");
		BKey id = GUID.create();
		return new ReqDoc(id, parse(s));
	}
	

	private static CList<Page> parse(String spec)
	{
		CList<Page> ps = new CList<>();
		
		CReader rd = new CReader(spec);
		try
		{
			String s;
			while((s = rd.readLine()) != null)
			{
				if(CKit.isNotBlank(s))
				{
					BKey id = GUID.create();
					long time = System.currentTimeMillis();
					int lev = countLeadingTabs(s);
					
					s = s.trim();
					
					String title;
					String text;
					String status;
					int ix = s.indexOf('|');
					if(ix < 0)
					{
						title = s;
						text = null;
						status = Page.STATUS_OPEN;
					}
					else
					{
						title = s.substring(0, ix);
						text = s.substring(ix + 1);
						text = text.replace("\\n", "\n");
						
						ix = text.indexOf('|');
						if(ix < 0)
						{
							status = Page.STATUS_OPEN;
						}
						else
						{
							status = parseStatus(text.substring(ix + 1));
							text = text.substring(0, ix);
						}
					}
					ps.add(new Page(id, time, time, lev, title, text, status));
				}
			}
		}
		catch(Exception e)
		{
			Log.ex(e);
		}
		finally
		{
			CKit.close(rd);
		}
		
		return ps;
	}
	
	
	private static String parseStatus(String s)
	{
		if(s == null)
		{
			return null;
		}
		
		s = s.toLowerCase(Locale.ENGLISH);
		if(s.startsWith("y"))
		{
			return Page.STATUS_DONE;
		}
		else if(s.startsWith("t"))
		{
			return Page.STATUS_TBD;
		}
		
		return Page.STATUS_OPEN; 
	}


	private static int countLeadingTabs(String s)
	{
		int ct = 0;
		int sz = s.length();
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			if(c == '\t')
			{
				ct++;
			}
			else
			{
				break;
			}
		}
		return ct;
	}
}
