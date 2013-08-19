package siddur.tool.core.exception;

public class ToolNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private static final String msg = "Can not find plugin with ID: ";

	public ToolNotFoundException(String pluginID) {
		super(msg + pluginID);
	}

	
}
