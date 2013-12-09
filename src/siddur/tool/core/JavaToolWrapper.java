package siddur.tool.core;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;


public class JavaToolWrapper extends ToolWrapper{
	private static Logger log4j = Logger.getLogger(JavaToolWrapper.class);
	
	private boolean cacheClass = false;
	private boolean singleton = false;
	private WeakReference<Class<?>> toolClassRef;
	private String classname;
	private ITool tool;
	private boolean firstLoad = true;
	
	
	public Class<?> getToolClass() {
		Class<?> clz = null;
		try {
			if(cacheClass && toolClassRef != null){
				clz = toolClassRef.get();
			}
			if(clz == null){
				clz = getToolClass(this);
			}else{
				log4j.info("Load class from cache");
			}
		} catch (Exception e) {
			log4j.error("", e);
		}
		
		return clz;
	}
	
	private void cacheClass(Class<?> clz){
		toolClassRef = new WeakReference<Class<?>>(clz);
	}
	
	
	@Override
	public ITool getTool() {
		if(singleton && tool != null){
			return tool;
		}
		
		Class<?> clz = getToolClass();
		
		ITool t = null;
		try {
			t = (ITool) clz.newInstance();
			t.init();
		} catch (Exception e) {
			log4j.error("", e);
		} 
		
		if(firstLoad){
			firstLoad = false;
			awareTool(t, clz);
		}
		
		return t;
	}

	//invoked only once; 
	private void awareTool(ITool t, Class<?> clz){
		if(t instanceof ISelfAware){
			ISelfAware selfAware = (ISelfAware) t;
			singleton = selfAware.storeMyInstance();
			cacheClass = selfAware.cacheMyClass();
			
			if(singleton){
				tool = t;
			}
			else if(cacheClass){
				cacheClass(clz);
			}
		}
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	/*
	 * Load tool class by classname
	 */
	private static Class<?> getToolClass(JavaToolWrapper jtw) throws Exception{
		File jarFile = new File(jtw.getToolfile());
		File parent = jarFile.getParentFile();
		File[] jars = parent.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".jar")){
					return true;
				}
				return false;
			}
		});
		URL[] urls = new URL[jars.length];
		for (int i = 0; i < urls.length; i++) {
			urls[i] = jars[i].toURI().toURL();
		}
		URLClassLoader rcl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
		Class<?> claz = rcl.loadClass(jtw.classname);
		return claz;
	}
}
