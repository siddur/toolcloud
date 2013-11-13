package siddur.tool.core.data;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class ToolDescriptor {
	private String pluginName;
	private String lang;
	private String catalog;
	private String description;
	private String keywords;
	private String similars;
	private String overrodeParam;
	private String icon;
	private String authorId;
	private Date publishAt = new Date();
	private DataTemplate[] inputModel;
	private DataTemplate[] outputModel;
	
	//not persisted
	private boolean isExt;
	
	public boolean isExt() {
		return isExt;
	}
	public void setExt(boolean isExt) {
		this.isExt = isExt;
	}
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
			return "Constants.WEBSITE_ROOT/ctrl/util/icon?words=" + pluginName;
		}
		if(!icon.startsWith("/")){
			return "Constants.WEBSITE_ROOT/file/" + icon;
		}
		return icon;
	}
	
	public void setIcon(String icon) {
		if(icon == null){
			icon = "";
		}
		this.icon = icon;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getSimilars() {
		return similars;
	}
	public void setSimilars(String similars) {
		this.similars = similars;
	}
	public String getOverrodeParam() {
		return overrodeParam;
	}
	public void setOverrodeParam(String overrodeParam) {
		this.overrodeParam = overrodeParam;
	}
	public boolean deleSimilar(String toolID){
		String similars = this.similars;
		if(StringUtils.isEmpty(similars))
			return false;
		int index = similars.indexOf(toolID);
		if(index > -1){
			similars = similars.replace(toolID, "").replace(",,", ",");
			if(similars.endsWith(",")){
				similars = similars.substring(0, similars.length() - 1);
			}
			else if(similars.startsWith(",")){
				similars = similars.substring(1, similars.length());
			}
			
			this.similars = similars;
			return true;
		}
		return false;
	}
}
