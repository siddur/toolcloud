package siddur.common.miscellaneous;

public class StringUtil {
	

	public static String convert(String src){
		if(src == null) return null;
		return src.replace("\r\n", "\n").replace("\n", "\\n");
	}
	
	public static String text2Html(String src){
		if(src == null) return null;
		return src.replace("\r\n", "\n").replace("\n", "<br/>").replace(" ", "&nbsp;");
	}
	
	public static String escape(String src){
		if(src == null) return null;
		return src.replace("<", "&lt;").replace(">", "&gt;");
	}
	
	public static String clearTag(String src){
		if(src == null) return null;
		return src.replaceAll("</?.+?>","");
	}
	
	
	
	public static void main(String[] args) {
		String s = "abc<input type='text' >bcd</pre>";
		System.out.println(clearTag(s));
	}
}
