package siddur.tool.core;

public class ScriptToolWrapper extends ToolWrapper{

	@Override
	public ITool getTool() {
		ScriptTool st = new ScriptTool();
		st.setFilepath(this.getToolfile());
		st.setLanguage(this.getDescriptor().getLang());
		st.setInputModel(this.getDescriptor().getInputModel());
		st.setOutputModel(this.getDescriptor().getOutputModel());
		return st;
	}

	@Override
	public Class<?> getToolClass(){
		return ScriptTool.class;
	}
}
