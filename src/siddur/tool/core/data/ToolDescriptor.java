package siddur.tool.core.data;

import java.util.Date;

public class ToolDescriptor {
	private String pluginName;
	private String lang;
	private String catalog;
	private String description;
	private String icon;
	private String authorId;
	private Date publishAt = new Date();
	private DataTemplate[] inputModel;
	private DataTemplate[] outputModel;
	
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public Date getPublishAt() {
		return publishAt;
	}
	
	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}
	
	public void setInputModel(DataTemplate[] inputModel) {
		this.inputModel = inputModel;
	}
	
	public DataTemplate[] getInputModel() {
		return inputModel;
	}
	
	public DataTemplate[] getOutputModel() {
		return outputModel;
	}
	
	public void setOutputModel(DataTemplate[] outputModel) {
		this.outputModel = outputModel;
	}
	
	public String getPluginID() {
		return publishAt.getTime() + "";
	}
	
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIcon() {
		return icon;
	}
	public String getDisplayIcon(){
		if(icon == null || icon.equals("")){
			return "/toolcloud/ctrl/util/icon?words=" + pluginName;
		}
		if(!icon.startsWith("/")){
			return "/toolcloud/file/" + icon;
		}
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	
	
}
