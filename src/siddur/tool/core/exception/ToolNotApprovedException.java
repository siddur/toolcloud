package siddur.tool.core.exception;

public class ToolNotApprovedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private static final String msg = "This tool has not been approved: ";
	
	public ToolNotApprovedException(String pluginID){
		super(msg + pluginID);
	}
}
