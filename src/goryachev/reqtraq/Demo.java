// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.util.CList;
import goryachev.common.util.GUID256;
import goryachev.reqtraq.data.ReqDoc;
import javafx.scene.control.TreeItem;


/**
 * Demo Data.
 */
public class Demo
{
	public static ReqDoc create()
	{
		String id = GUID256.get();
		CList<Page> pages = new CList<>();
		p(pages, 1, "Chapter 1", "First Chapter\n\n1.\n2.");
		p(pages, 2, "1.2", "sub-chapter\n");
		p(pages, 1, "Chapter 2", "Second Chapter\n\n1.\n2.");
		return new ReqDoc(id, pages);
	}
	

	private static void p(CList<Page> pages, int level, String title, String text)
	{
		String id = GUID256.get();
		Page p = new Page(id, title, text);
		p.setNestingLevel(level);
		
		pages.add(p);
	}
}
