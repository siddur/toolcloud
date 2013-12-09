package siddur.tool.core;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.common.util.ToolUtil;
import siddur.common.util.WebPointUtil;
import siddur.common.util.WebPointUtil.DefaultWebPointFilter;
import siddur.common.util.WebPointUtil.WebPointHandler;
import siddur.common.web.Result;

public class AppTool implements ITool, ISelfAware, WebPointHandler{

	private Map<String, Method> methodMap = new HashMap<String, Method>();
	
	@Override
	public void init() {
		WebPointUtil.parse("", this, new DefaultWebPointFilter(), this);
		
	}
	
	@Override
	public String[] execute(String[] inputs, IToolWrapper toolWrapper,
			Map<String, Object> context) throws Exception {
		return inputs;
	}


	@Override
	public void destroy() {
		
	}

	@Override
	public boolean cacheMyClass() {
		return true;
	}

	@Override
	public boolean storeMyInstance() {
		return false;
	}

	@Override
	public void handle(Method method, Object instance, String path) {
		methodMap.put(path, method);
	}
	
	public Result invokeMethod(String path, HttpServletRequest req, 
			HttpServletResponse resp) throws Exception{
		Method m = methodMap.get(path);
		if(m != null){
			Object obj = m.invoke(this, req, resp);
			Result r = (Result) obj;
			return r;
		}
		return Result.error("没有对应的方法");
	}
	
	protected void buildHtml(Object dataModel, Writer out, String templateFile) throws Exception{
		ToolUtil.buildHtml(this.getClass(), dataModel, out, templateFile);
	}
	
	protected void buildHtml(String data, Writer out) throws Exception{
		Map<String, String> dataModel = new HashMap<String, String>();
		dataModel.put("data", data);
		ToolUtil.buildHtml(this.getClass(), dataModel, out, "index.fm");
	}

}
