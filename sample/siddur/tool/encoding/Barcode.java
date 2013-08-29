package siddur.tool.encoding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import siddur.tool.core.ITool;
import siddur.tool.core.TempFileUtil;

public class Barcode implements ITool{
	
	@Override
	public String[] execute(String[] inputs) throws Exception {
		File dest = TempFileUtil.createEmptyFile("png");
		FileOutputStream os = new FileOutputStream(dest);
		
//		Code39Bean bean = new Code39Bean();
		Code128Bean bean = new Code128Bean();
		final int dpi = 150;
		bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
//		bean.setWideFactor(3);
		bean.doQuietZone(false);
		
		try{
		BitmapCanvasProvider canvas = new BitmapCanvasProvider(
	            os, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
		bean.generateBarcode(canvas, "123456");
		canvas.finish();
		}finally{
			os.close();
		}
		return null;
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
		Barcode b = new Barcode();
		b.execute(new String[]{"x"});
	}
}
