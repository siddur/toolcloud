package siddur.tool.encoding;

import java.io.File;
import java.util.Hashtable;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import siddur.tool.core.ITool;
import siddur.tool.core.TempFileUtil;

public class QREncoder implements ITool{

	/*
	 * String content
	 * Integer width
	 * String Encoding
	 * String mime //jpg,png
	 */
	@Override
	public String[] execute(String[] inputs) throws Exception {
		String content = inputs[0];
		int width = Integer.parseInt(inputs[1]);
		String encoding = inputs[2];
		
        com.google.zxing.Writer writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
        hints.put(EncodeHintType.CHARACTER_SET, encoding);
        BitMatrix matrix = writer.encode(content, com.google.zxing.BarcodeFormat.QR_CODE, width, width, hints);
        
        String mime = inputs[3];
        File dest = TempFileUtil.createEmptyFile(mime);
        MatrixToImageWriter.writeToFile(matrix, mime, dest);
        
        return new String[]{dest.getCanonicalPath()};
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
		QREncoder b = new QREncoder();
		b.execute(new String[]{"中国"});
	}

}
