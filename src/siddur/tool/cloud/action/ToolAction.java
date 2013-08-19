package siddur.tool.cloud.action;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.common.miscellaneous.ClickInfo;
import siddur.common.miscellaneous.Comment;
import siddur.common.miscellaneous.Constants;
import siddur.common.miscellaneous.FileSystemUtil;
import siddur.common.miscellaneous.Paging;
import siddur.common.miscellaneous.RunInfo;
import siddur.common.security.DoNotAuthenticate;
import siddur.common.security.Permission;
import siddur.common.security.RequestUtil;
import siddur.common.security.UserInfo;
import siddur.common.web.ActionMapper.Result;
import siddur.common.web.DBAction;
import siddur.common.web.Perm;
import siddur.tool.core.IToolManager;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.MemoryVisitor;
import siddur.tool.core.data.DataTemplate;
import siddur.tool.core.data.ToolDescriptor;

import com.google.gson.Gson;


public class ToolAction extends DBAction<Comment>{

	public static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private IToolManager tpm;
	
	@Override
	public void init(Map<String, Object> context) {
		super.init(context);
		tpm = (IToolManager)context.get(Constants.TOOL_PLUGIN_MANAGE);
	}

	@DoNotAuthenticate
	public Result list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String key = req.getParameter("key");
		int pageSize = 10;
		int pageIndex = 1;
		try {
			pageSize = Integer.parseInt(req.getParameter("pageSize"));
			pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
		} catch (NumberFormatException e) {
		}
		Paging<IToolWrapper> paging = getVisitor().findAll(key, pageSize, pageIndex);
		req.setAttribute("paging", paging);
		req.setAttribute("key", key);
		return Result.forward("/jsp/tool/tool-list.jsp");
		
	}
	
	@DoNotAuthenticate
	public Result detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String id = req.getParameter("toolId");
		List<Comment> comments = getEntityManager(req)
				.createQuery("from Comment c where c.subject='"+ id +"'", Comment.class).getResultList();
		req.setAttribute("comments", comments);
		
		IToolWrapper tpu = getVisitor().findById(id);
		req.setAttribute("tool", tpu);
		req.getRequestDispatcher("/jsp/tool/tool-detail.jsp").forward(req, resp);
		
		ClickInfo c = new ClickInfo();
		c.setIp(req.getRemoteAddr());
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u != null){
			c.setWho(u.getUsername());
		}
		getEntityManager(req).persist(c);
		return Result.ok();
	}
	
	@Perm(Permission.TOOL_EDIT)
	public Result update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String id = req.getParameter("toolId");
		IToolWrapper tpu = getVisitor().findById(id);
		req.setAttribute("tool", tpu);
		req.getRequestDispatcher("/jsp/tool/tool-update.jsp").forward(req, resp);
		return Result.ok();
	}
	
	public Result save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String id = req.getParameter("toolId");
		ToolDescriptor pd = null;
		File toolFile = null;
		if(id != null){
			if(!RequestUtil.hasPerm(req, Permission.TOOL_EDIT)){
				return Result.Forbidden;
			}
			pd = getVisitor().findById(id).getDescriptor();
		}
		else{
			if(!RequestUtil.hasPerm(req, Permission.TOOL_PUB)){
				return Result.Forbidden;
			}
			pd = new ToolDescriptor();
			toolFile = FileSystemUtil.getFileByRelativePath(req.getParameter("toolfile"));
		}
		populate(req, pd);
		
		tpm.save(pd, toolFile);		
		return list(req, resp);
	}
	
	private void populate(HttpServletRequest req, ToolDescriptor pd){
		String lang = req.getParameter("lang");
		if(lang != null)
			pd.setLang(lang);
		
		pd.setIcon(req.getParameter("icon"));
		pd.setPluginName(req.getParameter("name"));
		pd.setDescription(req.getParameter("description"));
		pd.setCatalog(req.getParameter("catelog"));
		
		String iTags[] = null;
		iTags = req.getParameterValues("i_tag");
		String iDataTypes[] = req.getParameterValues("i_dataType");
		String iDescriptions[] = req.getParameterValues("i_description");
		if(iDataTypes != null){
			DataTemplate[] iData = new DataTemplate[iDataTypes.length];
			for (int i = 0; i < iData.length; i++) {
				DataTemplate td = new DataTemplate();
				td.setTag(iTags[i]);
				td.setDataType(iDataTypes[i]);
				td.setDescription(iDescriptions[i]);
				iData[i] = td;
			}
			pd.setInputModel(iData);
		}
		
		String oDefaultValue[] = req.getParameterValues("o_default");
		String oDataTypes[] = req.getParameterValues("o_dataType");
		String oDescriptions[] = req.getParameterValues("o_description");
		if(oDataTypes != null){
			DataTemplate[] oData = new DataTemplate[oDataTypes.length];
			for (int i = 0; i < oData.length; i++) {
				DataTemplate td = new DataTemplate();
				if(oDefaultValue != null){
					td.setDefaultValue(oDefaultValue[i]);
				}
				td.setDataType(oDataTypes[i]);
				td.setDescription(oDescriptions[i]);
				oData[i] = td;
			}
			pd.setOutputModel(oData);
		}
		
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		pd.setAuthorId(u.getUserId() + "");
	}
	
	@Perm(Permission.TOOL_DEL)
	public Result delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		tpm.delete(req.getParameter("id"));
		return list(req, resp);
	}
	
	@Perm(Permission.TOOL_RUN)
	public Result exec(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String toolID = (req.getParameter("id"));
		String[] params = req.getParameterValues("input[]");
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(Constants.TICKET, req.getParameter(Constants.TICKET));
		String[] results = null;
		
		
		RunInfo run = new RunInfo();
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u != null){
			run.setWho(u.getUsername());
		}
		run.setIp(req.getRemoteAddr());
		try {
			results = tpm.run(toolID, params, context);
		} finally{
			run.setEndAt(new Date());
			run.setSuccess(results != null);
			getEntityManager(req).persist(run);
		}
		return Result.ajax(new Gson().toJson(results));
	}
	
	public Result comment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo) req.getSession().getAttribute(Constants.USER);
		String saidBy = null;
		if(u != null){
			saidBy = u.getName();
		}else{
			saidBy = req.getRemoteHost();
		}
		Comment c = new Comment();
		c.setSaidBy(saidBy);
		c.setContent(req.getParameter("comment"));
		c.setSubject(req.getParameter("toolId"));
		add(c, req);
		return detail(req, resp);
	}
	
	@Perm(Permission.COMMENT_DEL)
	public Result delComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String s = req.getParameter("id");
		if(s != null){
			int commentId = Integer.parseInt(s);
			delete(commentId, req);
		}
		return detail(req, resp);
	}
	
	private MemoryVisitor getVisitor(){
		MemoryVisitor t = new MemoryVisitor();
		tpm.accept(t);
		return t;
	}

	@Override
	public Class<Comment> getEntityClass() {
		return Comment.class;
	}
	
}
