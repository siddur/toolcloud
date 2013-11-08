package siddur.tool.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


import siddur.common.miscellaneous.ToolUtil;

public abstract class HtmlResponseTool extends ConsoleTool{

	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper,
			Map<String, Object> context) throws Exception {
		File workspace = ToolUtil.buildWorkspace(toolWrapper);
		log("Build workspace");
		
		File html = new File(workspace, "index.html");
		html.createNewFile();
		Writer out = new OutputStreamWriter(new FileOutputStream(html));
		try {
			execute(inputs, toolWrapper, workspace, out, context);
		} finally{
			out.close();
		}
		
		return new String[]{TempFileUtil.file2String(html)};
	}

	protected abstract void execute(String[] inputs, IToolWrapper toolWrapper,
		File workspace,	Writer out, Map<String, Object> context) throws Exception;
	
	protected void buildHtml(Object dataModel, Writer out, String templateFile) throws Exception{
		ToolUtil.buildHtml(this.getClass(), dataModel, out, templateFile);
	}
	
	protected void buildHtml(String data, Writer out) throws Exception{
		Map<String, String> dataModel = new HashMap<String, String>();
		dataModel.put("data", data);
		ToolUtil.buildHtml(this.getClass(), dataModel, out, "index.fm");
	}
}