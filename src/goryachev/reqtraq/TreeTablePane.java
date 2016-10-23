// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.util.D;
import goryachev.fx.CAction;
import goryachev.fx.CPane;
import goryachev.fx.CommonStyles;
import goryachev.fx.FX;
import goryachev.reqtraq.util.Tools;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;


// TODO drop zone indicator
// TODO cell factory
public class TreeTablePane
	extends CPane
{
	public final CAction expandAllAction = new CAction(this::expandAll);
	public final CAction insertAfterAction = new CAction(this::insertAfter);
	public final CAction insertChildAction = new CAction(this::insertChild);
	
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
		
		// set up actions
		insertAfterAction.disabledProperty().bind(Bindings.createBooleanBinding(() ->
		{
			boolean d = tree.getSelectionModel().getSelectedItems().size() != 1;
			D.print(d);
			return d;
		}, tree.getSelectionModel().getSelectedItems()));
		
		insertChildAction.disabledProperty().bind(Bindings.createBooleanBinding(() ->
		{
			return tree.getSelectionModel().getSelectedItems().size() != 1;
		}, tree.getSelectionModel().getSelectedItems()));
		
		addColumn("Title", Page.Field.TITLE);
		addColumn("Created", Page.Field.TIME_CREATED);
		
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


	// TODO format, alignment, width
	protected void addColumn(String label, Page.Field f)
	{
		TreeTableColumn<Page,String> column = new TreeTableColumn<>(label);
		column.setPrefWidth(150);
		column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Page,String> param) ->
		{
			Page p = param.getValue().getValue();
			return FX.toObservableValue(p == null ? null : p.getField(f));
		});

//		column.setCellFactory((x) ->
//		{
//			return new TreeTableCell()
//			{
//				
//			};
//		});
		
		tree.getColumns().add(column);
	}


	public Page getSelectedPage()
	{
		ObservableList<TreeItem<Page>> sel = tree.getSelectionModel().getSelectedItems();
		if(sel.size() == 1)
		{
			TreeItem<Page> t = sel.get(0);
			return t == null ? null : t.getValue();
		}

		return null;
	}
	
	
	public void expandAll()
	{
		expandRecursive(tree.getRoot());
	}
	
	
	public void selectFirst()
	{
		tree.getSelectionModel().selectFirst();
	}


	protected void expandRecursive(TreeItem<Page> p)
	{
		p.setExpanded(true);
		
		for(TreeItem<Page> ch: p.getChildren())
		{
			expandRecursive(ch);
		}
	}
	
	
	public TreeItem<Page> getSelectedItem()
	{
		return tree.getSelectionModel().getSelectedItem();
	}
	
	
	public void insertChild()
	{
		TreeItem<Page> sel = getSelectedItem();
		sel.setExpanded(true);
		TreeItem<Page> created = Tools.addPage(sel, 0, new Page());
		edit(created);
	}
	
	
	public void insertAfter()
	{
		TreeItem<Page> sel = getSelectedItem();
		TreeItem<Page> p = sel.getParent();
		int ix = p.getChildren().indexOf(sel) + 1;
		TreeItem<Page> created = Tools.addPage(p, ix, new Page());
		edit(created);
	}
	
	
	public void edit(TreeItem<Page> p)
	{
		tree.getSelectionModel().select(p);
		int ix = tree.getSelectionModel().getSelectedIndex();
		tree.getSelectionModel().clearAndSelect(ix);
		if(ix < 0)
		{
			return;
		}
		
		FX.later(() -> tree.edit(ix, tree.getColumns().get(0)));
	}
}