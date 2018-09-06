// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.util.Assert;
import goryachev.common.util.BKey;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Page.
 */
public class Page
{
	public static enum Field
	{
		// ETA,
		// GANTT,
		ID,
		// IMAGES,
		// PERCENT_COMPLETED,
		// RELEASE,
		STATUS,
		SYNOPSIS,
		TITLE,
		TIME_CREATED,
		TIME_MODIFIED,
		TEXT
	}
	
	//
	
	public final SimpleStringProperty title = new SimpleStringProperty();
	public final SimpleStringProperty text = new SimpleStringProperty();
	public final SimpleLongProperty modified = new SimpleLongProperty();
	public final SimpleObjectProperty<Status> status = new SimpleObjectProperty();
	public final ObservableList<Page> children = FXCollections.observableArrayList();
	private final BKey id;
	private final long created;
	private transient Page parent;
	private ObservableValue<String> synopsis;
	
	
	public Page()
	{
		id = GUID.create();
		created = System.currentTimeMillis();
		setTimeModified(created);
	}
	
	
	public Page(BKey id, long created, long modified, String title, String text, Status status)
	{
		Assert.notNull(id, "id");
		
		this.id = id;
		this.created = created;
		
		setTimeModified(modified);
		setTitle(title);
		setText(text);
		setStatus(status);
	}
	
	
	public void add(Page p)
	{
		children.add(p);
		p.setParent(this);
	}
	
	
	protected void setParent(Page p)
	{
		if(parent != null)
		{
			throw new Error();
		}
		
		parent = p;
	}
	
	
	public Page getParent()
	{
		return parent;
	}
	
	
	public Object getField(Field f)
	{
		switch(f)
		{
		case ID:
			return getID();
		case STATUS:
			return status;
		case SYNOPSIS:
			return synopsisProperty();
		case TEXT:
			return text;
		case TIME_CREATED:
			return getTimeCreated();
		case TIME_MODIFIED:
			return modified;
		case TITLE:
			return title;
		default:
			return null;
		}
	}
	
	
	public BKey getID()
	{
		return id;
	}
	
	
	public long getTimeCreated()
	{
		return created;
	}
	
	
	public long getTimeModified()
	{
		return modified.get();
	}
	
	
	public void setTimeModified(long t)
	{
		modified.set(t);
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
	
	
	public void setStatus(Status s)
	{
		status.set(s);
	}
	
	
	public Status getStatus()
	{
		return status.get();
	}
	
	
	public String toString()
	{
		return "Page:" + getTitle();
	}
}
