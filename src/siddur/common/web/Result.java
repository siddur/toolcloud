package siddur.common.web;

import org.apache.commons.lang3.StringUtils;

public class Result {
	
	public enum ResultType{
		ok, success, error, redirect, forward, invoke
	}
	
	public final static Result NotFound = new Result("404 not found", ResultType.error);
	public final static Result Forbidden = new Result("not permitted", ResultType.error);
	
	String message = "";
	ResultType type = ResultType.ok;
	
	public Result(){};
	
	public Result(String message) {
		this.message = message;
	}
	
	public Result(String message, ResultType type) {
		this.message = message;
		this.type = type;
	}
	

	public static Result ok(String msg){
		return new Result(msg, ResultType.ok);
	}
	
	public static Result success(String msg){
		return new Result(msg, ResultType.success);
	}
	
	public static Result error(String msg){
		if(StringUtils.isEmpty(msg)){
			msg = "操作失败";
		}
		return new Result(msg, ResultType.error);
	}
	
	public static Result redirect(String msg){
		return new Result(msg, ResultType.redirect);
	}
	
	public static Result invoke(String msg){
		return new Result(msg, ResultType.invoke);
	}
	
	public static Result ok(){
		return ok(null);
	}
	
	public static Result success(){
		return success(null);
	}
	
	public static Result error(){
		return error(null);
	}
	
	
	public static Result forward(String msg){
		return new Result(msg, ResultType.forward);
	}
	
	public static Result ajaxOK(Object msg){
		return new AjaxResult(msg, ResultType.ok);
	}
	
	public static Result ajaxError(String msg){
		return new AjaxResult(msg, ResultType.error);
	}
	
	public boolean isForward(){
		return type == ResultType.forward;
	}
	
	public boolean isRedirect(){
		return type == ResultType.redirect;
	}
	
	public boolean isInvoke(){
		return type == ResultType.invoke;
	}
	
	public boolean isOK(){
		return type == ResultType.ok;
	}
	
	public boolean isSuccessful(){
		return type == ResultType.success;
	}
	
	public boolean isError(){
		return type == ResultType.error;
	}
	
	
	public String getMessage(){
		return message;
	}
	
	public boolean isAjax(){
		return this instanceof AjaxResult;
	}
}
