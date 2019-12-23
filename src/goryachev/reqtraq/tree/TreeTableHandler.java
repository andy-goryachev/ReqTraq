// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.tree;
import goryachev.common.util.D;
import goryachev.fx.FX;
import goryachev.fx.table.FxTreeTable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Duration;


/**
 * Page Tree Table DND Handler.
 */
public class TreeTableHandler<T>
{
	private static final DataFormat JAVA_DATA_FORMAT = new DataFormat("application/x-java-serialized-object");
	protected final FxTreeTable<T> tree;
	private Timeline scrollTimeline;
	private double scrollDirection;

	
	public TreeTableHandler(FxTreeTable<T> tree)
	{
		this.tree = tree;
		
		tree.tree.setRowFactory(this::rowFactory);
		// mouse handlers
		tree.tree.addEventHandler(DragEvent.DRAG_EXITED, (ev) -> handleTreeDragExited(ev));
		tree.tree.addEventHandler(DragEvent.DRAG_ENTERED, (ev) -> handleTreeDragEntered(ev));
		tree.tree.addEventHandler(DragEvent.DRAG_DONE, (ev) -> handleTreeDragDone(ev));
		// key handler
		tree.tree.addEventFilter(KeyEvent.ANY, (ev) -> handleTreeKeyEvent(ev));
	}
	

	protected TreeTableRow<T> rowFactory(TreeTableView<T> view)
	{
		TreeTableRow<T> r = new TreeTableRow<T>();
		r.addEventHandler(MouseEvent.DRAG_DETECTED, (ev) -> handleRowDragDetected(r, ev));
		r.addEventHandler(DragEvent.DRAG_OVER, (ev) -> handleRowDragOver(r, ev));
		r.addEventHandler(DragEvent.DRAG_DROPPED, (ev) -> handleRowDragDropped(r, ev));
		return r;
	}
	

	protected void handleTreeDragEntered(DragEvent ev)
	{
		scrollTimeLine().stop();
	}
	
	
	protected void handleTreeDragExited(DragEvent ev)
	{
		if(ev.getY() > 0)
		{
			scrollDirection = 1.0 / tree.getExpandedItemCount();
		}
		else
		{
			scrollDirection = -1.0 / tree.getExpandedItemCount();
		}
		scrollTimeLine().play();
	}
	
	
	protected void handleTreeDragDone(DragEvent ev)
	{
		scrollTimeLine().stop();
	}
	
	
	protected Timeline scrollTimeLine()
	{
		if(scrollTimeline == null)
		{
			scrollTimeline = new Timeline();
			scrollTimeline.setCycleCount(Timeline.INDEFINITE);
			scrollTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(25), "scrolling animation", ev ->
			{
				dragScroll();
			}));
		}
		return scrollTimeline;
	}
	

	protected boolean isAcceptable(Dragboard db, TreeTableRow<T> row)
	{
		if(db.hasContent(JAVA_DATA_FORMAT))
		{
			int index = (Integer)db.getContent(JAVA_DATA_FORMAT);
			if(row.getIndex() != index)
			{
				TreeItem target = getTarget(row);
				TreeItem item = tree.getTreeItem(index);
				return !isParent(item, target);
			}
		}
		return false;
	}


	protected TreeItem getTarget(TreeTableRow<T> row)
	{
		TreeItem target = tree.getRoot();
		if(!row.isEmpty())
		{
			target = row.getTreeItem();
		}
		return target;
	}


	public static boolean isParent(TreeItem<?> parent, TreeItem<?> child)
	{
		// FIX rewrite
		boolean rv = false;
		while(!rv && (child != null))
		{
			rv = (child.getParent() == parent);
			child = child.getParent();
		}
		return rv;
	}


	public static ScrollBar getVerticalScrollbar(Node parent)
	{
		for(Node n: parent.lookupAll(".scroll-bar"))
		{
			if(n instanceof ScrollBar)
			{
				ScrollBar bar = (ScrollBar)n;
				if(bar.getOrientation().equals(Orientation.VERTICAL))
				{
					return bar;
				}
			}
		}
		return null;
	}


	protected void dragScroll()
	{
		ScrollBar b = getVerticalScrollbar(tree);
		if(b != null)
		{
			double d = FX.clip(b.getValue() + scrollDirection, 0, 1);
			b.setValue(d);
		}
	}

	
	protected void handleRowDragDetected(TreeTableRow<T> r, MouseEvent ev)
	{
		if(!r.isEmpty())
		{
			// TODO support multiple selection
			Dragboard db = r.startDragAndDrop(TransferMode.MOVE);
			db.setDragView(r.snapshot(null, null));
			ClipboardContent cc = new ClipboardContent();
			cc.put(JAVA_DATA_FORMAT, r.getIndex());
			db.setContent(cc);
			ev.consume();
		}
	}
	
	
	protected void handleRowDragOver(TreeTableRow<T> r, DragEvent ev)
	{
		Dragboard db = ev.getDragboard();
		if(isAcceptable(db, r))
		{
			ev.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			ev.consume();
		}
	}
	
	
	protected void handleRowDragDropped(TreeTableRow<T> r, DragEvent ev)
	{
		Dragboard db = ev.getDragboard();
		if(isAcceptable(db, r))
		{
			int ix = (Integer)db.getContent(JAVA_DATA_FORMAT);
			TreeItem item = tree.getTreeItem(ix);
			item.getParent().getChildren().remove(item);
			getTarget(r).getChildren().add(item);
			ev.setDropCompleted(true);
			
			tree.getSelectionModel().clearSelection();
			tree.getSelectionModel().select(item);
			// FIX focus and selection differ after this
			
			ev.consume();
		}
	}


	protected void handleTreeKeyEvent(KeyEvent ev)
	{
		D.print(ev);
	}
}
