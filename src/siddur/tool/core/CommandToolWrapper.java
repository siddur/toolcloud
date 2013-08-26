package siddur.tool.core;

public class CommandToolWrapper extends ToolWrapper{

	@Override
	public ITool getTool() {
		CommandTool st = null;
		String lang = this.getDescriptor().getLang();
		if(lang.equals("cmd") || lang.equals("bash")){
			st = new BuildinTool();
		}else{
			st = new ScriptTool();
		}
		st.setLanguage(lang);
		st.setFilepath(this.getToolfile());
		st.setInputModel(this.getDescriptor().getInputModel());
		st.setOutputModel(this.getDescriptor().getOutputModel());
		return st;
	}

	@Override
	public Class<?> getToolClass(){
		return ScriptTool.class;
	}
}
