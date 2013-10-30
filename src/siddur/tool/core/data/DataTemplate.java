package siddur.tool.core.data;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class DataTemplate {
	public static final String TEXT_SPLITTER = "|||";
	
	private String dataType = DataType.STRING.name();
	
	// -r-x etc.. 
	//If java, precede none
	private String tag = ""; //-r -x
	
	private String defaultValue = "";
	
	private String description = "";
	
	/*
	 * 格式：
	 * type|||content
	 * 类型：
	 * NOTNULL/ENUM/RANGE
	 * 例子
	 * NOTNULL
	 * RANGE|||value1|||value2
	 * ENUM|||value1|||description1|||value2|||description2
	 */
	private String constraint = "";
	
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
		return DataType.FILE.name().equals(dataType)
				|| DataType.IMAGE.name().equals(dataType)
				|| DataType.FILETREE.name().equals(dataType);
	}
	
	public boolean isZip(){
		return DataType.ZIPFILE.name().equals(dataType);
	}
	
	public boolean isTable(){
		return DataType.TABLE.name().equals(dataType);
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	
	public String getConstrantType(){
		if(!StringUtils.isEmpty(this.constraint)){
			int i = this.constraint.indexOf(TEXT_SPLITTER);
			if(i > 0){
				return this.constraint.substring(0, i);
			}
			return this.constraint;
		}
		return null;
	}
	
	public String[] getRange(){
		if("RANGE".equals(getConstrantType())){
			return Arrays.copyOfRange(this.constraint.split(TEXT_SPLITTER), 1, 3);
		}
		return null;
	}
	
	
	public String[][] getOptions(){
		if("ENUM".equals(getConstrantType())){
			String[] content = this.constraint.split("\\|\\|\\|");
			String[] array = Arrays.copyOfRange(content, 1, content.length);
			String[][] rr = new String[array.length/2][2];
			for (int i = 0; i < rr.length; i++) {
				rr[i] = new String[]{array[2*i], array[2*i+1]};
			}
			return rr;
		}
		return null;
	}
	
}
