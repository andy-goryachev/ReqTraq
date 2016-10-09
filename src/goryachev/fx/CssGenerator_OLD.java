// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * CssGenerator.
 */
@Deprecated
public abstract class CssGenerator_OLD
{
	protected abstract Object[] generate();
	
	//
	
	public static final CssPseudo DISABLED = new CssPseudo(":disabled");
	public static final CssPseudo FOCUSED = new CssPseudo(":focused");
	public static final CssPseudo HOVER = new CssPseudo(":hover");
	public static final CssPseudo PRESSED = new CssPseudo(":pressed");
	
	public static final String TRANSPARENT = "transparent";
	
	
	public String generateStyleSheet()
	{
		Object[] ss = generate();
		return new CssBuilder_OLD(ss).build();
	}
	
	
	private static CssProperty_OLD p(String name, Object x)
	{
		return new CssProperty_OLD(name, x);
	}
	
	
	public static CssProperty_OLD backgroundColor(Object x) { return p("-fx-background-color", CssTools.toColor(x)); }
	public static CssProperty_OLD backgroundInsets(Object x) { return p("-fx-background-insets", CssTools.toValue(x)); }
	public static CssProperty_OLD backgroundRadius(Object x) { return p("-fx-background-radius", CssTools.toValue(x)); }
	public static CssProperty_OLD fontSize(Object x) { return p("-fx-font-size", x); }
	public static CssProperty_OLD maxHeight(double x) { return p("-fx-max-height", x); }
	public static CssProperty_OLD padding(Object x) { return p("-fx-padding", CssTools.toValue(x)); }
	public static CssProperty_OLD regionBackground(Object x) { return p("-fx-region-background", CssTools.toValue(x)); }
	public static CssProperty_OLD textFill(Object x) { return p("-fx-text-fill", CssTools.toColor(x)); }
}
