package siddur.tool.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.Constants;
import siddur.common.miscellaneous.FileSystemUtil;
import siddur.common.security.UserInfo;
import siddur.tool.cloud.ToolInfo;
import siddur.tool.core.data.DataTemplate;
import siddur.tool.core.data.ToolDescriptor;
import siddur.tool.core.exception.ExecuteException;
import siddur.tool.core.exception.ToolNotApprovedException;
import siddur.tool.core.exception.ToolNotFoundException;

public class BasicToolManager implements IToolManager{

	private final static Logger log4j = Logger.getLogger(BasicToolManager.class);
	
	private Map<String, IToolWrapper> toolMap = new HashMap<String, IToolWrapper>();
	private ToolLoader toolLoader = new ToolLoader();
	private ToolPersister toolPersister = new ToolPersister();
	private EntityManager em;
	
	@Override
	public void loadAll() {
		em = JPAUtil.newEntityMgr();
		try{
			//load tools in tool dir
			File[] files = FileSystemUtil.getToolDir().listFiles();
			for (File file : files) {
				String name = file.getName();
				if(name.length() == 13 && file.isDirectory()){
					load(name);
				}
			}
			
			//load tools in ext dir;
			files = FileSystemUtil.getExtDir().listFiles();
			for (File file : files) {
				String name = file.getName();
				if(name.length() == 13 && file.isDirectory()){
					load(name);
				}
			}
		}finally{
			em.close();
			em = null;
		}
	}

	@Override
	public IToolWrapper load(String toolID) {
		EntityManager tempEm = null;
		IToolWrapper tw = null;
		try {
			tw = toolLoader.loadTool(toolID);
			
			//fetch status from DB
			ToolInfo toolInfo = null;
			if(em != null){ //invoked by loadAll()
				toolInfo = em.find(ToolInfo.class, toolID);
			}else{ //invoked by save()
				tempEm = JPAUtil.newEntityMgr();
				toolInfo = tempEm.find(ToolInfo.class, toolID);
			}
			
			//if toolIn fo not null, this tool is approved
			if(toolInfo != null){
				tw.setStatus(1);
			}
			
			toolMap.put(toolID, tw);
			
			log4j.info("Successfully load tool with ID " + toolID);
		} catch (Exception e) {
			log4j.info("Fail to load tool with ID " + toolID);
			log4j.warn(e);
		} finally{
			if(tempEm != null){
				tempEm.close();
			}
		}
		
		return tw;
	}

	@Override
	public void unload(String toolID) {
		toolMap.remove(toolID);
	}

	@Override
	public void delete(String toolID) {
		EntityManager em = JPAUtil.newEntityMgr();
		EntityTransaction et = em.getTransaction();
		et.begin();
		try {
			log4j.info("Start to delete tool with ID " + toolID);
			
			log4j.info("Remove from DB");
			toolPersister.remove(toolID, em);
			
			log4j.info("Remove tool files");
			File toolDir = new File(FileSystemUtil.getToolDir(), toolID);
			if(!toolDir.isDirectory()){
				toolDir = new File(FileSystemUtil.getExtDir(), toolID);
			}
			FileUtils.deleteDirectory(toolDir);
			
			log4j.info("Remove from memory");
			unload(toolID);
			et.commit();
			log4j.info("Successfully delete tool with ID " + toolID);
		} catch (IOException e) {
			log4j.warn(e);
			et.rollback();
		} finally{
			if(em.isOpen()){
				em.close();
			}
		}
	}

	@Override
	public void save(ToolDescriptor td, File toolFile) {
		try {
			toolPersister.saveTool(td, toolFile);
			load(td.getPluginID());
		} catch (Exception e) {
			log4j.warn(e.getMessage(), e);
		}
	}
	
	@Override
	public void save(ToolDescriptor pd) {
		this.save(pd, null);
		
	}

	protected String[] execute(IToolWrapper tw, String[] params, Map<String, Object> context) throws Exception {
		return tw.getTool().execute(params, tw, context);
	}
	
