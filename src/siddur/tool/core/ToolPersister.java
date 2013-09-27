package siddur.tool.core;

import java.io.File;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.commons.io.FileUtils;

import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.Constants;
import siddur.common.miscellaneous.FileSystemUtil;
import siddur.tool.cloud.ToolInfo;
import siddur.tool.core.data.ToolDescriptor;
import siddur.tool.core.data.XmlUtil;

public class ToolPersister {

	/*
	 * save or update
	 */
	public void saveTool(ToolDescriptor td, File toolFile) throws Exception{
		File parent = new File(FileSystemUtil.getToolDir(), td.getPluginID());
		
		EntityManager em = JPAUtil.newEntityMgr();
		ToolInfo tool = em.find(ToolInfo.class, td.getPluginID());
		if(tool == null){
			//13=new Date().toString().length
			if(toolFile.getName().endsWith(".zip")){
				File dir = null;
				File unzipped = ZipUtil.unZip(toolFile);
				File[] list = unzipped.listFiles();
				if(list.length == 1){
					File f = list[0];
					if(f.isDirectory()){
						dir = f;
					}else{
						dir = unzipped;
					}
				}
				FileUtils.copyDirectory(dir, parent);
			}else{
				File dest = new File(parent, toolFile.getName().substring(13));
				FileUtils.copyFile(toolFile, dest);
			}
			tool = new ToolInfo();
			tool.setId(td.getPluginID());
			EntityTransaction et = em.getTransaction();
			et.begin();
			em.persist(tool);
			et.commit();
			em.close();
		}
		
		XmlUtil.toXml(td, new File(parent, Constants.TOOL_PLUGIN_FILENAME));
	}
	
	public void updateToolFile(File toolFile, String toolID) throws Exception{
		File parent = new File(FileSystemUtil.getToolDir(), toolID);
		if(!parent.isDirectory()){
			throw new Exception("The tool directory doesn't exist");
		}
		//13=new Date().toString().length
		FileUtils.copyFile(toolFile, new File(parent, toolFile.getName().substring(13)));
	}
	
	public void updateToolDescriptor(ToolDescriptor td) throws Exception{
		File parent = new File(FileSystemUtil.getToolDir(), td.getPluginID());
		XmlUtil.toXml(td, new File(parent, Constants.TOOL_PLUGIN_FILENAME));
	}
	
	public void updateToolStatus(String toolID, int status){
		EntityManager em = JPAUtil.newEntityMgr();
		ToolInfo tool = em.find(ToolInfo.class, toolID);
		EntityTransaction et = em.getTransaction();
		et.begin();
		tool.setStatus(status);
		et.commit();
		em.close();
	}
	
	public void updateToolStatus(String toolID, int status, EntityManager em){
		ToolInfo tool = em.find(ToolInfo.class, toolID);
		tool.setStatus(status);
	}
	
}
