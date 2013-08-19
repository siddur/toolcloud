package siddur.tool.core.exception;

public class ToolChainEmptyException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private static final String msg = "This ToolInstanceChain has none of ToolInstance";

	public ToolChainEmptyException() {
		super(msg);
	}

	
}
