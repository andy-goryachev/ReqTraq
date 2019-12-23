// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.CPane;
import goryachev.fx.CssID;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.reqtraq.data.Page;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 * Editor Pane.
 */
public class EditorPane
	extends CPane
{
	public static final CssStyle STYLE = new CssStyle("EditorPane");
	public static final CssID ID_TITLE = new CssID("title");
	public static final CssID ID_TEXT = new CssID("text");
	
	public final TextField titleField;
	public final TextArea textField;
	
	
	public EditorPane()
	{
		FX.style(this, STYLE);
		
		titleField = new TextField();
		FX.style(titleField, ID_TITLE);
		
		textField = new TextArea();
		FX.style(textField, ID_TEXT, FxCtl.WRAP_TEXT);
	
		setTop(titleField);
		setCenter(textField);
	}


	public void setPage(Page p)
	{
		// TODO bind properties
		if(p == null)
		{
			titleField.setText(null);
			textField.setText(null);
		}
		else
		{
			titleField.setText(p.getTitle());
			textField.setText(p.getText());
		}
	}


	public String getText()
	{
		return textField.getText();
	}
}
