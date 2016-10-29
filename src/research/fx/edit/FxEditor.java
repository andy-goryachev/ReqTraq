// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


/**
 * Fx Text Editor.
 */
public class FxEditor
	extends Pane
{
	private ReadOnlyObjectWrapper<FxEditorModel> model = new ReadOnlyObjectWrapper<>();
	private ReadOnlyObjectWrapper<Boolean> wrap = new ReadOnlyObjectWrapper<>();
	private Handler handler = new Handler();
	private boolean layoutDirty;
	private int offsetx;
	private int offsety;
	private int startIndex;
	private CList<Line> lines;
	private ScrollBar vscroll;
	private ScrollBar hscroll;
	
	
	public FxEditor()
	{
		this(FxEditorModel.getEmptyModel());
	}
	
	
	public FxEditor(FxEditorModel m)
	{
		setModel(m);
	}
	
	
	public void setModel(FxEditorModel m)
	{
		FxEditorModel old = getModel();
		if(old != null)
		{
			old.removeListener(handler);
		}
		
		model.set(m);
		
		if(m != null)
		{
			m.addListener(handler);
		}
		
		requestLayout();
	}
	
	
	public FxEditorModel getModel()
	{
		return model.get();
	}
	
	
	public ReadOnlyObjectProperty<FxEditorModel> modelProperty()
	{
		return model.getReadOnlyProperty();
	}
	
	
	protected void layoutChildren()
	{
		updateParagraphs();
		
		for(Node n: getChildrenUnmodifiable())
		{
			if(n.isResizable() && n.isManaged())
			{
				n.autosize();
			}
		}
	}
	
	
	protected void updateParagraphs()
	{
		// TODO is loaded?
		FxEditorModel m = getModel();
		int lines = m.getLineCount();
		CList<Line> ls = new CList<>(32);
		
		getChildren().clear();
		
		double y = 0;
		double height = getHeight();
		
		for(int ix=startIndex; ix<lines; ix++)
		{
			Region n = m.getDecoratedLine(ix);
			n.setManaged(true);
			getChildren().add(n);
			
			double w = n.prefWidth(-1);
			double h = n.prefHeight(w);
			
			Line ln = new Line();
			ln.index = ix;
			ln.box = n;
			ls.add(ln);
			
			layoutInArea(n, 0, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			
			y += h;
			if(y > height)
			{
				break;
			}
		}
	}
	
	
	public void setAbsolutePosition(double pos)
	{
		// TODO
	}
	
	
	public void scroll(double pixels)
	{
		// TODO
	}
	
	
	//
	
	
	/** represents a box enclosing a single line of source text */
	public static class Line
	{
		public int index;
		public Region box;
		// TODO line numbers
	}
	
	
	public class Handler
		implements FxEditorModel.Listener
	{
		public void eventLinesDeleted(int start, int count)
		{
		}


		public void eventLinesInserted(int start, int count)
		{
		}


		public void eventLinesModified(int start, int count)
		{
		}
	}
}
