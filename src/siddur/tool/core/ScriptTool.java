package siddur.tool.core;

import java.io.File;

import siddur.tool.core.data.ToolDescriptor;


public class ScriptTool extends CommandTool{
	
	protected String getCommand(ToolDescriptor td, File scriptFile,
			String[] params) throws Exception {
		return ScriptUtil.getScriptExecuteString(td.getLang(), 
				scriptFile.getCanonicalPath(), td.getOverrodeParam(), params, td.getInputModel());
	}
	
}