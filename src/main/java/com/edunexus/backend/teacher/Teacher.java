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
	private double teacher_mob;
	
	@Column(name = "teacher_address")
	private String teacher_add;
	
	@Column(name = "teacher_experience")
	private String teacher_exp;
	
	@Column(name = "teacher_class")
	private String teacher_class;
	
	@Column(name = "isAdmin")
	private int isAdmin;
	
}
