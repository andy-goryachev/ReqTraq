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
import goryachev.reqtraq.data.ReqDoc;
import goryachev.reqtraq.data.ReqDocJsonReader;
import goryachev.reqtraq.data.ReqDocJsonWriter;
import goryachev.reqtraq.demo.Demo;
import java.io.File;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import research.fx.OpenFileController;


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
	public final SimpleObjectProperty<ReqDoc> document = new SimpleObjectProperty();
	
	
	public MainWindow()
	{
		super("MainWindow");
		
		// FIX
		FxDump.attach(this);
		
		setTitle("ReqTraq");
		setSize(650, 300);
		
		// TODO use method references
		openFileController = new OpenFileController(this)
		{
			protected void delegateSaveFile(File f) throws Exception
			{
				saveFile(f);
			}


			protected void delegateOpenFile(File f) throws Exception
			{
				openFile(f);
			}


			protected void delegateNewFile() throws Exception
			{
				newFile();
			}


			protected void delegateCommit()
			{
				commit();
			}
		};
		openFileController.addFileFilter("json", "*.json Text Files");
		openFileController.addFileFilter("*", "*.* All Files");
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

				add("Insert After", tree.insertAfterAction);
				add("Insert Child", tree.insertChildAction);
				separator();
				
				add("Expand All", tree.expandAllAction);
				separator();
				
				add("Delete");
				separator();
				
				add("Move Up");
				add("Move Down");
				add("Move Left");
				add("Move Right");
			}
		};
		
		// FIX remove
		setDocument(Demo.create());
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
		// edit
		m = b.addMenu("Edit");
		m.add("Undo");
		m.add("Redo");
		m.separator();
		m.add("Cut");
		m.add("Copy");
		m.add("Paste");
		m.separator();
		m.add("Insert After");
		m.add("Insert Child");
		m.separator();
		m.add("Move Left");
		m.add("Move Right");
		m.add("Move Up");
		m.add("Move Down");
		m.separator();
		m.add("Select All");
		m.separator();
		m.add("Delete");
		m.separator();
		m.add("Find");
		// search
		m = b.addMenu("Search");
		// reports
		m = b.addMenu("Reports");
		// view
		m = b.addMenu("View");
		m.add("Dashboard");
		m.add("Requirements");
		m.add("Releases");
		m.add("Search");
		m.add("Reports");
		m.separator();
		m.add("Layout");
		// window
		m = b.addMenu("Window");
		// help
		m = b.addMenu("Help");
		m.add("License");
		m.add("Open Source Licenses");
		m.add("About");
		return b;
	}

	
	public void setDocument(ReqDoc d)
	{
		document.set(d);
		tree.setRoot(d.getTreeRoot());
		// TODO memorize expanded state?
		tree.expandAll();
		tree.selectFirst();
	}
	
	
	public ReqDoc getDocument()
	{
		ReqDoc d = document.get();
		TreeItem<Page> root = tree.getRoot();
		d.setTreeRoot(d);
		return d;
	}
	
	
	protected void commit()
	{
		control.commit();
	}
	
	
	protected void newFile()
	{
		setDocument(new ReqDoc());
	}
	
	
	protected void openFile(File f) throws Exception
	{
		ReqDoc d = ReqDocJsonReader.readJSON(f);
		setDocument(d);
	}
	
	
	protected void saveFile(File f) throws Exception
	{
		ReqDoc d = getDocument();
		ReqDocJsonWriter.saveJSON(d, f);
	}
}
