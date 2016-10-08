// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CPane;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;


// TODO drop zone indicator
// TODO cell factory
public class TreeTablePane
	extends CPane
{
	public final TreeTableView<Page> tree;
	public final TreeHandler<Page> handler;


	public TreeTablePane()
	{
		tree = new TreeTableView<>();
		tree.setShowRoot(false);
		tree.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		handler = new TreeHandler<Page>(tree);
		
		addColumn("Title", Page.Field.TITLE);
		addColumn("Text", Page.Field.SYNOPSIS);
		
		setCenter(tree);
	}
	
	
	public void setRoot(TreeItem<Page> r)
	{
		tree.setRoot(r);
	}


	protected void addColumn(String label, Page.Field f)
	{
		TreeTableColumn<Page,String> column = new TreeTableColumn<>(label);
		column.setPrefWidth(150);
		column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Page,String> param) ->
		{
			ObservableValue<String> result = new ReadOnlyStringWrapper("");
			if(param.getValue().getValue() != null)
			{
				result = new ReadOnlyStringWrapper("" + param.getValue().getValue().getField(f));
			}
			return result;
		});
		tree.getColumns().add(column);
	}


	public Page getSelectedPage()
	{
		ObservableList<TreeItem<Page>> sel = tree.getSelectionModel().getSelectedItems();
		if(sel.size() == 1)
		{
			return (sel.get(0).getValue());
		}

		return null;
	}
}