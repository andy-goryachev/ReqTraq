// Copyright © 2005-2019 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.common.util.CPlatform;
import goryachev.common.util.GlobalSettings;
import goryachev.fx.FxWindow;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;


public class FxTestApp
	extends Application
{
	public static final String APP = "FxTest";
	public static final String COPYRIGHT = "copyright © 2016 andy goryachev";	
	
	//
	
	public static void main(String[] args)
	{
		GlobalSettings.setFileProvider(new File(CPlatform.getUserHome(), "settings.dat"));
		Application.launch(FxTestApp.class, args);
	}
	
	
	public void start(Stage s) throws Exception
	{
		FxWindow w = new FxWindow("TEST");
		w.setTitle("FxDemo");
		w.setCenter(new DemoLoginPane());
		w.setSize(400, 300);
		w.open();
	}
}
