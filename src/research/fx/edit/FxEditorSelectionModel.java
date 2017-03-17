// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import goryachev.fx.FX;
import goryachev.fx.FxInvalidationListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;


/**
 * FxEditor Selection and Caret Model.
 */
public class FxEditorSelectionModel
{
	protected final FxEditor editor;
	protected final Timeline caretTimeline;
	protected final Path caret;
	protected final Path highlight;
	/** multiple selection segments: the end position corresponds to the caret */ 
	protected final ObservableList<SelectionSegment> segments = FXCollections.observableArrayList();
	
	
	public FxEditorSelectionModel(FxEditor ed)
	{
		this.editor = ed;
		
		highlight = new Path();
		FX.style(highlight, FxEditor.HIGHLIGHT);
		highlight.setManaged(false);
		highlight.setStroke(null);
		highlight.setFill(Color.rgb(255, 255, 0, 0.25));
		
		caret = new Path();
		FX.style(caret, FxEditor.CARET);
		caret.setManaged(false);
		caret.setStroke(Color.BLACK);
		
		caretTimeline = new Timeline();
		caretTimeline.setCycleCount(Animation.INDEFINITE);
		new FxInvalidationListener(ed.blinkRateProperty(), true, () -> updateBlinkRate(ed.getBlinkRate()));
	}
	
	
	protected void updateBlinkRate(Duration d)
	{
		Duration period = d.multiply(2);
		
		caretTimeline.stop();
		caretTimeline.getKeyFrames().setAll
		(
			new KeyFrame(Duration.ZERO, (ev) -> setCaretVisible(true)),
			new KeyFrame(d, (ev) -> setCaretVisible(false)),
			new KeyFrame(period)
		);
		caretTimeline.play();
	}
	
	
	public void clear()
	{
		caret.getElements().clear();
		highlight.getElements().clear();
		segments.clear();
	}

	
	protected void setCaretVisible(boolean on)
	{
		caret.setVisible(on);
	}
	
	
	protected void setCaret2(TextPos p)
	{
		CaretLocation c = editor.getCaretLocation(p);
		if(c == null)
		{
			return;
		}
		
		// TODO square caret on insert
		PathElement[] es = new PathElement[2];
		es[0] = new MoveTo(c.x0, c.y0);
		es[1] = new LineTo(c.x1, c.y1);
		setCaretElements(es);
	}
	

	protected void setCaretElements(PathElement[] es)
	{
		// reset caret so it's always on when moving, unlike MS Word
		caretTimeline.stop();
		caret.getElements().setAll(es);
		caretTimeline.play();
	}

	
	public boolean isInsideSelection(TextPos pos)
	{
		for(SelectionSegment s: segments)
		{
			if(s.contains(pos))
			{
				return true;
			}
		}
		return false;
	}
	

	/** adds a new segment from start to end */
	public void addSegment(TextPos start, TextPos end)
	{
		segments.add(new SelectionSegment(start, end));
		highlight.getElements().addAll(createHighlightPath(start, end));
		caret.getElements().addAll(createCaretPath(end));
		
		// TODO combine overlapping segments
	}
	
	
	public void clearAndExtendLastSegment(TextPos pos)
	{
		TextPos anchor = lastAnchor();
		if(anchor == null)
		{
			anchor = pos;
		}
		
		clear();
		addSegment(anchor, pos);
	}
	
	
	public void extendLastSegment(TextPos pos)
	{
		int ix = segments.size() - 1;
		if(ix < 0)
		{
			 addSegment(pos, pos);
		}
		else
		{
			SelectionSegment s = segments.get(ix);
			TextPos anchor = s.getStart();
			segments.set(ix, new SelectionSegment(anchor, pos));
			
			// TODO combine overlapping segments
			reloadDecorations();
		}
	}
	
	
	protected TextPos lastAnchor()
	{
		int ix = segments.size() - 1;
		if(ix >= 0)
		{
			SelectionSegment s = segments.get(ix);
			return s.getStart();
		}
		return null;
	}
	
	
	// this method can possibly be optimized to modify decorations when possible
	// instead of re-creating them, to minimize flicker
	protected void reloadDecorations()
	{
		CList<PathElement> hs = new CList<>();
		CList<PathElement> cs = new CList<>();
		
		for(SelectionSegment s: segments)
		{
			TextPos start = s.getStart();
			TextPos end = s.getEnd();
			
			hs.addAll(createHighlightPath(start, end));
			cs.addAll(createCaretPath(end));
		}
		
		highlight.getElements().setAll(hs);
		caret.getElements().setAll(cs);
	}
	
	
	protected double right()
	{
		// FIX padding
		return editor.getWidth();
	}
	
	
	protected double left()
	{
		// FIX padding
		return 0.0;
	}
	
	
	protected boolean isNearlySame(double a, double b)
	{
		// in case for some reason floating point computation result is slightly off
		return Math.abs(a - b) < 0.01;
	}
	
	
	protected CList<PathElement> createCaretPath(TextPos p)
	{
		CList<PathElement> rv = new CList<>();
		CaretLocation c = editor.getCaretLocation(p);
		if(c != null)
		{
			// TODO insert shape?
			rv.add(new MoveTo(c.x0, c.y0));
			rv.add(new LineTo(c.x0, c.y1));
		}
		return rv;
	}
	
	
	protected CList<PathElement> createHighlightPath(TextPos start, TextPos end)
	{		
		CList<PathElement> rv = new CList<>();
		
		if((start == null) || (end == null))
		{
			return rv;
		}
		
		if(start.compareTo(end) > 0)
		{
			TextPos tmp = start;
			start = end;
			end = tmp;
		}

		CaretLocation top = editor.getCaretLocation(start);
		CaretLocation bot = editor.getCaretLocation(end);
		
		if((top == null) || (bot == null))
		{
			return rv;
		}
		
		rv.add(new MoveTo(top.x0, top.y0));
		if(isNearlySame(top.y0, bot.y0))
		{
			rv.add(new LineTo(bot.x0, top.y0));
			rv.add(new LineTo(bot.x0, bot.y1));
			rv.add(new LineTo(top.x0, bot.y1));
			//rv.add(new ClosePath());
			rv.add(new LineTo(top.x0, top.y0));
		}
		else
		{
			rv.add(new LineTo(right(), top.y0));
			rv.add(new LineTo(right(), bot.y0));
			rv.add(new LineTo(bot.x0, bot.y0));
			rv.add(new LineTo(bot.x0, bot.y1));
			rv.add(new LineTo(left(), bot.y1));
			rv.add(new LineTo(left(), top.y1));
			rv.add(new LineTo(top.x0, top.y1));
			//rv.add(new ClosePath());
			rv.add(new LineTo(top.x0, top.y0));
		}
		
		return rv;
	}
	
	
	public String toString()
	{
		return "Selection:" + segments.toString();
	}
}
