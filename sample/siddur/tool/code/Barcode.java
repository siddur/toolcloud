package siddur.tool.code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.impl.pdf417.PDF417Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.MimeTypes;
import org.krysalis.barcode4j.tools.UnitConv;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.TempFileUtil;

public class Barcode implements ITool{
	
	private static final String CODE128 = "code-128";
	private static final String CODE39 = "code-39";
	private static final String CODABAR = "codabar";
	private static final String INTERLEAVED2OF5 = "Interleaved2Of5";
	private static final String PDF417 = "PDF417";
	
//	private static final String PNG = "png";
	private static final String JPG = "jpg";
	private static final String GIF = "gif";
	
	
	/*
	 * String type
	 * Integer width
	 * Integer DPI
	 * String MIME
	 * String Content
	 */
	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		
		String type = inputs[0];
		float width = Float.parseFloat(inputs[1]);
		int dpi = Integer.parseInt(inputs[2]);
		double moduleWidth = UnitConv.in2mm(width/dpi);
		System.out.println(moduleWidth);
		String mime = inputs[3];
		File dest = TempFileUtil.createEmptyFile(mime);
		if(mime.equals(GIF)){
			mime = MimeTypes.MIME_GIF;
		}
		else if(mime.equals(JPG)){
			mime = MimeTypes.MIME_JPEG;
		}
		else{
			mime = MimeTypes.MIME_PNG;
		}
		
		String content = inputs[4];
		
		AbstractBarcodeBean bean = buildBean(type);
		bean.setModuleWidth(moduleWidth);
//		bean.setWideFactor(3);
		bean.doQuietZone(false);
		
		FileOutputStream os = new FileOutputStream(dest);
		try{
		BitmapCanvasProvider canvas = new BitmapCanvasProvider(
	            os, mime, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
		bean.generateBarcode(canvas, content);
		canvas.finish();
		}finally{
			os.close();
		}
		return new String[]{dest.getCanonicalPath()};
	}

	private AbstractBarcodeBean buildBean(String type){
		AbstractBarcodeBean bean = null;
		
		if(type.equals(CODE39)){
			bean = new Code39Bean();
		}
		else if(type.equals(CODABAR)){
			bean = new CodabarBean();
		}
		else if(type.equals(INTERLEAVED2OF5)){
			bean = new Interleaved2Of5Bean();
		}
		else if(type.equals(PDF417)){
			bean = new PDF417Bean();
		}
		else{
			bean = new Code128Bean();
		}
		return bean;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * String type
	 * Integer width
	 * Integer DPI
	 * String MIME
	 * String Content
	 */
	public static void main(String[] args) throws Exception {
		Barcode b = new Barcode();
		b.execute(new String[]{CODE128, "1", "96", "png", "12345678"}, null, null);
		b.execute(new String[]{CODE39, "1", "96", "jpg", "12345678"}, null, null);
		b.execute(new String[]{CODABAR, "1", "96", "gif", "12345678"}, null, null);
		b.execute(new String[]{INTERLEAVED2OF5, "1", "96", "png", "12345678"}, null, null);
		b.execute(new String[]{PDF417, "1", "96", "jpg", "12345678"}, null, null);
	}
}
