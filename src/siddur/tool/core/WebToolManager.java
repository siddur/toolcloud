package siddur.tool.core;

import java.util.Map;

import siddur.common.miscellaneous.Constants;


public class WebToolManager extends BasicToolManager{
	
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
				logCache.addFilter(new NewLineFilter());
				logCache.addFilter(new FileLinkFilter(tw.getDescriptor().getPluginID()));
				ct.setLogQueue(logCache);
			}
		}
		
		String[] output = super.execute(tw, params, context);
		
		if(useLog){
			LogCache.dispose(ticket);
		}
		
		return output;
	}

}
