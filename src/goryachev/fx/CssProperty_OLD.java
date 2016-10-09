// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * CssProperty.
 */
@Deprecated
public class CssProperty_OLD
{
	private final String name;
	private final Object value;
	
	
	public CssProperty_OLD(String name, Object val)
	{
		this.name = name;
		this.value = val;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public Object getValue()
	{
		return value;
	}
	
	
	public String toString()
	{
		return getName() + ": " + getValue() + ";";
	}
}
