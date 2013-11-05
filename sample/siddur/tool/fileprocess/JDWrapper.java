package siddur.tool.fileprocess;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.io.FileUtils;

import siddur.common.miscellaneous.FileSystemUtil;
import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;
import siddur.tool.core.ZipUtil;

public class JDWrapper implements ITool{

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		File f = TempFileUtil.findFile(inputs[0]);
		String name = f.getName();
		
		String exeName = "cmd /c jad.exe";
		String output = "src";
		
		StringBuilder sb = new StringBuilder();
		sb.append(exeName);
		sb.append(" -o -r -sjava -d" + output);
		
		
		if(name.endsWith(".jar")){
			File dir = ZipUtil.unZip(f);
			String dirPath = dir.getCanonicalPath().replace("\\", "/");
			sb.append(" " + dirPath + "/**/*.class");
		}
		else if(name.endsWith(".class")){
			sb.append(" " + f.getCanonicalPath().replace("\\", "/")); 
		}
		else{
			throw new Exception("不能识别的文件格式");
		}
		
		File workspace = buildWorkspace(toolWrapper);
		
		ExecuteWatchdog watchdog = new ExecuteWatchdog(10 * 1000);
		DefaultExecutor de = new DefaultExecutor();
		de.setWatchdog(watchdog);
		
		de.setWorkingDirectory(workspace.getCanonicalFile());
		
		CommandLine cl = CommandLine.parse(sb.toString());
		de.execute(cl);
		
		File outputFile = new File(workspace, output);
		return new String[]{TempFileUtil.file2String(outputFile)};
	}
	
	private File buildWorkspace(IToolWrapper tw) throws IOException{
		File parent = new File(tw.getToolfile()).getParentFile();
		File workspace = new File(FileSystemUtil.getTempDir(), parent.getName());
		
		FileUtils.copyDirectory(parent, workspace);
		return workspace;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

}