	@Override
	public String[] run(String toolID, String[] params, Map<String, Object> context) {
		IToolWrapper tw = toolMap.get(toolID);
		if(tw == null){
			throw new ToolNotFoundException(toolID);
		}
		
		boolean approved = tw.getStatus() == 1;
		if(!approved){
			UserInfo user = (UserInfo)context.get(Constants.USER);
			if(user == null){
				throw new ToolNotApprovedException(toolID);
			}
			
			boolean isOwner = false;
			if(Integer.toString(user.getUserId()).equals(tw.getDescriptor().getAuthorId())){
				isOwner = true;
			}
			
			Boolean hasPerm = (Boolean)context.get(Constants.HAS_PERM);
			if(!isOwner && !hasPerm){
				throw new ToolNotApprovedException(toolID);
			}
		}

		String[] output = null;
		ToolDescriptor td = tw.getDescriptor();
		
		try {
			preProcess(params, td.getInputModel());
			print(params, td.getInputModel());
			//execute
			output = execute(tw, params, context);
			postProcess(output, td.getOutputModel());
			
		} catch (Exception e) {
			log4j.error(toolID, e);
			throw new ExecuteException(toolID);
		}
		
		return output;
	}

	/*
	 * Save ToolInfo
	 * Set status of tool wrapper
	 */
	@Override
	public void approve(String pluginID) {
		IToolWrapper tw = toolMap.get(pluginID);
		if(tw != null){
			EntityManager em = JPAUtil.newEntityMgr();
			EntityTransaction et = em.getTransaction();
			et.begin();
			try {
				toolPersister.saveInfo(pluginID, em);
				tw.setStatus(1);
				et.commit();
				log4j.info("Succssfully save tool info with ID " + pluginID);
			} catch (Exception e) {
				log4j.warn(e);
				et.rollback();
			} finally{
				if(em.isOpen()){
					em.close();
				}
			}
		}
	}

	@Override
	public void init() {
		loadAll();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int numOfTools() {
		return toolMap.size();
	}

	@Override
	public IToolWrapper getToolWrapper(String toolID) {
		return toolMap.get(toolID);
	}

	@Override
	public void accept(MapVisitor<String, IToolWrapper> visitor) {
		visitor.visit(toolMap);
	}

	
	/**
	 * @param input
	 * @param inputModel
	 * @throws IOException
	 * 1) change relative path(refers to temp dir) into absolute path
	 * 2) unpack zip file to dir(absolute path) 
	 * 3) deal with boolean type input
	 */
	protected void preProcess(String[] input, DataTemplate[] inputModel) throws IOException{
		if(inputModel != null){
			for (int i = 0; i < inputModel.length; i++) {
				if(inputModel[i].isFile()){
					input[i] = TempFileUtil.findFile(input[i]).getCanonicalPath();
				}
				else if(inputModel[i].isZip()){
					File zip = TempFileUtil.findFile(input[i]);
					File dir = ZipUtil.unZip(zip);
					input[i] = dir.getCanonicalPath();
				}
			}
		}
	}
	
	
	protected void print(String[] input, DataTemplate[] inputModel){
		if(inputModel != null){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < inputModel.length; i++) {
				DataTemplate td = inputModel[i];
				if(!StringUtils.isEmpty(td.getTag())){
					sb.append(td.getTag());
					sb.append(" ");
				}
				if(!StringUtils.isEmpty(input[i])){
					sb.append(input[i]);
					sb.append(" ");
				}
			}
			log4j.info(sb);
		}
	}
	
	/**
	 * @param output
	 * @param outputModel
	 * @throws IOException
	 * 1)change absolute path into relative path(refers to Temp dir)
	 * 2)copy file(or directory) to fileserver/output dir
	 */
	protected void postProcess(String[] output, DataTemplate[] outputModel) throws IOException{
		if(outputModel != null){
			for (int i = 0; i < outputModel.length; i++) {
				if(outputModel[i].isFile()){
					String path = output[i];
					if(path.indexOf("|||") > 0){
						StringBuilder sb = new StringBuilder();
						StringTokenizer st = new StringTokenizer(path, "|||");
						while(st.hasMoreTokens()){
							sb.append(dealFile(st.nextToken()));
							sb.append("|||");
						}
						output[i] = sb.substring(0, sb.length() - 3);
					}else{
						output[i] = dealFile(path);
					}
				}
			}
		}
	}
	
	private String dealFile(String path) throws IOException{
		File src = null;
		
		String relativePath = null;
		if(FileSystemUtil.isRelative(path)){
			src = new File(FileSystemUtil.getTempDir(), path);
			relativePath = path;
		}else{
			src = new File(path);
			relativePath = TempFileUtil.getRelativePath(src.getCanonicalPath());
		}
		File dest = new File(FileSystemUtil.getOutputDir(), relativePath);
		if(src.isFile())
			FileUtils.copyFile(src, dest);
		else if(src.isDirectory())
			FileUtils.copyDirectory(src, dest);
		return relativePath;
	}
	
}
