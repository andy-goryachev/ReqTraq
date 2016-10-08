// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;


/**
 * Page.
 */
public class Page
{
	public static enum Field
	{
		SYNOPSIS,
		TITLE,
		TEXT
	}
	
	//
	
	private String title;
	private String text;
	
	
	public Page()
	{
	}
	
	
	public Page(String title, String text)
	{
		this.title = title;
		this.text = text;
	}
	
	
	public Object getField(Field f)
	{
		switch(f)
		{
		case SYNOPSIS:
			return getSynopsis();
		case TEXT:
			return text;
		case TITLE:
			return title;
		default:
			return "?" + f;
		}
	}
	
	
	public String getSynopsis()
	{
		if(text == null)
		{
			return null;
		}
		
		int max = 128;
		int sz = text.length();
		
		String s;
		if(sz > max)
		{
			s = text.substring(0, max);
		}
		else
		{
			s = text;
		}
		
		s = s.replace("\n", " ");
		s = s.replace("\t", " ");
		return s;
	}
	
	
	public String getTitle()
	{
		return title;
	}
	
	
	public String getText()
	{
		return text;
	}
	
	
	public String toString()
	{
		return getTitle();
	}
}
