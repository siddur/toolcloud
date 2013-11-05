package siddur.tool.core;

import java.util.Map;


public interface ITool {
	
	
	String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception;
	
	void init();
	
	void destroy();
	
}
