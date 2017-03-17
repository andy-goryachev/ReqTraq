// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.geometry.Insets;


/**
 * Insets.
 */
public class CInsets
	extends Insets
{
	public CInsets(double top, double right, double bottom, double left)
	{
		super(top, right, bottom, left);
	}
	
	
	public CInsets(double vert, double hor)
	{
		super(vert, hor, vert, hor);
	}
	
	
	public CInsets(double gap)
	{
		super(gap);
	}
}
