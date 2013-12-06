package siddur.tool.cloud.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import siddur.common.security.DoNotAuthenticate;
import siddur.common.util.FileSystemUtil;
import siddur.common.web.Action;
import siddur.common.web.ActionMapper.Result;

public class FileAction extends Action{

	@DoNotAuthenticate
	public Result dir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String relativePath = req.getParameter("path");
		String result = getFileModel(relativePath);
		return Result.ajax(result);
	}
	
	@DoNotAuthenticate
	public Result file(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String relativePath = req.getParameter("path");
		File file = new File(FileSystemUtil.getTempDir(), relativePath);
		if(file.isDirectory()){
			return Result.ajax(new Gson().toJson(getFileModel(file, null)));
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream((int)file.length());
		ReadableByteChannel r = new FileInputStream(file).getChannel();
		WritableByteChannel w = Channels.newChannel(baos);
		ByteBuffer bb = ByteBuffer.allocate(8192);
		while(r.read(bb) != -1){
			bb.flip();
			w.write(bb);
			bb.clear();
		}
		r.close();
		w.close();
		return Result.ajax(baos.toString());
	}
	
	private String getFileModel(String path) throws IOException{
		File file = new File(FileSystemUtil.getTempDir(), path);
		FileModel fm = getFileModel(file, null);
		
		FileModel javaFileModel = fm.clone();
		if(fm.isDir){
			for (FileModel f : fm.children) {
				toJavaPackageFileModel(javaFileModel, f, "");
			}
		}
		
		String result = new Gson().toJson(javaFileModel);
		return result;
	}
	
	private FileModel getFileModel(File f, FileModel parent) throws IOException{
		FileModel fm = new FileModel();
		fm.name = f.getName();
		fm.url = FileSystemUtil.getTempRelativePath(f.getCanonicalPath());
		if(parent != null){
			parent.addChild(fm);
		}
		
		if(f.isDirectory()){
			fm.isDir = true;
			for(File curFile : f.listFiles()){
				getFileModel(curFile, fm);
			}
		}
		return fm;
	}
	
	private static class FileModel{
		private static int increment = 0;
		private static int increase(){
			increment++;
			if(increment > 9999){
				increment = 0;
			}
			return increment;
		}
		
		private int id = increase();
		private String name;
		private String url;
		private List<FileModel> children;
		private boolean isDir;
		
		public void addChild(FileModel f){
			if(children == null){
				children = new ArrayList<FileModel>();
			}
			children.add(f);
		}
		
		public FileModel clone(){
			FileModel fm = new FileModel();
			fm.id = this.id;
			fm.name = this.name;
			fm.url = this.url;
			fm.isDir = this.isDir;
			if(fm.isDir){
				fm.children = new ArrayList<FileModel>();
			}
			return fm;
		}
		
		@Override
		public String toString() {
			return name + "=" + url;
		}
	}
	
	
	private boolean singleFolder(FileModel f){
		boolean singleFolder = false;  //one file or more than one folders
		if(f.isDir){
			if(f.children.size() == 1){
				singleFolder = f.children.get(0).isDir;
			}
			else{
				singleFolder = false;
			}
		}
		return singleFolder;
	}
	
	private void toJavaPackageFileModel(FileModel targetParent, FileModel source, String prefix){
		if(singleFolder(source)){
			FileModel f = source.children.get(0);
			prefix +=  source.name + "/";
			toJavaPackageFileModel(targetParent, f, prefix);
		}else{
			FileModel newTgtParent = source.clone();
			newTgtParent.name = prefix + newTgtParent.name;
			if(source.isDir){
				targetParent.addChild(newTgtParent);
				for(FileModel f : source.children){
					toJavaPackageFileModel(newTgtParent, f, "");
				}
			}else{
				targetParent.addChild(newTgtParent);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(new FileAction().getFileModel("1384151787843FileBrower"));
	}
}
