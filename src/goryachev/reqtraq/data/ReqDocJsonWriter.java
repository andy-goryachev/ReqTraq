// Copyright (c) 2013 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;
import goryachev.common.util.CKit;
import goryachev.common.util.FileTools;
import goryachev.json.JsonEncoder;
import goryachev.reqtraq.Page;
import java.io.File;


/** Writes ReqDoc JSON file */
public class ReqDocJsonWriter
{
	private ReqDocJsonWriter()
	{
	}
	
	
	private static void writePage(Page p, JsonEncoder wr) throws Exception
	{
		wr.beginObject();
		{
			wr.write(Schema.KEY_ID, p.getID());
			wr.write(Schema.KEY_TITLE, p.getTitle());
			wr.write(Schema.KEY_TEXT, p.getText());
		}
		wr.endObject();
	}
	
	
	public static void saveJSON(ReqDoc d, File f) throws Exception
	{
		FileTools.createBackup(f);
		
		// write
		JsonEncoder wr = new JsonEncoder(f);
		try
		{
			wr.beginObject();
			{
				if(d != null)
				{
					// file attributes
					wr.write(Schema.KEY_VERSION, Schema.VERSION);
					wr.write(Schema.KEY_ID, d.getID());

					// pages
					wr.name(Schema.KEY_PAGES);
					wr.beginArray();
					{
						for(Page c: d.getPages())
						{
							writePage(c, wr);
						}
					}
					wr.endArray();
				}
			}
			wr.endObject();
		}
		finally
		{
			CKit.close(wr);
		}
	}
}
