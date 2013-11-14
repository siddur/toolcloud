package siddur.common.miscellaneous;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;

public class ToolUtil {

	/**
	 * convert digit to byte;
	 */
	public static byte[] parse(String text){
		StringTokenizer st = new StringTokenizer(text, ",");
		List<Byte> list = new ArrayList<Byte>();
		while(st.hasMoreElements()){
			String s = st.nextToken().trim();
			byte b = (byte)Integer.parseInt(s);
			list.add(b);
		}
		
		byte[] bb = new byte[list.size()];
		for (int i = 0; i < bb.length; i++) {
			bb[i] = list.get(i);
		}
		return bb;
	}
	
	public static byte[] read(File f) throws IOException{
		int size = (int)f.length();
		ByteArrayOutputStream baos;
		ReadableByteChannel r = null;
		WritableByteChannel w = null;
		try {
			baos = new ByteArrayOutputStream(size);
			r = new FileInputStream(f).getChannel();
			w = Channels.newChannel(baos);
			
			ByteBuffer bb = ByteBuffer.allocate(size);
			while(r.read(bb) > 0){
				bb.flip();
				w.write(bb);
				bb.clear();
			}
		} finally{
			if(w != null)
				w.close();
			if(r != null)
				r.close();
		}
		
		return baos.toByteArray();
	}
	
	public static File buildWorkspace(IToolWrapper toolWrapper) throws IOException{
		File src = new File(toolWrapper.getToolfile());
		File parent = src.getParentFile();
		File workspace = TempFileUtil.createEmptyDir();
		if(workspace.isDirectory()){
			FileUtils.deleteDirectory(workspace);
		}else if(workspace.isFile()){
			workspace.delete();
		}
		workspace.mkdir();
		FileUtils.copyDirectory(parent, workspace);
		return workspace;
	}
	
	public static void buildHtml(Class<?> claz, Object dataModel, Writer out, String templateFile) throws Exception{
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		cfg.setClassForTemplateLoading(claz, "");
		cfg.setLocalizedLookup(false);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		Template temp = cfg.getTemplate(templateFile);
		temp.process(dataModel, out);
	}
	
	public static String[] getEncodings(Map<String, Object> context){
		return (String[])context.get(Constants.FILE_ENCODING);
	}

}
