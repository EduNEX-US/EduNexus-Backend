package com.edunexus.backend.teacher;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateTeacherRequest {

	@JsonProperty("tName")
	private String teacherName;
	
	@JsonProperty("pass")
	private String password;
	
	@JsonProperty("email")
	private String teacherEmail;
	
	@JsonProperty("tMob")
	private long teacherMob;
	
	
	@JsonProperty("exp")
	private int teacherExp;
	
	@JsonProperty("tAddress")
	private String teacherAdd;

	
	@JsonProperty("tClass")
	private int teacherClass;
	
	
	@JsonProperty("qualification")
	private String teacherQualification;
	
	@JsonProperty("role") // if role is admin/teacher
	private String role; 
	
	@JsonProperty("image")
	private String imageUrl;
	public String getImageUrl() {
		return imageUrl; 
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl; 
	}

	public String getTeacherQualification() {
		return teacherQualification;
	}

	public void setTeacherQualification(String teacherQualification) {
		this.teacherQualification = teacherQualification;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherEmail() {
		return teacherEmail;
	}

	public void setTeacherEmail(String teacherEmail) {
		this.teacherEmail = teacherEmail;
	}

	public long getTeacherMob() {
		return teacherMob;
	}

	public void setTeacherMob(long teacherMob) {
		this.teacherMob = teacherMob;
	}

	public String getTeacherAdd() {
		return teacherAdd;
	}

	public void setTeacherAdd(String teacherAdd) {
		this.teacherAdd = teacherAdd;
	}

	public int getTeacherExp() {
		return teacherExp;
	}

	public void setTeacherExp(int teacherExp) {
		this.teacherExp = teacherExp;
	}

	public int getTeacherClass() {
		return teacherClass;
	}

	public void setTeacherClass(int teacherClass) {
		this.teacherClass = teacherClass;
	}

	public String getRole() { // this is going to be returning role in body
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}