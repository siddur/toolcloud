package siddur.tool.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import siddur.tool.core.ITool;
import siddur.tool.core.TempFileUtil;

public class FromFile implements ITool{

	
	@Override
	public String[] execute(String[] inputs) throws Exception {
		String filepath = inputs[0];
		File f = TempFileUtil.findFile(filepath);
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String result = r.readLine();
		r.close();
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
	
}
