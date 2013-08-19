package siddur.common.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RoleInfo {

	public RoleInfo(Integer roleId){
		this.roleId = roleId;
	}
	
	public RoleInfo(){
		
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer roleId;
	
	@Column(length=20)
	private String rolename;
	
	@Column
	private Integer permission;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	
	
}
