package siddur.tool.core.data;


public class DataTemplate {

	private String dataType = DataType.STRING.name();
	
	// -r-x etc.. 
	//If java, precede none
	private String tag = ""; //-r -x
	
	private String defaultValue = "";
	
	private String description = "";
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isFile(){
		return DataType.FILE.equals(dataType);
	}
	
	public boolean isZip(){
		return DataType.ZIPFILE.equals(dataType);
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
