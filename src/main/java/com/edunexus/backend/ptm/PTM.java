package com.edunexus.backend.ptm;

import java.time.LocalDate;
import java.time.LocalTime;

import com.edunexus.backend.student.Student;
import com.edunexus.backend.teacher.Teacher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name="ptm")
@Table(name="ptm")
public class PTM {
	@Id
	@Column(name="ptm_id")
	private String ptmId;
	
	@ManyToOne
	@JoinColumn(name="student_id", nullable=false)
	private Student student;
	
	@ManyToOne
	@JoinColumn(name="teacher_id", nullable=false)
	private Teacher teacher;
	
	@Column(name="class_name")
	private String className;
	
	@Column(name="ptm_date")
	private LocalDate ptmDate;
	
	@Column(name="ptm_time")
	private LocalTime ptmTime;
	
	@Column(name="status")
	private String status; //completed, scheduled

	public String getPtmId() {
		return ptmId;
	}

	public void setPtmId(String ptmId) {
		this.ptmId = ptmId;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public LocalDate getPtmDate() {
		return ptmDate;
	}

	public void setPtmDate(LocalDate ptmDate) {
		this.ptmDate = ptmDate;
	}

	public LocalTime getPtmTime() {
		return ptmTime;
	}

	public void setPtmTime(LocalTime ptmTime) {
		this.ptmTime = ptmTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
