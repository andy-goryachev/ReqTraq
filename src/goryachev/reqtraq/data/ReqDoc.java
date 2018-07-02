// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.util.Assert;
import goryachev.common.util.BKey;
import goryachev.common.util.CList;
import goryachev.reqtraq.Page;
import goryachev.reqtraq.util.Tools;
import java.util.List;
import javafx.scene.control.TreeItem;


/**
 * Requirements Document.
 */
public class ReqDoc
{
	private BKey id;
	private CList<Page> pages;
	
	
	public ReqDoc()
	{
		id = GUID.create();
		pages = new CList<>();
	}
	
	
	public ReqDoc(BKey id, CList<Page> pages)
	{
		Assert.notNull(id, "id");
		
		this.id = id;
		this.pages = pages;
	}

	
	public BKey getID()
	{
		return id;
	}
	
	
	public List<Page> getPages()
	{
		return pages;
	}
	

	/** sets pages updating their nesting level */
	public void setTreeRoot(ReqDoc d)
	{
		CList<Page> ps = new CList<>();
		collectPages(ps, d.getTreeRoot(), -1);
		this.pages = ps;
	}

	
	/** reconstructs the tree from a list of pages based on the nesting level property */
	public TreeItem<Page> getTreeRoot()
	{
		CList<TreeItem<Page>> stack = new CList<>();
		
		TreeItem<Page> root = new TreeItem<Page>(null);
		stack.add(root);
		
		for(Page p: pages)
		{
			int cur = stack.size() - 1;
			int lev = p.getNestingLevel() + 1;
			if(lev < 0)
			{
				// safety
				lev = 0;
			}
			
			if(lev >= (cur + 1))
			{
				// a child
				TreeItem<Page> ch = Tools.addPage(stack.get(cur), p);
				stack.add(ch);
			}
			else if(lev == cur)
			{
				// a sibling
				TreeItem<Page> ch = Tools.addPage(stack.get(cur - 1), p);
				stack.set(lev, ch);
			}
			else
			{
				// a parent's sibling
				TreeItem<Page> ch = Tools.addPage(stack.get(lev - 1), p);
				stack.set(lev, ch);
				for(int i=cur; i>lev; --i)
				{
					stack.remove(i);
				}
			}
		}
		return root;
	}


	protected void collectPages(CList<Page> ps, TreeItem<Page> item, int level)
	{
		Page p = item.getValue();
		if(p != null)
		{
			p.setNestingLevel(level);
			ps.add(p);
		}
		
		level++;
		
		for(TreeItem<Page> ch: item.getChildren())
		{
			collectPages(ps, ch, level);
		}
	}
}
