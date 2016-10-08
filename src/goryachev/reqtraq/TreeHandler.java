// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq;
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
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Duration;


/**
 * Tree DND Handler.
 */
public class TreeHandler<T>
{
	private static final DataFormat JAVA_DATA_FORMAT = new DataFormat("application/x-java-serialized-object");
	protected final TreeTableView<T> tree;
	private Timeline scrollTimeline;
	private double scrollDirection;

	
	public TreeHandler(TreeTableView<T> tree)
	{
		this.tree = tree;
		
		tree.setRowFactory(this::rowFactory);
		
		tree.setOnDragExited(ev ->
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
		});
		
		tree.setOnDragEntered(ev ->
		{
			scrollTimeLine().stop();
		});
		
		tree.setOnDragDone(ev ->
		{
			scrollTimeLine().stop();
		});
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
			double d = b.getValue() + scrollDirection;
			d = Math.min(d, 1.0);
			d = Math.max(d, 0.0);
			b.setValue(d);
		}
	}


	protected TreeTableRow<T> rowFactory(TreeTableView<T> view)
	{
		TreeTableRow<T> row = new TreeTableRow<T>();
		
		row.setOnDragDetected(ev ->
		{
			if(!row.isEmpty())
			{
				Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
				db.setDragView(row.snapshot(null, null));
				ClipboardContent cc = new ClipboardContent();
				cc.put(JAVA_DATA_FORMAT, row.getIndex());
				db.setContent(cc);
				ev.consume();
			}
		});

		row.setOnDragOver(ev ->
		{
			Dragboard db = ev.getDragboard();
			if(isAcceptable(db, row))
			{
				ev.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				ev.consume();
			}
		});

		row.setOnDragDropped(ev ->
		{
			Dragboard db = ev.getDragboard();
			if(isAcceptable(db, row))
			{
				int ix = (Integer)db.getContent(JAVA_DATA_FORMAT);
				TreeItem item = tree.getTreeItem(ix);
				item.getParent().getChildren().remove(item);
				getTarget(row).getChildren().add(item);
				ev.setDropCompleted(true);
				tree.getSelectionModel().select(item);
				ev.consume();
			}
		});

		return row;
	}
}
