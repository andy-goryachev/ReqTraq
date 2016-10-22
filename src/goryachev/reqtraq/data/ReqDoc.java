// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.util.CList;
import goryachev.reqtraq.Page;
import java.util.List;
import javafx.scene.control.TreeItem;


/**
 * Requirements Document.
 */
public class ReqDoc
{
	private String id;
	private CList<Page> pages;
	
	
	public ReqDoc()
	{
		id = GUID.create();
		pages = new CList<>();
	}
	
	
	public ReqDoc(String id, CList<Page> pages)
	{
		this.id = id;
		this.pages = pages;
	}

	
	public String getID()
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
		collectPages(ps, d.getTreeRoot(), 0);
		this.pages = ps;
	}

	
	/** reconstructs the tree from a list of pages based on the nesting level property */
	public PageTreeItem getTreeRoot()
	{
		CList<PageTreeItem> stack = new CList<>();
		
		PageTreeItem root = new PageTreeItem(null);
		stack.add(root);
		
		for(Page p: pages)
		{
			int cur = stack.size() - 1;
			int lev = p.getNestingLevel();
			if(lev < 1)
			{
				// safety measure to prevent from adding to the root or AIOOBE
				lev = 1;
			}
			
			if(lev >= (cur + 1))
			{
				// a child
				PageTreeItem ch = stack.get(cur).addPage(p);
				stack.add(ch);
			}
			else if(lev == cur)
			{
				// a sibling
				PageTreeItem ch = stack.get(cur - 1).addPage(p);
				stack.set(lev, ch);
			}
			else
			{
				// a parent's sibling
				PageTreeItem ch = stack.get(lev - 1).addPage(p);
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
