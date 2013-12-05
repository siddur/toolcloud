package siddur.tool.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Enumeration;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipUtil {

	public static File zipDir(File dir){
		File zipFile = new File(dir.getAbsoluteFile() + ".zip");
		
		Project prj = new Project();  
        Zip zip = new Zip();  
        zip.setProject(prj);  
        zip.setDestFile(zipFile);  
        FileSet fileSet = new FileSet();  
        fileSet.setProject(prj);  
        fileSet.setDir(dir);  
        zip.addFileset(fileSet);
        
        zip.execute(); 
        
        return zipFile;
	}
	
	public static File unZip(File zipFile) throws IOException{
		File dir = makeDir(zipFile);
		
		ZipFile zip = new ZipFile(zipFile);
		@SuppressWarnings("rawtypes")
		Enumeration en = zip.getEntries();
		
		ByteBuffer bb = ByteBuffer.allocate(8192);
		while(en.hasMoreElements()){
			ZipEntry entry = (ZipEntry)en.nextElement();
			String name = entry.getName();
			System.out.println(name);
			File f = new File(dir, name);
			if(entry.isDirectory()){
				f.mkdirs();
			}else{
				f.getParentFile().mkdirs();
				InputStream is = zip.getInputStream(entry);
				ReadableByteChannel r = Channels.newChannel(is);
				WritableByteChannel w = new FileOutputStream(f).getChannel();
				while(r.read(bb) != -1){
					bb.flip();
					w.write(bb);
					bb.clear();
				}
				r.close();
				w.close();
			}
		}
		zip.close();
		File[] files = dir.listFiles();
		if(files.length == 1){
			File f = files[0];
			if(f.isDirectory() 
					&& (f.getName().equals(dir.getName()) 
					|| dir.getName().endsWith(f.getName()))){
				FileUtils.copyDirectory(f, dir);
				FileUtils.deleteDirectory(f);
			}
		}
		return dir;
	}
	
	public static File makeDir(File zipFile){
		String filename = zipFile.getName();
		int dot = filename.lastIndexOf(".");
		if(dot > 0){
			filename = filename.substring(0, dot);
		}
		File dir = new File(zipFile.getParent(), filename);
		dir.mkdir();
		return dir;
	}
	
	public static void main(String[] args) throws IOException {
//		zipDir(new File("d:/test/目录"));
		unZip(new File("d:/test/目录.zip"));
	}
}
