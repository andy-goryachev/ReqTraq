// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.SB;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


/**
 * test plain text model with 2 billion rows
 */
public class TestFxPlainEditorModel
	extends FxPlainEditorModel
{
	public TestFxPlainEditorModel()
	{
	}
	
	
	public Region getDecoratedLine(int line)
	{
		TextFlow t = new TextFlow();
		String s = getSearchText(line);
		if(s != null)
		{
			Text tx = new Text(s);
			t.getChildren().add(tx); 
		}
		return t;
	}


	public LoadInfo getLoadInfo()
	{
		long t = System.currentTimeMillis();
		return new LoadInfo(1.0, getLineCount(), t, t); 
	}


	public int getLineCount()
	{
		return Integer.MAX_VALUE;
	}


	public String getSearchText(int line)
	{
		String s = String.valueOf(line);
		int sz = s.length();
		
		SB sb = new SB();
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			String w = toWord(c);
			
			if(i > 0)
			{
				sb.sp();
			}
			sb.a(w);
		}
		return sb.toString();
	}
	
	
	protected String toWord(char c)
	{
		switch(c)
		{
		case '0': return "zero";
		case '1': return "one";
		case '2': return "two";
		case '3': return "three";
		case '4': return "four";
		case '5': return "five";
		case '6': return "six";
		case '7': return "seven";
		case '8': return "eight";
		case '9': return "nine";
		default: return String.valueOf(c);
		}
	}
}
