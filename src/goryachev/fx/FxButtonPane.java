// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Fx Button Pane.
 * 
 * TODO default button
 * TODO fix HPane layout
 * TODO set minimum button width
 */
public class FxButtonPane
	extends HPane
{
	public static final CssStyle PANE = new CssStyle("FxButtonPane_PANE");

	
	public FxButtonPane()
	{
		super(5);
		FX.style(this, PANE);
	}
	
	
	public FxButton addButton(String text, FxAction a)
	{
		FxButton b = new FxButton(text, a);
		b.setMinWidth(70);
		
		if(getChildren().size() == 0)
		{
			fill();
		}
		
		add(b);
		return b;
	}
	
	
	public FxButton addButton(String text)
	{
		FxButton b = new FxButton(text);
		b.setMinWidth(70);
		b.setDisable(true);
		
		if(getChildren().size() == 0)
		{
			fill();
		}
		
		add(b);
		return b;
	}
}
