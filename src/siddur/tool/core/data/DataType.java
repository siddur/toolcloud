package siddur.tool.core.data;

import java.util.ArrayList;
import java.util.List;

public enum DataType {
	EMPTY("empty", false, true),
	STRING("string", true, true),
	INT("integer", false, true),
	DOUBLE("double", false, true),
	BOOLEAN("boolean", false, true),
	DATE("date", false, true),
	TEXT("text", false, true),
	TABLE("table", false, true),
	HTML("html", true, false),
	FILE("file", true, true),
	ZIPFILE("zipfile", false, true),
	FILETREE("filetree", true, false),
	IMAGE("image", true, true);
	
	private String label;
	private boolean out;
	private boolean in;
	
	private DataType(String label, boolean out, boolean in) {
		this.label = label;
		this.out = out;
		this.in = in;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isOut() {
		return out;
	}
	public void setOut(boolean out) {
		this.out = out;
	}
	public boolean isIn() {
		return in;
	}
	public void setIn(boolean in) {
		this.in = in;
	}
	
	public static List<DataType> getOutTypes(){
		List<DataType> out = new ArrayList<DataType>();
		for (DataType dt : DataType.values()) {
			if(dt.out){
				out.add(dt);
			}
		}
		return out;
	}
	
	public static List<DataType> getInTypes(){
		List<DataType> in = new ArrayList<DataType>();
		for (DataType dt : DataType.values()) {
			if(dt.in){
				in.add(dt);
			}
		}
		return in;
	}
}
