package siddur.tool.fileprocess;

import java.util.Map;
import java.util.Vector;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;
import jlibdiff.Diff;
import jlibdiff.Hunk;

public class FileDiff0 implements ITool {
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context)
		    throws Exception
		  {
		    String file1 = TempFileUtil.findFile(inputs[0]).getCanonicalPath();
		    String file2 = TempFileUtil.findFile(inputs[1]).getCanonicalPath();
		    Diff diff = new Diff();
		    diff.diffFile(file1, file2);

		    Vector<Hunk> hunks = diff.getHunks();
		    StringBuilder sb = new StringBuilder();
		    for (Hunk hunk : hunks) {
		      sb.append(hunk.convert());
		    }
		    return new String[] { sb.toString() };
		  }

		  public void init()
		  {
		  }

		  public void destroy()
		  {
		  }
		  
		  public static void main(String[] args) {
			System.out.println(ITool.class.isAssignableFrom(FileDiff0.class));
		}
}
