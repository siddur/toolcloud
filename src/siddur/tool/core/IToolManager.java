package siddur.tool.core;

import java.io.File;
import java.util.Map;
import siddur.tool.core.data.ToolDescriptor;

public interface IToolManager{
	/*
	 * Load all tools from db or fs into memory.
	 */
	void loadAll();
	
	/*
	 * Load a tool from DB/FS into memory.
	 */
	IToolWrapper load(String toolID);
	
	/*
	 * Remove the tool from memory.
	 */
	void unload(String toolID);
	
	/*
	 * Remove the tool from memory and DB/FS
	 */
	void delete(String toolID);
	
	/*
	 * Save a tool to DB/FS.
	 */
	void save(ToolDescriptor pd, File toolFile); 
	
	/*
	 * Update pd
	 */
	void save(ToolDescriptor pd);
	
	
	String[] run(String toolID, String[]params,  Map<String, Object> context);
	
	void approve(String pluginID);
			
	void init();
	
	void destroy();
	
	int numOfTools();
	
	IToolWrapper getToolWrapper(String toolID);
	
	void accept(MapVisitor<String, IToolWrapper> visitor);
}
