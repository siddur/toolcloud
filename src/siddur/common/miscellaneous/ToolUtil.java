package siddur.common.miscellaneous;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import siddur.tool.core.IToolWrapper;
import siddur.tool.core.ScriptUtil;
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
		File workspace = TempFileUtil.createEmptyFile();
		if(workspace.isDirectory()){
			FileUtils.deleteDirectory(workspace);
		}else if(workspace.isFile()){
			workspace.delete();
		}
		
		
		//if there are more than one script files 
		//and if the main script is not exe
		//then copy it into workspace
		if(parent.list().length > 2 && !src.getName().endsWith(".exe")){
			Process process = Runtime.getRuntime()
					.exec("cp -R " + parent.getCanonicalPath() + " " + workspace.getCanonicalPath());
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			workspace.mkdir();
		}
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
	
	public static void copyCmdWorkspace(File src, File dest, boolean preservePermission) throws IOException{
		if(ScriptUtil.isWindows || !preservePermission){
			dest.mkdir();
			FileUtils.copyDirectory(src, dest);
		}else{
			Process process = Runtime.getRuntime()
					.exec("cp -R " + src.getCanonicalPath() + " " + dest.getCanonicalPath());
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * {n} represents the nth param. The first param is {0}
	 * {n.fp} represents prefix of file name. "/home/a.txt" ==> "a"
	 * {n.fs} represents suffix of file name. "/home/a.txt" ==> "txt"
	 * {n.fn} represents file name. "/home/a.txt" ==> "a.txt"
	 * {n.pp} represents parent full path. "/home/a.txt" ==> "/home"
	 */
	private static final Pattern pattern = Pattern.compile("\\{(\\d+)(?:\\:([^\\s\\}]+))?\\}");
	private static final String[] fileOrder = new String[]{"fn", "fp", "fs"};
	public static String overrideParam(String template, String[] params){
		Matcher m = pattern.matcher(template);
		StringBuffer sb = new StringBuffer();
		while(m.find()){
			int i = Integer.parseInt(m.group(1));
			String param = params[i];
			if(m.groupCount() == 2){
				String order = m.group(2);
				//fp/fs/fn
				if(ArrayUtils.contains(fileOrder, order)){
					param = param.replace("\\", "/");
					int slash = param.lastIndexOf("/");
					if(slash != -1){
						param = param.substring(slash + 1);
					}
					
					if(!order.equals("fn")){
						boolean isPrefix = order.equals("fp");
						int dot = param.lastIndexOf(".");
						if(dot != -1){
							param = isPrefix ? param.substring(0, dot)
									: param.substring(dot + 1);
						}else{
							param = isPrefix ? param : "";
						}
					}
				}else if("pp".equals(order)){ //pp
					param = FileSystemUtil.getParentPath(param);
				}
			}
			m.appendReplacement(sb, param.replace("\\", "\\\\\\"));
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public static File gatherFuzzyFiles(String path, File env) throws IOException{
		String parent = FileSystemUtil.getParentPath(path);
		String name = path.substring(parent.length() + 1);
		final Pattern p = Pattern.compile(name
				.replace("\\", "\\\\")
				.replace(".", "\\.")
				.replace("*", ".+"));
		File parentFile = findFile(parent, env);
		
		File tempDir = TempFileUtil.createEmptyDir();
		FileUtils.copyDirectory(parentFile, tempDir, new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				return p.matcher(f.getName()).matches();
			}
		});
		
		return tempDir;
	}
	
	public static File findFile(String path, File parent) throws IOException{
		if(FileSystemUtil.isRelative(path)){
			return new File(parent, path);
		}else{
			return new File(path);
		}
	}
	
	public static String[] getEncodings(Map<String, Object> context){
		return (String[])context.get(Constants.FILE_ENCODING);
	}

	
	public static void main(String[] args) throws IOException {
//		String[] params = {"/temp/a.txt", "c:\\temp\\b.txt"};
//		String template = "{0}{1}First param's prefix: '{0:fp }'. And second param's suffix: '{1:fs}'. And parent path '{0:pp}', '{1:pp}'";
//		System.out.println(overrideParam(template, params));
		
//		String[] params = {"D:\\SIDDUR\\indigo\\D\\toolcloud\\logs\\request-2013_08.log"};
//		String template = "{0:pp}/{0:fp}*.{0:fs}";
//		System.out.println(gatherFuzzyFiles(overrideParam(template, params), null));
		
		
		String template = "--color --language=html -E{0} --toc {1}/*  -o output.html";
		String[] params = {"java", "java"};
		System.out.println(overrideParam(template, params));
	}
}
