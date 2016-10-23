// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CAction;
import goryachev.fx.CPane;
import goryachev.fx.CommonStyles;
import goryachev.fx.FX;
import goryachev.reqtraq.util.Tools;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import research.fx.FxFormatter;
import research.fx.FxTreeTableColumn;


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
			return tree.getSelectionModel().getSelectedItems().size() != 1;
		}, tree.getSelectionModel().getSelectedItems()));
		
		insertChildAction.disabledProperty().bind(Bindings.createBooleanBinding(() ->
		{
			return tree.getSelectionModel().getSelectedItems().size() != 1;
		}, tree.getSelectionModel().getSelectedItems()));
		
		addColumn(Page.Field.TITLE, "Title");
		addColumn(Page.Field.TIME_CREATED, "Created");
		
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
	protected void addColumn(Page.Field f, String label)
	{
		FxTreeTableColumn<Page> c = new FxTreeTableColumn<Page>(label, true)
		{
			protected ObservableValue getCellValueProperty(Page p)
			{
				return FX.toObservableValue(p.getField(f));
			}
		};
		c.setAlignment(getAlignment(f));
		c.setFormatter(getFormatter(f));
		c.setPrefWidth(150);
		
		tree.getColumns().add(c);
	}
	
	
	protected Pos getAlignment(Page.Field f)
	{
		switch(f)
		{
//		case TIME_CREATED:
//			return Pos.CENTER_RIGHT;
		default:
			return Pos.CENTER_LEFT;
		}
	}
	
	
	protected FxFormatter getFormatter(Page.Field f)
	{
		switch(f)
		{
		case ID:
			return Formatters.ID;
		case TIME_CREATED:
		case TIME_MODIFIED:
			return Formatters.DATE_TIME;
		default:
			return null;
		}
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