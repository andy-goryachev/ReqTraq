// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.common.util.SB;


/**
 * Fx Css Property.
 */
public class FxCssProp
{
	protected final String name;
	protected final Object value;
	
	
	public FxCssProp(String name, Object value)
	{
		this.name = name;
		this.value = value;
	}
	

	public void write(SB sb)
	{
		sb.a(name);
		sb.a(": ");
		sb.a(value);
	}
}
