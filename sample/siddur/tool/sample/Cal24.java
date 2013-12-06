package siddur.tool.sample;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import siddur.tool.core.ITool;
import siddur.tool.core.IToolWrapper;


public class Cal24 implements ITool {

	private static final double precision = 0.00001;
	private static final int target = 24;
	

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper, Map<String, Object> context) {
		int[] digits = new int[4];
		for (int i = 0; i < inputs.length; i++) {
			digits[i] = Integer.valueOf(inputs[i]);
		}
		return new String[]{calc(digits)};
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	private String calc(final int data[]){
		final Set<String> out = new HashSet<String>();
		Combination digit = new Combination() {
			
			@Override
			protected void handle(int[] result) {
				final int[] r = result; 
				Combination oper = new Combination(){
					@Override
					protected void handle(int[] c) {
						double x = r[0];
						for (int i = 0; i < r.length - 1; i++) {
							x = doCalculate(x, r[i + 1], c[i]);
						}
						if(Math.abs(Math.abs(x) - target) < precision || Math.abs(Math.abs(1/x) - target) < precision){
							StringBuilder sb = new StringBuilder();
							for (int j = 0; j < r.length; j++) {
								sb.append(r[j]);
								if(j != r.length - 1){
									sb.append(getOperation(c[j]));
								}
							}
							out.add(sb.toString());
						}
						
					}
				};
				oper.combine(new int[]{0, 1, 2, 3}, data.length - 1, true);
				
			}
		};
		
		digit.combine(data);
		
		StringBuilder sb = new StringBuilder();
		for (String string : out) {
			sb.append(string);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	
	
	private double doCalculate(double x, double y, int operation){
		switch (operation) {
		case 0:
			return x + y;
		case 1:
			return x - y;
		case 2:
			return x * y;
		case 3:
			return x / y;
		default:
			return 0;
		}
	}
	
	private static String getOperation(int operation){
		switch (operation) {
		case 0:
			return "+";
		case 1:
			return "-";
		case 2:
			return "*";
		case 3:
			return "/";
		default:
			return "";
		}
	}


}
