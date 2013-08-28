package siddur.tool.encoding;

import siddur.tool.core.ITool;

public class Base64 implements ITool{

	@Override
	public String[] execute(String[] inputs) throws Exception {
		String s = inputs[0];
		org.apache.commons.codec.binary.Base64 b = new org.apache.commons.codec.binary.Base64(true);
		String r = b.encodeAsString(s.getBytes());
		return new String[]{r};
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
		Base64 b = new Base64();
		String r = b.execute(new String[]{"http://www.ostools.net/encrypt?type=3"})[0];
		System.out.println(r);
	}
}
