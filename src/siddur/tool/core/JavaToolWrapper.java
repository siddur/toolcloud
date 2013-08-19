package siddur.tool.core;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;


public class JavaToolWrapper extends ToolWrapper{
	private static Logger log4j = Logger.getLogger(JavaToolWrapper.class);
	
	private WeakReference<Class<?>> toolClassRef;
	private String classname;
	
	public Class<?> getToolClass() {
		Class<?> clz = null;
		try {
			if(toolClassRef != null){
				clz = toolClassRef.get();
			}
			if(clz == null){
				clz = getToolClass(this);
			}
			setToolClass(clz);
		} catch (Exception e) {
			log4j.error("", e);
		}
		
		return clz;
	}
	
	private void setToolClass(Class<?> clz){
		toolClassRef = new WeakReference<Class<?>>(clz);
	}
	
	
	@Override
	public ITool getTool() {
		Class<?> clz = null;
		if(toolClassRef != null){
			clz = toolClassRef.get();
		}
		if(clz == null){
			clz = getToolClass();
		}
		try {
			return (ITool) clz.newInstance();
		} catch (Exception e) {
			log4j.error("", e);
		} 
		return null;
	}


	public void setClassname(String classname) {
		this.classname = classname;
	}

	
	private static Class<?> getToolClass(JavaToolWrapper jtw) throws Exception{
		URL[] urls = new URL[1];
		urls[0] = new File(jtw.getToolfile()).toURI().toURL();
		URLClassLoader rcl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
		Class<?> claz = rcl.loadClass(jtw.classname);
		return claz;
	}
}
