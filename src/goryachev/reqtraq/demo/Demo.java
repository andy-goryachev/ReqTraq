// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.demo;
import goryachev.common.io.CReader;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.GUID256;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import goryachev.reqtraq.Page;
import goryachev.reqtraq.data.ReqDoc;


/**
 * Demo Data.
 */
public class Demo
{
	public static ReqDoc create()
	{
		String s = CKit.readStringQuiet(Demo.class, "requirements.txt");
		String id = GUID256.get();
		return new ReqDoc(id, parse(s));
	}
	

	private static CList<Page> parse(String text)
	{
		CList<Page> ps = new CList<>();
		
		CReader rd = new CReader(text);
		try
		{
			String s;
			while((s = rd.readLine()) != null)
			{
				if(CKit.isNotBlank(s))
				{
					int lev = countLeadingTabs(s);
					String title = s.trim();
					String id = GUID256.get();
					ps.add(new Page(id, lev, title, null));
				}
			}
		}
		catch(Exception e)
		{
			Log.fail(e);
		}
		finally
		{
			CKit.close(rd);
		}
		
		return ps;
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
