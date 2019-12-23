// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.fx.FxButton;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;


/**
 * This pane demonstrates the CPane functionality.
 */
public class DemoLoginPane
	extends CPane
{
	public final TextField userNameField;
	private final PasswordField passwordField;
	public final FxButton loginButton;
	
	
	public DemoLoginPane()
	{
		String info = "This demonstrates capabilities of CPane component.  This demonstrates capabilities of CPane component.  This demonstrates capabilities of CPane component.  This demonstrates capabilities of CPane component.";

		Text te = new Text(info);
		TextFlow t = new TextFlow();
		t.getChildren().add(te);

		Node infoField = t;

		// FX.label(info, FxCtl.WRAP_TEXT));
//		infoField.setWrappingWidth().bind(infoField.pre;
		
		userNameField = new TextField();

		passwordField = new PasswordField();

		loginButton = new FxButton("Login");
		loginButton.setMinWidth(100);

		CPane p = new CPane();
		p.setGaps(10, 5);
		p.setPadding(10);
		p.addColumns
		(
			10,
			CPane.PREF,
			CPane.FILL,
			CPane.PREF,
			10
		);
		p.addRows
		(
			10,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.FILL,
			10
		);
		int r = 1;
		p.add(1, r, 3, 1, infoField); 
		r++;
		p.add(1, r, FX.label("User name:", TextAlignment.RIGHT));
		p.add(2, r, 2, 1, userNameField);
		r++;
		p.add(1, r, FX.label("Password:", TextAlignment.RIGHT));
		p.add(2, r, 2, 1, passwordField);
		r++;
		p.add(3, r, loginButton);
		
		setCenter(p);
	}
}
