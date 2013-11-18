package siddur.common.security;

public enum Permission{
	TOOL_RUN("工具", "使用所有工具"),
	TOOL_APPROVE("工具", "批准发布工具"),
	TOOL_PUB("工具", "发布工具"),
	TOOL_EDIT("工具", "修改工具"),
	TOOL_DEL("工具", "删除工具"),
	TOOL_SETTING("工具", "辅助设置"),
	
	USER_VIEW("用户", "查看用户信息"),
	USER_ADD("用户", "新增用户"),
	USER_EDIT("用户", "修改用户信息"),
	USER_ROLE("用户", "修改用户角色"),
	USER_DEL("用户", "删除用户"),
	
	ROLE_LIST_VIEW("角色", "查看角色列表"),
	ROLE_ADD("角色", "新增角色"),
	ROLE_EDIT("角色", "修改角色权限"),
	ROLE_VIEW("角色", "查看角色权限"),
	
	COMMENT_DEL("评论", "删除评论"),
	QUERY_DEL("需求", "删除需求");
	
	
	private String catelog;
	private String description;
	
	private Permission(String catelog, String description) {
		this.catelog = catelog;
		this.description = description;
	}
	
	public String getCatelog() {
		return catelog;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getFullName(){
		return catelog + ":" + description;
	}
}
