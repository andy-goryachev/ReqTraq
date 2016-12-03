// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.css;
import goryachev.fx.CssPseudo;
import goryachev.fx.CssTools;
import javafx.scene.control.ScrollPane;


/**
 * Standard Fx Css Properties.
 */
public class StandardFxProperties
{
	// B
	public static Prop backgroundColor(Object x) { return new Prop("-fx-background-color", CssTools.toColor(x)); }
	public static Prop backgroundImage(Object x) { return new Prop("-fx-background-image", CssTools.toValue(x)); }
	public static Prop backgroundInsets(Object x) { return new Prop("-fx-background-insets", CssTools.toValue(x)); }
	public static Prop backgroundRadius(Object x) { return new Prop("-fx-background-radius", CssTools.toValue(x)); }
	/** A series of paint values or sets of four paint values, separated by commas. For each item in the series, if a single paint value is specified, then that paint is used as the border for all sides of the region; and if a set of four paints is specified, they are used for the top, right, bottom, and left borders of the region, in that order. If the border is not rectangular, only the first paint value in the set is used. */
	public static Prop borderColor(Object x) { return new Prop("-fx-border-color", CssTools.toColor(x)); }
	/** A series of paint values or sets of four paint values, separated by commas. For each item in the series, if a single paint value is specified, then that paint is used as the border for all sides of the region; and if a set of four paints is specified, they are used for the top, right, bottom, and left borders of the region, in that order. If the border is not rectangular, only the first paint value in the set is used. */
	public static Prop borderColor(Object ... xs) { return new Prop("-fx-border-color", CssTools.toColors(xs)); }
	/** A series of width or sets of four width values, separated by commas. For each item in the series, a single width value means that all border widths are the same; and if a set of four width values is specified, they are used for the top, right, bottom, and left border widths, in that order. If the border is not rectangular, only the first width value is used. Each item in the series of widths applies to the corresponding item in the series of border colors.  */
	public static Prop borderWidth(Object x) { return new Prop("-fx-border-width", CssTools.toValue(x)); }
	/** A series of width or sets of four width values, separated by commas. For each item in the series, a single width value means that all border widths are the same; and if a set of four width values is specified, they are used for the top, right, bottom, and left border widths, in that order. If the border is not rectangular, only the first width value is used. Each item in the series of widths applies to the corresponding item in the series of border colors.  */
	public static Prop borderWidth(Object ... xs) { return new Prop("-fx-border-width", CssTools.toValues(xs)); }

	// C
	public static Prop cellSize(Object x) { return new Prop("-fx-cell-size", x); }
	
	// F
	public static Prop fitToHeight(boolean x) { return new Prop("-fx-fit-to-height", x); }
	public static Prop fitToWidth(boolean x) { return new Prop("-fx-fit-to-width", x); }
	public static Prop fontSize(Object x) { return new Prop("-fx-font-size", x); }
	/** [ normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900 ] */
	public static Prop fontWeight(Object x) { return new Prop("-fx-font-weight", x); }
	// H
	public static Prop hBarPolicy(ScrollPane.ScrollBarPolicy x) { return new Prop("-fx-hbar-policy", CssTools.toValue(x)); }
	// M
	public static Prop maxHeight(double x) { return new Prop("-fx-max-height", x); }
	public static Prop maxWidth(double x) { return new Prop("-fx-max-width", x); }
	public static Prop minHeight(double x) { return new Prop("-fx-min-height", x); }
	public static Prop minWidth(double x) { return new Prop("-fx-min-width", x); }
	// O
	public static Prop opacity(double x) { return new Prop("-fx-opacity", x); }
	// P
	public static Prop padding(Object x) { return new Prop("-fx-padding", CssTools.toValue(x)); }
	public static Prop prefHeight(double x) { return new Prop("-fx-pref-height", x); }
	public static Prop prefWidth(double x) { return new Prop("-fx-pref-width", x); }
	// R
	public static Prop regionBackground(Object x) { return new Prop("-fx-region-background", CssTools.toValue(x)); }
	// S
	public static Prop shape(Object x) { return new Prop("-fx-shape", x); }
	// T
	public static Prop textFill(Object x) { return new Prop("-fx-text-fill", CssTools.toColor(x)); }
	// V
	public static Prop vBarPolicy(ScrollPane.ScrollBarPolicy x) { return new Prop("-fx-vbar-policy", CssTools.toValue(x)); }
}
