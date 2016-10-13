// Copyright © 2007-2016 Andy Goryachev <andy@goryachev.com>
package research.open;
import goryachev.common.util.CList;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SStream;
import goryachev.common.util.html.HtmlTools;
import goryachev.fx.Binder;
import goryachev.fx.CAction;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuItem;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.HasSettings;
import java.io.File;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Window;


/** Standard open file controller with recent files */
public abstract class OpenFileController
	implements HasSettings
{
	protected abstract void delegateCommit();
	
	protected abstract void delegateNewFile() throws Exception;
	
	protected abstract void delegateSaveFile(File f) throws Exception;
	
	protected abstract void delegateOpenFile(File f) throws Exception;
	
	//
	
	public final CAction newFileAction = new CAction(this::newFilePrivate);
	public final CAction openFileAction = new CAction(this::openFilePrivate);
	public final CAction saveAction = new CAction(this::saveFilePrivate);
	public final CAction saveAsAction = new CAction(this::saveFileAsPrivate);
	public final CAction clearRecentAction = new CAction(this::clearRecentFiles);
	
	public final SimpleObjectProperty<File> file = new SimpleObjectProperty<>();
	public final SimpleBooleanProperty modified = new SimpleBooleanProperty();
	public final ObservableList<File> recentFiles = FXCollections.observableArrayList();
	private CList<FileChooser.ExtensionFilter> filters;
	protected final Window parent;
	protected final String lastDirKey;
	protected final String recentFilesKey;
	protected int maxRecentFiles = 10;

	
	public OpenFileController(Window parent, String lastDirKey, String recentFilesKey)
	{
		this.parent = parent;
		this.lastDirKey = lastDirKey;
		this.recentFilesKey = recentFilesKey;
	}
	
	
	public OpenFileController(Window parent)
	{
		this(parent, "last.dir", "recent.files");
	}

	
	public void setMaxRecentFiles(int n)
	{
		this.maxRecentFiles = n;
	}
	
	
	public File getFile()
	{
		return file.get();
	}
	
	
	public boolean isModified()
	{
		return modified.get();
	}
	
	
	public void setModified(boolean on)
	{
		if(modified.get() != on)
		{
			modified.set(on);
		}
	}
	
	
	public void openRecentQuietly()
	{
		File f = getRecentFile();
		if(f != null)
		{
			if(f.exists() && f.isFile())
			{
				try
				{
					openFilePrivate(f);
				}
				catch(Exception e)
				{ }
			}
		}
	}
	
	
	public void clearRecentFiles()
	{
		recentFiles.clear();
		GlobalSettings.setString(recentFilesKey, null);
	}
	
	
	protected void loadRecentFiles()
	{
		CList<File> list = new CList<>();
		try
		{
			SStream ss = GlobalSettings.getStream(recentFilesKey);
			for(String s: ss)
			{
				list.add(new File(s));
			}
		}
		catch(Exception e)
		{ }
		
		recentFiles.setAll(list);
	}
	
	
	public void addRecentFile(File f)
	{
		if(f != null)
		{
			List<File> fs = recentFiles;
			int sz = fs.size();
			for(int i=sz-1; i>=0; --i)
			{
				if(fs.get(i).equals(f))
				{
					fs.remove(i);
				}
			}
			
			fs.add(0, f);
			while(fs.size() > maxRecentFiles)
			{
				fs.remove(fs.size() - 1);
			}
			
			SStream ss = new SStream();
			for(File ch: fs)
			{
				ss.add(ch.getAbsolutePath());
			}
			
			GlobalSettings.setStream(recentFilesKey, ss);
		}
	}
	
	
	public File getRecentFile()
	{
		List<File> fs = recentFiles;
		if(fs.size() > 0)
		{
			return fs.get(0);
		}
		return null;
	}
	
	
	public int getRecentFileCount()
	{
		return recentFiles.size();
	}
	
	
	public boolean checkModified()
	{
		if(isModified())
		{
			int rv = Dialogs.choice
			(
				parent, 
				"Modified",
				"Do you want to save current file?",
				new String[] { "Save", "Discard", "Cancel" }
			);
			try
			{
				switch(rv)
				{
				case 0:
					// save
					delegateSaveFile(getFile());
					return true;
				case 1:
					// discard
					return true;
				case 2:
				default:
					return false;
				}
			}
			catch(Exception e)
			{
				Dialogs.error(parent, e);
				return false;
			}
		}
		return true;
	}

	
	protected void newFilePrivate()
	{
		delegateCommit();
		
		try
		{
			if(checkModified())
			{
				file.set(null);
				delegateNewFile();
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}
	
	
	protected void openFilePrivate()
	{
		delegateCommit();
		
		try
		{
			if(checkModified())
			{
				FileChooser fc = new FileChooser();
				//parent, lastDirKey);
				configureFileFilter(fc);
				//fc.setApproveButtonText(openButtonText);
				File f = fc.showOpenDialog(parent);
				if(f != null)
				{
					openFilePrivate(f);
				}
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}
	
	
	protected CList<FileChooser.ExtensionFilter> filters()
	{
		if(filters == null)
		{
			filters = new CList<>();
		}
		return filters;
	}
	
	
	public void addFileFilter(String ext, final String description)
	{
		filters().add(0, new FileChooser.ExtensionFilter(description, ext));
	}


	protected void configureFileFilter(FileChooser fc)
	{
		fc.getExtensionFilters().setAll(filters());
	}


	public void doOpenFile(File f)
	{
		delegateCommit();
		
		try
		{
			if(checkModified())
			{
				openFilePrivate(f);
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}
	
	
	public void openFilePrivate(File f) throws Exception
	{
		delegateOpenFile(f);
		
		file.set(f);
		addRecentFile(f);
		setModified(false);
	}
	
	
	protected void saveFilePrivate()
	{
		try
		{
			if(file != null)
			{
				delegateCommit();
				delegateSaveFile(getFile());
				setModified(false);
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}
	
	
	protected File fixExtension(File f)
	{
		String name = f.getName();
		if(!name.contains("."))
		{
			String ext = getExtension();
			if(ext != null)
			{
				if(!ext.startsWith("."))
				{
					ext = "." + ext;
				}
				
				return new File(f.getParentFile(), name + ext); 
			}
		}
		return f;
	}
	
	
	protected String getExtension()
	{
		if(filters != null)
		{
			for(FileChooser.ExtensionFilter f: filters)
			{
				// TODO logic?
				return f.getExtensions().get(0);
			}
		}
		return null;
	}
	
	
	protected void saveFileAsPrivate()
	{
		delegateCommit();
		
		FileChooser fc = new FileChooser();
		//parent, lastDirKey);
		configureFileFilter(fc);
		//fc.setApproveButtonText(saveButtonText);
		File f = fc.showSaveDialog(parent);
		if(f != null)
		{
			f = fixExtension(f);
			
			if(f.exists())
			{
				int rv = Dialogs.choice
				(
					parent, 
					"Overwrite?", // FIX
					"File exists.  Do you want to overwrite it?",
					new String[] { "Overwrite", "Cancel" }
				);
				if(rv != 0)
				{
					return;
				}
			}
			
			try
			{
				delegateSaveFile(f);
				
				file.set(f);
				setModified(false);
				addRecentFile(f);
			}
			catch(Exception e)
			{
				Dialogs.error(parent, e);
			}
		}
	}


	public CMenu recentFilesMenu()
	{
		return new RecentFilesMenu();
	}
	
	
	public void storeSettings(String prefix)
	{
		// TODO store
	}
	
	
	public void restoreSettings(String prefix)
	{
		// TODO restore		
	}
	
	
	//
	
	
	public class RecentFilesMenu 
		extends CMenu
	{
		public RecentFilesMenu()
		{
			super("Open Recent");
			
			disableProperty().bind(Bindings.createBooleanBinding(() -> (getRecentFileCount() == 0), recentFiles));
			
			Binder.bind(this::rebuild, recentFiles);
		}


		protected void rebuild()
		{
			clear();

			for(final File f: recentFiles)
			{
				String name = f.getName();
				String path = f.getAbsolutePath();
				Node icon = null;
				
				if(path.endsWith(name))
				{
					path = path.substring(0, path.length() - name.length());
					icon = new TextFlow(FX.text(path), FX.text(name, FxCtl.BOLD));
					path = null;
				}

				add(new CMenuItem(path, icon, new CAction()
				{
					public void action()
					{
						doOpenFile(f);
					}
				}));
			}

			if(getRecentFileCount() > 0)
			{
				separator();
			}
			
			add(new CMenuItem("Clear Recent Files", clearRecentAction));
		}
	}
}
