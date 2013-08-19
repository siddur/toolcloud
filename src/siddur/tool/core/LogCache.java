package siddur.tool.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class LogCache {
	private static Logger log4j = Logger.getLogger(LogCache.class);
	private static Map<String, LogCache> pool = new HashMap<String, LogCache>();
	private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
	
	private List<LogFilter> filters = new ArrayList<LogFilter>();
	
	private List<String> logs = new ArrayList<String>();
	private Lock lock = new ReentrantLock();
	private Condition notEmpty = lock.newCondition();
	private Condition needCache = lock.newCondition();
	
	private boolean active = true;
	private boolean toClose = false;
	
	private LogCache(){}
	
	public static LogCache newInstance(String ticket){
		LogCache l = new LogCache();
		pool.put(ticket, l);
		return l;
	}
	
	public static LogCache getLogCache(String ticket){
		return pool.get(ticket);
	}
	
	public static void dispose(String ticket){
		final LogCache lc = pool.get(ticket);
		if(lc != null){
			lc.ready2Close();
			
			executor.schedule(new Runnable() {
				@Override
				public void run() {
					lc.close();
					//clear log cache
					pool.remove(lc);
				}
			}, 8, TimeUnit.SECONDS);
		}
	}
	
	public void addFilter(LogFilter filter){
		filters.add(filter);
	}
	

	public void close(){
		active = false;
		//flush fetching
		lock.lock();
		try {
			notEmpty.signal();
			needCache.signal();
		} finally{
			lock.unlock();
		}
	}
	
	public void log(String msg){
		lock.lock();
		try {
			System.out.println(msg);
			logs.add(msg);
			notEmpty.signal();
		} finally{
			lock.unlock();
		}
	}
	
	public void log(BufferedReader reader) throws IOException{
		String line = null;
		while((line = reader.readLine()) != null){
			log(line);
		}
	}
	
	public List<String> getLogs(){
		return accessLogs();
	}
	
	public void ready2Close(){
		toClose = true;
	}
	
	public OutputStream getOutputStream(){
		return new OutputStream() {
			private final int N = (int)'\n';
			private final int R = (int)'\r';
			private final byte[] buffer = new byte[1024];
			private int index = -1;
			
			private boolean foundN = false;
			@Override
			public void write(int b) throws IOException {
				if(index == 1024){
					return;
				}
				
				buffer[++ index] = (byte)b;
				
				if(!foundN && b == N){
					foundN = true;
					
					index --;
					while(index > -1 && buffer[index] == R){
						index --;
					}
					
					log(getLine());
					
					reset();
				}
			}
			
			private void reset(){
				foundN = false;
				index = -1;
			}
			
			private String getLine(){
				return new String(buffer, 0, index + 1);
			}
			
			@Override
			public void close() throws IOException {
				log(getLine());
//				toClose = true;
			}
			
		};
	}
	
	//access log cache every 200ms
	private List<String> accessLogs(){
		List<String> flip = new ArrayList<String>();
		if(!active){
			return flip;
		}
		lock.lock();
		try {
			while(logs.isEmpty() && !toClose){
				notEmpty.await();
			}
			flip.addAll(filter(logs));
			logs.clear();
			
			if(!toClose){
				needCache.await(200, TimeUnit.MILLISECONDS);
			}
		} catch (Exception e) {
			log4j.warn("", e);
			return flip;
		}finally{
			lock.unlock();
		}
		return flip;
	}
	
	
	private List<String> filter(List<String> logs){
		List<String> newLogs = new ArrayList<String>(logs.size());
		for(String log : logs){
			String newLog = log;
			for(LogFilter f : filters){
				newLog = f.doFilter(newLog);
			}
			newLogs.add(newLog);
		}
		return newLogs;
	}
	
}
