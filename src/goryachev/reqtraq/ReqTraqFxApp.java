// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.util.FileSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.fx.CssLoader;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Requirements Tracker Application.
 */
public class ReqTraqFxApp
	extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}


	public void init() throws Exception
	{
		// TODO change to something visible in Documents? platform-specific?
		File baseDir = new File(System.getProperty("user.home"), ".goryachev.com");
			
		File logFolder = new File(baseDir, "logs"); 
		Log.init(logFolder);
		
		File settingsFile = new File(baseDir, "settings.conf");
		FileSettingsProvider p = new FileSettingsProvider(settingsFile);
		GlobalSettings.setProvider(p);
		p.loadQuiet();
	}


	public void start(Stage stage) throws Exception
	{
		new MainWindow().open();
		
		CssLoader.setStyles(new Styles().createOldStyleGenerator());
	}
}
