// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.util.CKit;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.FxDump;
import goryachev.fx.FxMenuBar;
import goryachev.fx.FxPopupMenu;
import goryachev.fx.FxWindow;
import goryachev.fx.HPane;
import goryachev.reqtraq.data.v1.ReqDoc;
import goryachev.reqtraq.data.v1.ReqDocJsonReader;
import goryachev.reqtraq.data.v1.ReqDocJsonWriter;
import goryachev.reqtraq.demo.Demo;
import java.io.File;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import javafx.stage.Window;
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
		setBottom(createStatusBar());
		
		FX.setPopupMenu(tree.tree, this::createTreePopupMenu);
		
		// FIX remove
		setDocument(Demo.create());
	}
	
	
	protected FxPopupMenu createTreePopupMenu()
	{
		FxPopupMenu m = new FxPopupMenu();
		m.item("Cut");
		m.item("Copy");
		m.item("Paste");
		m.separator();

		m.item("Undo");
		m.item("Redo");
		m.separator();

		m.item("Insert After", tree.insertAfterAction);
		m.item("Insert Child", tree.insertChildAction);
		m.separator();
		
		m.item("Expand All", tree.expandAllAction);
		m.item("Collapse All", tree.collapseAllAction);
		m.separator();
		
		m.item("Delete", tree.deleteSelectionAction);
		m.separator();
		
		m.item("Move Up");
		m.item("Move Down");
		m.item("Move Left");
		m.item("Move Right");
		return m;
	}

	
	protected Node createMenu()
	{
		FxMenuBar m = new FxMenuBar();
		// app
		m.menu("ReqTraq");
		m.item("Quit", FX.exitAction());
		// file
		m.menu("File");
		m.item("New", openFileController.newFileAction);
		m.item("Open", openFileController.openFileAction);
		m.add(openFileController.recentFilesMenu());
		m.separator();
		m.item("Save", openFileController.saveAction);
		m.item("Save As...", openFileController.saveAsAction);
		m.separator();
		m.item("Print");
		// edit
		m.menu("Edit");
		m.item("Undo");
		m.item("Redo");
		m.separator();
		m.item("Cut");
		m.item("Copy");
		m.item("Paste");
		m.separator();
		m.item("Insert After");
		m.item("Insert Child");
		m.separator();
		m.item("Move Left");
		m.item("Move Right");
		m.item("Move Up");
		m.item("Move Down");
		m.separator();
		m.item("Select All");
		m.separator();
		m.item("Delete");
		m.separator();
		m.item("Find");
		// search
		m.menu("Search");
		// reports
		m.menu("Reports");
		// view
		m.menu("View");
		m.item("Dashboard");
		m.item("Requirements");
		m.item("Releases");
		m.item("Search");
		m.item("Reports");
		m.separator();
		m.item("Layout");
		// window
		m.menu("Window");
		
		if(CKit.isEclipse())
		{
			m.menu("Debug");
			m.item("Resize Windows for Screenshot", new FxAction(this::resizeWindows));
		}
		
		// help
		m.menu("Help");
		m.item("License");
		m.item("Open Source Licenses");
		m.item("About");
		return m;
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
	
	
	protected Node createStatusBar()
	{
		HPane p = new HPane();
		p.setPadding(new Insets(1, 10, 1, 10));
		
//		p.add(FX.label(statusProperty, Color.GRAY));
		p.fill();
		p.add(FX.label(Version.COPYRIGHT, Color.GRAY));
		return p;
	}
	
	
	protected void resizeWindows()
	{
		for(Window w: FX.getWindows())
		{
			w.setWidth(Config.SNAPSHOT_WIDTH);
			w.setHeight(Config.SNAPSHOT_HEIGHT);
		}
	}
}
