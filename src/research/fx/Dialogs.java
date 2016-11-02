// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.common.util.D;
import goryachev.common.util.Log;
import javafx.stage.Window;


/**
 * Dialogs. TODO
 */
public class Dialogs
{
	public static int choice(Window parent, String title, String text, String ... choices)
	{
		D.print();
		return -1;
	}
	

	public static void error(Window parent, Throwable e)
	{
		D.print();
		Log.ex(e);
	}
}
