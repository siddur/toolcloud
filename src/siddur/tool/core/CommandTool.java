package siddur.tool.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;

import siddur.common.miscellaneous.FileSystemUtil;
import siddur.tool.core.data.DataTemplate;

public abstract class CommandTool extends ConsoleTool{
	
	protected String language;
	protected String filepath;
	protected DataTemplate[] inputModel;
	protected DataTemplate[] outputModel;
	
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	protected abstract String getCommand(String scriptName, File scriptFile, 
			String[] params, DataTemplate[] tooldatas) throws Exception;

	@Override
	public String[] execute(String[] inputs) throws Exception {
		
		//copy script file from tool dir to temp dir.
		File temp = copyTemp();
		String s = getCommand(language, temp, inputs, inputModel);
		CommandLine cl = CommandLine.parse(s);
		
		// executing time < 10min
		ExecuteWatchdog watchdog = new ExecuteWatchdog(10 * 1000);
		DefaultExecutor de = new DefaultExecutor();
		de.setWatchdog(watchdog);
		OutputStream os = getOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(os);
		de.setStreamHandler(psh);
		
		//work dir
		de.setWorkingDirectory(temp.getParentFile());
		de.execute(cl);
		
		//do close()
		os.close();
		
		List<String> outputs = new ArrayList<String>(10);

		//extract file from outputModel
		if(outputModel != null){
			for(DataTemplate td : outputModel){
				if(td.isFile()){
					String v = td.getDefaultValue();
					if(!v.equals("")){
						outputs.add(findFile(v, temp.getParent()));
					}
				}
			}
		}
		
		return outputs.toArray(new String[outputs.size()]);
	}
	
	private String findFile(String path, String namespace) throws IOException{
		File outputFile = FileSystemUtil.getOutputFileInTempDir(path, namespace);
		return outputFile.getCanonicalPath();
	}

	private File copyTemp() throws IOException{
		File src = new File(filepath);
		String parent = src.getParentFile().getName();
		File workspace = new File(FileSystemUtil.getTempDir(), parent);
		workspace.mkdir();
		File dest = new File(workspace, src.getName());
		FileUtils.copyFile(src, dest);
		return dest;
	}
	
	
	public DataTemplate[] getInputModel() {
		return inputModel;
	}

	public void setInputModel(DataTemplate[] inputModel) {
		this.inputModel = inputModel;
	}
	
	
	public DataTemplate[] getOutputModel() {
		return outputModel;
	}

	public void setOutputModel(DataTemplate[] outputModel) {
		this.outputModel = outputModel;
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
