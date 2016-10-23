// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.fx.Converters;
import java.util.function.Function;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.StringConverter;


/**
 * Fx Tree Table Column.
 */
public abstract class FxTreeTableColumn<T>
	extends TreeTableColumn<T,Object>
{
	/** 
	 * Override to provide Observable value for the cell.  Example:
	 * return new ReadOnlyObjectWrapper(item); 
	 */
	protected abstract ObservableValue getCellValueProperty(T item);
	
	//
	
	protected Function<Object,Node> renderer;
	protected StringConverter<Object> formatter = Converters.OBJECT();
	protected Pos alignment = Pos.CENTER_LEFT;

	// TODO value generator: T -> Object
	// TODO formatter: Object -> String
	// TODO alignment
	// TODO sorting
	
	
	public FxTreeTableColumn(String text, boolean sortable)
	{
		super(text);
		
		setSortable(sortable);
		setCellValueFactory((f) -> getCellValueProperty(f.getValue().getValue()));
		setCellFactory((f) -> getCellFactory(f));
	}


	public FxTreeTableColumn(String text)
	{
		this(text, true);
	}
	
	
	public FxTreeTableColumn()
	{
		this(null, true);
	}
	
	
	public FxTreeTableColumn(boolean sortable)
	{
		this(null, sortable);
	}
	
	
	/** sets renderer node generator which creates a Node to represent a value */
	// TODO value object
	public void setRenderer(Function<Object,Node> r)
	{
		renderer = r;
	}
	
	
	public void setAlignment(Pos a)
	{
		alignment = a;
	}
	
	
	public void setFormatter(StringConverter c)
	{
		formatter = (c == null ? Converters.OBJECT() : c);
	}
	
	
	// TODO value, alignment
	private TreeTableCell<T,Object> getCellFactory(TreeTableColumn<T,Object> f)
	{
		return new TreeTableCell<T,Object>()
		{
			@Override
			protected void updateItem(Object item, boolean empty)
			{
				super.updateItem(item, empty);
				
				if(empty)
				{
					item = null;
				}
				
				if(renderer == null)
				{
					String text = formatter.toString(item);
					setText(text);
					setGraphic(null);
					setAlignment(alignment);
				}
				else
				{
					Node n = renderer.apply(item);
					setText(null);
					setGraphic(n);
				}
			}
		};
	}
}
