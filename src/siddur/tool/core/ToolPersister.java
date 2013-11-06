package siddur.tool.core;

import java.io.File;
import javax.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

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
		File baseDir = td.isExt() ? FileSystemUtil.getExtDir() : FileSystemUtil.getToolDir();
		File parent = new File(baseDir, td.getPluginID());
		
		log4j.info("Start to save tool with ID " + td.getPluginID());
		if(toolFile != null){
			log4j.info("This is a new tool");
			//13=new Date().toString().length
			if(toolFile.getName().endsWith(".zip")){
				File unzipped = ZipUtil.unZip(toolFile);
				log4j.info("Unzip the tool file");
				log4j.info("Allocate the tool");
				FileUtils.copyDirectory(unzipped, parent);
			}else{
				File dest = new File(parent, toolFile.getName().substring(13));
				log4j.info("Allocate the tool");
				FileUtils.copyFile(toolFile, dest);
			}
		}
		
		XmlUtil.toXml(td, new File(parent, Constants.TOOL_PLUGIN_FILENAME));
		log4j.info("Save to file system");
		log4j.info("Successfully save the tool with ID " + td.getPluginID());
	}
	
//	public void updateToolFile(File toolFile, String toolID) throws Exception{
//		File parent = new File(FileSystemUtil.getToolDir(toolID), toolID);
//		if(!parent.isDirectory()){
//			throw new Exception("The tool directory doesn't exist");
//		}
//		//13=new Date().toString().length
//		FileUtils.copyFile(toolFile, new File(parent, toolFile.getName().substring(13)));
//	}
	
	public void updateToolDescriptor(ToolDescriptor td) throws Exception{
		File baseDir = td.isExt() ? FileSystemUtil.getExtDir() : FileSystemUtil.getToolDir();
		File parent = new File(baseDir, td.getPluginID());
		XmlUtil.toXml(td, new File(parent, Constants.TOOL_PLUGIN_FILENAME));
	}
	
	
	public void remove(String toolID, EntityManager em){
		ToolInfo tool = em.find(ToolInfo.class, toolID);
		em.remove(tool);
	}
	
	public void saveInfo(String toolID, EntityManager em){
		ToolInfo ti = new ToolInfo();
		ti.setId(toolID);
		em.persist(ti);
		
	}
	
}
