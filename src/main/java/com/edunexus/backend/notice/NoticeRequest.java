package com.edunexus.backend.notice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NoticeRequest {
	@JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("date")
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
