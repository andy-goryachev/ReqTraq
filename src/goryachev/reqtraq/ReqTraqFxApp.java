// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
import goryachev.common.log.Log;
import goryachev.common.util.ASettingsStore;
import goryachev.common.util.FileSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.fx.FxFramework;
import goryachev.fx.settings.FxSettingsSchema;
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


	@Override
	public void init() throws Exception
	{
		// TODO change to something visible in Documents? platform-specific?
		File baseDir = new File(System.getProperty("user.home"), ".goryachev.com");
			
		File logFolder = new File(baseDir, "logs"); 
		Log.initConsoleForDebug();
		
		File settingsFile = new File(baseDir, "settings.conf");
		FileSettingsProvider p = new FileSettingsProvider(settingsFile);
		GlobalSettings.setProvider(p);
		p.loadQuiet();
	}


	@Override
	public void start(Stage stage) throws Exception
	{
		FxFramework.setStyleSheet(Styles::new);
		ASettingsStore store = GlobalSettings.instance();
		FxFramework.openLayout(new FxSettingsSchema(store)
		{
			@Override
			public Stage createDefaultWindow()
			{
				return new MainWindow();
			}

			@Override
			protected Stage createWindow(String name)
			{
				return null;
			}
		});
	}
}
