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

import siddur.common.miscellaneous.FileSystemUtil;
import siddur.common.web.Action;
import siddur.common.web.ActionMapper.Result;

public class FileAction extends Action{

	public Result dir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String relativePath = req.getParameter("path");
		String result = getFileModel(relativePath);
		return Result.ajax(result);
	}
	
	
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
		String result = new Gson().toJson(fm);
		return result;
	}
	
	private FileModel getFileModel(File f, FileModel parent) throws IOException{
		FileModel fm = new FileModel();
		fm.name = f.getName();
		fm.url = FileSystemUtil.getTempRelativePath(f.getCanonicalPath());
		if(parent != null){
			parent.children.add(fm);
		}
		
		if(f.isDirectory()){
			fm.isDir = true;
			fm.children = new ArrayList<FileModel>();
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
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(new FileAction().getFileModel("tabs"));
	}
}
