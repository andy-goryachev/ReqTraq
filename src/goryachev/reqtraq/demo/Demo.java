// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.demo;
import goryachev.reqtraq.data.AppState;
import goryachev.reqtraq.data.GUID;
import goryachev.reqtraq.data.Page;
import goryachev.reqtraq.data.Status;


/**
 * Demo Data.
 */
public class Demo
{
	public static void init()
	{
		Page root = new Page();
		
		for(int i=0; i<3; i++)
		{
			long t = System.currentTimeMillis();
			String title = "page " + (i + 1);
			String text = i + "\n" + i + "\n" + i;
			
			Page p = new Page(GUID.create(), t, t, title, text, Status.OPEN);
			root.add(p);
		}
		
		AppState.root.set(root);
	}
}
