package siddur.tool.encoding;

import siddur.tool.core.ITool;

public class Native2HTML implements ITool{

	@Override
	public String[] execute(String[] inputs) throws Exception {
		String nativeText = inputs[0];
		char[] chs = nativeText.toCharArray();
		StringBuilder sb = new StringBuilder(chs.length);
		for (int i = 0; i < chs.length; i++) {
			char ch = chs[i];
			if(ch > 128){
				sb.append("&#x");
				if(ch < 256){
					sb.append("0");
				}
				if(ch < 4069){
					sb.append("0");
				}
				sb.append(Integer.toHexString(ch));
				sb.append(";");
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
		Native2HTML n = new Native2HTML();
		String content = "这是一个例子,this is a example";
		String[] inputs = new String[]{content};
		String[] outputs = n.execute(inputs);
		System.out.println(outputs[0]);
	}
}
