package com.edunexus.backend.login;

public class ChangePasswordResponse {
    private String message;

    public ChangePasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
