package siddur.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class WebPointUtil {
	
	public static void parse(String path, Object obj, 
			WebPointFilter filter, WebPointHandler handler){
		
		Method[] methods = obj.getClass().getDeclaredMethods();
		for(Method m : methods){
			if((m.getModifiers() & Modifier.PUBLIC) == 1){
				if(filter.filter(m)){
					String methodName = m.getName().toLowerCase(); 
					/*
					 *  /path/methodName
					 */
					String mpath = path + "/" + methodName;
					handler.handle(m, obj, mpath);
				}
			}
		}
		
	}
	
	public static abstract class WebPointHandler{
		public abstract void handle(Method method, Object instance, String path);
	}
	
	public static abstract class WebPointFilter{
		public abstract boolean filter(Method m);
	}
}
