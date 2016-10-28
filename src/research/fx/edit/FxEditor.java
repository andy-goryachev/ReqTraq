// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


/**
 * Fx Text Editor.
 */
public class FxEditor
	extends Pane
{
	private ReadOnlyObjectWrapper<FxEditorModel> model = new ReadOnlyObjectWrapper<>();
	private Handler handler = new Handler();
	
	
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
		// TODO check if changed, reload paragraphs
		
		for(Node n: getChildrenUnmodifiable())
		{
			if(n.isResizable() && n.isManaged())
			{
				n.autosize();
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
	
	
	public class Handler
		implements FxEditorModel.Listener
	{
		
	}
}
