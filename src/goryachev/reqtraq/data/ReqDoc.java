// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.util.CList;
import goryachev.reqtraq.Page;
import java.util.List;


/**
 * Requirements Document.
 */
public class ReqDoc
{
	private String id;
	
	
	public ReqDoc()
	{
		
	}
	
	
	public ReqDoc(String id, CList<Page> pages)
	{
		this.id = id;
		// TODO this.pages = pages;
	}


	public String getID()
	{
		return id;
	}
	
	
	public List<Page> getPages()
	{
		return new CList();
	}
}
