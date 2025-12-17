package com.edunexus.backend.ptm;

import java.time.LocalDate;
import java.time.LocalTime;

public class PTMScheduleRequest {
	 private String sClass;
	  private String student; // studentId
	  private String teacher; // teacherId
	  private LocalDate date;
	  private LocalTime time;
	  
	  public String getSClass() {
		  return sClass;
	  }
	  public void setSClass(String sClass) {
		  this.sClass = sClass;
	  }
	  public String getStudent() {
		  return student;
	  }
	  public void setStudent(String student) {
		  this.student = student;
	  }
	  public String getTeacher() {
		  return teacher;
	  }
	  public void setTeacher(String teacher) {
		  this.teacher = teacher;
	  }
	  public LocalDate getDate() {
		  return date;
	  }
	  public void setDate(LocalDate date) {
		  this.date = date;
	  }
	  public LocalTime getTime() {
		  return time;
	  }
	  public void setTime(LocalTime time) {
		  this.time = time;
	  }
}
