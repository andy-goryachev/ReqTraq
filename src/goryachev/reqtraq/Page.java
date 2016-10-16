// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.util.GUID256;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;


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
	
	public final SimpleStringProperty title = new SimpleStringProperty();
	public final SimpleStringProperty text = new SimpleStringProperty();
	private final String id;
	private ObservableValue<String> synopsis;
	
	
	public Page()
	{
		id = GUID256.get();
	}
	
	
	public Page(String id, String title, String text)
	{
		this.id = id;
		setTitle(title);
		setText(text);
	}
	
	
	public Page(String title, String text)
	{
		this.id = GUID256.get();
		setTitle(title);
		setText(text);
	}
	
	
	public ObservableValue<String> getField(Field f)
	{
		switch(f)
		{
		case SYNOPSIS:
			return synopsisProperty();
		case TEXT:
			return text;
		case TITLE:
			return title;
		default:
			return null;
		}
	}
	
	
	public String getID()
	{
		return id;
	}
	
	
	public int getNestingLevel()
	{
		return 0; // TODO
	}
	
	
	public ObservableValue<String> synopsisProperty()
	{
		if(synopsis == null)
		{
			synopsis = Bindings.createStringBinding(() -> computeSynopsis(), text);
		}
		return synopsis;
	}
	
	
	protected String computeSynopsis()
	{
		String s = getText();
		if(s == null)
		{
			return null;
		}
		
		int max = 128;
		int sz = s.length();
		
		String rv;
		if(sz > max)
		{
			rv = s.substring(0, max);
		}
		else
		{
			rv = s;
		}
		
		rv = rv.replace("\n", " ");
		rv = rv.replace("\t", " ");
		return rv;
	}
	
	
	public String getTitle()
	{
		return title.get();
	}
	
	
	public void setTitle(String s)
	{
		title.set(s);
	}
	
	
	public String getText()
	{
		return text.get();
	}
	
	
	public void setText(String s)
	{
		text.set(s);
	}
	
	
	public String toString()
	{
		return getTitle();
	}
}
