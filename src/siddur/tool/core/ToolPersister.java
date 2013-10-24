package siddur.tool.core;

import java.io.File;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import siddur.common.jpa.JPAUtil;
import siddur.common.miscellaneous.Constants;
import siddur.common.miscellaneous.FileSystemUtil;
import siddur.tool.cloud.ToolInfo;
import siddur.tool.core.data.ToolDescriptor;
import siddur.tool.core.data.XmlUtil;

public class ToolPersister {
	
	private final static Logger log4j = Logger.getLogger(ToolPersister.class);
	/*
	 * save or update
	 */
	public void saveTool(ToolDescriptor td, File toolFile) throws Exception{
		File parent = new File(FileSystemUtil.getToolDir(), td.getPluginID());
		
		log4j.info("Start to save tool with ID " + td.getPluginID());
		EntityManager em = JPAUtil.newEntityMgr();
		try {
			ToolInfo tool = em.find(ToolInfo.class, td.getPluginID());
			if(tool == null){
				log4j.info("This is a new tool");
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
					log4j.info("Unzip the tool file");
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
				log4j.info("Save to database");
				et.commit();
			}
		} finally {
			em.close();
		}
		
		XmlUtil.toXml(td, new File(parent, Constants.TOOL_PLUGIN_FILENAME));
		log4j.info("Save to file system");
		log4j.info("Successfully save the tool with ID " + td.getPluginID());
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
		try{
			ToolInfo tool = em.find(ToolInfo.class, toolID);
			EntityTransaction et = em.getTransaction();
			et.begin();
			tool.setStatus(status);
			et.commit();
		}finally{
			em.close();
		}
	}
	
	public void updateToolStatus(String toolID, int status, EntityManager em){
		ToolInfo tool = em.find(ToolInfo.class, toolID);
		tool.setStatus(status);
	}
	
}
