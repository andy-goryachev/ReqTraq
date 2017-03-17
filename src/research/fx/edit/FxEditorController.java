// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.D;
import goryachev.fx.FX;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.PathElement;


/**
 * FxEditor Controller.
 */
public class FxEditorController
	implements FxEditorModel.Listener
{
	public final FxEditorSelectionModel selection;
	protected final FxEditor editor;
	protected ScrollBar vscroll;
	protected ScrollBar hscroll;
	protected boolean dragging;


	public FxEditorController(FxEditor ed)
	{
		this.editor = ed;
		
		selection = new FxEditorSelectionModel(ed);
		
		ed.getChildren().addAll(selection.highlight, vscroll(), selection.caret);
		
		ed.addEventFilter(KeyEvent.KEY_PRESSED, (ev) -> handleKeyPressed(ev));
		ed.addEventFilter(KeyEvent.KEY_RELEASED, (ev) -> handleKeyReleased(ev));
		ed.addEventFilter(KeyEvent.KEY_TYPED, (ev) -> handleKeyTyped(ev));
		ed.addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> handleMousePressed(ev));
		ed.addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> handleMouseReleased(ev));
		ed.addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> handleMouseDragged(ev));
		ed.addEventFilter(ScrollEvent.ANY, (ev) -> handleScroll(ev));
	}
	
	
	protected ScrollBar vscroll()
	{
		if(vscroll == null)
		{
			vscroll = createVScrollBar();
		}
		return vscroll;
	}
	
	
	protected ScrollBar createVScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.VERTICAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		s.valueProperty().addListener((src,old,val) -> setAbsolutePosition(val.doubleValue()));
		return s;
	}
	
	
	public FxEditorSelectionModel selection()
	{
		return selection;
	}


	public void eventLinesDeleted(int start, int count)
	{
	}


	public void eventLinesInserted(int start, int count)
	{
	}


	public void eventLinesModified(int start, int count)
	{
	}
	
	
	public void setAbsolutePosition(double pos)
	{
		// TODO account for visible line count
		int start = FX.round(editor.getModel().getLineCount() * pos);
		editor.setStartIndex(start);
	}
	
	
	public void scrollRelative(double pixels)
	{
		if(pixels < 0)
		{
			double toScroll = pixels;
			int ix = editor.getViewStartLine();
			int offsety = editor.getOffsetY();
			
			LayoutOp op = editor.newLayoutOp();
			
			// TODO
			// using the current layout, add lines until scrolled up to the necessary number of pixels
			// or the first/last line
//			while(toScroll > 0)
//			{
//				if(ix <= 0)
//				{
//					break;
//				}
//			}
		}
		else
		{
			
		}
	}
	
	
	public void moveCaret(boolean right)
	{
		// TODO
	}


	protected void handleKeyPressed(KeyEvent ev)
	{
		switch(ev.getCode())
		{
		case PAGE_DOWN:
			scrollRelative(editor.getHeight());
			break;
		case LEFT:
			moveCaret(false);
			break;
		case PAGE_UP:
			scrollRelative(-editor.getHeight());
			break;
		case RIGHT:
			moveCaret(true);
			break;
		}
	}
	
	
	protected void handleKeyReleased(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected void handleKeyTyped(KeyEvent ev)
	{
		// TODO
	}
	
	
	protected TextPos getTextPos(MouseEvent ev)
	{
		double x = ev.getScreenX();
		double y = ev.getScreenY();
		return editor.getTextPos(x, y);
	}
	
	
	protected void handleMousePressed(MouseEvent ev)
	{
		// TODO property: multiple selection
		TextPos pos = getTextPos(ev);
		
		if(ev.isShiftDown())
		{
			// FIX there might be a zero length segment (single caret)
			
			// expand selection from the anchor point to the current position
			// clearing existing (possibly multiple) selection
			selection.clearAndExtendLastSegment(pos);
		}
		else if(ev.isShortcutDown())
		{
			if(selection.isInsideSelection(pos))
			{
				// replace selection with a single caret
				selection.clear();
				selection.addSegment(pos, pos);
			}
			else
			{
				// FIX add a new caret
				selection.addSegment(pos, pos);
			}
		}
		else
		{
			selection.clear();
			selection.addSegment(pos, pos);
		}
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
		dragging = true;
		
		TextPos pos = getTextPos(ev);
		selection.extendLastSegment(pos);
	}
	
	
	protected void handleMouseReleased(MouseEvent ev)
	{
		dragging = false;
		
		D.print(selection); // FIX
	}
	
	
	protected void handleScroll(ScrollEvent ev)
	{
		// TODO mouse wheel scroll
		D.print(ev);
	}
}