package siddur.tool.fileprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Map;
import java.util.Vector;

import jlibdiff.Diff;
import jlibdiff.Hunk;
import jlibdiff.HunkAdd;
import jlibdiff.HunkChange;
import jlibdiff.HunkDel;
import siddur.tool.core.HtmlResponseTool;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;

public class FileDiff extends HtmlResponseTool{

	@Override
	protected void execute(String[] inputs, IToolWrapper toolWrapper,
			File workspace, Writer out, Map<String, Object> context)
			throws Exception {
		String file1 = TempFileUtil.findFile(inputs[0]).getCanonicalPath();
		String file2 = TempFileUtil.findFile(inputs[1]).getCanonicalPath();
		Diff diff = new Diff();
		diff.diffFile(file1, file2);
		@SuppressWarnings("unchecked")
		Vector<Hunk> hunks = diff.getHunks();
		StringBuilder sb = new StringBuilder();
		append(sb, file1, hunks, 0);
		append(sb, file2, hunks, 1);
		
		buildHtml(sb.toString(), out);
	}
	
	
	private static int append(StringBuilder sb, String f, Vector<Hunk> hunks, int index) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String line = null;
		int lineNo = 0;
		int hunkIndex = 0;
		boolean conver = false;
		boolean left = index == 0;
		sb.append("<div class='file'><table cellpadding='0' cellspacing='0'>");
		while((line = br.readLine()) != null){
			line = line.replace("<", "&lt;");
			line = line.replace(">", "&gt;");
			lineNo ++;
			sb.append("<tr>");
			if(lineNo == 1){
				sb.append("<td class='lineNo' width='20'>"+lineNo+"</td>");
			}else{
				sb.append("<td class='lineNo'>"+lineNo+"</td>");
			}
			if(hunkIndex < hunks.size()){
				Hunk hunk = hunks.get(hunkIndex);
				if(hunk.lowLine(index) == lineNo || hunk.highLine(index) == lineNo){
					String claz = "";
					if(hunk instanceof HunkChange || 
							(hunk instanceof HunkAdd && !left) ||
							(hunk instanceof HunkDel && left)){
						claz += "diff";
						if(hunk.lowLine(index) == lineNo){
							conver = true;
							claz += " diff_start";
						}
					}
					if(hunk.highLine(index) == lineNo){
						conver = false;
						claz += " diff_end";
						hunkIndex++;
					}
					sb.append("<td class='"+claz+"'>");
				}
				else if(conver){
					sb.append("<td class='diff'>");
				}
				else{
					sb.append("<td>");
				}
				
			}else{
				sb.append("<td>");
			}
			sb.append("<pre>" + line + "</pre>");
			sb.append("</td>");
			sb.append("</tr>");
			
			
		}
		sb.append("</table></div>");
		return lineNo;
	}


	
	public static void main(String[] args) throws Exception {
		FileDiff f = new FileDiff();
		String[] inputs = new String[]{"temp\\common1.txt", "temp\\common2.txt"};
		f.execute(inputs, null, null);
	}
}
