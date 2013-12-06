package siddur.tool.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import siddur.common.miscellaneous.Constants;


public class WebBatchToolManager extends BasicToolManager{
	
	@Override
	protected String[] execute(IToolWrapper tw, String[] params,
			Map<String, Object> context) throws Exception {
		ITool tool = tw.getTool();
		
		boolean useLog = false;
		
		final String ticket = (String)context.get(Constants.TICKET);
		if(tool instanceof ConsoleTool){
			ConsoleTool ct = (ConsoleTool)tool;
			if(ticket != null){
				useLog = true;
				LogCache logCache = LogCache.newInstance(ticket);
				logCache.addFilter(new EscapeFilter());
				logCache.addFilter(new FileLinkFilter(tw.getDescriptor().getPluginID()));
				ct.setLogQueue(logCache);
			}
		}
		
		String[] output = null;
		@SuppressWarnings("unchecked")
		Map<Integer, String> splitMap = (Map<Integer, String>)context.get(Constants.SPLIT_MAP);
		try {
			if (splitMap != null && !splitMap.isEmpty()) {
				output = exec(tw, tool, params, splitMap, context);
			} else {
				output = tool.execute(params, tw, context);
			}
		} finally{
			if(useLog){
				LogCache.dispose(ticket);
			}
		}
		
		return output;
	}

	
	private String[] exec(IToolWrapper tw, ITool tool, String[] inputs, Map<Integer, 
			String> splitMap, Map<String, Object> context) throws Exception{
		Set<Integer> keys = splitMap.keySet();
		Map<Integer, String[]> splittedMap = new HashMap<Integer, String[]>(keys.size());
		int len = 0;
		for (Integer k : keys) {
			String[] v = StringUtils.split(inputs[k], splitMap.get(k));
			if(len != 0 && len != v.length){
				throw new Exception("批量输入的数量不等");
			}
			len = v.length;
			splittedMap.put(k, v);
		}
		
		String [][] results = new String[len][];
		for (int x = 0; x < len; x++) {
			String[] itemParams = new String[inputs.length];
			for (int i = 0; i < itemParams.length; i++) {
				if(keys.contains(i)){
					itemParams[i] = splittedMap.get(i)[x];
				}else{
					itemParams[i] = inputs[i];
				}
			}
			String[] result = tool.execute(itemParams, tw, context);
			results[x] = result;
		}
		
		StringBuilder[] sbs = new StringBuilder[results[0].length];
		for (int i = 0; i < results[0].length; i++) {
			sbs[i] = new StringBuilder();
		}
		for (int j = 0; j < results.length; j++) {
			for (int i = 0; i < results[j].length; i++) {
				sbs[i].append(results[j][i]);
				sbs[i].append("|||");
			}
		}
		
		String[] output = new String[sbs.length];
		for (int i = 0; i < sbs.length; i++) {
			output[i] = sbs[i].substring(0, sbs[i].length() - 3);
		}
		return output;
	}
}
