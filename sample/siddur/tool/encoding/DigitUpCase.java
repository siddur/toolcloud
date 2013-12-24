package siddur.tool.encoding;

import java.util.Map;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;

public class DigitUpCase implements ITool {
	private static String A[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
	private static String B[] = new String[]{"#", "拾", "佰", "仟"};
	private static String C[] = new String[]{"萬", "亿"};
	
	private boolean found0 = false;

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) throws Exception {
		String digitText = inputs[0];

		//validate
		if(digitText.startsWith("-")){
			return new String[]{"无效的输入"};
		}
		if(digitText.contains(".")){
			return new String[]{"无效的输入"};
		}
		long digit = 0;
		try {
			digit = Long.parseLong(digitText);
		} catch (Exception e) {
			return new String[]{"无效的输入"};
		}
		StringBuilder sb = new StringBuilder(20);
		int i = 0;
		do{
			long newDigit = digit/10;
			int mod = (int)(digit - newDigit*10);
			transfer(sb, mod, i);
			i++;
			digit = newDigit;
		}while (digit > 0);
		
		//去掉拾前面的壹
		if(sb.length() >= 2 && sb.charAt(sb.length() - 1) == '壹' && sb.charAt(sb.length() - 2) == '拾'){
			sb.deleteCharAt(sb.length() - 1);
		}
		
		//去掉末尾的零
		if(sb.length() > 1 && sb.charAt(0) == '零'){
			sb.deleteCharAt(0);
		}
		
		String result = sb.reverse().toString();
		return new String[]{result};
	}
	
	
	
	private void transfer(StringBuilder sb, int mod, int x){
		boolean repeat0 = false;
		if(mod == 0){
			repeat0 = found0;
			found0 = true;
		}else{
			found0 = false;
		}
		
		int loc = x%4;
		if(x > 0){
			if(loc == 0){
				sb.append(C[x/4 - 1]);
				if(!found0)
					sb.append(A[mod]);
			}else{
				if(!found0)
					sb.append(B[loc]);
				if(!repeat0)
					sb.append(A[mod]);
			}
		}
		else{
			sb.append(A[mod]);
		}
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
		DigitUpCase d = new DigitUpCase();
		String[] inputs = new String[]{"0"};
		System.out.print(inputs[0]);
		System.out.println(d.execute(inputs, null, null)[0]);
		inputs = new String[]{"123456789"};
		System.out.print(inputs[0]);
		System.out.println(d.execute(inputs, null, null)[0]);
		inputs = new String[]{"10"};
		System.out.print(inputs[0]);
		System.out.println(d.execute(inputs, null, null)[0]);
		inputs = new String[]{"100"};
		System.out.print(inputs[0]);
		System.out.println(d.execute(inputs, null, null)[0]);
		inputs = new String[]{"101"};
		System.out.print(inputs[0]);
		System.out.println(d.execute(inputs, null, null)[0]);
		inputs = new String[]{"1011000"};
		System.out.print(inputs[0]);
		System.out.println(d.execute(inputs, null, null)[0]);
	}
}
