// Copyright © 2018-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.util;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import java.util.List;
import java.util.function.Function;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;


/**
 * Binds an ObservableList<S> of source types to an ObservableList<T> of target types,
 * applying transformation specified by the converter.
 * 
 * TODO move to fx
 * TODO weak listener
 */
public class ListTransform<S,T>
	implements ListChangeListener<S>
{
	protected final ObservableList<S> source;
	protected final ObservableList<T> target;
	protected final Function<S,T> converter;


	public ListTransform(ObservableList<S> src, ObservableList<T> target, Function<S,T> converter)
	{
		this.source = src;
		this.target = target;
		this.converter = converter;
		
		CKit.transform(src, target, converter);
		
		src.addListener(this);
	}
	
	
	public void disconnect()
	{
		source.removeListener(this);
	}
	
	
	public void onChanged(Change<? extends S> ev)
	{
		List<T> deleted = new CList();
		while(ev.next())
		{
			if(ev.wasAdded())
			{
				handleAddedItems(ev.getFrom(), ev.getTo());
			}
			else if(ev.wasReplaced())
			{
				handleReplacedItems(ev.getFrom(), ev.getTo(), ev.getRemovedSize(), deleted);
			}
			else if(ev.wasRemoved())
			{
				handleRemovedItems(ev.getFrom(), ev.getRemovedSize(), deleted);
			}
			else if(ev.wasUpdated())
			{
				handleUpdatedItems(ev.getFrom(), ev.getTo());
			}
		}

		handleDeletedItems(deleted);
	}


	
	public ObservableList<S> getSourceList()
	{
		return source;
	}
	
	
	public ObservableList<T> getTargetList()
	{
		return target;
	}
	
	
	protected T transform(S item)
	{
		return converter.apply(item);
	}
	
	
	protected void handleAddedItems(int from, int to)
	{
		CList<T> items = new CList(to - from);
		for(int i=from; i<to; i++)
		{
			S item = source.get(i);
			items.add(transform(item));
		}
		target.addAll(from, items);
	}


	protected void handleRemovedItems(int from, int size, List<T> removed)
	{
		for(int i=0; i<size; i++)
		{
			removed.add(target.get(from + i));
		}
	}


	protected void handleReplacedItems(int from, int to, int size, List<T> removed)
	{
		handleRemovedItems(from, size, removed);
		handleDeletedItems(removed);
		handleAddedItems(from, to);
	}


	protected void handleUpdatedItems(int from, int to)
	{
		for(int i=from; i<to; i++)
		{
			S item = source.get(i);
			target.set(i, transform(item));
		}
	}


	protected void handleDeletedItems(List<T> deleted)
	{
		target.removeAll(deleted);
	}
}
