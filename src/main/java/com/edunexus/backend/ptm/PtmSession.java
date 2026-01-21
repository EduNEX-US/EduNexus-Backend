package com.edunexus.backend.ptm;

import jakarta.persistence.*;
import java.time.*;

@Entity
public class PtmSession {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String ptmScope;    
 private String ptmTarget;   

 private Integer classNo;    
 private String studentId;   
 private String adminId;     

 private String teacherId;   

 private String meetLink;

 private LocalDate ptmDate;
 private LocalTime startTime;
 private LocalTime endTime;

 private String status;

//===================== PtmSession.java (Entity) =====================
//add this field + getters/setters

@Column(name = "purpose", length = 255)
private String purpose;

public String getPurpose() {
	return purpose; 
	}

public void setPurpose(String purpose) { 
	this.purpose = purpose; 
	}

 // ===== Join Allowed =====
 public boolean isJoinAllowed() {
   LocalDateTime now = LocalDateTime.now();
   LocalDateTime start = LocalDateTime.of(ptmDate, startTime);
   LocalDateTime end = LocalDateTime.of(ptmDate, endTime);
   return now.isAfter(start) && now.isBefore(end);
 }

 // ===== Expired after 1 day =====
 public boolean isExpired() {
   LocalDateTime expiry = LocalDateTime.of(
        ptmDate.plusDays(1),
        LocalTime.of(23,59,59)
   );
   return LocalDateTime.now().isAfter(expiry);
 }

 public String getMeetLink() {
	return meetLink;
 }

 public void setMeetLink(String meetLink) {
	this.meetLink = meetLink;
 }

 public String getPtmTarget() {
	return ptmTarget;
 }

 public void setPtmTarget(String ptmTarget) {
	this.ptmTarget = ptmTarget;
 }

 public String getPtmScope() {
	return ptmScope;
 }

 public void setPtmScope(String ptmScope) {
	this.ptmScope = ptmScope;
 }

 public Integer getClassNo() {
	return classNo;
 }

 public void setClassNo(Integer classNo) {
	this.classNo = classNo;
 }

 public String getStudentId() {
	return studentId;
 }

 public void setStudentId(String studentId) {
	this.studentId = studentId;
 }

 public String getAdminId() {
	return adminId;
 }

 public void setAdminId(String adminId) {
	this.adminId = adminId;
 }

 public String getTeacherId() {
	return teacherId;
 }

 public void setTeacherId(String teacherId) {
	this.teacherId = teacherId;
 }

 public String getStatus() {
	return status;
 }

 public void setStatus(String status) {
	this.status = status;
 }

 public LocalDate getPtmDate() {
	 return ptmDate;
 }
 
 public void setPtmDate(LocalDate ptmDate) {
	 this.ptmDate = ptmDate;
 }
 
 public LocalTime getStartTime() {
	 return startTime;
 }
 
 public void setStartTime(LocalTime startTime) {
	 this.startTime = startTime;
 }
 
 public LocalTime getEndTime() {
	 return endTime;
 }
 
 public void setEndTime(LocalTime endTime) {
	 this.endTime = endTime;
 }
 
 public void setId(Long id) {
	 this.id = id;
 }
 
 public Long getId() {
	 return id;
 }
}
