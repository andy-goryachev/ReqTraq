// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.util.BKey;
import goryachev.common.util.GUID256;


/**
 * Globally unique identifier.
 */
public class GUID
{
	public static BKey create()
	{
		return GUID256.generateKey();
	}
}
