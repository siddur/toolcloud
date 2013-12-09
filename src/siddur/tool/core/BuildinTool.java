package siddur.tool.core;


import java.io.File;

import siddur.common.util.ScriptUtil;
import siddur.tool.core.data.ToolDescriptor;

public class BuildinTool extends CommandTool{

	@Override
	protected String getCommand(ToolDescriptor td, File scriptFile,
			String[] params) throws Exception {
		boolean isBuildin =  scriptFile.length() == 0; //such as ls, cp
		String orderName = null;
		if(isBuildin){
			orderName = scriptFile.getName();
		}else{
			orderName = scriptFile.getCanonicalPath();
		}
		return ScriptUtil.getSystemExecuteString(orderName, 
				td.getOverrodeParam(), params, td.getInputModel());
	}
	
	
}
