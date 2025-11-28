package com.edunexus.backend.login.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    @JsonProperty("edu_id")
    private String eduId;

    @JsonProperty("edu_password")
    private String eduPassword;

    private String role;

    public String getEduId() {
        return eduId;
    }
    public void setEduId(String eduId) {
        this.eduId = eduId;
    }

    public String getEduPassword() {
        return eduPassword;
    }
    public void setEduPassword(String eduPassword) {
        this.eduPassword = eduPassword;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
