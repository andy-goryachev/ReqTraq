// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.FxWindow;


/**
 * TestFxEditorWindow.
 */
public class TestFxEditorWindow
	extends FxWindow
{
	public TestFxEditorWindow()
	{
		super("TestFxEditorWindow");
		
		setTitle("FxEditor Test");
		setCenter(new FxEditor(new TestFxPlainEditorModel()));
		setSize(600, 700);
	}
}
