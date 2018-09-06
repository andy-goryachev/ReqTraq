// Copyright © 2018 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data.v2;
import goryachev.common.io.CWriter;
import goryachev.common.util.CKit;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import java.io.OutputStream;


/**
 * ReqTrack Writer V2.
 */
public class ReqTrackWriter
{
	private CWriter wr;


	public ReqTrackWriter()
	{
	}
	
	
	public void write(OutputStream out) throws Exception
	{
		wr = new CWriter(out);
		try
		{
			header("version");
		}
		finally
		{
			CKit.close(wr);
			wr = null;
		}
	}
	
	
	protected static boolean isIllegalSymbol(char c)
	{
		switch(c)
		{
		case '%':
			return true;
		default:
			return (c < 0x20);
		}
	}
	
	
	protected boolean hasIllegalSymbols(String s)
	{
		for(int i=0; i<s.length(); i++)
		{
			char c = s.charAt(i);
			if(isIllegalSymbol(c))
			{
				return true;
			}
		}
		return false;
	}
	
	
	protected String safe(String s)
	{
		if(hasIllegalSymbols(s))
		{
			SB sb = new SB(s.length() * 2);
			for(int i=0; i<s.length(); i++)
			{
				char c = s.charAt(i);
				if(isIllegalSymbol(c))
				{
					sb.a('%');
					sb.a(Hex.toHexChar(c >> 4));
					sb.a(Hex.toHexChar(c));
				}
				else
				{
					sb.a(c);
				}
			}
			return sb.toString();
		}
		return s;
	}
	
	
	protected void text(String s) throws Exception
	{
		wr.write(safe(s));
	}
	
	
	protected void header(String s) throws Exception
	{
		wr.write("[");
		text(s);
		wr.write("]\n");
	}
}
