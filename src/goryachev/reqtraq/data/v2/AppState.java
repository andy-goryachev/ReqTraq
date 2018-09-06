// Copyright © 2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data.v2;
import goryachev.common.util.Log;
import goryachev.reqtraq.data.Page;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * App State.
 */
public class AppState
{
	// FIX root is different every time
	public static final Page root = new Page();
	
	
	public static void save(OutputStream out) throws Exception
	{
		new AppStateWriter(out).write();
	}
	
	
	public static void load(InputStream in) throws Exception
	{
		// TODO
	}
}