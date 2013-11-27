package siddur.common.miscellaneous;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QueryInfo {
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column private Date publishAt = new Date();
	@Column private String saidBy;
	@Column private String title;
	
	@Column(length=1024 * 20)
	private String content;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getPublishAt() {
		return publishAt;
	}
	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}
	public String getSaidBy() {
		return saidBy;
	}
	public void setSaidBy(String saidBy) {
		this.saidBy = saidBy;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	
	public String getPlainContent(){
		return StringUtil.escape(this.content);
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
