package siddur.common.miscellaneous;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ToolUtil {

	public static byte[] parse(String text){
		StringTokenizer st = new StringTokenizer(text, ",");
		List<Byte> list = new ArrayList<Byte>();
		while(st.hasMoreElements()){
			String s = st.nextToken().trim();
			byte b = (byte)Integer.parseInt(s);
			list.add(b);
		}
		
		byte[] bb = new byte[list.size()];
		for (int i = 0; i < bb.length; i++) {
			bb[i] = list.get(i);
		}
		return bb;
	}
}
