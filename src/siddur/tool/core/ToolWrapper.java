package siddur.tool.core;

import siddur.tool.core.data.ToolDescriptor;

public abstract class ToolWrapper implements IToolWrapper{

	private ToolDescriptor descriptor;
	private String toolfile;
	
	//&1 approved
	private int status = 0;
	
	@Override
	public int compareTo(IToolWrapper o) {
		return o.getDescriptor().getPublishAt()
						.compareTo(this.descriptor.getPublishAt());
	}

	@Override
	public ToolDescriptor getDescriptor() {
		return this.descriptor;
	}

	@Override
	public void setDescriptor(ToolDescriptor descriptor) {
		this.descriptor = descriptor;
	}


	@Override
	public String getToolfile() {
		return toolfile;
	}

	@Override
	public void setToolfile(String toolfile) {
		this.toolfile = toolfile;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

}
