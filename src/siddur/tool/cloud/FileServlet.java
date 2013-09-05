package siddur.tool.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.util.log.Log;

import siddur.common.miscellaneous.FileSystemUtil;
import siddur.tool.core.ZipUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FileServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private ServletFileUpload uploader;
	private static final int THRESHOLD = 10 * 1024;
	private static final long IMAGE_SIZE_MAX = 10 * 1024;
	private static final long TEMP_SIZE_MAX = 10 * 1024 * 1024;
	
	@Override
	public void init() throws ServletException {
		DiskFileItemFactory ff = new DiskFileItemFactory();
		ff.setSizeThreshold(THRESHOLD);
		uploader = new ServletFileUpload(ff);
		uploader.setFileSizeMax(TEMP_SIZE_MAX);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//perform downloading
		String path = req.getPathInfo();
		if(path == null){
			return;
		}
		
		File f = null;
		if(path.startsWith("/fileserver")){
			f = new File(FileSystemUtil.getHome(), path);
		}else{
			f = new File(FileSystemUtil.getOutputDir(), path);
		}
		if(f.exists() /*&& FileSystemUtil.containedInOutputDir(f)*/){
			boolean download = "1".equals(req.getParameter("d"));
			if(download){
				if(f.isDirectory()){
					f = ZipUtil.zipDir(f);
				}
				
				String filename = f.getName();
				resp.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("utf-8"),"ISO-8859-1"));
				resp.addHeader("Content-Length", "" + f.length());
			}
			ReadableByteChannel r = new FileInputStream(f).getChannel();
			WritableByteChannel w = Channels.newChannel(resp.getOutputStream());
			ByteBuffer bb = ByteBuffer.allocate(8192);
			while(r.read(bb) != -1){
				bb.flip();
				w.write(bb);
				bb.clear();
			}
			r.close();
			w.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getPathInfo();
		
		
		boolean isImage = false;
		if("/image".equals(type)){
			isImage = true;
		}
		
		long sizeMax = isImage ? IMAGE_SIZE_MAX : TEMP_SIZE_MAX;
		
		int size = req.getContentLength();
		double threhold = (sizeMax + 1024);
		if(size > threhold){
			resp.getWriter().write("file uploaded is too big");
			resp.getWriter().close();
		}
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		File parent = isImage ? FileSystemUtil.getImageDir() : FileSystemUtil.getTempDir();
		if(isMultipart){
			List<FileItem> items = null;
			try {
				items = uploader.parseRequest(req);
			} catch (FileUploadException e) {
				Log.warn(e);
			}
			if(items != null){
				JsonArray ja = new JsonArray();
				for(FileItem fi : items){
					if(! fi.isFormField()){
						String filename = fi.getName();
						File f = new File(parent, new Date().getTime() + filename);
						
						JsonObject jo = new JsonObject();
						jo.addProperty("filename", filename);
						jo.addProperty("filepath", FileSystemUtil.getRelativePath(f.getCanonicalPath()));
						ja.add(jo);
						
						try {
							fi.write(f);
						} catch (Exception e) {
							Log.warn(e);
							e.printStackTrace();
						}
						fi.delete();
					}
				}
				OutputStream w = resp.getOutputStream();
				w.write(ja.toString().getBytes());
				w.close();
			}	
		}
	}
}
