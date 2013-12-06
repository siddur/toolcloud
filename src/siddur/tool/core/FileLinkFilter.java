package siddur.tool.core;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import siddur.common.util.FileSystemUtil;

/**
 *	Find file-like string, copy into fileserver/output,
 *	 and wrap with <a>
 *
 */
public class FileLinkFilter implements LogFilter{
	private static Logger log4j = Logger.getLogger(FileLinkFilter.class);
	private static final Pattern p = Pattern.compile("(([CDEFcdef]:)?[\\\\/])?\\w+([\\\\/]+\\w+)+(\\.\\w+)?");
	private String namespace;
	
	public FileLinkFilter(String namespace) {
		this.namespace = namespace;
	}
	
	public FileLinkFilter() {
		this(null);
	}
	
	@Override
	public String doFilter(String log) {
		//file
		// C:/abc/cde.f
		// /abc/cde.f
		// abc/cde.f
		Matcher m = p.matcher(log);
		StringBuffer sb = new StringBuffer();
		while(m.find()){
			String file = m.group();
			try {
				String url = dealFile(file);
				if(url != null){
					String newString = "<a target='_blank' href='<file-url>"+url+"?d=1'>" + url + "</a>";
					newString = newString.replace("\\", "\\\\");
					m.appendReplacement(sb, newString);
				}
			} catch (IOException e) {
				log4j.warn(e);
			}
		}
		m.appendTail(sb);
		log = sb.toString();
		
		return log;
	}
	
	
	private String dealFile(String s) throws IOException{
		File f = null;
		if(FileSystemUtil.isRelative(s)){
			f = FileSystemUtil.getOutputFileInTempDir(s, namespace);
		}
		else{
			f = new File(s);
		}
		return FileSystemUtil.copy2Temp(f, namespace);
	}
}
