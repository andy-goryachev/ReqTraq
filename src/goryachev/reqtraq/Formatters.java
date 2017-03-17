// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import research.fx.FxDateFormatter;
import research.fx.FxFormatter;


/**
 * Formatters.
 */
public class Formatters
{
	/** date-time formatter */
	public static final FxDateFormatter DATE_TIME = new FxDateFormatter("yyyy-MM-dd HH:mm");
	
	/** formatter uses toString() */
	public static final FxFormatter ID = new FxFormatter()
	{
		public String toString(Object x)
		{
			return format(x);
		}
	};
}
