package com.ssafy.happyhouse.model;

import java.util.Date;

public class QnADto {

	private Long id;
	
	private String title;
	
	private String content;
	
	private String userName;
	
	private Date createdAt;
	
	private Date updatedAt;

	public QnADto() {};

	public QnADto(Long id, String title, String content, String userName, Date createdAt,
			Date updatedAt) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.userName = userName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "QnADto [id=" + id + ", title=" + title + ", content=" + content + ", userName=" + userName
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
