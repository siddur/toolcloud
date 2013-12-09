package siddur.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.common.web.Result;

public class WebPointUtil {
	
	public static void parse(String parentPath, Object obj, 
			WebPointFilter filter, WebPointHandler handler){
		
		Method[] methods = obj.getClass().getDeclaredMethods();
		for(Method m : methods){
			if((m.getModifiers() & Modifier.PUBLIC) == 1){
				if(filter == null || filter.filter(m)){
					String methodName = m.getName().toLowerCase(); 
					/*
					 *  /path/methodName
					 */
					String mpath = parentPath + "/" + methodName;
					if(handler != null)
						handler.handle(m, obj, mpath);
				}
			}
		}
		
	}
	
	public static interface WebPointHandler{
		public abstract void handle(Method method, Object instance, String path);
	}
	
	public static interface WebPointFilter{
		public abstract boolean filter(Method m);
	}
	
	public static class DefaultWebPointFilter implements WebPointFilter{
			
		@Override
		public boolean filter(Method m) {
			Class<?>[] c = m.getParameterTypes();
			return Result.class.isAssignableFrom(m.getReturnType())
					&& c.length == 2 
					&& HttpServletRequest.class.isAssignableFrom(c[0]) 
					&& HttpServletResponse.class.isAssignableFrom(c[1]);
		}
		
	}
}
