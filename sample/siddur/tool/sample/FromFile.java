package siddur.tool.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;

public class FromFile implements ITool{

	
	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		String filepath = inputs[0];
		String encoding = "UTF-8";
		if(inputs.length > 1){
			encoding = inputs[1];
		}
		File f = TempFileUtil.findFile(filepath);
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while((line = r.readLine()) != null){
			sb.append(line);
		}
		r.close();
		return new String[]{sb.toString()};
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
		FromFile f = new FromFile();
		String r = f.execute(new String[]{"temp\\test.txt"}, null, null)[0];
		System.out.println(r);
	}
}
