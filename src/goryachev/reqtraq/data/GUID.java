// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.util.GUID256;


/**
 * Globally unique identifier.
 */
public class GUID
{
	public static String create()
	{
		return GUID256.generateDecimalString();
	}
}
