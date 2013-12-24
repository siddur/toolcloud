package siddur.common.util;

public class IdentityCreator {

	/*
	 * ID by HEX
	 * 0009 000a 000b
	 */
	public static synchronized String increment(String currentId, int length){
		int id = -1;
		if(currentId != null){
			id = Integer.valueOf(currentId, 16);
		}
		id ++;
		String result = Integer.toHexString(id);
		int len = result.length();
		int n = length - len;
		for (int i = 0; i < n; i++) {
			result = "0" + result;
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		String s = "0";
		for (int i = 0; i < 100; i++) {
			s = increment(s, 6);
			System.out.print(s + " ");
		}
	}
}
