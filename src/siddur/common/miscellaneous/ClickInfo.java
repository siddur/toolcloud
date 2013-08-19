package siddur.common.miscellaneous;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClickInfo {
	@Id private String id;
	@Column private String who;
	@Column private String ip;
	@Column private Date runAt = new Date();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
}
