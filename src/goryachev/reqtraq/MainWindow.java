// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.CMenuItem;
import goryachev.fx.CPane;
import goryachev.fx.CPopupMenu;
import goryachev.fx.FX;
import goryachev.fx.FxWindow;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;


/**
 * Main Window.
 */
public class MainWindow
	extends FxWindow
{
	public final TreeTablePane tree;
	public final EditorPane editor;
	public final MainController control;
	
	
	public MainWindow()
	{
		super("MainWindow");
		
		setTitle("ReqTraq © 2016 Andy Goryachev");
		setSize(650, 300);
		
		tree = new TreeTablePane();
		
		editor = new EditorPane();
		
		control = new MainController(tree, editor);
		
		SplitPane split = new SplitPane();
		split.getItems().add(tree);
		split.getItems().add(editor);

		CPane cp = new CPane();
		cp.setCenter(split);
		
		setTop(createMenu());
		setCenter(cp);
		
		new CPopupMenu(tree.tree)
		{
			protected void createPopupMenu()
			{
				add("Cut");
				add("Copy");
				add("Paste");
				separator();

				add("Undo");
				add("Redo");
				separator();

				add("Insert Row");
				add("Insert Child");
				separator();
				
				add("Delete");
				separator();
				
				add("Move Up");
				add("Move Down");
				add("Move Left");
				add("Move Right");
			}
		};
		
		tree.setRoot(createTree());
	}


	private TreeItem<Page> createTree()
	{
		TreeItem<Page> root = new TreeItem<Page>();
		TreeItem<Page> ch;
		ch = ch(root, "Chapter 1", "First Chapter\n\n1.\n2.");
		ch(ch, "1.1", null);
		ch(ch, "1.2", null);
		ch(ch, "1.3", null);
		ch(root, "Chapter 2", "Second Chapter\n\n1.\n2.");
		return root;
	}
	
	
	protected Node createMenu()
	{
		CMenuBar b = new CMenuBar();
		CMenu m = b.addMenu("ReqTraq");
		m.add(new CMenuItem("Quit", FX.exitAction()));
		// file
		m = b.addMenu("File");
		m.add("New");
		m.add("Open");
		m.add("Open Recent");
		m.separator();
		m.add("Close");
		m.separator();
		m.add("Save");
		m.add("Save As...");
		m.separator();
		m.add("Print");
		// view
		m = b.addMenu("View");
		m = b.addMenu("Window");
		// help
		m = b.addMenu("Help");
		m.add("License");
		m.add("Open Source Licenses");
		m.add("About");
		return b;
	}


	private TreeItem<Page> ch(TreeItem<Page> parent, String title, String text)
	{
		Page p = new Page(title, text);
		
		TreeItem<Page> ch = new TreeItem<Page>(p);
		parent.getChildren().add(ch);
		return ch;
	}
}
