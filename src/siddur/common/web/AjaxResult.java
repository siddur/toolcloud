package siddur.common.web;

public class AjaxResult extends Result{
	
	Object message;
	
	public AjaxResult(Object message, ResultType type) {
		this.message = message;
		this.type = type;
	}

}
