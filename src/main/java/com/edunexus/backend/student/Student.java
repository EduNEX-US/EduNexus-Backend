package com.edunexus.backend.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
	@Id
	@Column(name = "student_id")
	private String stud_id;
	
	@Column(name = "student_name")
	private String stud_name;
	
	@Column(name = "student_email")
	private String stud_email;
	
	@Column(name = "student_mobile")
	private long stud_mobile;
	
	@Column(name = "student_class")
	private String stud_class;
	
	@Column(name = "student_address")
	private String stud_address;
	
	@Column(name = "student_alt_mob")
	private long stud_alt_mob;
	
	@Column(name = "student_guardian")
	private String stud_guardian;
	
	@Column(name = "basicFee")  // Use backticks to preserve exact case
	private double basicFee;
	
	@Column(name = "password")
	private String password;
	
	public String getStud_id() {
		return stud_id;	
	}

	public void setStud_id(String stud_id) {
		this.stud_id = stud_id;
	}

	public String getStud_name() {
		return stud_name;
	}

	public void setStud_name(String stud_name) {
		this.stud_name = stud_name;
	}

	public String getStud_email() {
		return stud_email;
	}

	public void setStud_email(String stud_email) {
		this.stud_email = stud_email;
	}

	public long getStud_mobile() {
		return stud_mobile;
	}

	public void setStud_mobile(long stud_mobile) {
		this.stud_mobile = stud_mobile;
	}

	public String getStud_class() {
		return stud_class;
	}

	public void setStud_class(String stud_class) {
		this.stud_class = stud_class;
	}

	public String getStud_address() {
		return stud_address;
	}

	public void setStud_address(String stud_address) {
		this.stud_address = stud_address;
	}

	public long getStud_alt_mob() {
		return stud_alt_mob;
	}

	public void setStud_alt_mob(long stud_alt_mob) {
		this.stud_alt_mob = stud_alt_mob;
	}

	public String getStud_guardian() {
		return stud_guardian;
	}

	public void setStud_guardian(String stud_guardian) {
		this.stud_guardian = stud_guardian;
	}

	public double getbasicFee() {
	    return basicFee;
	}

	public void setbasicFee(double basicFee) {
        this.basicFee = basicFee;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}