// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.util.D;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.CMenuItem;
import goryachev.fx.CPane;
import goryachev.fx.CPopupMenu;
import goryachev.fx.FX;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import java.io.File;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import research.open.OpenFileController;


/**
 * Main Window.
 */
public class MainWindow
	extends FxWindow
{
	public final TreeTablePane tree;
	public final EditorPane editor;
	public final MainController control;
	public final OpenFileController openFileController;
	
	
	public MainWindow()
	{
		super("MainWindow");
		
		// FIX
		FxDump.attach(this);
		
		setTitle("ReqTraq © 2016 Andy Goryachev");
		setSize(650, 300);
		
		openFileController = new OpenFileController(this)
		{
			protected void delegateSaveFile(File f) throws Exception
			{
				D.print(f);
			}


			protected void delegateOpenFile(File f) throws Exception
			{
				D.print(f);
			}


			protected void delegateNewFile() throws Exception
			{
				D.print();
			}


			protected void delegateCommit()
			{
				D.print();
			}
		};
		openFileController.addFileFilter("*", "*.* All Files");
		//openFileController.addFileFilter("txt", "*.txt Text Files");
		bind("FILE", openFileController);
		
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

	
	protected Node createMenu()
	{
		CMenuBar b = new CMenuBar();
		// app
		CMenu m = b.addMenu("ReqTraq");
		m.add(new CMenuItem("Quit", FX.exitAction()));
		// file
		m = b.addMenu("File");
		m.add("New", openFileController.newFileAction);
		m.add("Open", openFileController.openFileAction);
		m.add(openFileController.recentFilesMenu());
		m.separator();
		m.add("Save", openFileController.saveAction);
		m.add("Save As...", openFileController.saveAsAction);
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
	

	private TreeItem<Page> ch(TreeItem<Page> parent, String title, String text)
	{
		Page p = new Page(title, text);
		
		TreeItem<Page> ch = new TreeItem<Page>(p);
		parent.getChildren().add(ch);
		return ch;
	}
}
