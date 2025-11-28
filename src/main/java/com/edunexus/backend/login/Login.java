package com.edunexus.backend.login;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "login")
public class Login {
	@Id
	@Column(name = "edu_id")
	private String eduId;
	
	@Column(name = "edu_pass")
	private String eduPassword;

	public String getEdu_id() {
		return eduId;
	}

	public void setEdu_id(String edu_id) {
		this.eduId = edu_id;
	}

	public String getEdu_pass() {
		return eduPassword;
	}

	public void setEdu_pass(String edu_pass) {
		this.eduPassword = edu_pass;
	}
	
}
