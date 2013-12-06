package siddur.tool.core;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

import siddur.common.util.ToolUtil;
import siddur.tool.core.data.DataTemplate;
import siddur.tool.core.data.ToolDescriptor;

public abstract class CommandTool extends ConsoleTool{
	private static final Logger log4j = Logger.getLogger(CommandTool.class);

	protected abstract String getCommand(ToolDescriptor td, File scriptFile,
			String[] params) throws Exception;

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		ToolDescriptor td = toolWrapper.getDescriptor();
		//copy script file from tool dir to temp dir.
		File wp = ToolUtil.buildWorkspace(toolWrapper);
		File exeFile = new File(toolWrapper.getToolfile());
		String s = getCommand(td, exeFile, inputs);
		log4j.info(s);
		CommandLine cl = CommandLine.parse(s);
		
		// executing time < 10min
		ExecuteWatchdog watchdog = new ExecuteWatchdog(30 * 1000);
		DefaultExecutor de = new DefaultExecutor();
		de.setWatchdog(watchdog);
		OutputStream os = getOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(os);
		de.setStreamHandler(psh);
		
		//work dir
		de.setWorkingDirectory(wp);
		de.execute(cl);
		
		//do close()
		os.close();
		
		List<String> outputs = new ArrayList<String>(10);

		//extract file from outputModel
		if(td.getOutputModel() != null){
			for(DataTemplate dt : td.getOutputModel()){
				//must be file
				String v = dt.getDefaultValue();
				if(!v.equals("")){
					v = ToolUtil.overrideParam(v, inputs);
					if(v.contains("*")){
						outputs.add(ToolUtil.gatherFuzzyFiles(v, wp).getCanonicalPath());
					}else{
						outputs.add(ToolUtil.findFile(v, wp).getCanonicalPath());
					}
					
				}
			}
		}
		
		return outputs.toArray(new String[outputs.size()]);
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
