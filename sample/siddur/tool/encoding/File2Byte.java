package siddur.tool.encoding;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import siddur.common.miscellaneous.ToolUtil;
import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;

public class File2Byte implements ITool{

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		String filepath = inputs[0];
		File f = TempFileUtil.findFile(filepath);
		byte[] data = ToolUtil.read(f);
		System.out.println(Arrays.toString(data));
		StringBuffer sb = new StringBuffer(data.length * 3);
		for (int i = 0; i < data.length; i++) {
			int b = data[i] & 0xff;
			sb.append(Integer.toHexString(b));
			sb.append(" ");
		}
		String result = sb.substring(0, sb.length() - 1);
		
		return new String[]{result};
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
		File2Byte b = new File2Byte();
		String r = b.execute(new String[]{"temp\\test.txt"}, null, null)[0];
		System.out.println(r);
	}
}
