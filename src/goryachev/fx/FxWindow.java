// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.internal.FxWindowBoundsMonitor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * FxWindow.
 */
public class FxWindow
	extends Stage
{
	/** 
	 * Override to ask the user to confirm closing of window.
	 * Make sure to check if the argument already has the user's choice and
	 * perform the necessary action.
	 * If a dialog must be shown, make sure to call toFront().
	 */
	public void confirmClosing(OnWindowClosing choice) { }
	
	//
	
	public final CAction closeWindowAction = new CAction() { public void action() { closeWithConfirmation(); }};
	private final String name;
	private final BorderPane pane;
	private final FxWindowBoundsMonitor normalBoundsMonitor = new FxWindowBoundsMonitor(this);
	
	
	public FxWindow(String name)
	{
		this.name = name;
		this.pane = new BorderPane();
		setScene(new Scene(pane));
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public double getNormalX()
	{
		return normalBoundsMonitor.getX();
	}
	
	
	public double getNormalY()
	{
		return normalBoundsMonitor.getY();
	}
	
	
	public double getNormalWidth()
	{
		return normalBoundsMonitor.getWidth();
	}
	
	
	public double getNormalHeight()
	{
		return normalBoundsMonitor.getHeight();
	}
	
	
	public void open()
	{
		FX.open(this);
	}
	
	
	public void setTop(Node n)
	{
		pane.setTop(n);
	}
	
	
	public void setBottom(Node n)
	{
		pane.setBottom(n);
	}
	
	
	public void setLeft(Node n)
	{
		pane.setLeft(n);
	}
	
	
	public void setRight(Node n)
	{
		pane.setRight(n);
	}
	
	
	public void setCenter(Node n)
	{
		pane.setCenter(n);
	}
	
	
	public Node getCenter()
	{
		return pane.getCenter();
	}
	
	
	public void setSize(int width, int height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	
	public void setMinSize(int width, int height)
	{
		setMinWidth(width);
		setMinHeight(height);
	}
	
	
	public void setMaxSize(int width, int height)
	{
		setMaxWidth(width);
		setMaxHeight(height);
	}
	
	
	public void closeWithConfirmation()
	{
		OnWindowClosing ch = new OnWindowClosing(false);
		confirmClosing(ch);
		if(!ch.isCancelled())
		{
			close();
		}
	}
}
