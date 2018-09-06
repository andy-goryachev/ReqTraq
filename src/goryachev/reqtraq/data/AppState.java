// Copyright © 2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import java.io.InputStream;
import java.io.OutputStream;
import javafx.beans.property.SimpleObjectProperty;


/**
 * App State.
 */
public class AppState
{
	public static final SimpleObjectProperty<Page> root = new SimpleObjectProperty(new Page());
	
	//
	

	public static Page getRootPage()
	{
		return root.get();
	}
	
	
	public static void save(OutputStream out) throws Exception
	{
		new AppStateWriter(out).write();
	}
	
	
	public static void load(InputStream in) throws Exception
	{
		// TODO
	}
}
