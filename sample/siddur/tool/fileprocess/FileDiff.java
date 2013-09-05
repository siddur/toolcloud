package siddur.tool.fileprocess;

import java.util.Vector;

import jlibdiff.Diff;
import jlibdiff.Hunk;
import siddur.tool.core.ITool;
import siddur.tool.core.TempFileUtil;

public class FileDiff implements ITool{

	@Override
	public String[] execute(String[] inputs) throws Exception {
		String file1 = TempFileUtil.findFile(inputs[0]).getCanonicalPath();
		String file2 = TempFileUtil.findFile(inputs[1]).getCanonicalPath();
		Diff diff = new Diff();
		diff.diffFile(file1, file2);
		@SuppressWarnings("unchecked")
		Vector<Hunk> hunks = diff.getHunks();
		StringBuilder sb = new StringBuilder();
		for (Hunk hunk : hunks) {
			sb.append(hunk.convert());
		}
		return new String[]{sb.toString()};
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws Exception {
		FileDiff f = new FileDiff();
		String[] inputs = new String[]{"temp\\common1.txt", "temp\\common2.txt"};
		System.out.println(f.execute(inputs)[0]);
	}

}
