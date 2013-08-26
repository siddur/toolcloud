package siddur.tool.core;


import java.io.File;

import siddur.tool.core.data.DataTemplate;

public class BuildinTool extends CommandTool{

	@Override
	protected String getCommand(String scriptName, File scriptFile,
			String[] params, DataTemplate[] tooldatas) throws Exception {
		String orderName = scriptFile.getName();
		return ScriptUtil.getExecuteString(orderName, params, tooldatas);
	}
	
	
}
