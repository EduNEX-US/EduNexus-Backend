package com.edunexus.backend.ptm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.edunexus.backend.student.Student;
import com.edunexus.backend.teacher.Teacher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "ptm")
@Table(name = "ptm")
public class PTM{

    @Id
    @Column(name="ptm_id", unique = true)
    private String ptmId;

    @ManyToOne
	@JoinColumn(name="teacher_id", nullable=false)
    private Teacher teacherId;
    
    @ManyToOne
	@JoinColumn(name="student_id", nullable=false)
    private Student studentId;

    @Column(name="class_name")
    private String className;
    
    @Column(name="ptm_time")
    private LocalTime ptmTime;
    
    @Column(name="ptm_date")
    private LocalDate ptmDate;

    @Column(name="status")
    private String status; // SCHEDULED, ACTIVE, COMPLETED
    
    public String getPtmId() {
		return this.ptmId;
	}

	public void setPtmId(String ptmId) {
		this.ptmId = ptmId;
	}

	public Student getStudent() {
		return studentId;
	}

	public void setStudent(Student student) {
		this.studentId = student;
	}

	public Teacher getTeacher() {
		return teacherId;
	}

	public void setTeacher(Teacher teacher) {
		this.teacherId = teacher;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	public LocalTime getPtmTime() {
		return ptmTime;
	}

	public void setPtmTime(LocalTime ptmTime) {
		this.ptmTime = ptmTime;
	}

	public LocalDate getPtmDate() {
		return this.ptmDate;
	}
	
	public void setPtmDate(LocalDate ptmDate) {
		this.ptmDate = ptmDate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}