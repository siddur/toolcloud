package siddur.common.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.commons.exec.OS;
import org.apache.commons.lang3.StringUtils;

import siddur.tool.core.data.DataTemplate;


public class ScriptUtil {

	private static Properties p;
	public static boolean isWindows = OS.isFamilyWindows();
	
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
	
	public static String getScriptExecuteString(String scriptName, String scriptFile, String template,
			String[] params, DataTemplate[] tooldatas) throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append(getOrderPath(scriptName));
		sb.append(" ");
		appendQuot(sb);
		sb.append(scriptFile);
		appendQuot(sb);
		sb.append(" ");
		sb.append(overrideParam(template, params, tooldatas));
		return sb.toString();
	}
	
	//template like this: -r {0} {1} -d {2}
	private static String overrideParam(String template, String[] params, DataTemplate[] tooldatas){
		if(StringUtils.isEmpty(template)){
			return concatParams(params, tooldatas);
		}else{
			return ToolUtil.overrideParam(template, params);
		}
	}
	
		
	public static String getSystemExecuteString(String orderName, String template,
			String[] params, DataTemplate[] tooldatas) throws Exception{
		StringBuilder sb = new StringBuilder();
		if(isWindows){
			sb.append("cmd /c ");
		}
		else{
			if(orderName.endsWith(".exe")){
				sb.append("wine ");
			}
			
		}
		sb.append(orderName);
		sb.append(" ");
		sb.append(overrideParam(template, params, tooldatas));
		return sb.toString();
	}
	
	private static String concatParams(String[]params, DataTemplate[] tooldatas){
		StringBuilder sb = new StringBuilder(); 
		if(tooldatas != null){
			for (int i = 0; i < tooldatas.length; i++) {
				sb.append(" ");
				sb.append(tooldatas[i].getTag() + " " + params[i]);
			}
		}
		return sb.toString();
	}
}
