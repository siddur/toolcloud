package siddur.tool.core;


import java.io.File;

import siddur.tool.core.data.ToolDescriptor;

public class BuildinTool extends CommandTool{

	@Override
	protected String getCommand(ToolDescriptor td, File scriptFile,
			String[] params) throws Exception {
		String orderName = scriptFile.getName();
		return ScriptUtil.getSystemExecuteString(orderName, 
				td.getOverrodeParam(), params, td.getInputModel());
	}
	
	
}
