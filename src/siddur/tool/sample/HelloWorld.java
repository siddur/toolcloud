package siddur.tool.sample;

import siddur.tool.core.ITool;


public class HelloWorld implements ITool{

	private static final String ID = "TOOL1";

	@Override
	public String[] execute(String[] inputs) {
		return inputs;
	}

	@Override
	public void init() {
		System.out.println(ID + " initiated.");
		
	}

	@Override
	public void destroy() {
		System.out.println(ID + " destroyed.");
		
	}


}
