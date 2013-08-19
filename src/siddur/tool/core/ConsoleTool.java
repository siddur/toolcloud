package siddur.tool.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public abstract class ConsoleTool implements ITool{

	private LogCache lc;
	
	public void setLogQueue(LogCache logCache) {
		this.lc = logCache;
	}
	
	public void log(String msg){
		lc.log(msg);
	}
	
	public void log(InputStream in) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		lc.log(br);
	}
	
	public void close(){
		lc.close();
	}
	
	public OutputStream getOutputStream(){
		return lc.getOutputStream();
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public void destroy() {
	}
}
