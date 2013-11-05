package siddur.tool.encoding;

import java.net.URLEncoder;
import java.util.Map;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;

public class Native2URL implements ITool {
	
	
	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		String nativeText = inputs[0];
		String encoding = inputs[1];
		return new String[]{URLEncoder.encode(nativeText, encoding)};
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
		Native2URL n = new Native2URL();
		String content = "http://www.oschina.net/search?scope=bbs&q=C语言";
		String[] inputs = new String[]{content, "utf-8"};
		String[] outputs = n.execute(inputs, null, null);
		System.out.println(outputs[0]);
	}

}
