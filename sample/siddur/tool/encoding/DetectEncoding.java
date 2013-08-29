package siddur.tool.encoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.mozilla.universalchardet.UniversalDetector;

import siddur.tool.core.ITool;
import siddur.tool.core.TempFileUtil;

public class DetectEncoding implements ITool{

	@Override
	public String[] execute(String[] inputs) throws Exception {
		String filepath = inputs[0];
		File f = TempFileUtil.findFile(filepath);
		
		InputStream is = new FileInputStream(f);
		UniversalDetector detector = new UniversalDetector(null);
		int len;
		byte[] buf = new byte[1024];
		int total = 0;
		while((len = is.read(buf)) > 0 && !detector.isDone()){
			detector.handleData(buf, 0, len);
			total += len;
		}
		is.close();
		detector.dataEnd();
		
		String encoding = detector.getDetectedCharset();
		if(encoding == null){
			encoding = "Sorry";
		}else{
			encoding = total + "bytes:" + encoding;
		}
		
		return new String[]{encoding};
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
		DetectEncoding b = new DetectEncoding();
		String r = b.execute(new String[]{"temp\\test.txt"})[0];
		System.out.println(r);
	}
}
