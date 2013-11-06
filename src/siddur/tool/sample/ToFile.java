package siddur.tool.sample;

import java.io.IOException;
import java.util.Map;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;

public class ToFile implements ITool{

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws IOException {
		String path = TempFileUtil.save(inputs[0], "txt");
		return new String[]{path};
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
