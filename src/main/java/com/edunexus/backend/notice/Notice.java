package com.edunexus.backend.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notices")
public class Notice {
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "body")
	private String body;
	
	@Column(name = "date")
	private String date;
	
	public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
