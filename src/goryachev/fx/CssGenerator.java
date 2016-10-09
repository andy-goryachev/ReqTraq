// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.SB;


/**
 * Base class for an FX CSS Generator.
 */
public abstract class CssGenerator
{
	/** use selector() and property methods to build the style sheet */
	protected abstract void generate();
	
	//
	
	public static final CssPseudo DISABLED = new CssPseudo(":disabled");
	public static final CssPseudo FOCUSED = new CssPseudo(":focused");
	public static final CssPseudo HOVER = new CssPseudo(":hover");
	public static final CssPseudo PRESSED = new CssPseudo(":pressed");
	
	public static final String TRANSPARENT = "transparent";
	
	enum State
	{
		IDLE,
		PROPERTY,
		SELECTOR,
		VALUE
	}
	
	private State state;
	private final SB sb = new SB(16384);
	
	
	public CssGenerator()
	{
	}
	
	
	/** generates style sheet */
	public String generateStyleSheet()
	{
		sb.clear();
		
		state = State.IDLE;
		generate();
		
		switch(state)
		{
		case PROPERTY:
			sb.a("}\n");
			break;
		case SELECTOR:
			sb.a("\n{\n}\n");
			break;
		case VALUE:
			throw new Error("missing property value");
		case IDLE:
			break;
		default:
			throw new Error("?" + state);
		}
		
		return sb.toString();
	}
	
	
	public void sel(Object x)
	{
		switch(state)
		{
		case IDLE:
			writeStyle(x);
			break;
		case PROPERTY:
			sb.a("}\n\n");
			writeStyle(x);
			break;
		case SELECTOR:
			writeStyle(x);
			break;
		default:
			throw new Error("unexpected state: " + state);
		}
	}
	
	
	public void sel(Object a, Object b)
	{
		switch(state)
		{
		case IDLE:
			writeStyle(a);
			writeStyle(b);
			break;
		case PROPERTY:
			sb.a("}\n\n");
			writeStyle(a);
			writeStyle(b);
			break;
		case SELECTOR:
			writeStyle(a);
			writeStyle(b);
			break;
		default:
			throw new Error("unexpected state: " + state);
		}
		
		state = State.SELECTOR;
	}
	
	
	public void sel(Object ... ss)
	{
		switch(state)
		{
		case IDLE:
			for(Object x: ss)
			{
				writeStyle(x);
			}
			break;
		case PROPERTY:
			sb.a("}\n\n");
			for(Object x: ss)
			{
				writeStyle(x);
			}
			break;
		case SELECTOR:
			for(Object x: ss)
			{
				writeStyle(x);
			}
			break;
		default:
			throw new Error("unexpected state: " + state);
		}
	}
	
	
	private void writeStyle(Object x)
	{
		// TODO insert space if:
		// - state==style and this object does not start with ":" 
		if(x instanceof CssStyle)
		{
			if(state == State.SELECTOR)
			{
				sb.a(' ');
			}
			
			CssStyle s = (CssStyle)x;
			sb.a('.');
			sb.a(s.getName());
		}
		else if(x instanceof CssID)
		{
			if(state == State.SELECTOR)
			{
				sb.a(' ');
			}
			
			CssID s = (CssID)x;
			sb.a('#');
			sb.a(s.getID());
		}
		else if(x instanceof String)
		{
			String s = (String)x;
			if(state == State.SELECTOR)
			{
				if(!s.startsWith(":"))
				{
					sb.a(' ');
				}
			}
			
			sb.a(s);
		}
		else if(x instanceof CssPseudo)
		{
			CssPseudo s = (CssPseudo)x;
			sb.a(s.getName());
		}

		state = State.SELECTOR;
	}
	
	
	public void prop(String name, Object x)
	{
		switch(state)
		{
		case SELECTOR:
			sb.a("\n{\n");
			// fall through
		case PROPERTY:
			sb.a('\t');
			sb.a(name);
			sb.a(": ");
			sb.append(x == null ? "null" : x);
			sb.a(";\n");
			break;
		default:
			throw new Error("unexpected state: " + state);
		}
		
		state = State.PROPERTY;
	}
	
	
	public void backgroundColor(Object x) { prop("-fx-background-color", CssTools.toColor(x)); }
	public void backgroundInsets(Object x) { prop("-fx-background-insets", CssTools.toValue(x)); }
	public void backgroundRadius(Object x) { prop("-fx-background-radius", CssTools.toValue(x)); }
	public void fontSize(Object x) { prop("-fx-font-size", x); }
	public void maxHeight(double x) { prop("-fx-max-height", x); }
	public void padding(Object x) { prop("-fx-padding", CssTools.toValue(x)); }
	public void regionBackground(Object x) { prop("-fx-region-background", CssTools.toValue(x)); }
	public void textFill(Object x) { prop("-fx-text-fill", CssTools.toColor(x)); }
}
