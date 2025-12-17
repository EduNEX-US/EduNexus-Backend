package com.edunexus.backend.student;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateStudentRequest {
//	private String studentId;
	@JsonProperty("sName")
	private String studentName;
	@JsonProperty("pass")
	private String studentPassword;
	
	@JsonProperty("email")
	private String studentEmail;
	@JsonProperty("sMob")
	private long studentMobile;
	@JsonProperty("address")
	private String studentAddress;
	
	@JsonProperty("altMob")
	private long studentAltMobile;
	@JsonProperty("guardian")
	private String studentGuardian;
	@JsonProperty("sClass")
	private String studentClass;
	@JsonProperty("basicFee")
	private double basicFee;
	@JsonProperty("role")
	private String role;
	
//	public String getStudentId() {
//		return studentId;
//	}
//	public void setStudentId(String studentId) {
//		this.studentId = studentId;
//	}
	public String getStudentPassword() {
		return studentPassword;
	}
	public void setStudentPassword(String studentPassword) {
		this.studentPassword = studentPassword;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentEmail() {
		return studentEmail;
	}
	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}
	public long getStudentMobile() {
		return studentMobile;
	}
	public void setStudentMobile(long studentMobile) {
		this.studentMobile = studentMobile;
	}
	public String getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}
	public String getStudentAddress() {
		return studentAddress;
	}
	public void setStudentAddress(String studentAddress) {
		this.studentAddress = studentAddress;
	}
	public long getStudentAltMobile() {
		return studentAltMobile;
	}
	public void setStudentAltMobile(long studentAltMobile) {
		this.studentAltMobile = studentAltMobile;
	}
	public String getStudentGuardian() {
		return studentGuardian;
	}
	public void setStudentGuardian(String studentGuardian) {
		this.studentGuardian = studentGuardian;
	}
	public double getBasicFee() {
		return basicFee;
	}
	public void setBasicFee(double basicFee) {
		this.basicFee = basicFee;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}