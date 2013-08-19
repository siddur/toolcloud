package siddur.tool.sample;

import siddur.tool.core.ConsoleTool;



public class UnixCmdTest extends ConsoleTool{

	@Override
	public String[] execute(String[] inputs) throws Exception {
		for (String string : inputs) {
			log("参数：" + string);
		}
		
		return null;
	}

}
