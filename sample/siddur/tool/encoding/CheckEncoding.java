package siddur.tool.encoding;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import siddur.tool.core.ITool;
import siddur.tool.sample.Combination;

public class CheckEncoding implements ITool{
	private static final String[] encodings = {"UTF-8", "GBK", "ISO-8859-1"};
	
	/*
	 * text
	 */
	@Override
	public String[] execute(String[] inputs) throws Exception {
		String text = inputs[0];
		final StringBuilder sb = new StringBuilder();
		append(text, sb);
		
		int[] indices = {0, 1, 2};
		final String t = text;
		Combination c = new Combination() {
			
			@Override
			protected void handle(int[] result) {
				try {
					append(t, sb, encodings[result[0]], encodings[result[1]]);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		c.combine(indices, 2);
		
		return new String[]{sb.toString()};
	}
	
	private void append(String text, StringBuilder sb){
		sb.append(text);
		sb.append(Arrays.toString(text.getBytes()));
		sb.append("\n");
		
		char[] ch = text.toCharArray();
		for (char c : ch) {
			sb.append((int)c);
			sb.append(" ");
		}
		sb.append("\n");
	}
	
	private void append(String text, StringBuilder sb, String from, String to) throws UnsupportedEncodingException{
		sb.append(from + " --> " + to + "\n");
		byte[] bb = text.getBytes(from);
		text = new String(bb, to);
		sb.append(text);
		sb.append(Arrays.toString(bb));
		sb.append("\n");
		
		char[] ch = text.toCharArray();
		for (char c : ch) {
			sb.append((int)c);
			sb.append(" ");
		}
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
		CheckEncoding ce = new CheckEncoding();
		System.out.println(ce.execute(new String[]{"´¢»Û"})[0]);
	}
}
