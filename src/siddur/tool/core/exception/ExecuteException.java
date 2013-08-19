package siddur.tool.core.exception;

public class ExecuteException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private static final String msg = "Error occurs when execute the plugin: ";

	public ExecuteException(String pluginID) {
		super(msg + pluginID);
	}

	
}
