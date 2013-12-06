package siddur.common.miscellaneous;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import siddur.common.util.FileSystemUtil;

public class TempFileCleaner {
	private static final Logger log4j = Logger.getLogger(TempFileCleaner.class);
	private static final long ONE_DAY = 1000 * 60 * 60 * 24;
	
	public void startClean(){
		Timer timer = new Timer(true);
		Calendar tonight = Calendar.getInstance();
		tonight.set(Calendar.HOUR_OF_DAY, 2);
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				scanAndClean();
			}
		}, tonight.getTime(), ONE_DAY);
	}
	
	public int scanAndClean(){
		log4j.info("Start scan and clean file.");
		int count = 0;
		long start = System.currentTimeMillis();
		File tmpDir = FileSystemUtil.getTempDir();
		for(File f : tmpDir.listFiles()){
			long lastModified = f.lastModified();
			if(start - lastModified > ONE_DAY){
				try {
					if(f.isFile()){
						f.delete();
					}else{
						FileUtils.deleteDirectory(f);
					}
					count ++;
				} catch (IOException e) {
					log4j.warn("Fail to delete file: " + f.getName());
				}
			}
		}
		
		log4j.info("Successfully delete " + count + " files(or dirs)");
		return count;
	}
}
