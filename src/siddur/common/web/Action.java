package siddur.common.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public abstract class Action {
	private Map<String, Object> context;
	
	public void init(Map<String, Object> context){
		this.context = context;
	};
	
	public String getPath(){
		String s = this.getClass().getSimpleName().toLowerCase();
		if(s.endsWith("action")){
			s = s.substring(0, s.length() - "action".length());
		}
		return s.toLowerCase();
	}
	
	public static void HOME(HttpServletRequest req){
		req.getRequestDispatcher("/index.jsp");
	}
	
	protected Object getContextItem(String key){
		return context.get(key);
	}
	
}
