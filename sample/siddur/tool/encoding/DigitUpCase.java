package siddur.tool.encoding;

import siddur.tool.core.ITool;

public class DigitUpCase implements ITool {
	private static String A[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
	private static String B[] = new String[]{"#", "拾", "佰", "仟"};
	private static String C[] = new String[]{"萬", "亿"};
	
	private boolean found0 = false;

	@Override
	public String[] execute(String[] inputs) throws Exception {
		String digitText = inputs[0];
		long digit = Long.parseLong(digitText);
		StringBuilder sb = new StringBuilder(20);
		int i = 0;
		while(digit > 0){
			i++;
			long newDigit = digit/10;
			int mod = (int)(digit - newDigit*10);
			transfer(sb, mod, i);
			digit = newDigit;
		}
		
		//去掉拾前面的壹
		if(sb.charAt(sb.length() - 1) == '壹' && sb.charAt(sb.length() - 2) == '拾'){
			sb.deleteCharAt(sb.length() - 1);
		}
		
		return new String[]{sb.reverse().toString()};
	}
	
	
	
	private void transfer(StringBuilder sb, int mod, int i){
		boolean repeat0 = false;
		if(mod == 0){
			repeat0 = found0;
			found0 = true;
		}else{
			found0 = false;
		}
		
		int x = i - 1;
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
		else if(!found0){
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
		String[] inputs = new String[]{"120078"};
		System.out.print(inputs[0]);
		System.out.println(d.execute(inputs)[0]);
		inputs = new String[]{"123456789"};
		System.out.print(inputs[0]);
		System.out.println(d.execute(inputs)[0]);
	}
}
