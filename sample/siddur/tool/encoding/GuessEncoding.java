package siddur.tool.encoding;

import java.io.UnsupportedEncodingException;
import siddur.common.miscellaneous.ToolUtil;
import siddur.tool.core.ITool;

public class GuessEncoding implements ITool{
	private static final String[] encodings = {"UTF-8", "GBK", "ISO-8859-1"};
	
	/*
	 * text
	 */
	@Override
	public String[] execute(String[] inputs) throws Exception {
		String text = inputs[0];
		StringBuilder sb = new StringBuilder();
		sb.append(text);
		sb.append("\n");
		
		
		byte[] bb = ToolUtil.parse(text);
		
		for (String  encoding : encodings) {
			append(bb, encoding, sb);
		}
		
		return new String[]{sb.toString()};
	}
	
	private void append(byte[] bb, String encoding, StringBuilder sb) throws UnsupportedEncodingException{
		sb.append("------>" + encoding);
		sb.append("\n");
		sb.append(new String(bb, encoding));
		sb.append("\n");
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
		GuessEncoding ce = new GuessEncoding();
		System.out.println(ce.execute(new String[]{"-76, -94, -69, -37"})[0]);
	}
}
