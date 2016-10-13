// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;


/**
 * FX Bindings Redux.
 */
@Deprecated // TODO think about it
public class Binder
{
	// do we need this method?  or use WeakListener class directly?
	public static void bind(Runnable r, Observable ... props)
	{
		new WeakListener(props, r);
	}
	
	
	public static class WeakListener implements InvalidationListener
	{
		private final WeakReference<Runnable> ref;
		private final Observable[] props;
		
		
		public WeakListener(Observable[] props, Runnable r)
		{
			this.props = props;
			this.ref = new WeakReference<>(r);
			
			for(Observable p: props)
			{
				p.addListener(this);
			}
		}
		
		
		public void invalidated(Observable src)
		{
			Runnable r = ref.get();
			if(r == null)
			{
				// disconnect
				for(Observable p: props)
				{
					p.removeListener(this);
				}
			}
			else
			{
				r.run();
			}
		}
	}
}
