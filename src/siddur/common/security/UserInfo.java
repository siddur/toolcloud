package siddur.common.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class UserInfo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer userId;
	
	@Column(nullable=false, unique=true)
	private String username;
	
	@Column
	private String password;
	
	@ManyToOne
	private RoleInfo role;
	
	@Column
	private String email;
	
	@Column
	private String realname;
	
	@Column
	private String nickname;
	
	
	
	
	public Integer getUserId() {
		return userId;
	}




	public void setUserId(Integer userId) {
		this.userId = userId;
	}




	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public RoleInfo getRole() {
		return role;
	}




	public void setRole(RoleInfo role) {
		this.role = role;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public String getRealname() {
		return realname;
	}




	public void setRealname(String realname) {
		this.realname = realname;
	}




	public String getNickname() {
		return nickname;
	}




	public void setNickname(String nickname) {
		this.nickname = nickname;
	}




	public String getName(){
		if(nickname == null || nickname.equals("")){
			return username;
		}
		return nickname;
	}
}
