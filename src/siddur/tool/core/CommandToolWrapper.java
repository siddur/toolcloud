package siddur.tool.core;

public class CommandToolWrapper extends ToolWrapper{

	@Override
	public ITool getTool() {
		CommandTool st = null;
		String lang = this.getDescriptor().getLang();
		if(lang.equals("cmd")){
			st = new BuildinTool();
		}else{
			st = new ScriptTool();
		}
		return st;
	}

	@Override
	public Class<?> getToolClass(){
		return ScriptTool.class;
	}
}
