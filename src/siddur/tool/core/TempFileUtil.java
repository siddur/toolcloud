package siddur.tool.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import siddur.common.miscellaneous.FileSystemUtil;


public class TempFileUtil {
	private static final int BUFFER_SIZE = 8 * 1024;
	
	public static String getRelativePath(String file) throws IOException{
		String parentPath = FileSystemUtil.getTempDir().getCanonicalPath().replace("\\", "/");
		file = file.replace("\\", "/");
		file = file.replace(parentPath, "");
		if(file.startsWith("/")){
			file = file.substring(1);
		}
		return file;
	}
	
	public static File createEmptyDir() throws IOException{
		File d = createEmptyFile();
		d.mkdir();
		return d;
	}
	
	public static File createEmptyFile() throws IOException{
		return new File(FileSystemUtil.getTempDir(), new Date().getTime() + "");
	}
	
	public static File createEmptyFile(String suffix) throws IOException{
		return new File(FileSystemUtil.getTempDir(), new Date().getTime() + "." + suffix);
	}
	
	public static String save(File source, String suffix) throws IOException{
		File dest = createEmptyFile(suffix);
		FileUtils.copyFile(source, dest);
		return getRelativePath(dest.getCanonicalPath());
	}
	
	public static String save(InputStream is, String suffix) throws IOException{
		File dest = createEmptyFile(suffix);
		ReadableByteChannel input = Channels.newChannel(is);
		FileChannel output = new FileOutputStream(dest).getChannel();

		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		while(true){
			buffer.clear();
			int r = input.read(buffer);
			if(r == -1){
				break;
			}
			buffer.flip();
			output.write(buffer);
		}
		input.close();
		output.close();
		
		return dest.getCanonicalPath();
	}
	
	public static String save(String str, String suffix) throws IOException{
		return save(str, "utf-8", suffix);
	}
	
	public static String save(String str, String charset, String suffix) throws IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes(charset));
		return save(bais, suffix);
	}
	
	public static String file2String(File f) throws IOException{
		return FileSystemUtil.getTempRelativePath(f.getCanonicalPath()) ;
	}
	
	public static File findFile(String relativePath){
		File f = new File(relativePath);
		if(f.exists()){
			return f;
		}
		return FileSystemUtil.getFileByRelativePath(relativePath);
	}
}
