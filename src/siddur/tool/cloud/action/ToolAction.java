package siddur.tool.cloud.action;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import siddur.common.miscellaneous.ClickInfo;
import siddur.common.miscellaneous.Comment;
import siddur.common.miscellaneous.Constants;
import siddur.common.miscellaneous.FileSystemUtil;
import siddur.common.miscellaneous.Paging;
import siddur.common.miscellaneous.QueryInfo;
import siddur.common.miscellaneous.RunInfo;
import siddur.common.security.DoNotAuthenticate;
import siddur.common.security.Permission;
import siddur.common.security.RequestUtil;
import siddur.common.security.UserInfo;
import siddur.common.web.ActionMapper.Result;
import siddur.common.web.DBAction;
import siddur.common.web.Perm;
import siddur.tool.cloud.ToolInfo;
import siddur.tool.core.ConsoleTool;
import siddur.tool.core.IToolManager;
import siddur.tool.core.IToolWrapper;
import siddur.tool.core.MemoryVisitor;
import siddur.tool.core.ScriptUtil;
import siddur.tool.core.data.DataTemplate;
import siddur.tool.core.data.ToolDescriptor;

import com.google.gson.Gson;


public class ToolAction extends DBAction<Comment>{

	private Logger log4j = Logger.getLogger(ToolAction.class);
	public static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private IToolManager tpm;
	
	@Override
	public void init(Map<String, Object> context) {
		super.init(context);
		tpm = (IToolManager)context.get(Constants.TOOL_PLUGIN_MANAGE);
	}

	@DoNotAuthenticate
	public Result home(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int maxRecord = 10;
		EntityManager em = getEntityManager(req);
		TypedQuery<String> hotQ = em.createQuery("select t.id from ToolInfo t order by t.clicks desc", String.class);
		TypedQuery<String> likeQ = null;
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u != null){
			likeQ = em.createQuery("select r.target from RunInfo r where r.who = '"+u.getUsername()+"' group by r.target order by max(r.startAt) desc", String.class);
		}else{
			String ip = req.getRemoteAddr();
			likeQ = em.createQuery("select r.target from RunInfo r where r.ip = '"+ip+"' group by r.target order by max(r.startAt) desc", String.class);
		}
		TypedQuery<QueryInfo> queryQ = em.createQuery("from QueryInfo q order by q.publishAt desc", QueryInfo.class);
		
		hotQ.setMaxResults(maxRecord);
		likeQ.setMaxResults(maxRecord);
		queryQ.setMaxResults(maxRecord);
		
		req.setAttribute("hottest", getVisitor().findAll(hotQ.getResultList()));
		req.setAttribute("latest", getVisitor().findLatestOnes(maxRecord));
		req.setAttribute("favorite", getVisitor().findAll(likeQ.getResultList()));
		req.setAttribute("queries", queryQ.getResultList());
		
