package siddur.tool.cloud;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.common.jpa.EntityManagerWrapper;
import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.Constants;
import siddur.common.web.ActionMapper;
import siddur.tool.core.IToolManager;
import siddur.tool.core.WebBatchToolManager;

public class ControlServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private siddur.common.web.ActionMapper actionMapper;
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		config.getServletContext().setAttribute("root", Constants.WEBSITE_ROOT);
		
		HashMap<String, Object> context = new HashMap<String, Object>();
		//init tool
		IToolManager tm = new WebBatchToolManager();
		tm.init();
		context.put(Constants.TOOL_PLUGIN_MANAGE, tm);
		
		//init db
		JPAUtil.init();
		//init entitymanage wrapper
		context.put(Constants.ENTITY_MANAGER_WRAPPER, new EntityManagerWrapper());
		
		
		actionMapper = new ActionMapper(context);
		
		super.init(config);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		actionMapper.doAction(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
}
