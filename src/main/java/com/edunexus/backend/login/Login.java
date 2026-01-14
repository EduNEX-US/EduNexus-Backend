package com.edunexus.backend.login;

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

	@Column(name = "role")
	private String role;
	
	@Column(name = "must_change_password")
	private boolean mustChangePassword;
	
	public Login() {
		
	}
	
	public Login(String eduId, String password, String role) {
        this.eduId = eduId;
        this.eduPassword = password;
        this.role = role;
    }
	
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
	
	public String getRole() {
		return this.role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }
}
