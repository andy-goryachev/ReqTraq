// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx;
import javafx.util.StringConverter;


/**
 * A StringConverter extension.
 */
public abstract class FxFormatter
	extends StringConverter<Object>
{
	public abstract String toString(Object x);
	
	//
	
	public FxFormatter()
	{
	}
	
 
    public Object fromString(String string)
    {
    	throw new Error("FxFormatter: fromString not supported");
    }
    
    
    protected String formatString(Object x)
    {
    	return (x == null ? null : x.toString());
    }
}
