package siddur.common.miscellaneous;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClickInfo {
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Column private String who;
	@Column private String ip;
	@Column private Date runAt = new Date();
	@Column private String target;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWho() {
		return who;
	}
	public void setWho(String who) {
		this.who = who;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getRunAt() {
		return runAt;
	}
	public void setRunAt(Date runAt) {
		this.runAt = runAt;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
}
