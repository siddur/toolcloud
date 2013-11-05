package siddur.tool.encoding;

import java.io.File;
import java.util.Map;

import siddur.common.miscellaneous.ToolUtil;
import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;

public class Base64 implements ITool{

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		String filepath = inputs[0];
		File f = TempFileUtil.findFile(filepath);
		
		byte[] data = ToolUtil.read(f);
		
		org.apache.commons.codec.binary.Base64 b = new org.apache.commons.codec.binary.Base64();
		String result = b.encodeAsString(data);
		result = "data:img/jpg;base64," + result;
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
		Base64 b = new Base64();
		String r = b.execute(new String[]{"temp\\favicon.ico"}, null, null)[0];
		System.out.println(r);
	}
}
