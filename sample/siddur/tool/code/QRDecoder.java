package siddur.tool.code;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;

import siddur.common.util.TempFileUtil;
import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;

public class QRDecoder implements ITool{

	/*
	 *  file imageFile
	 * 	String Encoding
	 */
	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		String imagePath = inputs[0];
		File imageFile = TempFileUtil.findFile(imagePath);
		String encoding = inputs[1];
		com.google.zxing.Reader reader = new MultiFormatReader();
        LuminanceSource ls = new BufferedImageLuminanceSource(ImageIO.read(imageFile.getAbsoluteFile()));
        Binarizer b = new GlobalHistogramBinarizer(ls);
        Hashtable<DecodeHintType, String> hint1 = new Hashtable<DecodeHintType, String>(2);
        hint1.put(DecodeHintType.CHARACTER_SET, encoding);
        Result r = reader.decode(new BinaryBitmap(b), hint1);
        return new String[]{r.getText(), r.getBarcodeFormat().name()};
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
		QRDecoder b = new QRDecoder();
		String[] outputs = b.execute(new String[]{"temp\\canvas.png", "iso-8859-1"}, null, null);
		System.out.println(outputs[0]);
		System.out.println(outputs[1]);
	}

}
