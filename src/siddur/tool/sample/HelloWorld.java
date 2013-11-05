package siddur.tool.sample;

import java.util.Map;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;


public class HelloWorld implements ITool{


	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) {
		String[] output = new String[2];
		output[0] = "Hello World世界";
		output[1] = inputs[0];
		return output;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void destroy() {
		
	}
}
