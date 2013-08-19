package siddur.tool.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.FileSystemUtil;
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
	
	@Override
	public void loadAll() {
		List<ToolInfo> tools = JPAUtil.findAll(ToolInfo.class);
		for(ToolInfo t : tools){
			load(t.getId());
		}
	}

	@Override
	public void load(String toolID) {
		try {
			IToolWrapper tw = toolLoader.loadTool(toolID);
			toolMap.put(toolID, tw);
			log4j.info("Successfully load tool with ID " + toolID);
		} catch (Exception e) {
			log4j.info("Fail to load tool with ID " + toolID);
			log4j.warn(e);
		}
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
			toolPersister.updateToolStatus(toolID, -1, em);
			
			File toolDir = new File(FileSystemUtil.getToolDir(), toolID);
			FileUtils.deleteDirectory(toolDir);
			
			unload(toolID);
			et.commit();
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
			log4j.warn(e);
		}
	}

	protected String[] execute(IToolWrapper tw, String[] params, Map<String, Object> context) throws Exception {
		return tw.getTool().execute(params);
	}
	
	@Override
	public String[] run(String toolID, String[] params, Map<String, Object> context) {
		IToolWrapper tw = toolMap.get(toolID);
		if(tw == null){
			throw new ToolNotFoundException(toolID);
		}
		
		boolean approved = (tw.getStatus() == 1);
		if(!approved){
			throw new ToolNotApprovedException(toolID);
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

	@Override
	public void approve(String pluginID) {
		toolMap.get(pluginID).setStatus(1);
		toolPersister.updateToolStatus(pluginID, 1);
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
					File src = null;;
					
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
					output[i] = relativePath;
				}
			}
		}
	}
	
}
