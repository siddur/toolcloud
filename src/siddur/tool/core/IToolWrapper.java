package siddur.tool.core;

import siddur.tool.core.data.ToolDescriptor;

public interface IToolWrapper extends Comparable<IToolWrapper>{

	public ToolDescriptor getDescriptor();
	public void setDescriptor(ToolDescriptor descriptor);

	public ITool getTool();
	public Class<?> getToolClass();
	
	public String getToolfile();
	public void setToolfile(String toolfile);

	public int getStatus();
	public void setStatus(int status);
	
}
