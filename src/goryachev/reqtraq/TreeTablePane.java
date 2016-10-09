// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.util.Parsers;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
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
	
	// TODO move to standard place later, including the stylesheet generator
	public static final CssStyle STYLE_NO_HORIZONTAL_SCROLL_BAR = new CssStyle("NoHorizontalScrollBar");


	public TreeTablePane()
	{
		tree = new TreeTableView<>();
		tree.setShowRoot(false);
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		tree.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		FX.style(tree, STYLE_NO_HORIZONTAL_SCROLL_BAR);
		
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
			Page p = param.getValue().getValue();
			Object v = (p == null ? null : p.getField(f));
			String s = Parsers.parseString(v);
			return new ReadOnlyStringWrapper(s);
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