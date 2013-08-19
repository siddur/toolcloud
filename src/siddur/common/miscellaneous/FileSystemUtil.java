package siddur.common.miscellaneous;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileSystemUtil {
	private static File home;
	
	public static File getHome(){
		if(home == null){
			home = new File(".");
		}
		return home;
	}
	
	public static void setHome(File _home){
		if(_home.isDirectory())
			home = _home;
	}
	
	public static File getToolDir(){
		return getChildDir("tools");
	}
	
	public static File getFileServer(){
		return getChildDir("fileserver");
	}
	
	public static File getImageDir(){
		File file = new File(getFileServer(), "images");
		if(!file.isDirectory()){
			file.mkdir();
		}
		return file;
	}
	
	public static File getOutputDir(){
		File file = new File(getFileServer(), "output");
		if(!file.isDirectory()){
			file.mkdir();
		}
		return file;
	}
	
	public static File getTempDir(){
		return getChildDir("temp");
	}
	
	public static String getRelativePath(String file, String parent){
		parent = parent.replace("\\", "/");
		file = file.replace("\\", "/");
		file = file.replace(parent, "");
		if(file.startsWith("/")){
			file = file.substring(1);
		}
		return file;
	}
	
	public static String getRelativePath(String file) throws IOException{
		return getRelativePath(file, getHome().getCanonicalPath());
	}
	
	public static File getFileByRelativePath(String relativePath){
		return new File(getHome(), relativePath);
	}
	
	private static File getChildDir(String name){
		File f = new File(getHome(), name);
		if(!f.isDirectory()){
			f.mkdir();
		}
		return f;
	}
	
	public static File getOutputFileInTempDir(String path, String namespace) throws IOException{
		if(namespace == null){
			namespace = "";
		}
		return new File(FileSystemUtil.getTempDir().getCanonicalPath()
				+ "/" + namespace, path);
	}
	
	public static String copy2Server(File f, String namespace) throws IOException{
		if(f.exists()){
			File downloadableDir = new File(FileSystemUtil.getOutputDir(), namespace);
			if(!downloadableDir.isDirectory()){
				downloadableDir.mkdir();
			}
			File dest = new File(downloadableDir, f.getName());
			if(f.isFile())
				FileUtils.copyFile(f, dest);
			else
				FileUtils.copyDirectory(f, dest);
			return FileSystemUtil.getRelativePath(dest.getCanonicalPath(), 
					FileSystemUtil.getOutputDir().getCanonicalPath());
		}
		return null;
	}
	
	public static boolean containedInOutputDir(File f) throws IOException{
		String p = f.getCanonicalFile().getPath();
		String output = getOutputDir().getCanonicalFile().getPath() + File.separator;
		return p.length() > output.length() && p.startsWith(output);
	}
	
	public static boolean isRelative(String s) throws IOException{
		return !((s.startsWith("/") 
				|| s.startsWith("\\") 
				|| s.startsWith(":", 1)));//absolute path
	}
}
