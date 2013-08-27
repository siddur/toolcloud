package siddur.tool.encoding;

import java.util.Properties;

import siddur.tool.core.ITool;

public class Native2ASCII implements ITool {

	@Override
	public String[] execute(String[] inputs) throws Exception {
		String nativeText = inputs[0];
		boolean filter123_abc = "1".equals(inputs[1]);
		char[] chs = nativeText.toCharArray();
		StringBuilder sb = new StringBuilder(chs.length);
		for (int i = 0; i < chs.length; i++) {
			char ch = chs[i];
			if(!filter123_abc || ch > 128){
				sb.append("\\u");
				if(ch < 256){
					sb.append("0");
				}
				if(ch < 4069){
					sb.append("0");
				}
				sb.append(Integer.toHexString(ch));
			}else{
				sb.append(ch);
			}
		}
		return new String[]{sb.toString()};
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws Exception {
//		Native2ASCII n = new Native2ASCII();
//		String content = "fasdfasfa；分类是就算啦；“abc”" + '\u4068';
//		String[] inputs = new String[]{content, "0"};
//		String[] outputs = n.execute(inputs);
//		System.out.println(outputs[0]);
		Properties p = new Properties();
		p.load(Native2ASCII.class.getResourceAsStream("test.properties"));
		System.out.println(p.get("test"));
	}

}
