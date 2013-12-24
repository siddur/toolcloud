package siddur.common.miscellaneous;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TextResource {
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column private Date publishAt = new Date();
	@Column private String saidBy;
	@Column private String title;
	
	//1=blog 2=needs 3=info(news) 4=document
	@Column(length=1)
	private Integer type;
	
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
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
