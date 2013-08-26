package siddur.tool.core;

import java.io.File;

import siddur.tool.core.data.DataTemplate;


public class ScriptTool extends CommandTool{
	
	protected String getCommand(String scriptName, File scriptFile, 
			String[] params, DataTemplate[] tooldatas) throws Exception{
		return ScriptUtil.getExecuteString(language, scriptFile.getCanonicalPath(), params, inputModel);
	}
	
}