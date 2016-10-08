// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import javafx.beans.Observable;


/**
 * Main Controller.
 */
public class MainController
{
	public final TreeTablePane tree;
	public final EditorPane editor;
	
	
	public MainController(TreeTablePane tree, EditorPane editor)
	{
		this.tree = tree;
		this.editor = editor;
		
		tree.tree.getSelectionModel().getSelectedItems().addListener((Observable s) -> updatePage());
	}
	
	
	protected void updatePage()
	{
		Page p = tree.getSelectedPage();
		setDetail(p);
	}


	protected void setDetail(Page p)
	{
		editor.setPage(p);
	}
}
