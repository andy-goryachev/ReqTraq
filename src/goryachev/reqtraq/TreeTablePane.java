// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.util.CList;
import goryachev.fx.CAction;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.OBSOLETE.CommonStyles;
import goryachev.reqtraq.util.Tools;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import research.fx.FxFormatter;
import research.fx.FxTreeTableColumn;


// TODO drop zone indicator
public class TreeTablePane
	extends CPane
{
	public static final CssStyle TREE = new CssStyle("TreeTablePane_TREE");
	
	public final CAction collapseAllAction = new CAction(this::collapseAll);
	public final CAction deleteSelectionAction = new CAction(this::deleteSelection);
	public final CAction expandAllAction = new CAction(this::expandAll);
	public final CAction insertAfterAction = new CAction(this::insertAfter);
	public final CAction insertChildAction = new CAction(this::insertChild);
	
	public final TreeTableView<Page> tree;
	public final TreeTableHandler<Page> handler;
	

	public TreeTablePane()
	{
		tree = new TreeTableView<>();
		tree.setShowRoot(false);
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tree.setEditable(true);
		tree.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		FX.style(tree, CommonStyles.NO_HORIZONTAL_SCROLL_BAR, TREE);
		
		handler = new TreeTableHandler<Page>(tree);
		
		setupActions();
		
		addColumn(Page.Field.TITLE, "Title");
		addColumn(Page.Field.TIME_CREATED, "Created");
		addColumn(Page.Field.STATUS, "Status");
		
		setCenter(tree);
	}
	
	
	protected void setupActions()
	{
		tree.getSelectionModel().getSelectedItems().addListener((Observable s) ->
		{
			int sz = tree.getSelectionModel().getSelectedItems().size();
			boolean atLeastOne = (sz > 0);
			boolean one = (sz == 1);
			
			deleteSelectionAction.setEnabled(atLeastOne);
			insertAfterAction.setEnabled(one);
			insertChildAction.setEnabled(one);
		});
	}
	
	
	public void setRoot(TreeItem<Page> r)
	{
		tree.setRoot(r);
	}
	
	
	public TreeItem<Page> getRoot()
	{
		return tree.getRoot();
	}


	protected void addColumn(Page.Field f, String label)
	{
		FxTreeTableColumn<Page> tc = new FxTreeTableColumn<Page>(label, true)
		{
			protected ObservableValue getCellValueProperty(Page p)
			{
				return FX.toObservableValue(p.getField(f));
			}
		};
		tc.setCellFactory((x) -> new TextFieldTreeTableCell(tc.getConverter()));
		tc.setAlignment(getAlignment(f));
		tc.setConverter(getFormatter(f));
		tc.setPrefWidth(getPreferredWidth(f));
		tc.setEditable(isColumnEditable(f));
		tc.setSortable(false);
		
		tree.getColumns().add(tc);
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
	
	
	protected int getPreferredWidth(Page.Field f)
	{
		switch(f)
		{
		case ID:
			return 100;
		case STATUS:
			return 50;
		case SYNOPSIS:
			return 500;
		case TEXT:
			return 500;
		case TIME_CREATED:
			return 100;
		case TIME_MODIFIED:
			return 100;
		case TITLE:
			return 500;
		default:
			return 100;
		}
	}
	
	
	protected boolean isColumnEditable(Page.Field f)
	{
		switch(f)
		{
		case TITLE:
			return true;
		default:
			return false;
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
	
	
	public void selectFirst()
	{
		tree.getSelectionModel().selectFirst();
	}

	
	public void collapseAll()
	{
		TreeItem<Page> root = tree.getRoot();
		collapseRecursive(root, root);
	}
	
	
	protected void collapseRecursive(TreeItem<Page> root, TreeItem<Page> p)
	{
		if(p != root)
		{
			p.setExpanded(false);
		}
		
		for(TreeItem<Page> ch: p.getChildren())
		{
			collapseRecursive(root, ch);
		}
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
		
		FX.later(() -> 
		{
			tree.edit(ix, tree.getColumns().get(0));
			
			Node n = tree.lookup(".text-input");
			
			// FIX focus the editor
			FX.later(() -> 
			{
				// it looks like the editor is losing focus
				n.requestFocus();
			});
		});
	}
	
	
	public void deleteSelection()
	{
		int ix = tree.getSelectionModel().getSelectedIndex();
		CList<TreeItem<Page>> sel = new CList<>(tree.getSelectionModel().getSelectedItems());
		
		tree.getSelectionModel().clearSelection();
		
		for(TreeItem<Page> p: sel)
		{
			p.getParent().getChildren().remove(p);
		}
		
		tree.getSelectionModel().select(ix);
		
		// TODO undo
	}
}