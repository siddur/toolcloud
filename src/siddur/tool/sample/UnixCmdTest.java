package siddur.tool.sample;

import java.util.Map;

import siddur.tool.core.ConsoleTool;
import siddur.tool.core.IToolWrapper;



public class UnixCmdTest extends ConsoleTool{

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		for (String string : inputs) {
			log("参数：" + string);
		}
		
		return null;
	}

}
