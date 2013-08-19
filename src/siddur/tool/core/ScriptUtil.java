package siddur.tool.core;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import siddur.tool.core.data.DataTemplate;


public class ScriptUtil {

	private static Properties p;
	
	private static void loadPropertes(){
		if(p == null){
			p = new Properties();
			try {
				p.load(new FileInputStream("lang.conf"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static String getOrderPath(String lang){
		loadPropertes();
		return p.getProperty(lang);
	}
	
	public static String[] getLangs(){
		loadPropertes();
		String[] sa = new String[p.size()];
		int idx = 0;
		for(Object key : p.keySet()){
			sa[idx ++] = (String) key;
		}
		Arrays.sort(sa);
		return sa;
	}
	
	private static void appendQuot(StringBuilder sb){
		sb.append("\"");
	}
	
	
	public static String determineScriptType(String scriptFile){
		String type = null;
		int dotIndex = scriptFile.lastIndexOf(".");
		if(dotIndex > -1){
			String suffix = scriptFile.substring(dotIndex + 1).toLowerCase();
			if("pl".equals(suffix)){
				type = "perl";
			}else if("py".equals(suffix)){
				type = "python";
			}else if("bat".equals(suffix)){
				type = "";
			}else if("sh".equals(suffix)){
				type = "";
			}
		}
		return type;
	}
	
	public static String getExecuteString(String scriptName, String scriptFile, 
			String[] params, DataTemplate[] tooldatas) throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append(getOrderPath(scriptName));
		sb.append(" ");
		appendQuot(sb);
		sb.append(scriptFile);
		appendQuot(sb);
		sb.append(" ");
		if(tooldatas != null){
			for (int i = 0; i < tooldatas.length; i++) {
				sb.append(" ");
				sb.append(tooldatas[i].getTag() + " " + params[i]);
			}
		}
		return sb.toString();
	}
	
}