		return Result.forward("/home.jsp");
	}
	
	@DoNotAuthenticate
	public Result list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String key = req.getParameter("key");
		int pageSize = 12;
		int pageIndex = 1;
		try {
			pageSize = Integer.parseInt(req.getParameter("pageSize"));
			pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
		} catch (NumberFormatException e) {
		}
		Paging<IToolWrapper> paging = getVisitor().findAll(key, pageSize, pageIndex, true);
		req.setAttribute("paging", paging);
		req.setAttribute("key", key);
		req.setAttribute("mine", false);
		req.setAttribute("editable", false);
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
		
		ClickInfo c = new ClickInfo();
		c.setTarget(id);
		c.setIp(req.getRemoteAddr());
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u != null){
			c.setWho(u.getUsername());
		}
		
		if(tpu.getStatus() == 1){
			ToolInfo tInfo = getEntityManager(req).find(ToolInfo.class, id);
			if(tInfo != null){
				tInfo.setClicks(tInfo.getClicks() + 1);
				getEntityManager(req, true).persist(tInfo);
			}
		}
		getEntityManager(req, true).persist(c);
		
		req.setAttribute("canDelComment", RequestUtil.hasPerm(req, Permission.COMMENT_DEL));
		
		boolean editable = RequestUtil.hasPerm(req, Permission.TOOL_EDIT) 
				&& tpu.getDescriptor().getAuthorId().equals(u.getUserId() + "");
		req.setAttribute("updatable", editable);
		req.setAttribute("approve", RequestUtil.hasPerm(req, Permission.TOOL_APPROVE));
		req.setAttribute("similars", getVisitor().findAll(tpu.getDescriptor().getSimilars()));
		
		if(tpu.getDescriptor().getLang().equals("client-side")){
			File f = new File(tpu.getToolfile());
			File dir = f.getParentFile();
			FileSystemUtil.copy2Temp(dir);
			req.setAttribute("toolFile", dir.getName() + "/" + f.getName());
			return Result.forward("/jsp/tool/tool-cs-detail.jsp");
		}else{
			boolean needConsole = ConsoleTool.class.isAssignableFrom(tpu.getToolClass());
			req.setAttribute("needConsole", needConsole);
			return Result.forward("/jsp/tool/tool-ss-detail.jsp");
		}
	}
	
	@Perm(Permission.TOOL_PUB)
	public Result toAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.setAttribute("langs", ScriptUtil.getLangs());
		return Result.forward("/jsp/tool/tool-add.jsp");
	}
	
	public Result mine(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int pageSize = 12;
		int pageIndex = 1;
		try {
			pageSize = Integer.parseInt(req.getParameter("pageSize"));
			pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
		} catch (NumberFormatException e) {
		}
		
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		Paging<IToolWrapper> paging = null;
		if(u.isAdmin()){
			paging = getVisitor().findAll("", pageSize, pageIndex, false);
		}else{
			paging = getVisitor().findMine(u.getUserId() + "", pageSize, pageIndex);
		}
		req.setAttribute("paging", paging);
		
		req.setAttribute("mine", true);
		req.setAttribute("editable", RequestUtil.hasPerm(req, Permission.TOOL_EDIT));
		
		return Result.forward("/jsp/tool/tool-list.jsp");
	}
	
	@Perm(Permission.TOOL_EDIT)
	public Result update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String id = req.getParameter("toolId");
		IToolWrapper tpu = getVisitor().findById(id);
		req.setAttribute("tool", tpu);
		return Result.forward("/jsp/tool/tool-update.jsp");
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
			UserInfo u = (UserInfo)req.getSession().getAttribute("user");
			if(u.isAdmin()){
				pd.setExt(true);
			}
		}
		populate(req, pd);
		
		tpm.save(pd, toolFile);		
		return mine(req, resp);
	}
	
	private void populate(HttpServletRequest req, ToolDescriptor pd){
		String lang = req.getParameter("lang");
		if(lang != null)
			pd.setLang(lang);
		
		pd.setIcon(req.getParameter("icon"));
		pd.setOverrodeParam(req.getParameter("overrodeParam"));
		pd.setPluginName(req.getParameter("name"));
		pd.setDescription(req.getParameter("description"));
		pd.setCatalog(req.getParameter("catelog"));
		
		String iTags[] = null;
		iTags = req.getParameterValues("i_tag");
		String iDataTypes[] = req.getParameterValues("i_dataType");
		String iDescriptions[] = req.getParameterValues("i_description");
		String iConstaints[] = req.getParameterValues("i_constraint");
		if(iDataTypes != null){
			DataTemplate[] iData = new DataTemplate[iDataTypes.length];
			for (int i = 0; i < iData.length; i++) {
				DataTemplate td = new DataTemplate();
				td.setTag(iTags[i]);
				td.setDataType(iDataTypes[i]);
				td.setDescription(iDescriptions[i]);
				td.setConstraint(iConstaints[i]);
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
		String toolID = (req.getParameter("id"));
		List<IToolWrapper> list = getVisitor().findAll();
		for (IToolWrapper tw : list) {
			ToolDescriptor td = tw.getDescriptor();
			boolean dele = td.deleSimilar(toolID);
			if(dele){
				tpm.save(td);
			}
		}
		tpm.delete(toolID);
		return blocks(req, resp);
	}
	
	@DoNotAuthenticate
	public Result exec(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String toolID = (req.getParameter("id"));
		String[] params = req.getParameterValues("input[]");
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(Constants.TICKET, req.getParameter(Constants.TICKET));
		context.put(Constants.FILE_ENCODING, req.getParameterValues(Constants.FILE_ENCODING + "[]"));

		String[] splitters = req.getParameterValues("splitter[]");
		if(splitters != null && splitters.length > 0){
			Map<Integer, String> splitMap = new HashMap<Integer, String>(splitters.length);
			for (int i = 0; i < splitters.length; i++) {
				String item = splitters[i];
				int index = item.indexOf("=");
				splitMap.put(Integer.parseInt(item.substring(0, index)), item.substring(index + 1));
			}
			context.put(Constants.SPLIT_MAP, splitMap);
		}
		String[] results = null;
		
		RunInfo run = new RunInfo();
		run.setTarget(toolID);
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u != null){
			run.setWho(u.getUsername());
			
			boolean hasPerm = RequestUtil.hasPerm(req, Permission.TOOL_RUN);
			context.put(Constants.HAS_PERM, hasPerm);
			context.put(Constants.USER, u);
		}
		run.setIp(req.getRemoteAddr());
		log4j.info("Running tool with ID" + toolID);
		try {
			results = tpm.run(toolID, params, context);
		}catch(Exception e){
			log4j.warn(e.getMessage(), e);
			return Result.ajax("error=" + e.getMessage());
		}finally{
			run.setEndAt(new Date());
			run.setSuccess(results != null);
			getEntityManager(req, true).persist(run);
			
			if(tpm.getToolWrapper(toolID).getStatus() == 1){
				ToolInfo tInfo = getEntityManager(req).find(ToolInfo.class, toolID);
				if(tInfo != null){
					tInfo.setRuns(tInfo.getRuns() + 1);
					getEntityManager(req, true).persist(tInfo);
				}
			}
		}
		return Result.ajax(new Gson().toJson(results));
	}
	
	@DoNotAuthenticate
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
		String toolId = req.getParameter("toolId");
		c.setSubject(toolId);
		add(c, req);
		return Result.redirect("tool/detail?toolId=" + toolId);
	}
	
	@Perm(Permission.COMMENT_DEL)
	public Result delComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String s = req.getParameter("id");
		if(s != null){
			int commentId = Integer.parseInt(s);
			delete(commentId, req);
		}
		return Result.redirect("tool/detail?toolId=" + req.getParameter("subjectId"));
	}
	
	@Perm(Permission.TOOL_APPROVE)
	public Result approve(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String toolId = req.getParameter("toolId");
		tpm.approve(toolId);
		return Result.redirect("tool/detail?toolId=" + toolId);
	}
	
	@Perm(Permission.TOOL_SETTING)
	public Result blocks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String id = req.getParameter("toolId");
		List<IToolWrapper> list;
		IToolWrapper tpu = null;
		if(id != null){
			tpu = getVisitor().findById(id);
			
			String keywords = req.getParameter("keywords");
			if(keywords != null){
				String similars = req.getParameter("similars");
				ToolDescriptor td = tpu.getDescriptor();
				td.setKeywords(keywords);
				td.setSimilars(similars);
				tpm.save(td);
				list = getVisitor().findAll();
				tpu = getVisitor().findById(id);
			}else{
				list = getVisitor().findAll();
			}
		}else{
			list = getVisitor().findAll();
			tpu = list.get(0);
		}
		req.setAttribute("current", tpu);
		req.setAttribute("list", list);
		req.setAttribute("crumb", "manage > setting");
		return Result.forward("/jsp/tool/tool-blocks.jsp");
		
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
