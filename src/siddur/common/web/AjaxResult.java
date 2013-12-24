package siddur.common.web;

public class AjaxResult extends Result{
	
	Object data;
	
	public AjaxResult(Object data, ResultType type) {
		this.data = data;
		this.type = type;
	}

}
