package siddur.tool.core;


public interface ITool {
	
	
	String[] execute(String[] inputs) throws Exception;
	
	void init();
	
	void destroy();
	
}
