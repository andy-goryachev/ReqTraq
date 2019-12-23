// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.fx.FxDateFormatter;
import goryachev.fx.FxFormatter;


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
