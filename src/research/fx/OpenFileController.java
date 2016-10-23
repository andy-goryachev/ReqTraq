// Copyright © 2007-2016 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SStream;
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
import javafx.scene.text.TextAlignment;
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
	private final SimpleObjectProperty<File> lastFolder = new SimpleObjectProperty<>();
	private CList<FileChooser.ExtensionFilter> filters;
	protected final Window parent;
	protected int maxRecentFiles = 10;
	public static final String KEY_DIR = "DIR";
	public static final String KEY_RECENT_FILES = "RECENT";

	
	public OpenFileController(Window parent)
	{
		this.parent = parent;
	}
	
	
	public void setMaxRecentFiles(int n)
	{
		this.maxRecentFiles = n;
	}
	
	
	public void setFile(File f)
	{
		file.set(f);
	}
	
	
	public File getFile()
	{
		return file.get();
	}
	
	
	public String getFileName()
	{
		File f = getFile();
		return (f == null ? null : f.getName());
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
		try
		{
			delegateCommit();

			if(checkModified())
			{
				setFile(null);
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
		try
		{
			delegateCommit();
			
			if(checkModified())
			{
				FileChooser fc = fileChooser();
				File f = fc.showOpenDialog(parent);
				if(f != null)
				{
					File dir = f.getParentFile();
					lastFolder.set(dir);
					
					openFilePrivate(f);
					
					FX.storeSettings();
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


	protected FileChooser fileChooser()
	{
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(getLastFolder());
		fc.getExtensionFilters().setAll(filters());
		return fc;
	}
	
	
	protected File getLastFolder()
	{
		return lastFolder.get();
	}


	public void doOpenFile(File f)
	{
		try
		{
			delegateCommit();

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
		
		setFile(f);
		addRecentFile(f);
		setModified(false);
	}
	
	
	protected void saveFilePrivate()
	{
		try
		{
			if(getFile() != null)
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
				String ext = f.getExtensions().get(0);
				if(!isWildcard(ext))
				{
					return ext;
				}
			}
		}
		return null;
	}
	
	
	protected boolean isWildcard(String s)
	{
		if(s != null)
		{
			if(CKit.containsAny(s, "*?"))
			{
				return true;
			}
		}
		return false;
	}
	
	
	protected void saveFileAsPrivate()
	{
		try
		{
			delegateCommit();
			
			FileChooser fc = fileChooser();
			fc.setInitialFileName(getFileName());
			File f = fc.showSaveDialog(parent);
			if(f != null)
			{
				f = fixExtension(f);
				
				// new FX file chooser seems to ask this question...
//				if(f.exists())
//				{
//					int rv = Dialogs.choice
//					(
//						parent, 
//						"Overwrite?", // FIX
//						"File exists.  Do you want to overwrite it?",
//						new String[] { "Overwrite", "Cancel" }
//					);
//					if(rv != 0)
//					{
//						return;
//					}
//				}
				
				File dir = f.getParentFile();
				lastFolder.set(dir);
			
				delegateSaveFile(f);
				
				setFile(f);
				setModified(false);
				addRecentFile(f);
				
				FX.storeSettings();
			}
		}
		catch(Exception e)
		{
			Dialogs.error(parent, e);
		}
	}


	public CMenu recentFilesMenu()
	{
		return new RecentFilesMenu();
	}
	
	
	public void storeSettings(String prefix)
	{
		GlobalSettings.setFile(KEY_DIR, lastFolder.get());
		GlobalSettings.setFiles(KEY_RECENT_FILES, recentFiles);
	}
	
	
	public void restoreSettings(String prefix)
	{
		lastFolder.set(GlobalSettings.getFile(KEY_DIR));
		recentFiles.setAll(GlobalSettings.getFiles(KEY_RECENT_FILES));
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
					// FIX right alignment
					icon = new TextFlow(FX.text(path, TextAlignment.RIGHT), FX.text(name, FxCtl.BOLD, TextAlignment.RIGHT));
					path = null;
				}

				CMenuItem mi = new CMenuItem(path, icon, new CAction()
				{
					public void action()
					{
						doOpenFile(f);
					}
				});
				add(mi);
			}

			if(getRecentFileCount() > 0)
			{
				separator();
			}
			
			add(new CMenuItem("Clear Recent Files", clearRecentAction));
		}
	}
}
