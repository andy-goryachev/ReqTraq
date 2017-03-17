// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;


/**
 * Main Controller.
 */
public class MainController
{
	public final TreeTablePane tree;
	public final EditorPane editor;
	private final SimpleObjectProperty<Page> page = new SimpleObjectProperty<>();
	private boolean handleEvents = true;
	
	
	public MainController(TreeTablePane tree, EditorPane editor)
	{
		this.tree = tree;
		this.editor = editor;
		
		tree.tree.getSelectionModel().getSelectedItems().addListener((Observable s) -> updatePage());
		
		editor.titleField.textProperty().addListener((s,p,c) -> updatePageTitle(c));
		editor.textField.textProperty().addListener((s,p,c) -> updatePageText());
	}
	
	
	protected void updatePage()
	{		
		Page p = tree.getSelectedPage();
		if(p != getPage())
		{
			commit();

			setPage(p);
		}
	}


	protected void setPage(Page p)
	{
		handleEvents = false;
		
		editor.setPage(p);
		page.set(p);
		
		handleEvents = true;
	}
	
	
	public Page getPage()
	{
		return page.get();
	}
	
	
	public void commit()
	{
		Page p = getPage();
		if(p != null)
		{
			String text = editor.getText();
			p.setText(text);
		}
	}
	
	
	protected void updatePageTitle(String s)
	{
		if(handleEvents)
		{
			Page p = getPage();
			if(p != null)
			{
				p.setTitle(s);
			}
		}
	}
	
	
	protected void updatePageText()
	{
		if(handleEvents)
		{
			// TODO set modified only
		}
	}
}
