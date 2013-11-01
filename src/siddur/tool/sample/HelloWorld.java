package siddur.tool.sample;

import siddur.tool.core.ITool;


public class HelloWorld implements ITool{


	@Override
	public String[] execute(String[] inputs) {
		String[] output = new String[2];
		output[0] = "Hello World";
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
