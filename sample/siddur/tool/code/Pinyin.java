package siddur.tool.code;

import java.util.Arrays;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import siddur.tool.core.ITool;

public class Pinyin implements ITool{

	@Override
	public String[] execute(String[] inputs) throws Exception {
		String text = inputs[0];
		
		HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
		hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuilder sb = new StringBuilder(text.length());
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if(c < 0x9FA5 && c > 0x4E00){
				String[] ss = PinyinHelper.toHanyuPinyinStringArray(c, hanYuPinOutputFormat);
				sb.append(ss[0]);
				sb.append(" ");
			}else{
				sb.append(c);
			}
		}
		return new String[]{sb.substring(0, sb.length())};
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
		String s = "中华1223=‘；x;；";
		
		String r = new Pinyin().execute(new String[]{s})[0];
		System.out.println(r);
	}
}
