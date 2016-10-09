// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
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
	
	public static final CssStyle STYLE_DISABLED_HOR_SCROLL_BAR = new CssStyle("ConstraintResizeTable");


	public TreeTablePane()
	{
		tree = new TreeTableView<>();
		tree.setShowRoot(false);
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// disable horizontal scroll bar
		tree.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		FX.style(tree, STYLE_DISABLED_HOR_SCROLL_BAR);
		/*
		tree.skinProperty().addListener((s) -> 
		{
			for(Node n: tree.lookupAll(".scroll-bar"))
			{
				if(n instanceof ScrollBar)
				{
					ScrollBar b = (ScrollBar)n;
					if(b.getOrientation() == Orientation.HORIZONTAL)
					{
						b.setMaxHeight(0);
						b.setPrefHeight(0);
						b.setDisable(true);
//						b.setOpacity(0);
						b.setPadding(Insets.EMPTY);
					}
				}
			}
		});
		*/
		
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