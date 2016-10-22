// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.reqtraq.data.GUID;
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
		ID,
		SYNOPSIS,
		TITLE,
		TEXT
	}
	
	//
	
	public final SimpleStringProperty title = new SimpleStringProperty();
	public final SimpleStringProperty text = new SimpleStringProperty();
	private final String id;
	private transient int level;
	private ObservableValue<String> synopsis;
	
	
	public Page()
	{
		id = GUID.create();
	}
	
	
	public Page(String id, int level, String title, String text)
	{
		this.id = id;
		this.level = level;
		setTitle(title);
		setText(text);
	}
	
	
	public Object 
	//ObservableValue<String> 
	getField(Field f)
	{
		switch(f)
		{
		case ID:
			return getID();
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
	
	
	/** nesting level 0 corresponds to the root not (invisible).  visible pages start at level 1 */
	public int getNestingLevel()
	{
		return level;
	}
	
	
	public void setNestingLevel(int x)
	{
		level = x;
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
