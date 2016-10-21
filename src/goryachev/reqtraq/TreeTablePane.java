// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CAction;
import goryachev.fx.CPane;
import goryachev.fx.CommonStyles;
import goryachev.fx.FX;
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
	public final CAction expandAllAction = new CAction(this::expandAll);
	
	public final TreeTableView<Page> tree;
	public final TreeHandler<Page> handler;
	

	public TreeTablePane()
	{
		tree = new TreeTableView<>();
		tree.setShowRoot(false);
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		tree.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		FX.style(tree, CommonStyles.NO_HORIZONTAL_SCROLL_BAR);
		
		handler = new TreeHandler<Page>(tree);
		
		addColumn("Title", Page.Field.TITLE);
		addColumn("Text", Page.Field.SYNOPSIS);
		
		setCenter(tree);
	}
	
	
	public void setRoot(TreeItem<Page> r)
	{
		tree.setRoot(r);
	}
	
	
	public TreeItem<Page> getRoot()
	{
		return tree.getRoot();
	}


	protected void addColumn(String label, Page.Field f)
	{
		TreeTableColumn<Page,String> column = new TreeTableColumn<>(label);
		column.setPrefWidth(150);
		column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Page,String> param) ->
		{
			Page p = param.getValue().getValue();
			ObservableValue<String> v = (p == null ? null : p.getField(f));
			return v;
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
	
	
	public void expandAll()
	{
		expandRecursive(tree.getRoot());
	}


	protected void expandRecursive(TreeItem<Page> p)
	{
		p.setExpanded(true);
		
		for(TreeItem<Page> ch: p.getChildren())
		{
			expandRecursive(ch);
		}
	}
}