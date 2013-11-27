package siddur.common.miscellaneous;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer commentId;
	
	@Column
	private Date saidAt = new Date();
	
	@Column
	private String saidBy;
	
	@Column(length=1024 * 5)
	private String content;
	
	@Column
	private String subject;

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Date getSaidAt() {
		return saidAt;
	}

	public void setSaidAt(Date saidAt) {
		this.saidAt = saidAt;
	}

	public String getSaidBy() {
		return saidBy;
	}

	public void setSaidBy(String saidBy) {
		this.saidBy = saidBy;
	}

	public String getContent() {
		return content;
	}
	
	public String getPreContent(){
		return StringUtil.escape(this.content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	
}
