package com.edunexus.backend.teacher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher {
	@Id
	@Column(name = "teacher_id")
	private String teacher_id;
	
	@Column(name = "teacher_name")
	private String teacher_name;
	
	@Column(name = "teacher_email")
	private String teacher_email;
	
	@Column(name = "teacher_mobile")
	private long teacher_mob;
	
	@Column(name = "teacher_address")
	private String teacher_add;
	
	@Column(name = "teacher_experience")
	private int teacher_exp;
	
	@Column(name = "teacher_class")
	private String teacher_class;
	
	@Column(name = "isAdmin")
	private int isAdmin;
	
	@Column(name = "teacher_qualification")
	private String teacher_qualification;
	
	public String getTeacher_qualification() {
		return teacher_qualification;
	}

	public void setTeacher_qualification(String teacher_qualification) {
		this.teacher_qualification = teacher_qualification;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getTeacher_email() {
		return teacher_email;
	}

	public void setTeacher_email(String teacher_email) {
		this.teacher_email = teacher_email;
	}

	public long getTeacher_mob() {
		return teacher_mob;
	}

	public void setTeacher_mob(long teacher_mob) {
		this.teacher_mob = teacher_mob;
	}
	public String getTeacher_add() {
		return teacher_add;
	}

	public void setTeacher_add(String teacher_add) {
		this.teacher_add = teacher_add;
	}

	public int getTeacher_exp() {
		return teacher_exp;
	}

	public void setTeacher_exp(int teacher_exp) {
		this.teacher_exp = teacher_exp;
	}

	public String getTeacher_class() {
		return teacher_class;
	}

	public void setTeacher_class(String teacher_class) {
		this.teacher_class = teacher_class;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
}