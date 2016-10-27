// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import javafx.scene.layout.Region;


/**
 * FxEditor Model.
 */
public abstract class FxEditorModel
{
	public interface Listener
	{
	}
	
	//
	
	public abstract boolean isLoaded();
	
	// FIX replace with progress?
	public abstract int getApproximalLineCount();
	
	/** returns line count or -1 if unknown */
	public abstract int getLineCount();
	
	/** returns plain text at the specified line, or null if unknown */
	public abstract String getSearchText(int line);
	
	/** returns a non-null Region containing Text, TextFlow, or any other Nodes representing a line */
	public abstract Region getDecoratedLine(int line);
	
	//

	private CList<Listener> listeners = new CList<>();
	private static FxEditorModel empty;
	
	
	public FxEditorModel()
	{
	}
	
	
	public void addListener(Listener li)
	{
		listeners.add(li);
	}
	
	
	public void removeListener(Listener li)
	{
		listeners.remove(li);
	}
	
	
	public static FxEditorModel getEmptyModel()
	{
		if(empty == null)
		{
			empty = new FxEditorModel()
			{
				public boolean isLoaded() { return true; }
				public int getApproximalLineCount() { return 0; }
				public int getLineCount() { return 0; }
				public String getSearchText(int line) { return null; }
				public Region getDecoratedLine(int line) { return null; }
			};
		}
		return empty;
	}
}
