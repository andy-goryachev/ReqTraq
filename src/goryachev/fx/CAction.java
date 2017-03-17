// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Log;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleButton;


/**
 * An AbstractAction equivalent for FX, using method references.
 * 
 * Usage:
 *    public final CAction backAction = new CAction(this::actionBack);
 */
public class CAction
    implements EventHandler<ActionEvent>
{
	private final BooleanProperty selectedProperty = new SimpleBooleanProperty(this, "selected");
	private final BooleanProperty disabledProperty = new SimpleBooleanProperty(this, "disabled");
	private Runnable onAction;
	
	
	public CAction(Runnable onAction, boolean enabled)
	{
		this.onAction = onAction;
		setEnabled(enabled);
	}
	
	
	public CAction(Runnable onAction)
	{
		this.onAction = onAction;
	}
	
	
	public CAction()
	{
	}
	
	
	protected void action()
	{
		if(onAction != null)
		{
			try
			{
				onAction.run();
			}
			catch(Exception e)
			{
				Log.ex(e);
			}
		}
	}


	public void attach(ButtonBase b)
	{
		b.setOnAction(this);
		b.disableProperty().bind(disabledProperty());

		if(b instanceof ToggleButton)
		{
			((ToggleButton)b).selectedProperty().bindBidirectional(selectedProperty());
		}
	}


	public void attach(MenuItem m)
	{
		m.setOnAction(this);
		m.disableProperty().bind(disabledProperty());

		if(m instanceof CheckMenuItem)
		{
			((CheckMenuItem)m).selectedProperty().bindBidirectional(selectedProperty());
		}
		else if(m instanceof RadioMenuItem)
		{
			((RadioMenuItem)m).selectedProperty().bindBidirectional(selectedProperty());
		}
	}


	public final BooleanProperty selectedProperty()
	{
		return selectedProperty;
	}


	public final boolean isSelected()
	{
		return selectedProperty.get();
	}


	public final void setSelected(boolean on)
	{
		selectedProperty.set(on);
	}


	public final BooleanProperty disabledProperty()
	{
		return disabledProperty;
	}


	public final boolean isDisabled()
	{
		return disabledProperty.get();
	}


	public final void setDisabled(boolean on)
	{
		disabledProperty.set(on);
	}
	
	
	public final boolean isEnabled()
	{
		return !isDisabled();
	}
	
	
	public final void setEnabled(boolean on)
	{
		disabledProperty.set(!on);
	}
	
	
	public final void enable()
	{
		setEnabled(true);
	}
	
	
	public final void disable()
	{
		setEnabled(false);
	}
	
	
	public void fire()
	{
		if(isEnabled())
		{
			handle(null);
		}
	}


	/** override to obtain the ActionEvent */
	public void handle(ActionEvent ev)
	{
		if(isEnabled())
		{
			if(ev != null)
			{
				if(ev.getSource() instanceof Menu)
				{
					if(ev.getSource() != ev.getTarget())
					{
						// selection of a cascading child menu triggers action event for the parent 
						// for some unknown reason.  ignore this.
						return;
					}
				}
				
				ev.consume();
			}
			
			try
			{
				action();
			}
			catch(Exception e)
			{
				Log.ex(e);
			}
		}
	}
}
